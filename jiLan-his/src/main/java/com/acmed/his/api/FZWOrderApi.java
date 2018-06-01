package com.acmed.his.api;

import com.acmed.his.constants.StatusCode;
import com.acmed.his.model.fzw.FZWOrder;
import com.acmed.his.model.fzw.FZWServicePackage;
import com.acmed.his.pojo.mo.FZWOrderMo;
import com.acmed.his.pojo.vo.FZWOrderPatientVo;
import com.acmed.his.service.FZWOrderManager;

import com.acmed.his.support.AccessInfo;
import com.acmed.his.support.AccessToken;
import com.acmed.his.support.WithoutToken;
import com.acmed.his.util.ResponseResult;
import com.acmed.his.util.ResponseUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * FZWOrderApi
 *
 * @author jimson
 * @date 2018/5/18
 */
@RestController
@Api(tags = "肺诊网相关")
@RequestMapping("/fzw")
public class FZWOrderApi {
    @Autowired
    private FZWOrderManager fzwOrderManager;

    @WithoutToken
    @ApiOperation(value = "服务包列表")
    @GetMapping("servicePackageList")
    public ResponseResult<List<FZWServicePackage>> servicePackageList(){
        return ResponseUtil.setSuccessResult(fzwOrderManager.allFZWServicePackage());
    }

    @ApiOperation(value = "用户创建和修改订单")
    @PostMapping("save")
    public ResponseResult save(@AccessToken AccessInfo info, @RequestBody FZWOrderMo fzwOrderMo){
        FZWOrder fzwOrder = new FZWOrder();
        BeanUtils.copyProperties(fzwOrderMo,fzwOrder);
        if (StringUtils.isNotEmpty(fzwOrderMo.getId())){
            FZWOrder byId = fzwOrderManager.getById(fzwOrderMo.getId());
            if (byId.getStatus()!=1){
                return ResponseUtil.setErrorMeg(StatusCode.FAIL,"本订单已支付，不支持修改");
            }
            fzwOrderManager.patientUpdateFzwOrder(info.getPatientId(),fzwOrderMo);
            return ResponseUtil.setSuccessResult();
        }else {
            fzwOrder.setCreateBy(info.getPatientId());
            FZWOrder fzwOrder1 = fzwOrderManager.createFZWOrder(fzwOrder);
            return ResponseUtil.setSuccessResult(fzwOrder1.getId());
        }

    }

    @ApiOperation(value = "获取自己的订单列表")
    @GetMapping("getselflist")
    public ResponseResult<List<FZWOrderPatientVo>> save(@AccessToken AccessInfo info,@ApiParam("状态，不传表全部 1，未支付  2已支付")@RequestParam(value = "status",required = false)Integer status){
        List<FZWOrder> byCrateByAndStatus = fzwOrderManager.getByCrateByAndStatus(info.getPatientId(), status);
        List<FZWOrderPatientVo> list = new ArrayList<>();
        for (FZWOrder fzwOrder : byCrateByAndStatus){
            FZWOrderPatientVo p = new FZWOrderPatientVo();
            BeanUtils.copyProperties(fzwOrder,p);
            list.add(p);
        }
        return ResponseUtil.setSuccessResult(list);
    }


    @ApiOperation(value = "订单详情")
    @GetMapping("orderDetail")
    public ResponseResult<FZWOrderPatientVo> orderDetail(@ApiParam("订单id")@RequestParam(value = "id")String id){
        return ResponseUtil.setSuccessResult(fzwOrderManager.getById(id));
    }

    @ApiOperation(value = "同意退款")
    @GetMapping(" agreeRefund")
    public ResponseResult agreeRefund(@ApiParam("订单id")@RequestParam(value = "id")String id,@AccessToken AccessInfo info){
        boolean refund = fzwOrderManager.refund(id, info.getUserId().toString());
        if (refund){
            return ResponseUtil.setSuccessResult();
        }
        return ResponseUtil.setErrorMeg(StatusCode.FAIL,"退款失败");

    }



}
