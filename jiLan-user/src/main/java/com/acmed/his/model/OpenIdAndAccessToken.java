package com.acmed.his.model;

import lombok.Data;

/**
 * OpenIdAndAccessToken
 *
 * @author jimson
 * @date 2018/3/12
 */
@Data
public class OpenIdAndAccessToken {
    private String openId;
    private String accessToken;
}
