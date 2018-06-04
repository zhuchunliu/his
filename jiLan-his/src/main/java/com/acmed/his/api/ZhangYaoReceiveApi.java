package com.acmed.his.api;

import com.acmed.his.model.dto.ZyOrderItemReceiveDto;
import com.acmed.his.pojo.zy.ReceiveOrderVo;
import com.acmed.his.pojo.zy.ZYReceiveMo;
import com.acmed.his.pojo.zy.ZYReceiveQueryMo;
import com.acmed.his.service.ZhangYaoReceiveManager;
import com.acmed.his.support.AccessInfo;
import com.acmed.his.support.AccessToken;
import com.acmed.his.util.ResponseResult;
import com.acmed.his.util.ResponseUtil;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * 掌药收发
 *
 * Created by Darren on 2018-06-04
 **/
@Api(tags = "掌药")
@RequestMapping("/zy")
@RestController
public class ZhangYaoReceiveApi {

    @Autowired
    private ZhangYaoReceiveManager receiveManager;


    @ApiOperation(value = "历史订单")
    @GetMapping("/recepit/list")
    public ResponseResult<List<ReceiveOrderVo>> getRecepitOrder(ZYReceiveQueryMo mo,
                                                                @AccessToken AccessInfo info){
        List<ZyOrderItemReceiveDto> dtoList = receiveManager.getReceiveOrder(info.getUser(),null == mo?new ZYReceiveQueryMo():mo);
        Map<String,List<ZyOrderItemReceiveDto>> map = Maps.newHashMap();
        for(ZyOrderItemReceiveDto dto: dtoList){
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
            for(ZyOrderItemReceiveDto dto : childList){
                ReceiveOrderVo.ReceiveOrderDetailVo detailVo = new ReceiveOrderVo.ReceiveOrderDetailVo();
                BeanUtils.copyProperties(dto,detailVo);
                detailVoList.add(detailVo);
            }
            vo.setDetailVoList(detailVoList);
            list.add(vo);
        }

        return ResponseUtil.setSuccessResult(list);
    }


    @ApiOperation(value = "确认收货")
    @PostMapping("/recepit")
    public ResponseResult recepit(@RequestBody List<ZYReceiveMo> list,
                                  @AccessToken AccessInfo info){
        receiveManager.recepit(list,info.getUser());
        return ResponseUtil.setSuccessResult();
    }


}
