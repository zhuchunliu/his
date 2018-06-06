package com.acmed.his.api;

import com.acmed.his.model.dto.ZyDispenseOrderDto;
import com.acmed.his.model.dto.ZyOrderItemReceiveDto;
import com.acmed.his.pojo.zy.*;
import com.acmed.his.service.ZhangYaoReceiveManager;
import com.acmed.his.support.AccessInfo;
import com.acmed.his.support.AccessToken;
import com.acmed.his.support.WithoutToken;
import com.acmed.his.util.PageBase;
import com.acmed.his.util.PageResult;
import com.acmed.his.util.ResponseResult;
import com.acmed.his.util.ResponseUtil;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * 掌药收发
 *
 * Created by Darren on 2018-06-04
 **/
@Api(tags = "掌药-云药房收发")
@RequestMapping("/zy")
@RestController
public class ZhangYaoReceiveApi {

    private Logger logger = LoggerFactory.getLogger(ZhangYaoReceiveApi.class);

    @Autowired
    private ZhangYaoReceiveManager receiveManager;



    @ApiOperation(value = "待收货/已收货订单")
    @GetMapping("/recepit/list")
    public ResponseResult<List<ReceiveOrderVo>> getRecepitOrder(@RequestBody(required = false) PageBase<ZYReceiveQueryMo> pageBase,
                                                                @AccessToken AccessInfo info){
        PageResult<ZyOrderItemReceiveDto> result = receiveManager.getReceiveOrder(null == pageBase?new PageBase<>():pageBase,info.getUser());
        Map<String,List<ZyOrderItemReceiveDto>> map = Maps.newHashMap();
        for(ZyOrderItemReceiveDto dto: result.getData()){
            if(!map.containsKey(dto.getOrderId())){
                map.put(dto.getOrderId(), Lists.newArrayList());
            }
            map.get(dto.getOrderId()).add(dto);
        }

        List<ReceiveOrderVo> list = Lists.newArrayList();
        Iterator iterator = map.keySet().iterator();
        while (iterator.hasNext()){
            List<ZyOrderItemReceiveDto> childList = map.get(iterator.next());
            ReceiveOrderVo vo = new ReceiveOrderVo();
            BeanUtils.copyProperties(childList.get(0),vo);

            List<ReceiveOrderVo.ReceiveOrderDetailVo> detailVoList = Lists.newArrayList();
            int totalNum =0;
            int receiveNum = 0;
            for(ZyOrderItemReceiveDto dto : childList){
                ReceiveOrderVo.ReceiveOrderDetailVo detailVo = new ReceiveOrderVo.ReceiveOrderDetailVo();
                BeanUtils.copyProperties(dto,detailVo);
                detailVoList.add(detailVo);
                totalNum += dto.getNum();
                receiveNum += Optional.ofNullable(dto.getReceiveNum()).orElse(0);
            }
            if(vo.getRecepitStatus() ==2) {
                vo.setTotalNum(totalNum);
                vo.setMissingNum(totalNum - receiveNum);
            }
            vo.setDetailVoList(detailVoList);
            list.add(vo);
        }

        PageResult<ReceiveOrderVo> pageResult = new PageResult<ReceiveOrderVo>();
        pageResult.setTotal(result.getTotal());
        pageResult.setData(list);
        return ResponseUtil.setSuccessResult(pageResult);
    }


    @ApiOperation(value = "确认收货")
    @PostMapping("/recepit")
    public ResponseResult recepit(@RequestBody List<ZYReceiveMo> list,
                                  @AccessToken AccessInfo info){
        receiveManager.recepit(list,info.getUser());
        return ResponseUtil.setSuccessResult();
    }

    @ApiOperation(value = "患者发药")
    @PostMapping("/dispense/list")
    public ResponseResult<PageResult<ZyDispenseOrderDto>> getDispenseOrder(@RequestBody(required = false) PageBase<ZYDispenseMo> pageBase,
                                                                     @AccessToken AccessInfo info) {
        return ResponseUtil.setSuccessResult(receiveManager.getDispenseOrder(null == pageBase?new PageBase<>():pageBase,info.getUser()));
    }

    @ApiOperation(value = "患者发药")
    @GetMapping("/dispense/detail")
    public ResponseResult<List<ZyDispenseOrderDto>> getDispenseDetail(@ApiParam("订单主键") @RequestParam String applyId,
                                                                     @AccessToken AccessInfo info) {
        return ResponseUtil.setSuccessResult(receiveManager.getDispenseDetail(applyId));
    }

    @ApiOperation(value = "确认发药")
    @PostMapping("/dispense")
    public ResponseResult dispense(@ApiParam("{\"applyId\":\"\"} applyId：订单主键") @RequestBody String param,
                                   @AccessToken AccessInfo info) {
        if(org.apache.commons.lang3.StringUtils.isEmpty(param) || null == JSONObject.parseObject(param).get("id")){
            return ResponseUtil.setParamEmptyError("id");
        }
        receiveManager.dispense(JSONObject.parseObject(param).get("id").toString());
        return ResponseUtil.setSuccessResult();
    }

    @ApiOperation(value = "发货回调")
    @PostMapping("/callback")
    @WithoutToken
    public ResponseResult callback(@RequestBody ZYCallbackMo mo){
        logger.info("callback info : "+mo);
        receiveManager.callback(mo);
        return ResponseUtil.setSuccessResult();
    }


}
