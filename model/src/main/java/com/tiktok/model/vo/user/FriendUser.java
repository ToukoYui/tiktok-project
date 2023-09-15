package com.tiktok.model.vo.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@EqualsAndHashCode(callSuper = true)
@Data
public class FriendUser extends UserVo {

    String massage;

    Integer msgType;
}
