package com.tiktok.service_relation;

import com.tiktok.feign_util.utils.RelationFeignClient;
import com.tiktok.service_relation.controller.RelationController;
import com.tiktok.service_relation.mapper.RelationMapper;
import com.tiktok.service_relation.service.RelationService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

@SpringBootTest
class ServiceRelationApplicationTests {
    @Autowired
    private RelationFeignClient relationFeignClient;

    @Autowired
    private RelationService relationService;

    @Autowired
    private RelationMapper relationMapper;

    @Autowired
    private RelationController relationController;

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Test
    void contextLoads() {
    }

    @Test
    void testGetIsRelated() {
        boolean isRelated = relationFeignClient.getIsRelated(1L, 4L);
        System.out.println(isRelated);
//        Integer isRelated1 = relationMapper.getIsRelated(1L, 4L);
//        System.out.println(isRelated1);
//        boolean isRelated = relationController.getIsRelated(1L, 4L);
//        System.out.println(isRelated);
    }

    @Test
    void testSaveIds() {
        String idKey = "followUserIds:" + 4L;
        List<Long> followUserIds = relationMapper.getFollowUserIds(4L);
        List<String> followUserIdsString = followUserIds.stream().map(String::valueOf).collect(Collectors.toList());
        stringRedisTemplate.opsForSet().add(idKey,followUserIdsString.toArray(new String[0]));
    }

}
