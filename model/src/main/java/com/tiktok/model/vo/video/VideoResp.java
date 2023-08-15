package com.tiktok.model.vo.video;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class VideoResp {
    @JsonProperty("status_code")
    private String statusCode;

    @JsonProperty("status_msg")
    private String statusMsg;

    @JsonProperty("next_time")
    private Integer nextTime; //这里前端要的是时间戳

    @JsonProperty("video_list")
    private List<VideoVo> videoVoList;
}
