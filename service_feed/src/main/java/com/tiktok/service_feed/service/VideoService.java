package com.tiktok.service_feed.service;

import com.qcloud.cos.COSClient;
import com.qcloud.cos.ClientConfig;
import com.qcloud.cos.auth.BasicCOSCredentials;
import com.qcloud.cos.auth.COSCredentials;
import com.qcloud.cos.http.HttpProtocol;
import com.qcloud.cos.model.ObjectMetadata;
import com.qcloud.cos.model.PutObjectRequest;
import com.qcloud.cos.region.Region;
import com.tiktok.feign_util.utils.UserFeignClient;
import com.tiktok.model.vo.user.UserVo;
import com.tiktok.model.vo.video.VideoVo;
import com.tiktok.service_feed.config.OssPropertiesUtils;
import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class VideoService {
    @Autowired
    private UserFeignClient userFeignClient;

    public List<VideoVo> getVideoList(String latestTimeStr, LocalDateTime lastTime,String token) {
        String userId ="1";
        UserVo userInfo;
        // 获取用户信息
        if (StringUtils.isEmpty(token)){
           userInfo = userFeignClient.getUserInfoFromUserModelByNotToken(userId);
        }else{
            userInfo = userFeignClient.getUserInfoFromUserModel(userId,token);
        }
        return null;
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
    public String uploadVideo(MultipartFile multipartFile,String title) {
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
}
