package com.acmed.his.api;

import com.acmed.his.model.Supply;
import com.acmed.his.service.DrugManager;
import com.acmed.his.util.ResponseResult;
import com.acmed.his.util.ResponseUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * SupplyApi
 *
 * @author jimson
 * @date 2017/11/23
 */
@RestController
@Api(tags = "供应商")
@RequestMapping("supply")
public class SupplyApi {
    @Autowired
    private DrugManager drugManager;

    @ApiOperation(value = "添加")
    @PostMapping("save")
    public ResponseResult saveSupply(@RequestBody Supply supply){
        drugManager.saveSupply(supply);
        return ResponseUtil.setSuccessResult();
    }

    @ApiOperation(value = "根据id查询")
    @GetMapping("id")
    public ResponseResult<Supply> getSupplyById(Integer id){
        return ResponseUtil.setSuccessResult(drugManager.getSupplyById(id));
    }

    @ApiOperation(value = "列表")
    @GetMapping("list")
    public ResponseResult<List<Supply>> getAllSupply(){
        return ResponseUtil.setSuccessResult(drugManager.getAllSupply());
    }
}
