package com.acmed.his.api;

import com.acmed.his.model.zy.OrderItemDrugDto;
import com.acmed.his.pojo.vo.OrgVo;
import com.acmed.his.pojo.zy.OrderItemDrugVo;
import com.acmed.his.pojo.zy.ZyOrderQueryMo;
import com.acmed.his.pojo.zy.ZyOrderVo;
import com.acmed.his.service.ZhangYaoManager;
import com.acmed.his.support.AccessInfo;
import com.acmed.his.support.AccessToken;
import com.acmed.his.util.PageBase;
import com.acmed.his.util.PageResult;
import com.acmed.his.util.ResponseResult;
import com.acmed.his.util.ResponseUtil;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

/**
 * Created by Darren on 2018-04-11
 **/
@Api(tags = "掌药")
@RequestMapping("/zy")
@RestController
public class ZhangYaoApi {

    @Autowired
    private ZhangYaoManager zhangYaoManager;


    @ApiOperation(value = "未下单的处方")
    @GetMapping("/unorder/list")
    public ResponseResult<List<OrderItemDrugVo>> getUnOrder(@AccessToken AccessInfo info){
        List<OrderItemDrugDto> source = zhangYaoManager.getUnOrder(info.getUser().getOrgCode());
        List<OrderItemDrugVo> list = Lists.newArrayList();
        source.forEach(obj->{
            OrderItemDrugVo vo = new OrderItemDrugVo();
            BeanUtils.copyProperties(obj,vo);
            list.add(vo);
        });
        return ResponseUtil.setSuccessResult(list);
    }


    @ApiOperation(value = "下单")
    @PostMapping("/order")
    public ResponseResult order(@ApiParam("{\"ids\":\"\"},ids：处方药品详情id,多个值逗号间隔") @RequestBody String param,
                                             @AccessToken AccessInfo info){

        if(StringUtils.isEmpty(param) || null == JSONObject.parseObject(param).get("ids")){
            return ResponseUtil.setParamEmptyError("ids");
        }
        zhangYaoManager.order(JSONObject.parseObject(param).get("ids").toString(),info.getUser());

        return ResponseUtil.setSuccessResult();
    }


    @ApiOperation(value = "已经下单列表")
    @PostMapping("/order/list")
    public ResponseResult<List<ZyOrderVo>> getOrderList(@RequestBody(required = false) PageBase<ZyOrderQueryMo> pageBase,
                                                        @AccessToken AccessInfo info){
        pageBase = Optional.ofNullable(pageBase).orElse(new PageBase<>());
        PageResult result = zhangYaoManager.getOrderList(pageBase,info.getUser());
        return ResponseUtil.setSuccessResult(result);
    }

    @ApiOperation(value = "订单处方详情 - 数据来源:his")
    @GetMapping("/order/item")
    public ResponseResult<List<OrgVo>> getOrderItem(@Param("掌药订单id") @RequestParam("id") String id){
        List<OrderItemDrugDto> source = zhangYaoManager.getOrderItemList(id);
        List<OrderItemDrugVo> list = Lists.newArrayList();
        source.forEach(obj->{
            OrderItemDrugVo vo = new OrderItemDrugVo();
            BeanUtils.copyProperties(obj,vo);
            list.add(vo);
        });
        return ResponseUtil.setSuccessResult(list);
    }

    @ApiOperation(value = "获取掌药订单详情 - 数据来源:掌药")
    @GetMapping("/order/detail")
    public ResponseResult<List<OrgVo>> getOrderDetail(@Param("掌药订单id") @RequestParam("id") String id){
        return ResponseUtil.setSuccessResult(zhangYaoManager.getOrderDetail(id));
    }

    @ApiOperation(value = "确认收货")
    @PostMapping("/recepit")
    public ResponseResult recepit(@ApiParam("{\"id\":},id：掌药订单id") @RequestBody String param,
                                     @AccessToken AccessInfo info){
        if(StringUtils.isEmpty(param) || null == JSONObject.parseObject(param).get("id")){
            return ResponseUtil.setParamEmptyError("id");
        }
        zhangYaoManager.recepit(JSONObject.parseObject(param).getString("id"),info.getUser());
        return ResponseUtil.setSuccessResult();
    }


}
