package com.acmed.his.api;

import com.acmed.his.model.dto.DrugDayDetailDto;
import com.acmed.his.model.dto.DrugDayDto;
import com.acmed.his.model.dto.PurchaseDayDetailDto;
import com.acmed.his.model.dto.PurchaseDayDto;
import com.acmed.his.pojo.mo.ReportQueryMo;
import com.acmed.his.service.PrescriptionManager;
import com.acmed.his.service.PurchaseManager;
import com.acmed.his.service.ReportDrugManager;
import com.acmed.his.support.AccessInfo;
import com.acmed.his.support.AccessToken;
import com.acmed.his.util.*;
import com.google.common.collect.Maps;
import io.swagger.annotations.*;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.text.DecimalFormat;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Created by Darren on 2018-01-08
 **/
@Api(tags = "报表-药品出入库统计")
@RequestMapping("/report")
@RestController
public class ReportDrugApi {

    @Autowired
    private ReportDrugManager reportDrugManager;

    @Autowired
    private PrescriptionManager prescriptionManager;

    @Autowired
    private PurchaseManager purchaseManager;


    @ApiOperation("当天出入库实时统计")
    @GetMapping("/drug/recent")
    @ApiResponse(code = 100,message = "saleFee:出库金额,purchaseFee:入库金额")
    public ResponseResult getRecentStatis(@AccessToken AccessInfo info){
        Double saleFee = prescriptionManager.getCurrentDayItemFee(info.getUser().getOrgCode());
        Double purchaseFee =purchaseManager.getCurrentDayFee(info.getUser().getOrgCode());
        Map<String,Double> map = Maps.newHashMap();
        map.put("saleFee",saleFee);
        map.put("purchaseFee",purchaseFee);
        return ResponseUtil.setSuccessResult(map);

    }

    @ApiOperation("收支概况")
    @GetMapping("/drug/survey")
    @ApiResponse(code = 100,message = "saleFee:出库金额,purchaseFee:入库金额")
    public ResponseResult getSurveyStatis(@ApiParam("开始时间") @RequestParam(value = "startTime",required = false) String startTime,
                                          @ApiParam("结束时间") @RequestParam(value = "endTime",required = false) String endTime,
                                          @AccessToken AccessInfo info){
        startTime = Optional.ofNullable(startTime).map(DateTimeUtil::getBeginDate).orElse(null);
        endTime = Optional.ofNullable(endTime).map(DateTimeUtil::getEndDate).orElse(null);

        Double saleFee = prescriptionManager.getSurveySaleFee(info.getUser().getOrgCode(),startTime,endTime);
        Double purchaseFee =prescriptionManager.getSurveyPurchaseFee(info.getUser().getOrgCode(),startTime,endTime);
        Map<String,Object> map = Maps.newHashMap();
        map.put("saleFee",saleFee);
        map.put("purchaseFee",purchaseFee);
        map.put("profit",new DecimalFormat("#.00").format(saleFee.doubleValue()-purchaseFee.doubleValue()));

        return ResponseUtil.setSuccessResult(map);

    }

    @ApiOperation(value = "药品出库统计")
    @PostMapping("/sale/page")
    public ResponseResult<PageResult<DrugDayDto>> getSalePageList(@RequestBody(required = false) PageBase<ReportQueryMo> pageBase,
                                                          @AccessToken AccessInfo info){

        List<DrugDayDto> list = reportDrugManager.getDrugDayList(info.getUser().getOrgCode(),
                Optional.ofNullable(pageBase.getParam()).map(obj->obj.getStartTime()).orElse(null),
                Optional.ofNullable(pageBase.getParam()).map(obj->obj.getEndTime()).orElse(null)
                ,pageBase.getPageNum(), pageBase.getPageSize());
        int total = reportDrugManager.getDrugDayTotal(info.getUser().getOrgCode(),
                Optional.ofNullable(pageBase.getParam()).map(obj->obj.getStartTime()).orElse(null),
                Optional.ofNullable(pageBase.getParam()).map(obj->obj.getEndTime()).orElse(null));
        PageResult pageResult = new PageResult();
        BeanUtils.copyProperties(pageBase,pageResult);
        pageResult.setTotal((long)total);
        pageResult.setData(list);
        return ResponseUtil.setSuccessResult(pageResult);
    }

    @ApiOperation(value = "药品出库统计")
    @GetMapping("/sale/list")
    public ResponseResult<DrugDayDto> getSaleList(@ApiParam("开始时间") @RequestParam(value = "startTime",required = false) String startTime,
                                                @ApiParam("结束时间") @RequestParam(value = "endTime",required = false) String endTime,
                                                @AccessToken AccessInfo info){

        startTime = Optional.ofNullable(startTime).map(DateTimeUtil::getBeginDate).orElse(null);
        endTime = Optional.ofNullable(endTime).map(DateTimeUtil::getEndDate).orElse(null);

        List<DrugDayDto> list =  reportDrugManager.getDrugDayList(info.getUser().getOrgCode(),startTime,endTime,1, 10);
        return ResponseUtil.setSuccessResult(list);
    }

    @ApiOperation(value = "药品出库明细")
    @PostMapping("/sale/detail")
    public ResponseResult<PageResult<DrugDayDetailDto>> getSaleDetail(@RequestBody(required = false) PageBase<ReportQueryMo> pageBase,
                                                                      @AccessToken AccessInfo info){

        List<DrugDayDetailDto> list =  reportDrugManager.getDrugDayDetailList(info.getUser().getOrgCode(),
                Optional.ofNullable(pageBase.getParam()).map(obj->obj.getStartTime()).orElse(null),
                Optional.ofNullable(pageBase.getParam()).map(obj->obj.getEndTime()).orElse(null),pageBase.getPageNum(), pageBase.getPageSize());
        int total = reportDrugManager.getDrugDayDetailTotal(info.getUser().getOrgCode(),
                Optional.ofNullable(pageBase.getParam()).map(obj->obj.getStartTime()).orElse(null),
                Optional.ofNullable(pageBase.getParam()).map(obj->obj.getEndTime()).orElse(null));
        PageResult pageResult = new PageResult();
        BeanUtils.copyProperties(pageBase,pageResult);
        pageResult.setTotal((long)total);
        pageResult.setData(list);
        return ResponseUtil.setSuccessResult(pageResult);
    }

    @ApiOperation(value = "药品入库列表")
    @PostMapping("/purchase/page")
    public ResponseResult<PageResult<PurchaseDayDto>> getPurchasePageList(@RequestBody(required = false) PageBase<ReportQueryMo> pageBase,
                                                                         @AccessToken AccessInfo info){

        List<PurchaseDayDto> list =  reportDrugManager.getPurchaseDayList(info.getUser().getOrgCode(),
                Optional.ofNullable(pageBase.getParam()).map(obj->obj.getStartTime()).orElse(null),
                Optional.ofNullable(pageBase.getParam()).map(obj->obj.getEndTime()).orElse(null),pageBase.getPageNum(), pageBase.getPageSize());
        int total = reportDrugManager.getPurchaseDayTotal(info.getUser().getOrgCode(),
                Optional.ofNullable(pageBase.getParam()).map(obj->obj.getStartTime()).orElse(null),
                Optional.ofNullable(pageBase.getParam()).map(obj->obj.getEndTime()).orElse(null));
        PageResult pageResult = new PageResult();
        BeanUtils.copyProperties(pageBase,pageResult);
        pageResult.setTotal((long)total);
        pageResult.setData(list);
        return ResponseUtil.setSuccessResult(pageResult);
    }

    @ApiOperation(value = "药品入库列表")
    @GetMapping("/purchase/list")
    public ResponseResult<PurchaseDayDto> getPurchaseList(@ApiParam("开始时间") @RequestParam(value = "startTime",required = false) String startTime,
                                                                @ApiParam("结束时间") @RequestParam(value = "endTime",required = false) String endTime,
                                                                @AccessToken AccessInfo info){

        List<PurchaseDayDto> list =  reportDrugManager.getPurchaseDayList(info.getUser().getOrgCode(),startTime,endTime,1, 10);
        return ResponseUtil.setSuccessResult(list);
    }

    @ApiOperation(value = "药品入库明细")
    @PostMapping("/purchase/detail")
    public ResponseResult<PageResult<PurchaseDayDetailDto>> getPurchaseDayDetail(@RequestBody(required = false) PageBase<ReportQueryMo> pageBase,
                                                                                 @AccessToken AccessInfo info){

        List<PurchaseDayDetailDto> list =  reportDrugManager.getPurchaseDayDetailList(info.getUser().getOrgCode(),
                Optional.ofNullable(pageBase.getParam()).map(obj->obj.getStartTime()).orElse(null),
                Optional.ofNullable(pageBase.getParam()).map(obj->obj.getEndTime()).orElse(null),pageBase.getPageNum(), pageBase.getPageSize());
        int total = reportDrugManager.getPurchaseDayDetailTotal(info.getUser().getOrgCode(),
                Optional.ofNullable(pageBase.getParam()).map(obj->obj.getStartTime()).orElse(null),
                Optional.ofNullable(pageBase.getParam()).map(obj->obj.getEndTime()).orElse(null));
        PageResult pageResult = new PageResult();
        BeanUtils.copyProperties(pageBase,pageResult);
        pageResult.setTotal((long)total);
        pageResult.setData(list);
        return ResponseUtil.setSuccessResult(pageResult);
    }



}
