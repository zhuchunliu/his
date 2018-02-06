package com.acmed.his.api;

import com.acmed.his.service.QNManager;
import com.acmed.his.util.ResponseResult;
import com.acmed.his.util.ResponseUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * QiNiuApi
 *
 * @author jimson
 * @date 2017/12/27
 */
@Api(tags = "图片管理")
@RestController
@RequestMapping("qiniu")
public class QiNiuApi {

    @Autowired
    private QNManager qnManager;

    @ApiOperation(value = "获取上传token")
    @RequestMapping(value = "getTokenAndKey",method = RequestMethod.GET)
    public ResponseResult getTokenAndKey(@ApiParam("字典表 QnUpType") @RequestParam("type") String type){
        return ResponseUtil.setSuccessResult(qnManager.getUpToken(type));
    }
}
