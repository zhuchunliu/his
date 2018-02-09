package com.acmed.his.api;

import com.acmed.his.constants.StatusCode;
import com.acmed.his.dao.*;
import com.acmed.his.model.*;
import com.acmed.his.model.dto.DispensingDto;
import com.acmed.his.pojo.mo.DispensQueryMo;
import com.acmed.his.pojo.mo.DispensingRefundMo;
import com.acmed.his.pojo.vo.*;
import com.acmed.his.service.BaseInfoManager;
import com.acmed.his.service.DispensingManager;
import com.acmed.his.support.AccessInfo;
import com.acmed.his.support.AccessToken;
import com.acmed.his.util.*;
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
import tk.mybatis.mapper.entity.Example;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * 发药接口
 *
 * Created by Darren on 2017-12-05
 **/
@RestController
@Api(tags = "发药管理")
@RequestMapping("/dispens")
public class DispensingApi {

    @Autowired
    private ChargeMapper chargeMapper;

    @Autowired
    private PrescriptionMapper preMapper;

    @Autowired
    private PrescriptionItemMapper preItemMapper;

    @Autowired
    private InspectMapper inspectMapper;

    @Autowired
    private BaseInfoManager baseInfoManager;

    @Autowired
    private DispensingManager dispensingManager;

    @Autowired
    private PrescriptionItemStockMapper preItemStockMapper;

    @Autowired
    private PayStatementsMapper payStatementsMapper;

    @Autowired
    private PayRefuseMapper payRefuseMapper;


    @ApiOperation(value = "收支概况")
    @GetMapping("/fee/survey")
    public ResponseResult<DispensingFeeSurveyVo> getFeeSurvey(@Param("日期 yyyy-MM-dd格式 默认当天")@RequestParam(required = false) String date,
                                                              @AccessToken AccessInfo info){
        date = Optional.ofNullable(date).orElse(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        List<Map<String,Object>> payList = payStatementsMapper.getFeeSurvey(date,info.getUser().getOrgCode());
        List<Map<String,Object>> refundList = payRefuseMapper.getFeeSurvey(date,info.getUser().getOrgCode());

        return ResponseUtil.setSuccessResult(new DispensingFeeSurveyVo(payList,refundList));
    }

    @ApiOperation(value = "收费发药列表")
    @PostMapping("/list")
    public ResponseResult<PageResult<DispensingVo>> getDispensingList(
            @RequestBody(required = false) PageBase<DispensQueryMo> mo,
            @AccessToken AccessInfo info){
        List<DispensingVo> list = new ArrayList<>();
        List<DispensingDto> applyList = dispensingManager.getDispensingList(mo.getPageNum(),mo.getPageSize(),
                info.getUser().getOrgCode(), Optional.ofNullable(mo.getParam()).map(obj->obj.getName()).orElse(null),
                Optional.ofNullable(mo.getParam()).map(obj->obj.getStatus()).orElse(null),
                Optional.ofNullable(mo.getParam()).map(obj->obj.getDate()).orElse(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))));
        applyList.forEach(obj->{
            DispensingVo vo = new DispensingVo();
            BeanUtils.copyProperties(obj,vo);
            if("0".equals(obj.getIsPaid())) vo.setStatus("1");
            if("1".equals(obj.getIsPaid()) && "0".equals(obj.getIsDispensing())) vo.setStatus("2");
            if("2".equals(obj.getIsPaid())) vo.setStatus("3");
            if("3".equals(obj.getIsPaid())) vo.setStatus("4");
            if("1".equals(obj.getIsDispensing()) || ("1".equals(obj.getIsPaid()) && "0".equals(obj.getContanisMedicine()))) vo.setStatus("5");

            list.add(vo);
        });
        int total = dispensingManager.getDispensingTotal(info.getUser().getOrgCode(),
                Optional.ofNullable(mo.getParam()).map(obj->obj.getName()).orElse(null),
                Optional.ofNullable(mo.getParam()).map(obj->obj.getStatus()).orElse(null),
                Optional.ofNullable(mo.getParam()).map(obj->obj.getDate()).orElse(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))));
        return ResponseUtil.setSuccessResult(new PageResult(list,(long)total));
    }

    @ApiOperation(value = "付费费用列表，支付页面显示")
    @GetMapping("/pay/fee")
    public ResponseResult<PrescriptionFeeVo> getPreFeeList(@ApiParam("挂号主键") @RequestParam("applyId") String applyId){
        List<PrescriptionFee> sourceList = dispensingManager.getPreFeeList(applyId);
        List<PrescriptionFeeVo> list = Lists.newArrayList();
        sourceList.forEach(obj->{
            PrescriptionFeeVo vo = new PrescriptionFeeVo();
            BeanUtils.copyProperties(obj,vo);
            list.add(vo);
        });
        return ResponseUtil.setSuccessResult(list);
    }


    @ApiOperation(value = "付费")
    @PostMapping("/pay")
    public ResponseResult pay(@ApiParam("{\"applyId\":\"\",\"feeType\":\"\"},applyId：处方主键、feeType：付费类型") @RequestBody String param,
                              @AccessToken AccessInfo info){
        if(StringUtils.isEmpty(param) || null == JSONObject.parseObject(param).get("applyId")){
            return ResponseUtil.setParamEmptyError("applyId");
        }
        dispensingManager.pay(JSONObject.parseObject(param).getString("applyId"),
                JSONObject.parseObject(param).getString("feeType"),info.getUser());
        return ResponseUtil.setSuccessResult();
    }

    @ApiOperation(value = "确认发药")
    @PostMapping
    public ResponseResult dispensing(@ApiParam("{\"applyId\":},applyId：挂号单id") @RequestBody String param,
                                     @AccessToken AccessInfo info){
        if(StringUtils.isEmpty(param) || null == JSONObject.parseObject(param).get("applyId")){
            return ResponseUtil.setParamEmptyError("applyId");
        }
        dispensingManager.dispensing(JSONObject.parseObject(param).getString("applyId"),info.getUser());
        return ResponseUtil.setSuccessResult();
    }


    @ApiOperation(value = "退款")
    @PostMapping("/refund")
    public ResponseResult refund(@RequestBody DispensingRefundMo mo,
                                 @AccessToken AccessInfo info){
        Prescription prescription = preMapper.getPreByApply(mo.getApplyId()).get(0);
        if("0".equals(prescription.getIsPaid())){
            return ResponseUtil.setErrorMeg(StatusCode.FAIL,"尚未付款，无法退款！");
        }
        if("1".equals(prescription.getIsDispensing())){
            return ResponseUtil.setErrorMeg(StatusCode.FAIL,"已经发药，无法退款！");
        }
        dispensingManager.refund(mo,info.getUser());
        return ResponseUtil.setSuccessResult();
    }

    @ApiOperation(value = "退费列表")
    @GetMapping("/refund/list")
    public ResponseResult<DispensingRefundVo> getDispensingDetail(@ApiParam("挂号主键") @RequestParam("applyId") String applyId){

        Example example = new Example(Prescription.class);
        example.createCriteria().andEqualTo("applyId",applyId);
        Prescription prescription = preMapper.selectByExample(example).get(0);

        example = new Example(PrescriptionItem.class);
        example.createCriteria().andEqualTo("applyId",applyId);
        List<PrescriptionItem> itemList = preItemMapper.selectByExample(example);

        example = new Example(Inspect.class);
        example.createCriteria().andEqualTo("applyId",applyId);
        example.orderBy("id").asc();
        List<Inspect> inspectList = inspectMapper.selectByExample(example);


        example = new Example(Charge.class);
        example.createCriteria().andEqualTo("applyId",applyId);
        example.orderBy("id").asc();
        List<Charge> chargeList = chargeMapper.selectByExample(example);


        return ResponseUtil.setSuccessResult(new DispensingRefundVo(prescription,itemList,inspectList,chargeList,baseInfoManager));
    }


    @ApiOperation(value = "发药")
    @PostMapping("/medicine")
    public ResponseResult medicine(@ApiParam("{\"applyId\":\"\"},applyId：处方主键") @RequestBody String param,
                                 @AccessToken AccessInfo info){

        if(StringUtils.isEmpty(param) || null == JSONObject.parseObject(param).get("applyId")){
            return ResponseUtil.setParamEmptyError("applyId");
        }
        dispensingManager.medicine(JSONObject.parseObject(param).getString("applyId"),info.getUser());
        return ResponseUtil.setSuccessResult();
    }

    @ApiOperation(value = "发药列表")
    @GetMapping("/medicine/list")
    public ResponseResult<DispensingMedicineVo> getMedicineDetail(@ApiParam("挂号主键") @RequestParam("applyId") String applyId,
                                                                  @AccessToken AccessInfo info){

        dispensingManager.lockStock(applyId,info.getUser());//锁定库存

        Prescription prescription = preMapper.getPreByApply(applyId).get(0);

        Example example = new Example(PrescriptionItem.class);
        example.createCriteria().andEqualTo("applyId",applyId).andEqualTo("payStatus","1");
        List<PrescriptionItem> itemList = preItemMapper.selectByExample(example);

        Map<String,List<PrescriptionItemStock>> map = Maps.newHashMap();
        itemList.forEach(obj->{
            Example stockExample = new Example(PrescriptionItemStock.class);
            stockExample.createCriteria().andEqualTo("itemId",obj.getId());
            map.put(obj.getId(),preItemStockMapper.selectByExample(stockExample));
        });



        return ResponseUtil.setSuccessResult(new DispensingMedicineVo(prescription,itemList,map));
    }





}
