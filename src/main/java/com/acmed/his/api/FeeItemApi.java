package com.acmed.his.api;

import com.acmed.his.constants.CommonConstants;
import com.acmed.his.pojo.mo.FeeItemMo;
import com.acmed.his.service.FeeItemManager;
import com.acmed.his.support.AccessInfo;
import com.acmed.his.support.AccessToken;
import com.acmed.his.util.ResponseResult;
import com.acmed.his.util.ResponseUtil;
import io.swagger.annotations.*;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Darren on 2017-11-22
 **/
@RestController
@Api(tags = "收费项目管理")
@RequestMapping("/fee")
public class FeeItemApi {

    @Autowired
    private FeeItemManager feeItemManager;

    @ApiOperation(value = "新增/编辑 收费项目信息")
    @ApiImplicitParams({ @ApiImplicitParam(paramType = "header", dataType = "String", name = CommonConstants.USER_HEADER_TOKEN, value = "token", required = true) })
    @PostMapping("/save")
    public ResponseResult saveFeeItem(@ApiParam("id等于null:新增; feeItemCode不等于null：编辑") @RequestBody FeeItemMo feeItemMo,
                                      @AccessToken AccessInfo info){
        feeItemManager.saveFeeItem(feeItemMo,info.getUser());
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
    @ApiImplicitParams({ @ApiImplicitParam(paramType = "header", dataType = "String", name = CommonConstants.USER_HEADER_TOKEN, value = "token", required = true) })
    public ResponseResult delFeeItem(@ApiParam("收费项目主键") @RequestParam("id") Integer id,
                                     @AccessToken AccessInfo info){
        feeItemManager.delFeeItem(id,info.getUser());
        return ResponseUtil.setSuccessResult();
    }
}
