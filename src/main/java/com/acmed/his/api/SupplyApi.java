package com.acmed.his.api;

import com.acmed.his.model.Supply;
import com.acmed.his.service.DrugManager;
import com.acmed.his.util.ResponseResult;
import com.acmed.his.util.ResponseUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * SupplyApi
 *
 * @author jimson
 * @date 2017/11/23
 */
@RestController
@Api("供应商")
public class SupplyApi {
    @Autowired
    private DrugManager drugManager;

    @ApiOperation(value = "添加")
    @PostMapping("/supply/save")
    public ResponseResult saveSupply(@RequestBody Supply supply){
        drugManager.saveSupply(supply);
        return ResponseUtil.setSuccessResult();
    }

    @ApiOperation(value = "添加")
    @GetMapping("/supply/id")
    public ResponseResult getSupplyById(Integer id){
        return ResponseUtil.setSuccessResult(drugManager.getSupplyById(id));
    }

    @ApiOperation(value = "添加")
    @GetMapping("/supply/list")
    public ResponseResult getAllSupply(){
        return ResponseUtil.setSuccessResult(drugManager.getAllSupply());
    }
}
