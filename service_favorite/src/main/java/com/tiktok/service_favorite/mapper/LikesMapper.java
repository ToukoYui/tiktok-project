package com.tiktok.service_favorite.mapper;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface LikesMapper {
    void updateFavorite(Long videoId, Integer isFavorite);

    Integer getLikeCount(Long videoId);

    Integer getLikeCountByUserId(Long userId);

    Integer getIsLike(Long userId);
}
