package com.acmed.his.pojo.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * WxConfig
 *
 * @author jimson
 * @date 2018/3/13
 */
@Data
public class WxConfig {
    @ApiModelProperty("微信appid")
    private String appId;
    @ApiModelProperty("时间戳")
    private String timestamp;
    @ApiModelProperty("随机字符串")
    private String nonceStr;
    @ApiModelProperty("签名")
    private String signature;
}
