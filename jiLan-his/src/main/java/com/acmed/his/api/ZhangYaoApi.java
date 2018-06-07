package com.acmed.his.api;

import com.acmed.his.constants.StatusCode;
import com.acmed.his.exceptions.BaseException;
import com.acmed.his.model.Org;
import com.acmed.his.model.ZyAddress;
import com.acmed.his.model.dto.ZyOrderItemHistoryDto;
import com.acmed.his.model.dto.ZyOrderItemUnpaidDto;
import com.acmed.his.model.dto.ZyOrderItemUnsubmitDto;
import com.acmed.his.pojo.mo.DrugZYQueryMo;
import com.acmed.his.pojo.zy.*;
import com.acmed.his.pojo.zy.dto.ZYCityObj;
import com.acmed.his.pojo.zy.dto.ZYExpressObj;
import com.acmed.his.pojo.zy.dto.ZYLogisticsObj;
import com.acmed.his.pojo.zy.dto.ZYStoreDetailObj;
import com.acmed.his.service.OrgManager;
import com.acmed.his.service.ZhangYaoManager;
import com.acmed.his.service.ZhangYaoOrderManager;
import com.acmed.his.support.AccessInfo;
import com.acmed.his.support.AccessToken;
import com.acmed.his.util.PageBase;
import com.acmed.his.util.PageResult;
import com.acmed.his.util.ResponseResult;
import com.acmed.his.util.ResponseUtil;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.ImmutableMap;
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

        PageResult<ZYDrugListVo> pageResult = zhangYaoManager.getDrugList(pageBase,info.getUser());
        return ResponseUtil.setSuccessResult(pageResult);

    }

    @ApiOperation(value = "掌药药品详情")
    @GetMapping("/drug/detail")
    public ResponseResult<ZYDrugDetailVo> getDrugDetail(@Param("药店id") @RequestParam("storeId") String storeId,
                                                          @Param("药品id") @RequestParam("drugId") String drugId){
        ZYDrugDetailVo detail = zhangYaoManager.getDrugDetail(storeId,drugId);
        return ResponseUtil.setSuccessResult(detail);

    }


    @ApiOperation(value = "待提交订单")
    @GetMapping("/unsubmit/list")
    public ResponseResult<List<UnsubmitOrderVo>> getUnSubmitOrder(@Param("订单号,或者药品名称") @RequestParam(value = "name",required = false) String name,
                                                                  @AccessToken AccessInfo info){
        List<ZyOrderItemUnsubmitDto> dtoList = orderManager.getUnSubmitOrder(info.getUser(),name);
        Map<String,List<ZyOrderItemUnsubmitDto>> map = Maps.newHashMap();
        for(ZyOrderItemUnsubmitDto dto: dtoList){
            if(!map.containsKey(dto.getOrderId())){
                map.put(dto.getOrderId(),Lists.newArrayList());
            }
            map.get(dto.getOrderId()).add(dto);
        }

        List<UnsubmitOrderVo> list = Lists.newArrayList();
        Iterator iterator = map.keySet().iterator();
        while (iterator.hasNext()){
            List<ZyOrderItemUnsubmitDto> childList = map.get(iterator.next());
            UnsubmitOrderVo vo = new UnsubmitOrderVo();
            BeanUtils.copyProperties(childList.get(0),vo);

            List<UnsubmitOrderVo.UnsubmitDetailVo> detailVoList = Lists.newArrayList();
            for(ZyOrderItemUnsubmitDto dto : childList){
                UnsubmitOrderVo.UnsubmitDetailVo detailVo = new UnsubmitOrderVo.UnsubmitDetailVo();
                BeanUtils.copyProperties(dto,detailVo);
                detailVoList.add(detailVo);
            }
            vo.setDetailVoList(detailVoList);
            list.add(vo);
        }

        return ResponseUtil.setSuccessResult(list);
    }

    @ApiOperation(value = "删除待提交订单")
    @DeleteMapping("/unsubmit/del")
    public ResponseResult<List<UnsubmitOrderVo>> delUnSubmitOrder(@Param("订单主键") @RequestParam(value = "orderId",required = false) String orderId,
                                                                  @Param("详情主键") @RequestParam(value = "itemId",required = false) String itemId,
                                                                  @AccessToken AccessInfo info){
        orderManager.delUnSubmitOrder(info.getUser(),orderId,itemId);
        return ResponseUtil.setSuccessResult();
    }



    @ApiOperation(value = "下单")
    @PostMapping("/submit")
    public ResponseResult pay(@RequestBody ZYOrderSubmitPayMo mo,
                              @AccessToken AccessInfo info){

        String url = orderManager.submit(mo,info.getUser());
        if(url.equalsIgnoreCase("false")){
            throw new BaseException(StatusCode.FAIL,"订单提交失败，数据已被刷新，请到待支付重新提交");
        }
        return ResponseUtil.setSuccessResult(ImmutableMap.of("url",url));
    }


    @ApiOperation(value = "待支付订单")
    @GetMapping("/unpaid/list")
    public ResponseResult<List<UnpaidOrderVo>> getUnpaidOrder(@Param("订单号,或者药品名称") @RequestParam(value = "name",required = false) String name,
                                                                  @AccessToken AccessInfo info){
        List<ZyOrderItemUnpaidDto> dtoList = orderManager.getUnpaidOrder(info.getUser(),name);


        return ResponseUtil.setSuccessResult();
    }

    @ApiOperation(value = "待支付订单详情列表")
    @GetMapping("/unpaid/detail")
    public ResponseResult<List<UnpaidOrderVo>> getUnpaidDetail(@Param("订单号,或者药品名称") @RequestParam(value = "name",required = false) String name,
                                                              @AccessToken AccessInfo info){
        List<ZyOrderItemUnpaidDto> dtoList = orderManager.getUnpaidDetail(info.getUser(),name);


        return ResponseUtil.setSuccessResult();
    }



    @ApiOperation(value = "删除待提交订单")
    @DeleteMapping("/unpaid/del")
    public ResponseResult delUnpaidOrder(@Param("订单主键") @RequestParam(value = "orderId",required = false) String orderId,
                                                                  @Param("详情主键") @RequestParam(value = "itemId",required = false) String itemId,
                                                                  @AccessToken AccessInfo info){
        orderManager.delUnpaidOrder(info.getUser(),orderId,itemId);
        return ResponseUtil.setSuccessResult();
    }

    @ApiOperation(value = "删除待提交订单")
    @GetMapping("/unpaid/detail/list")
    public ResponseResult<List<UnpaidOrderVo>> delUnpaidOrderDetail(@Param("订单号,或者药品名称") @RequestParam(value = "name",required = false) String name,
                                                                        @AccessToken AccessInfo info){
        List<ZyOrderItemUnpaidDto> dtoList = orderManager.getUnpaidOrder(info.getUser(),name);


        return ResponseUtil.setSuccessResult();
    }

    @ApiOperation(value = "历史订单")
    @PostMapping("/history/list")
    public ResponseResult<PageResult<HistoryOrderVo>> getHistoryOrder(@Param("分页条件")@RequestBody(required = false) PageBase<ZYHistoryQueryMo> mo,
                                                              @AccessToken AccessInfo info){
        PageResult<ZyOrderItemHistoryDto> result = orderManager.getHistoryOrder(info.getUser(),mo == null?new PageBase<>():mo);
        Map<String,List<ZyOrderItemHistoryDto>> map = Maps.newHashMap();
        for(ZyOrderItemHistoryDto dto: result.getData()){
            if(!map.containsKey(dto.getOrderId())){
                map.put(dto.getOrderId(),Lists.newArrayList());
            }
            map.get(dto.getOrderId()).add(dto);
        }

        List<HistoryOrderVo> list = Lists.newArrayList();
        Iterator iterator = map.keySet().iterator();
        while (iterator.hasNext()){
            List<ZyOrderItemHistoryDto> childList = map.get(iterator.next());
            HistoryOrderVo vo = new HistoryOrderVo();
            BeanUtils.copyProperties(childList.get(0),vo);

            List<HistoryOrderVo.HistoryOrderDetailVo> detailVoList = Lists.newArrayList();
            for(ZyOrderItemHistoryDto dto : childList){
                HistoryOrderVo.HistoryOrderDetailVo detailVo = new HistoryOrderVo.HistoryOrderDetailVo();
                BeanUtils.copyProperties(dto,detailVo);
                detailVoList.add(detailVo);
            }
            vo.setDetailVoList(detailVoList);
            list.add(vo);
        }
        PageResult<HistoryOrderVo> pageResult = new PageResult<>();
        pageResult.setData(list);
        pageResult.setTotal(result.getTotal());

        return ResponseUtil.setSuccessResult(result);
    }

    @ApiOperation(value = "删除历史订单")
    @DeleteMapping("/history/del")
    public ResponseResult delHistoryOrder(@Param("订单主键") @RequestParam(value = "orderId",required = false) String orderId,
                                                                @AccessToken AccessInfo info){
        orderManager.delHistoryOrder(info.getUser(),orderId);
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
    public ResponseResult<ZYLogisticsVo> getLogistics(@Param("订单主键") @RequestParam(value = "orderId",required = false) String orderId){
        ZYLogisticsVo obj = zhangYaoManager.getLogistics(orderId);

        return ResponseUtil.setSuccessResult(obj);

    }

    @ApiOperation(value = "获取收件地址")
    @GetMapping("/address/list")
    public ResponseResult<ZyAddress> getAddressList(@Param("是否只查默认地址 0:否；1:是; 默认0,即所有地址信息") @RequestParam(required = false,defaultValue = "0") Integer isOnlyDefault,
                                                         @AccessToken AccessInfo info){
        List<ZyAddress> list = zhangYaoManager.getAddressList(isOnlyDefault,info.getUser());
        return ResponseUtil.setSuccessResult(list);

    }

    @ApiOperation(value ="保存收件地址",hidden = true)
    @PostMapping("/address/save")
    public ResponseResult saveAddress(@RequestBody ZYAddressMo mo,
                                      @AccessToken AccessInfo info){
        zhangYaoManager.saveAddress(mo,info.getUser());
        return ResponseUtil.setSuccessResult();

    }

    @ApiOperation(value ="设置默认地址信息",hidden = true)
    @PostMapping("/address/default")
    public ResponseResult<ZYLogisticsObj> setDefaultAddress(@ApiParam("{\"id\":\"\"} id：地址主键") @RequestBody String param,
                                                      @AccessToken AccessInfo info){
        if(org.apache.commons.lang3.StringUtils.isEmpty(param) || null == JSONObject.parseObject(param).get("id")){
            return ResponseUtil.setParamEmptyError("id");
        }
        zhangYaoManager.setDefaultAddress(JSONObject.parseObject(param).getInteger("id"),info.getUser());
        return ResponseUtil.setSuccessResult();

    }

    @ApiOperation(value = "删除收件地址信息",hidden = true)
    @DeleteMapping("/address/del")
    public ResponseResult delAddress(@Param("地址主键") @RequestParam(value = "id") Integer id,
                                     @AccessToken AccessInfo info){
        zhangYaoManager.delAddress(id,info.getUser());
        return ResponseUtil.setSuccessResult();

    }

    @ApiOperation(value = "掌药药店详情",hidden = true)
    @GetMapping("/store/detail")
    public ResponseResult<ZYStoreDetailObj> getStoreDetail(@Param("药店id") @RequestParam("storeId") String storeId){
        ZYStoreDetailObj detail = zhangYaoManager.getStoreDetail(storeId);
        return ResponseUtil.setSuccessResult(detail);

    }

}
