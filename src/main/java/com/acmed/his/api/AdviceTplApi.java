package com.acmed.his.api;

import com.acmed.his.model.AdviceTpl;
import com.acmed.his.service.AdviceTplManager;
import com.acmed.his.util.ResponseResult;
import com.acmed.his.util.ResponseUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tk.mybatis.mapper.entity.Example;

/**
 * Created by Darren on 2017-11-20
 **/
@RestController
@Api("医嘱模板")
@RequestMapping("/adviceTpl")
public class AdviceTplApi {

    @Autowired
    private AdviceTplManager adviceTplManager;

    @ApiOperation("取得诊疗字典")
    @GetMapping("/list")
    public ResponseResult getAllInfo(){
        return ResponseUtil.setSuccessResult(adviceTplManager.info());
    }

}
