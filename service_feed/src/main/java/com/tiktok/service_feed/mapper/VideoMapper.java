package com.tiktok.service_feed.mapper;

import com.tiktok.model.entity.video.Video;
import org.apache.ibatis.annotations.Mapper;
import java.util.List;

@Mapper
public interface VideoMapper {
    List<Video> getVideoList(String lastTime);
}
