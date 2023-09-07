package com.tiktok.service_favorite.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface LikesMapper {
    void insertFavorite(@Param("videoId") Long videoId,
                        @Param("isFavorite") Integer isFavorite,
                        @Param("userId") Long userId);

    Integer getLikeCountByVideoId(@Param("videoId") Long videoId);

    Integer getLikeCountByUserId(Long userId);

    Integer getIsLike(@Param("userId")Long userId,@Param("videoId")Long videoId);

    Integer selectLike(@Param("userId") Long userId,@Param("videoId") Long videoId);

    void updateFavorite(@Param("videoId") Long videoId,
                        @Param("isFavorite") Integer isFavorite,
                        @Param("userId") Long userId);


    List<Long> getVideoIdByUserId(Long userId);

    Integer selectIsLiked(@Param("userId") Long userId,@Param("videoId") Long videoId);
}

