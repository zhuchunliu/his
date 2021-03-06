package com.acmed.his.api;

import com.acmed.his.model.Manufacturer;
import com.acmed.his.service.ManufacturerManager;
import com.acmed.his.support.AccessInfo;
import com.acmed.his.support.AccessToken;
import com.acmed.his.util.PageBase;
import com.acmed.his.util.PageResult;
import com.acmed.his.util.ResponseResult;
import com.acmed.his.util.ResponseUtil;
import com.google.common.collect.ImmutableMap;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;


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

    @ApiOperation(value = "添加/编辑 存在id则编辑")
    @PostMapping("save")
    public ResponseResult saveManufacturer(@RequestBody Manufacturer manufacturer,
                                           @AccessToken AccessInfo info){
        manufacturer = manufacturerManager.saveManufacturer(manufacturer,info.getUser());
        return ResponseUtil.setSuccessResult(ImmutableMap.of("id",
                null == manufacturer || null == manufacturer.getId() ?0:1));
    }

    @ApiOperation(value = "根据id查询详情")
    @GetMapping("id")
    public ResponseResult<Manufacturer> getManufacturerById(@ApiParam("药厂id") @RequestParam(value = "id") Integer id){
        Manufacturer manufacturerById = manufacturerManager.getManufacturerById(id);
        return ResponseUtil.setSuccessResult(manufacturerById);
    }

    @ApiOperation(value = "根据名字模糊查询 分页")
    @PostMapping("listByPage")
    public ResponseResult<PageResult<Manufacturer>> getManufacturerByNameByPage(@RequestBody PageBase<String> pageBase,
                                                                                @AccessToken AccessInfo info){
        return ResponseUtil.setSuccessResult(manufacturerManager.getManufacturerLikeNameByPage(pageBase,info.getUser()));
    }

    @ApiOperation(value = "根据名字模糊查询")
    @GetMapping("list")
    public ResponseResult<List<Manufacturer>> getManufacturerByName(@ApiParam("药厂名字") @RequestParam(value = "name",required = false) String name,
                                                                    @AccessToken AccessInfo info){
        return ResponseUtil.setSuccessResult(manufacturerManager.getManufacturerLikeName(name,info.getUser()));
    }


}
