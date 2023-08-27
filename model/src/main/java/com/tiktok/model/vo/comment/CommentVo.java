package com.tiktok.model.vo.comment;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.tiktok.model.vo.user.UserVo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommentVo {
    private Long id;

    private UserVo user;

    @JsonProperty("video_id")
    private Long videoId;

    private String content;

    @JsonFormat(pattern = "mm-dd")
    @JsonProperty("create_date")
    private String create_date;

    @JsonProperty("like_count")
    private int likeCount;

    @JsonProperty("tease_count")
    private int teaseCount;


}
