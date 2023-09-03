package com.tiktok.service_favorite.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface LikesMapper {
    void insertFavorite(@Param("videoId") Long videoId,
                        @Param("isFavorite") Integer isFavorite,
                        @Param("userId") Long userId);

    Integer getLikeCount(Long videoId);

    Integer getLikeCountByUserId(Long userId);

    Integer getIsLike(Long userId,Long videoId);

    Integer selectLike(@Param("userId") Long userId,@Param("videoId") Long videoId);

    void updateFavorite(@Param("videoId") Long videoId,
                        @Param("isFavorite") Integer isFavorite,
                        @Param("userId") Long userId);

    Integer getLikedVideoNumByUserId(Long userId);

    List<Long> getVideoIdByUserId(Long userId);
}

