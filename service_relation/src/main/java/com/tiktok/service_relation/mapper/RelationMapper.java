package com.tiktok.service_relation.mapper;

import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface RelationMapper {

    Integer selectIsRelated(Long userId, Long toUserId);

    void insertRelate(Long userId, Long toUserId, Integer isRelated);

    void updateRelated(Long userId, Long toUserId, Integer isRelated);

    List<Integer> getFollowUserIds(Long userId);

    Integer selectFollowUserCount(Long userId);

    Integer selectFollowerCount(Long userId);

    List<Integer> getFollowerIds(Long userId);
}
