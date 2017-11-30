package com.acmed.his.api;

import com.acmed.his.model.Manufacturer;
import com.acmed.his.service.DrugManager;
import com.acmed.his.util.ResponseResult;
import com.acmed.his.util.ResponseUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * ManufacturerApi
 *
 * @author jimson
 * @date 2017/11/23
 */
@RestController
@RequestMapping("manufacturer")
@Api(tags = "药厂")
public class ManufacturerApi {
    @Autowired
    private DrugManager drugManager;

    @ApiOperation(value = "添加")
    @PostMapping("save")
    public ResponseResult saveManufacturer(@RequestBody Manufacturer manufacturer){
        drugManager.saveManufacturer(manufacturer);
        return ResponseUtil.setSuccessResult();
    }

    @ApiOperation(value = "根据id查询详情")
    @GetMapping("id")
    public ResponseResult<Manufacturer> getManufacturerById(Integer id){
        Manufacturer manufacturerById = drugManager.getManufacturerById(id);
        return ResponseUtil.setSuccessResult(manufacturerById);
    }

    @ApiOperation(value = "根据名字模糊查询")
    @GetMapping("name")
    public ResponseResult<List<Manufacturer>> getManufacturerById(String name){
        List<Manufacturer> manufacturerLikeName = drugManager.getManufacturerLikeName(name);
        return ResponseUtil.setSuccessResult(manufacturerLikeName);
    }

    @ApiOperation(value = "全部")
    @GetMapping("all")
    public ResponseResult<List<Manufacturer>> getAllManufacturers(){
        List<Manufacturer> allManufacturers = drugManager.getAllManufacturers();
        return ResponseUtil.setSuccessResult(allManufacturers);
    }
}
