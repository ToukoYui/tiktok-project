package com.tiktok.service_comment.mapper;

import com.tiktok.model.entity.comment.Comment;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface CommentMapper {

    void publishComment(Comment comment);

    int deleteCommentById(Long commentId);

    List<Comment> getCommentList(Long videoId);

    Comment getCommentById(String userId, Long videoId);

    Long getUserById(Long videoId, Long commentId);
}
