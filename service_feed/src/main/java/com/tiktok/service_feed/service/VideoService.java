package com.tiktok.service_feed.service;

import cn.hutool.core.bean.BeanUtil;
import com.qcloud.cos.COSClient;
import com.qcloud.cos.ClientConfig;
import com.qcloud.cos.auth.BasicCOSCredentials;
import com.qcloud.cos.auth.COSCredentials;
import com.qcloud.cos.http.HttpProtocol;
import com.qcloud.cos.model.ObjectMetadata;
import com.qcloud.cos.model.PutObjectRequest;
import com.qcloud.cos.region.Region;
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
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Slf4j
@Service
public class VideoService {
    @Autowired
    private UserFeignClient userFeignClient;

    @Autowired
    private VideoMapper videoMapper;

    @Resource
    private RedisTemplate<String, List<VideoVo>> redisTemplate;

    public List<VideoVo> getVideoList(String lastTime, String token, String userId) {
        System.out.println("lastTime = " + lastTime);
        // 查询redis中是否有缓存
        String redisKey = StringUtils.isEmpty(userId) ? "videolist:public" : "videolist:" + userId;
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
            UserVo userInfo = userFeignClient.getUserInfoFromUserModelByNotToken(authorId);
            VideoVo videoVo = new VideoVo();
            BeanUtils.copyProperties(video, videoVo);
            videoVo.setAuthor(userInfo);
            // todo 获取点赞数
            // todo 获取评论数
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
        String[] suffixArr = new String[]{

        };
        return true;
    }

    // 上传文件到腾讯云后返回该存储文件的url
    public String uploadVideo(MultipartFile multipartFile, String title) {
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
        String coverKey = "picture/" + dateString + "/" + uuid + fileName + ".jpg";
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
            // todo 保存视频信息到数据库

            // 获取视频链接
            URL objectUrl = cosClient.getObjectUrl(bucketName, videoKey);
            cosClient.shutdown();
            inputStream.close();
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
            userInfo = userFeignClient.getUserInfoFromUserModelByNotToken(userId);
        } else {
            // todo
            userInfo = userFeignClient.getUserInfoFromUserModel(userId, token).getUserVo();
        }
        // 根据当前用户id查找已发布的视频
        List<Video> videos = videoMapper.selectVideoByUserId(userId);
        if (videos == null) {
            // 用户id不存在或当前用户未发布视频则返回空
            return null;
        }
        // 将集合中的video数据类型转换为videoVo类型
        // 交互功能还没实现,数值目前先设置为0
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
}
