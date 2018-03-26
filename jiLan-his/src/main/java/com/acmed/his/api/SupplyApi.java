package com.acmed.his.api;

import com.acmed.his.model.Supply;
import com.acmed.his.service.SupplyManager;
import com.acmed.his.support.AccessInfo;
import com.acmed.his.support.AccessToken;
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
    private SupplyManager supplyManager;

    @ApiOperation(value = "添加/编辑  存在id就是编辑")
    @PostMapping("save")
    public ResponseResult saveSupply(@RequestBody Supply supply,@AccessToken AccessInfo info){
        supplyManager.saveSupply(supply,info.getUser());
        return ResponseUtil.setSuccessResult();
    }

    @ApiOperation(value = "根据id查询")
    @GetMapping("id")
    public ResponseResult<Supply> getSupplyById(@ApiParam("供应商id") @RequestParam(value = "id" )Integer id){
        return ResponseUtil.setSuccessResult(supplyManager.getSupplyById(id));
    }

    @ApiOperation(value = "列表分页")
    @PostMapping("listByPage")
    public ResponseResult<PageResult<Supply>> getAllSupplyByPage(@ApiParam("param 名字  短名 拼音都可以模糊搜索   不传就是全部查询")@RequestBody PageBase<String> pageBase,
                                                                 @AccessToken AccessInfo info){
        return ResponseUtil.setSuccessResult(supplyManager.getSupplyByPage(pageBase,info.getUser()));
    }

    @ApiOperation(value = "列表分页")
    @GetMapping("list")
    public ResponseResult<List<Supply>> getAllSupply(@ApiParam("名字  短名 拼音都可以模糊搜索") @RequestParam(value = "param",required = false)String param,
                                                     @AccessToken AccessInfo info){
        return ResponseUtil.setSuccessResult(supplyManager.getSupply(param,info.getUser()));
    }
}
