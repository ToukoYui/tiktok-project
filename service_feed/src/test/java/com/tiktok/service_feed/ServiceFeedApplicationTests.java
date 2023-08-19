package com.tiktok.service_feed;

import com.tiktok.model.entity.video.Video;
import com.tiktok.model.vo.video.VideoVo;
import com.tiktok.service_feed.mapper.VideoMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.stream.Collectors;

@SpringBootTest
class ServiceFeedApplicationTests {
    @Autowired
    private VideoMapper videoMapper;


    @Test
    void contextLoads() {
    }


    @Test
    void testGetVideoList() {
        List<Video> videos = videoMapper.selectVideoByUserId(String.valueOf(1));
        // 出bug了,两个url变成null了
        List<VideoVo> videoVoList = videos.stream().map(
                video -> new VideoVo(
                        video.getId(), null, video.getPlayUrl(),
                        video.getCoverUrl(), 0, 0, false,
                        video.getTitle(), video.getCreatedTime()
                )
        ).collect(Collectors.toList());
        for (VideoVo videoVo : videoVoList) {
            System.out.println(videoVo);
        }


    }


}
