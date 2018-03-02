package com.acmed.his.api;

import com.acmed.his.model.Manufacturer;
import com.acmed.his.service.ManufacturerManager;
import com.acmed.his.util.PageBase;
import com.acmed.his.util.PageResult;
import com.acmed.his.util.ResponseResult;
import com.acmed.his.util.ResponseUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
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
    private ManufacturerManager manufacturerManager;

    @ApiOperation(value = "添加")
    @PostMapping("save")
    public ResponseResult saveManufacturer(@RequestBody Manufacturer manufacturer){
        manufacturerManager.saveManufacturer(manufacturer);
        return ResponseUtil.setSuccessResult();
    }

    @ApiOperation(value = "根据id查询详情")
    @GetMapping("id")
    public ResponseResult<Manufacturer> getManufacturerById(@ApiParam("药厂id") @RequestParam(value = "id") Integer id){
        Manufacturer manufacturerById = manufacturerManager.getManufacturerById(id);
        return ResponseUtil.setSuccessResult(manufacturerById);
    }

    @ApiOperation(value = "根据名字模糊查询 分页")
    @PostMapping("listByPage")
    public ResponseResult<PageResult<Manufacturer>> getManufacturerByNameByPage(@RequestBody PageBase<String> pageBase){
        return ResponseUtil.setSuccessResult(manufacturerManager.getManufacturerLikeNameByPage(pageBase));
    }

    @ApiOperation(value = "根据名字模糊查询")
    @GetMapping("list")
    public ResponseResult<List<Manufacturer>> getManufacturerByName(@ApiParam("药厂名字") @RequestParam(value = "name",required = false) String name){
        return ResponseUtil.setSuccessResult(manufacturerManager.getManufacturerLikeName(name));
    }
}
