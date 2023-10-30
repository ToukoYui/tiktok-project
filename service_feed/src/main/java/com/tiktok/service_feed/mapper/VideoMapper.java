package com.tiktok.service_feed.mapper;

import com.tiktok.model.entity.video.Video;
import org.apache.ibatis.annotations.Mapper;

import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface VideoMapper {
    List<Video> getVideoList(String lastTime);

    List<Video> selectVideoByUserId(@Param("id") Long id);

    void insertVideo(Video video);

    Integer getVideoNumByUserId(Long userId);

    List<Video> getVideoListByIdList(@Param("videoIdList") List<Long> videoIdList);

}
