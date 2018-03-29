package com.acmed.his.api;

import com.acmed.his.support.WithoutToken;
import com.acmed.his.util.ZXingCode;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;

/**
 * CommonApi
 *
 * @author jimson
 * @date 2018/3/29
 */
@Api(tags = "通用")
@RestController
@RequestMapping("common")
public class CommonApi {

    @ApiOperation(value = "文字转二维码")
    @WithoutToken
    @GetMapping("text2img")
    public void getCodeImg(String txt, HttpServletResponse response){

        ZXingCode.zxingCodeCreate(txt,200,200,response);
    }
}
