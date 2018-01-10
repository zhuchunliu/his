package com.acmed.his.api;

import com.acmed.his.model.dto.DrugDayDetailDto;
import com.acmed.his.model.dto.DrugDayDto;
import com.acmed.his.model.dto.PurchaseDayDetailDto;
import com.acmed.his.model.dto.PurchaseDayDto;
import com.acmed.his.service.PrescriptionManager;
import com.acmed.his.service.PurchaseManager;
import com.acmed.his.service.ReportDrugManager;
import com.acmed.his.support.AccessInfo;
import com.acmed.his.support.AccessToken;
import com.acmed.his.util.PageBase;
import com.acmed.his.util.PageResult;
import com.acmed.his.util.ResponseResult;
import com.acmed.his.util.ResponseUtil;
import com.google.common.collect.Maps;
import io.swagger.annotations.*;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

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


    @ApiModelProperty("当前")
    @GetMapping("/recent")
    @ApiResponse(code = 100,message = "saleFee:出库金额,purchaseFee:入库金额")
    public ResponseResult getRecentStatis(@AccessToken AccessInfo info){
        Double saleFee = prescriptionManager.getCurrentDayItemFee(info.getUser().getOrgCode());
        Double purchaseFee =purchaseManager.getCurrentDayFee(info.getUser().getOrgCode());
        Map<String,Double> map = Maps.newHashMap();
        map.put("saleFee",saleFee);
        map.put("purchaseFee",purchaseFee);
        return ResponseUtil.setSuccessResult(map);

    }

    @ApiOperation(value = "药品出库统计")
    @PostMapping("/sale/page")
    public ResponseResult<PageResult<DrugDayDto>> getSalePageList(@RequestBody(required = false) PageBase pageBase,
                                                          @ApiParam("开始时间") @RequestParam(value = "startTime",required = false) String startTime,
                                                          @ApiParam("结束时间") @RequestParam(value = "endTime",required = false) String endTime,
                                                          @AccessToken AccessInfo info){

        List<DrugDayDto> list = reportDrugManager.getDrugDayList(info.getUser().getOrgCode(),startTime,endTime,pageBase.getPageNum(), pageBase.getPageSize());
        int total = reportDrugManager.getDrugDayTotal(info.getUser().getOrgCode(),startTime,endTime);
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

        List<DrugDayDto> list =  reportDrugManager.getDrugDayList(info.getUser().getOrgCode(),startTime,endTime,1, 10);
        return ResponseUtil.setSuccessResult(list);
    }

    @ApiOperation(value = "药品出库明细")
    @PostMapping("/sale/detail")
    public ResponseResult<PageResult<DrugDayDetailDto>> getSaleDetail(@RequestBody(required = false) PageBase pageBase,
                                                                      @ApiParam("开始时间") @RequestParam(value = "startTime",required = false) String startTime,
                                                                      @ApiParam("结束时间") @RequestParam(value = "endTime",required = false) String endTime,
                                                                      @AccessToken AccessInfo info){

        List<DrugDayDetailDto> list =  reportDrugManager.getDrugDayDetailList(info.getUser().getOrgCode(),startTime,endTime,1, 10);
        int total = reportDrugManager.getDrugDayDetailTotal(info.getUser().getOrgCode(),startTime,endTime);
        PageResult pageResult = new PageResult();
        BeanUtils.copyProperties(pageBase,pageResult);
        pageResult.setTotal((long)total);
        pageResult.setData(list);
        return ResponseUtil.setSuccessResult(list);
    }

    @ApiOperation(value = "药品入库列表")
    @PostMapping("/purchase/page")
    public ResponseResult<PageResult<PurchaseDayDto>> getPurchasePageList(@RequestBody(required = false) PageBase pageBase,
                                                                         @ApiParam("开始时间") @RequestParam(value = "startTime",required = false) String startTime,
                                                                         @ApiParam("结束时间") @RequestParam(value = "endTime",required = false) String endTime,
                                                                         @AccessToken AccessInfo info){

        List<PurchaseDayDto> list =  reportDrugManager.getPurchaseDayList(info.getUser().getOrgCode(),startTime,endTime,1, 10);
        int total = reportDrugManager.getPurchaseDayTotal(info.getUser().getOrgCode(),startTime,endTime);
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
    public ResponseResult<PageResult<PurchaseDayDetailDto>> getPurchaseDayDetail(@RequestBody(required = false) PageBase pageBase,
                                                                                 @ApiParam("开始时间") @RequestParam(value = "startTime",required = false) String startTime,
                                                                                 @ApiParam("结束时间") @RequestParam(value = "endTime",required = false) String endTime,
                                                                                 @AccessToken AccessInfo info){

        List<PurchaseDayDetailDto> list =  reportDrugManager.getPurchaseDayDetailList(info.getUser().getOrgCode(),startTime,endTime,1, 10);
        int total = reportDrugManager.getPurchaseDayDetailTotal(info.getUser().getOrgCode(),startTime,endTime);
        PageResult pageResult = new PageResult();
        BeanUtils.copyProperties(pageBase,pageResult);
        pageResult.setTotal((long)total);
        pageResult.setData(list);
        return ResponseUtil.setSuccessResult(pageResult);
    }



}
