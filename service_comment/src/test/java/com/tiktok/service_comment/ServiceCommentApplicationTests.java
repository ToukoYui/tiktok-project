package com.tiktok.service_comment;

import com.tiktok.feign_util.utils.UserFeignClient;
import com.tiktok.model.entity.comment.Comment;
import com.tiktok.model.vo.TokenAuthSuccess;
import com.tiktok.model.vo.user.UserResp;
import com.tiktok.model.vo.user.UserVo;
import com.tiktok.service_comment.mapper.CommentMapper;
import com.tiktok.service_comment.service.CommentService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.text.SimpleDateFormat;
import java.util.Date;

@SpringBootTest
public class ServiceCommentApplicationTests {
    @Autowired
    private CommentMapper commentMapper;

    @Autowired
    private UserFeignClient userFeignClient;

    @Test
    void testPublishComment() {
        // 获取当前日期 mm-dd
        long current = System.currentTimeMillis();
        Date currentDate = new Date(current);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM-dd");
        String date = simpleDateFormat.format(currentDate);
        Comment comment = new Comment();
        comment.setUserId(1L);
        comment.setVideoId(1L);
        comment.setContent("可以啊,还有这种操作");
        comment.setCreate_date(date);
        commentMapper.publishComment(comment);
    }

    @Test
    void testDeleteComment() {
        commentMapper.deleteCommentById(3L);
    }

    @Test
    void testGetUserInfo() {
        String userId = "4";
        String token = "eyJhbGciOiJIUzUxMiIsInppcCI6IkdaSVAifQ.H4sIAAAAAAAAAKtWKi5NUrJS8vYM8fYP0Q0Ndg1S0lFKrShQsjI0szQ2MDY3N7bQUSotTi3yTFGyMoEw_RJzU4GacjKLM5VqAbyZrFVDAAAA.olhcd8MOPqjwrXxw-ygLVfSPi_-V-tcBL8ReJaBbM23k3GwmCkJFaCRR446UUu7sQjTvxIchSvPrMLD3KQt7aA";
//        UserResp info = userFeignClient.getUserInfoFromUserModel(userId, token);
//        UserVo userVo = info.getUserVo();
        UserVo userVo = userFeignClient.getUserInfoFromUserModelByNotToken(userId);
        System.out.println(userVo);
    }
}
