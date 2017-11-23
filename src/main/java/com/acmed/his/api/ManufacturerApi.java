package com.acmed.his.api;

import com.acmed.his.model.Manufacturer;
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
 * ManufacturerApi
 *
 * @author jimson
 * @date 2017/11/23
 */
@RestController
@Api("药厂")
public class ManufacturerApi {
    @Autowired
    private DrugManager drugManager;

    @ApiOperation(value = "添加")
    @PostMapping("/manufacturer/save")
    public ResponseResult saveManufacturer(@RequestBody Manufacturer manufacturer){
        drugManager.saveManufacturer(manufacturer);
        return ResponseUtil.setSuccessResult();
    }

    @ApiOperation(value = "根据id查询详情")
    @GetMapping("/manufacturer/id")
    public ResponseResult getManufacturerById(Integer id){
        return ResponseUtil.setSuccessResult(drugManager.getManufacturerById(id));
    }

    @ApiOperation(value = "根据名字模糊查询")
    @GetMapping("/manufacturer/name")
    public ResponseResult getManufacturerById(String name){
        return ResponseUtil.setSuccessResult(drugManager.getManufacturerLikeName(name));
    }

    @ApiOperation(value = "全部")
    @GetMapping("/manufacturer/all")
    public ResponseResult getAllManufacturers(){
        return ResponseUtil.setSuccessResult(drugManager.getAllManufacturers());
    }
}
