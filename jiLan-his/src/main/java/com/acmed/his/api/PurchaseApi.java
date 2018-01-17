package com.acmed.his.api;

import com.acmed.his.model.PurchaseMo;
import com.acmed.his.model.dto.PurchaseDto;
import com.acmed.his.model.dto.PurchaseStockDto;
import com.acmed.his.service.PurchaseManager;
import com.acmed.his.support.AccessInfo;
import com.acmed.his.support.AccessToken;
import com.acmed.his.util.PageBase;
import com.acmed.his.util.PageResult;
import com.acmed.his.util.ResponseResult;
import com.acmed.his.util.ResponseUtil;
import com.alibaba.fastjson.JSONObject;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 采购API
 * Created by Darren on 2018-01-03
 **/
@RestController
@Api(tags = "药品进销存",description = "采购入库/入库审核/入库批次查询")
public class PurchaseApi {

    @Autowired
    private PurchaseManager purchaseManager;

    @ApiOperation(value = "采购入库")
    @PostMapping("/purchase/save")
    public ResponseResult save(@RequestBody PurchaseMo mo,
                               @AccessToken AccessInfo info){
        purchaseManager.save(mo,info.getUser());
        return ResponseUtil.setSuccessResult();
    }

    @ApiOperation(value = "入库审核列表")
    @GetMapping("/purchase/audit/list")
    public ResponseResult<PurchaseDto> auditList(@ApiParam("采购单号") @RequestParam(value = "purchaseNo",required = false) String purchaseNo,
                                    @ApiParam("审核状态 0:未审核,1:已审核") @RequestParam(value = "userId",required = false) Integer status,
                                    @ApiParam("供应商") @RequestParam(value = "supplierId",required = false) Integer supplierId,
                                    @ApiParam("采购开始时间") @RequestParam(value = "startTime",required = false) String startTime,
                                    @ApiParam("采购结束时间") @RequestParam(value = "endTime",required = false) String endTime,
                                    @AccessToken AccessInfo info){
        List<PurchaseDto> list = purchaseManager.getAuditList(purchaseNo,status,supplierId,startTime,endTime,info.getUser());
        return ResponseUtil.setSuccessResult(list);
    }

    @ApiOperation(value = "审核入库")
    @PostMapping("/purchase/audit")
    public ResponseResult audit(@ApiParam("{\"id\":\"\"}") @RequestBody String param,
                                @AccessToken AccessInfo info){
        purchaseManager.audit(JSONObject.parseObject(param).get("id").toString(),info.getUser());
        return ResponseUtil.setSuccessResult();
    }

    @ApiOperation(value = "删除入库信息")
    @DeleteMapping("/purchase/del")
    public ResponseResult<PurchaseDto> deleteInfo(@ApiParam("{\"id\":\"\"}") @RequestBody String param,
                                                  @AccessToken AccessInfo info){
        if(org.apache.commons.lang3.StringUtils.isEmpty(param) || null == JSONObject.parseObject(param).get("id")){
            return ResponseUtil.setParamEmptyError("id");
        }
        purchaseManager.deleteInfo(JSONObject.parseObject(param).get("id").toString(),info.getUser());
        return ResponseUtil.setSuccessResult();
    }


    @ApiOperation(value = "批次库存查询")
    @PostMapping("/batch")
    public ResponseResult<PageResult<PurchaseStockDto>> batch(@RequestBody(required = false) PageBase<String> page,
                                                  @AccessToken AccessInfo info){
        List<PurchaseStockDto> list = purchaseManager.getBatchList(page.getPageNum(),page.getPageSize(),page.getParam(),info.getUser());
        Integer total = purchaseManager.getBatchTotal(page.getParam(),info.getUser());
        PageResult result = new PageResult();
        result.setData(list);
        result.setTotal((long)total);
        return ResponseUtil.setSuccessResult(result);
    }


}
