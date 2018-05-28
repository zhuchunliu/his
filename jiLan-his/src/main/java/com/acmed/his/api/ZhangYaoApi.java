package com.acmed.his.api;

import com.acmed.his.constants.StatusCode;
import com.acmed.his.exceptions.BaseException;
import com.acmed.his.model.Org;
import com.acmed.his.model.ZyAddress;
import com.acmed.his.model.dto.ZyOrderItemDto;
import com.acmed.his.pojo.mo.DrugZYQueryMo;
import com.acmed.his.pojo.zy.*;
import com.acmed.his.pojo.zy.dto.ZYCityObj;
import com.acmed.his.pojo.zy.dto.ZYExpressObj;
import com.acmed.his.pojo.zy.dto.ZYLogisticsObj;
import com.acmed.his.pojo.zy.dto.ZYStoreDetailObj;
import com.acmed.his.service.OrgManager;
import com.acmed.his.service.ZhangYaoManager;
import com.acmed.his.support.AccessInfo;
import com.acmed.his.support.AccessToken;
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
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by Darren on 2018-04-11
 **/
@Api(tags = "掌药")
@RequestMapping("/zy")
@RestController
public class ZhangYaoApi {

    @Autowired
    private ZhangYaoManager zhangYaoManager;

    @Autowired
    private ZhangYaoOrderManager orderManager;

    @Autowired
    private OrgManager orgManager;

    @ApiOperation(value = "掌药药品信息列表")
    @PostMapping("/drug/list")
    public ResponseResult<PageResult<ZYDrugListVo>> getDrugList(@RequestBody(required = false) PageBase<DrugZYQueryMo> pageBase,
                                                                  @AccessToken AccessInfo info){
        if(null == pageBase.getParam() || StringUtils.isEmpty(pageBase.getParam().getName())){
            throw new BaseException(StatusCode.FAIL,"药品名称不能为空");
        }
        if(StringUtils.isEmpty(pageBase.getParam().getLng()) || StringUtils.isEmpty(pageBase.getParam().getLat())){
            Org org = orgManager.getOrgDetail(info.getUser().getOrgCode());
            pageBase.getParam().setLng(org.getLng());
            pageBase.getParam().setLat(org.getLat());
        }

        if(StringUtils.isEmpty(pageBase.getParam().getLng()) || StringUtils.isEmpty(pageBase.getParam().getLat())){
            throw new BaseException(StatusCode.FAIL,"经纬度不能为空");
        }

        PageResult<ZYDrugListVo> pageResult = zhangYaoManager.getDrugList(pageBase);
        return ResponseUtil.setSuccessResult(pageResult);

    }

    @ApiOperation(value = "掌药药品详情")
    @GetMapping("/drug/detail")
    public ResponseResult<ZYDrugDetailVo> getDrugDetail(@Param("药店id") @RequestParam("storeId") String storeId,
                                                          @Param("药品id") @RequestParam("drugId") String drugId){
        ZYDrugDetailVo detail = zhangYaoManager.getDrugDetail(storeId,drugId);
        return ResponseUtil.setSuccessResult(detail);

    }

    @ApiOperation(value = "掌药药店详情")
    @GetMapping("/store/detail")
    public ResponseResult<ZYStoreDetailObj> getStoreDetail(@Param("药店id") @RequestParam("storeId") String storeId){
        ZYStoreDetailObj detail = zhangYaoManager.getStoreDetail(storeId);
        return ResponseUtil.setSuccessResult(detail);

    }


    @ApiOperation(value = "待支付订单")
    @GetMapping("/unpaid/list")
    public ResponseResult<List<UnpaidOrderVo>> getUnpaidList(@RequestBody(required = false) PageBase<String> pageBase,
                                                             @AccessToken AccessInfo info){
        List<ZyOrderItemDto> dtoList = orderManager.getUnpaidList(info.getUser());
        Map<String,List<ZyOrderItemDto>> map = Maps.newHashMap();
        for(ZyOrderItemDto dto: dtoList){
            if(!map.containsKey(dto.getOrderId())){
                map.put(dto.getOrderId(),Lists.newArrayList());
            }
            map.get(dto.getOrderId()).add(dto);
        }

        List<UnpaidOrderVo> list = Lists.newArrayList();
        Iterator iterator = map.keySet().iterator();
        while (iterator.hasNext()){
            List<ZyOrderItemDto> childList = map.get(iterator.next());
            UnpaidOrderVo vo = new UnpaidOrderVo();
            BeanUtils.copyProperties(childList.get(0),vo);

            List<UnpaidOrderVo.UnpaidOrderDetailVo> detailVoList = Lists.newArrayList();
            for(ZyOrderItemDto dto : childList){
                UnpaidOrderVo.UnpaidOrderDetailVo detailVo = new UnpaidOrderVo.UnpaidOrderDetailVo();
                BeanUtils.copyProperties(dto,detailVo);
                detailVoList.add(detailVo);
            }
            vo.setDetailVoList(detailVoList);
            list.add(vo);
        }

        return ResponseUtil.setSuccessResult(list);
    }



    @ApiOperation(value = "付费")
    @PostMapping("/pay")
    public ResponseResult pay(@RequestBody List<ZYOrderPayMo> mo,
                              @AccessToken AccessInfo info){

        orderManager.pay(mo,info.getUser());
        return ResponseUtil.setSuccessResult();
    }




//    @ApiOperation(value = "已经下单列表")
//    @PostMapping("/order/list")
//    public ResponseResult<List<ZyOrderVo>> getOrderList(@RequestBody(required = false) PageBase<ZyOrderQueryMo> pageBase,
//                                                        @AccessToken AccessInfo info){
//        pageBase = Optional.ofNullable(pageBase).orElse(new PageBase<>());
//        PageResult result = orderManager.getOrderList(pageBase,info.getUser());
//        return ResponseUtil.setSuccessResult(result);
//    }
//
//    @ApiOperation(value = "订单处方详情 - 数据来源:his")
//    @GetMapping("/order/item")
//    public ResponseResult<List<UnpaidOrderVo>> getOrderItem(@Param("掌药订单id") @RequestParam("id") String id){
//        List<OrderItemDrugDto> source = orderManager.getOrderItemList(id);
//        List<UnpaidOrderVo> list = Lists.newArrayList();
//        source.forEach(obj->{
//            UnpaidOrderVo vo = new UnpaidOrderVo();
//            BeanUtils.copyProperties(obj,vo);
//            list.add(vo);
//        });
//        return ResponseUtil.setSuccessResult(list);
//    }
//
//    @ApiOperation(value = "获取掌药订单详情 - 数据来源:掌药")
//    @GetMapping("/order/detail")
//    public ResponseResult<ZYOrderDetailVo> getOrderDetail(@Param("掌药订单id") @RequestParam("id") String id){
//        return ResponseUtil.setSuccessResult(orderManager.getOrderDetail(id));
//    }

    @ApiOperation(value = "确认收货")
    @PostMapping("/recepit")
    public ResponseResult recepit(@ApiParam("{\"id\":},id：掌药订单id") @RequestBody String param,
                                  @AccessToken AccessInfo info){
        if(StringUtils.isEmpty(param) || null == JSONObject.parseObject(param).get("id")){
            return ResponseUtil.setParamEmptyError("id");
        }
        orderManager.recepit(JSONObject.parseObject(param).getString("id"),info.getUser());
        return ResponseUtil.setSuccessResult();
    }



    @ApiOperation(value = "获取省市县信息")
    @GetMapping("/city")
    public ResponseResult<List<ZYCityObj>> getCity(@Param("省市县id,默认0获取所有省") @RequestParam(value = "areaId",defaultValue = "0") String areaId){
        List<ZYCityObj> list = zhangYaoManager.getCity(areaId);
        return ResponseUtil.setSuccessResult(list);

    }

    @ApiOperation(value = "获取快递信息")
    @GetMapping("/express")
    public ResponseResult<List<ZYExpressObj>> getExpress(@Param("药店id") @RequestParam(value = "storeId") String storeId,
                                                         @Param("省份id") @RequestParam(value = "provinceId") String provinceId){
        List<ZYExpressObj> list = zhangYaoManager.getExpress(storeId,provinceId);
        return ResponseUtil.setSuccessResult(list);

    }

    @ApiOperation(value = "获取物流信息")
    @GetMapping("/logistics")
    public ResponseResult<ZYLogisticsObj> getLogistics(@Param("订单号") @RequestParam(value = "orderSn") String orderSn){
        ZYLogisticsObj obj = zhangYaoManager.getLogistics(orderSn);
        return ResponseUtil.setSuccessResult(obj);

    }

    @ApiOperation(value = "获取快递地址")
    @GetMapping("/address/list")
    public ResponseResult<ZYLogisticsObj> getAddressList(@Param("是否是默认地址 0:否；1:是") @RequestParam(value = "isDefault") Integer isDefault,
                                                         @AccessToken AccessInfo info){
        List<ZyAddress> zyAddresses = zhangYaoManager.getAddressList(isDefault,info.getUser());
        return ResponseUtil.setSuccessResult();

    }

    @ApiOperation(value ="获取快递地址")
    @GetMapping("/address/save")
    public ResponseResult<ZYLogisticsObj> saveAddress(@RequestBody ZyAddressMo mo,
                                                      @AccessToken AccessInfo info){
        zhangYaoManager.saveAddress(mo,info.getUser());
        return ResponseUtil.setSuccessResult();

    }

    @ApiOperation(value ="设置默认地址信息")
    @PostMapping("/address/default")
    public ResponseResult<ZYLogisticsObj> setDefaultAddress(@ApiParam("{\"id\":\"\"} id：地址主键") @RequestBody String param,
                                                      @AccessToken AccessInfo info){
        if(org.apache.commons.lang3.StringUtils.isEmpty(param) || null == JSONObject.parseObject(param).get("id")){
            return ResponseUtil.setParamEmptyError("id");
        }
        zhangYaoManager.setDefaultAddress(JSONObject.parseObject(param).getInteger("id"),info.getUser());
        return ResponseUtil.setSuccessResult();

    }

    @ApiOperation(value = "删除快递地址信息")
    @DeleteMapping("/address/del")
    public ResponseResult delAddress(@Param("地址主键") @RequestParam(value = "id") Integer id,
                                     @AccessToken AccessInfo info){
        zhangYaoManager.delAddress(id,info.getUser());
        return ResponseUtil.setSuccessResult();

    }

}
