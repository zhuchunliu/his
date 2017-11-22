package com.acmed.his.api;

import com.acmed.his.pojo.mo.FeeItemMo;
import com.acmed.his.service.FeeItemManager;
import com.acmed.his.util.ResponseResult;
import com.acmed.his.util.ResponseUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Darren on 2017-11-22
 **/
@RestController
@Api("收费项目管理")
@RequestMapping("/fee")
public class FeeItemApi {

    @Autowired
    private FeeItemManager feeItemManager;

    @ApiOperation(value = "新增/编辑 收费项目信息")
    @PostMapping("/save")
    public ResponseResult saveFeeItem(@ApiParam("id等于null:新增; feeItemCode不等于null：编辑") @RequestBody FeeItemMo feeItemMo){
        feeItemManager.saveFeeItem(feeItemMo);
        return ResponseUtil.setSuccessResult();
    }

    @ApiOperation(value = "获取收费项目列表")
    @GetMapping("/list")
    public ResponseResult<List<FeeItemMo>> getFeeItemList(){
        List<FeeItemMo> list = new ArrayList<>();
        FeeItemMo feeItemMo = new FeeItemMo();
        feeItemManager.getFeeItemList().forEach((obj)->{
            BeanUtils.copyProperties(obj,feeItemMo);list.add(feeItemMo);});
        return ResponseUtil.setSuccessResult(list);
    }

    @ApiOperation(value = "获取收费项目详情")
    @GetMapping("/detail")
    public ResponseResult<FeeItemMo> getFeeItemDetail(@ApiParam("收费项目主键") @RequestParam("id") Integer id){
        FeeItemMo feeItemMo = new FeeItemMo();
        BeanUtils.copyProperties(feeItemManager.getFeeItemDetail(id),feeItemMo);
        return ResponseUtil.setSuccessResult(feeItemMo);
    }

    @ApiOperation(value = "删除收费项目信息")
    @DeleteMapping("/del")
    public ResponseResult delFeeItem(@ApiParam("收费项目主键") @RequestParam("id") Integer id){
        feeItemManager.delFeeItem(id);
        return ResponseUtil.setSuccessResult();
    }
}
