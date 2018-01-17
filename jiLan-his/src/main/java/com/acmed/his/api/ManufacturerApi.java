package com.acmed.his.api;

import com.acmed.his.model.Manufacturer;
import com.acmed.his.service.DrugManager;
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
    @GetMapping("name")
    public ResponseResult<ResponseResult<PageResult<Manufacturer>>> getManufacturerByName(@RequestBody PageBase<String> pageBase){
        return ResponseUtil.setSuccessResult(manufacturerManager.getManufacturerLikeNameByPage(pageBase));
    }

    @ApiOperation(value = "全部 分页查询")
    @PostMapping("all")
    public ResponseResult<PageResult<Manufacturer>> getAllManufacturers(@RequestBody PageBase pageBase){
        return ResponseUtil.setSuccessResult(manufacturerManager.getAllManufacturersByPage(pageBase));
    }
}
