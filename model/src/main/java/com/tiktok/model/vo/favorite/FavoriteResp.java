package com.tiktok.model.vo.favorite;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.tiktok.model.vo.video.VideoVo;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class FavoriteResp {
    @JsonProperty("status_code")
    private String statusCode;
    @JsonProperty("status_msg")
    private String statusMsg;
    @JsonProperty("video_list")
    private List<VideoVo> videoVoList;

}
