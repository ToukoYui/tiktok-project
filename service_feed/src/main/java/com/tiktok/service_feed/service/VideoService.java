package com.tiktok.service_feed.service;

import com.qcloud.cos.COSClient;
import com.qcloud.cos.ClientConfig;
import com.qcloud.cos.auth.BasicCOSCredentials;
import com.qcloud.cos.auth.COSCredentials;
import com.qcloud.cos.http.HttpProtocol;
import com.qcloud.cos.model.ObjectMetadata;
import com.qcloud.cos.model.PutObjectRequest;
import com.qcloud.cos.region.Region;
import com.tiktok.feign_util.utils.CommentFeignClient;
import com.tiktok.feign_util.utils.LikeFeignClient;
import com.tiktok.feign_util.utils.UserFeignClient;
import com.tiktok.model.entity.video.Video;
import com.tiktok.model.vo.TokenAuthSuccess;
import com.tiktok.model.vo.user.UserVo;
import com.tiktok.model.vo.video.VideoVo;
import com.tiktok.service_feed.config.OssPropertiesUtils;
import com.tiktok.service_feed.mapper.VideoMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Slf4j
@Service
public class VideoService {
    @Autowired
    private CommentFeignClient commentFeignClient;

    @Autowired
    private UserFeignClient userFeignClient;

    @Autowired
    private VideoMapper videoMapper;
    @Autowired
    private LikeFeignClient likeFeignClient;

    @Resource
    private RedisTemplate<String, List<VideoVo>> redisTemplate;

    private static String[] suffixArr = {
            "mp4", "avi", "mkv", "mov", "flv",
            "wmv", "webm", "m4v", "3gp", "mpeg",
            "mpg", "m2v", "ts", "vob", "rm",
            "rmvb", "swf", "asf", "m2ts", "f4v"
    };

    public List<VideoVo> getVideoList(String lastTime) {
        System.out.println("lastTime = " + lastTime);
        // 查询redis中是否有缓存
        // 这里直接public就好了,因为退出应用不代表用户就退出
        // 这样你再次登录的时候会获取的是该用户的发布视频
        // 如果用户没有发布视频,controller返回-1就会异常
        String redisKey = "videolist:public";
        List<VideoVo> videoVoListFromRedis = redisTemplate.opsForValue().get(redisKey);
        if (videoVoListFromRedis != null) {
            log.info("获取视频流，从缓存中获取------------->" + videoVoListFromRedis.toString());
            return videoVoListFromRedis;
        }

        // 缓存中没有，查询数据库
        List<Video> videoList = videoMapper.getVideoList(lastTime);
        log.info("获取视频流，从MYSQL中获取------------->" + videoList.toString());
        // 封装VideoVo
        List<VideoVo> videoVoList = videoList.stream().map((video) -> {
            String authorId = video.getUserId().toString();
            // 获取投稿视频的用户信息
            UserVo userInfo = userFeignClient.getUserInfoFromUserModuleByNotToken(authorId);
            VideoVo videoVo = new VideoVo();
            BeanUtils.copyProperties(video, videoVo);
            videoVo.setAuthor(userInfo);
            // 获取点赞数
            Integer likeCount = likeFeignClient.getLikeCount(video.getId());
            videoVo.setFavoriteCount(likeCount);
            // 获取评论数
            Integer commentCount = commentFeignClient.getCommnetNumFromCommentModule(video.getId());
            videoVo.setCommentCount(commentCount);
            return videoVo;
        }).collect(Collectors.toList());
        // 存入redis
        redisTemplate.opsForValue().set(redisKey, videoVoList, 3, TimeUnit.MINUTES);
        return videoVoList;
    }

    private COSClient getCosClient() {
        // 1 初始化用户身份信息（secretId, secretKey）。
        String secretId = OssPropertiesUtils.SECRECT_ID;
        String secretKey = OssPropertiesUtils.SECRECT_KEY;
        COSCredentials cred = new BasicCOSCredentials(secretId, secretKey);
        // 2 设置 bucket 的地域, COS 地域的简称请参照 https://cloud.tencent.com/document/product/436/6224
        // clientConfig 中包含了设置 region, https(默认 http), 超时, 代理等 set 方法, 使用可参见源码或者常见问题 Java SDK 部分。
        Region region = new Region(OssPropertiesUtils.COS_REGION);
        ClientConfig clientConfig = new ClientConfig(region);
        // 这里建议设置使用 https 协议
        // 从 5.6.54 版本开始，默认使用了 https
        clientConfig.setHttpProtocol(HttpProtocol.https);
        // 3 生成 cos 客户端。
        return new COSClient(cred, clientConfig);
    }

    // 判断上传的文件是否为视频格式
    private boolean fileTypeFilter(String fileSuffix) {
        return Arrays.binarySearch(suffixArr, fileSuffix) >= 0;
    }

    // 上传文件到腾讯云后返回该存储文件的url
    public String uploadVideo(MultipartFile multipartFile, String title, String userId) {
        // 获取文件名及后缀
        String originalFilename = multipartFile.getOriginalFilename();
        // 获取文件后缀
        String fileNameSuffix = originalFilename.substring(originalFilename.lastIndexOf(".")); // “.mp4”
        if (!fileTypeFilter(fileNameSuffix)) {
            return null;
        }
        // 获取文件名
        String fileName = originalFilename.substring(0, originalFilename.lastIndexOf("."));
        //设置上传到存储桶的路径
        String uuid = UUID.randomUUID().toString().replace("-", "");
        String dateString = new DateTime().toString("yyyy/MM/dd");
        // 生成视频和封面的上传存储路径
        String videoKey = "video/" + dateString + "/" + uuid + originalFilename;
        String coverKey = "picture/video/" + dateString + "/" + uuid + fileName + ".jpg";
        //生成cos客户端
        COSClient cosClient = getCosClient();
        String bucketName = OssPropertiesUtils.BUCKET;
        try {
            InputStream inputStream = multipartFile.getInputStream();
            ObjectMetadata objectMetadata = new ObjectMetadata();
            objectMetadata.setContentLength(multipartFile.getSize());
            // 上传视频
            PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, videoKey, inputStream, objectMetadata);
            cosClient.putObject(putObjectRequest);
            // 获取视频链接
            URL objectUrl = cosClient.getObjectUrl(bucketName, videoKey);
            cosClient.shutdown();
            inputStream.close();

            // 保存视频信息到数据库
            Video video = new Video();
            video.setUserId(Long.valueOf(userId));
            video.setPlayUrl(objectUrl.toString());
            video.setCoverUrl("https://" + bucketName + ".cos." + OssPropertiesUtils.COS_REGION + ".myqcloud.com/" + coverKey);
            video.setTitle(title);
            video.setCreatedTime(LocalDateTime.now());
            videoMapper.insertVideo(video);

            // 发布视频后删除该用户缓存
            redisTemplate.delete("user:" + userId);
            return objectUrl.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<VideoVo> getMyVideoList(TokenAuthSuccess tokenAuthSuccess) {
        String userId = tokenAuthSuccess.getUserId();
        // 查询redis中是否有缓存
        String redisKey = "videolist:" + userId;

        List<VideoVo> videoVoListFromRedis = redisTemplate.opsForValue().get(redisKey);

        if (videoVoListFromRedis != null) {
            log.info("获取视频流，从缓存中获取------------->" + videoVoListFromRedis.toString());
            return videoVoListFromRedis;
        }

        // 缓存中没有,查询数据库
        String token = tokenAuthSuccess.getToken();
        UserVo userInfo;
        // 获取当前登录用户的信息
        if (StringUtils.isEmpty(token)) {
            userInfo = userFeignClient.getUserInfoFromUserModuleByNotToken(userId);
        } else {
            userInfo = userFeignClient.getUserInfoFromUserModule(userId, token).getUserVo();
        }
        //获取当前用户的喜欢视频数
        userInfo.setFavoriteCount(likeFeignClient.getLikeCount(Long.valueOf(userId)));
        // 根据当前用户id查找已发布的视频
        List<Video> videos = videoMapper.selectVideoByUserId(userId);
        if (videos == null) {
            // 用户id不存在或当前用户未发布视频则返回空
            return null;
        }
        // 将集合中的video数据类型转换为videoVo类型
        // todo 交互功能还没实现,数值目前先设置为0
        List<VideoVo> videoVoList = videos.stream().map(
                video -> new VideoVo(
                        video.getId(), userInfo, video.getPlayUrl(),
                        video.getCoverUrl(), 0, 0, false,
                        video.getTitle(), video.getCreatedTime()
                )
        ).collect(Collectors.toList());
        // 存入redis
        redisTemplate.opsForValue().set(redisKey, videoVoList, 3, TimeUnit.MINUTES);
        return videoVoList;

    }

    // 根据用户id获取该用户发布的视频数
    public Integer getVideoNumByUserId(Long userId) {
        return videoMapper.getVideoNumByUserId(userId);
    }

}
