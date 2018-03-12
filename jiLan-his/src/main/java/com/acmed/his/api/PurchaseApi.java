package com.acmed.his.api;

import com.acmed.his.constants.StatusCode;
import com.acmed.his.consts.DicTypeEnum;
import com.acmed.his.dao.DrugMapper;
import com.acmed.his.dao.ManufacturerMapper;
import com.acmed.his.dao.PurchaseItemMapper;
import com.acmed.his.dao.PurchaseMapper;
import com.acmed.his.model.Drug;
import com.acmed.his.model.Purchase;
import com.acmed.his.model.PurchaseItem;
import com.acmed.his.model.dto.PurchaseDto;
import com.acmed.his.model.dto.PurchaseStockDto;
import com.acmed.his.pojo.mo.PurchaseMo;
import com.acmed.his.pojo.vo.PurchaseVo;
import com.acmed.his.service.BaseInfoManager;
import com.acmed.his.service.PermissionManager;
import com.acmed.his.service.PurchaseManager;
import com.acmed.his.service.SupplyManager;
import com.acmed.his.support.AccessInfo;
import com.acmed.his.support.AccessToken;
import com.acmed.his.util.PageBase;
import com.acmed.his.util.PageResult;
import com.acmed.his.util.ResponseResult;
import com.acmed.his.util.ResponseUtil;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import tk.mybatis.mapper.entity.Example;

import java.util.List;
import java.util.Optional;

/**
 * 采购API
 * Created by Darren on 2018-01-03
 **/
@RestController
@Api(tags = "药品进销存",description = "采购入库/入库审核/入库批次查询")
public class PurchaseApi {

    @Autowired
    private PurchaseManager purchaseManager;

    @Autowired
    private PurchaseMapper purchaseMapper;

    @Autowired
    private PurchaseItemMapper purchaseItemMapper;

    @Autowired
    private SupplyManager supplyManager;

    @Autowired
    private DrugMapper drugMapper;

    @Autowired
    private ManufacturerMapper manufacturerMapper;

    @Autowired
    private BaseInfoManager baseInfoManager;

    @Autowired
    private PermissionManager permissionManager;

    @ApiOperation(value = "采购入库")
    @PostMapping("/purchase/save")
    public ResponseResult save(@RequestBody PurchaseMo mo,
                               @AccessToken AccessInfo info){

        Purchase purchase = purchaseMapper.selectByPrimaryKey(mo.getId());
        if(null != purchase && purchase.getStatus()==1){
            return ResponseUtil.setErrorMeg(StatusCode.FAIL,"禁止重复审核");
        }

        purchaseManager.save(mo,info.getUser());
        return ResponseUtil.setSuccessResult();
    }


    @ApiOperation(value = "查看审核权利")
    @PostMapping("/purchase/permission")
    public ResponseResult getPermission(@AccessToken AccessInfo info){

        return ResponseUtil.setSuccessResult(ImmutableMap.of("hasPermission",
                permissionManager.hasPermission(info.getUser().getId().toString(),"rksh")));
    }



    @ApiOperation(value = "入库审核列表")
    @PostMapping("/purchase/audit/list")
    public ResponseResult<PurchaseDto> auditList(@ApiParam("采购单号") @RequestParam(value = "purchaseNo",required = false) String purchaseNo,
                                    @ApiParam("审核状态 0未审核, 1：待审核, 2已审核 , 3:已驳回") @RequestParam(value = "status",required = false) Integer status,
                                    @ApiParam("供应商") @RequestParam(value = "supplierId",required = false) Integer supplierId,
                                    @ApiParam("采购开始时间") @RequestParam(value = "startTime",required = false) String startTime,
                                    @ApiParam("采购结束时间") @RequestParam(value = "endTime",required = false) String endTime,
                                    @AccessToken AccessInfo info){

        List<PurchaseDto> list = purchaseManager.getAuditList(purchaseNo,status,supplierId,startTime,endTime,info.getUser());
        return ResponseUtil.setSuccessResult(list);
    }

    @ApiOperation(value = "入库详情")
    @GetMapping("/purchase/detail")
    public ResponseResult<PurchaseVo> getPurchaseDetail(@ApiParam("入库主键") @RequestParam(value = "id") String id,
                                                        @AccessToken AccessInfo info){
        Purchase purchase = purchaseMapper.selectByPrimaryKey(id);
        Example example = new Example(PurchaseItem.class);
        example.createCriteria().andEqualTo("purchaseId",id);
        List<PurchaseItem> list = purchaseItemMapper.selectByExample(example);
        PurchaseVo vo = new PurchaseVo();
        BeanUtils.copyProperties(purchase,vo);
        vo.setSupplierName(Optional.ofNullable(supplyManager.getSupplyById(vo.getSupplierId())).map(obj->obj.getSupplyerName()).orElse(null));
        List<PurchaseVo.PurchaseVoDetail> detailList = Lists.newArrayList();
        list.forEach(obj->{
            PurchaseVo.PurchaseVoDetail detail = new PurchaseVo.PurchaseVoDetail();
            BeanUtils.copyProperties(obj,detail);
            Drug drug = drugMapper.selectByPrimaryKey(obj.getDrugId());
            detail.setName(drug.getName());
            detail.setGoodsName(drug.getGoodsName());
            detail.setSpec(drug.getSpec());
            detail.setUnit(drug.getUnit());
            detail.setUnitName(null == drug.getUnit()?"":baseInfoManager.getDicItem(DicTypeEnum.UNIT.getCode(),drug.getUnit().toString()).getDicItemName());
            detail.setManufacturerName(null == drug.getManufacturer()?"":manufacturerMapper.selectByPrimaryKey(drug.getManufacturer()).getName());

            detailList.add(detail);
        });
        vo.setDetailList(detailList);
        return ResponseUtil.setSuccessResult(vo);
    }

    @ApiOperation(value = "审核入库")
    @PostMapping("/purchase/audit")
    public ResponseResult audit(@ApiParam("{\"id\":\"\"}") @RequestBody String param,
                                @AccessToken AccessInfo info){
        purchaseManager.audit(JSONObject.parseObject(param).get("id").toString(),info.getUser());
        return ResponseUtil.setSuccessResult();
    }

    @ApiOperation(value = "驳回")
    @PostMapping("/purchase/reject")
    public ResponseResult reject(@ApiParam("{\"id\":\"\"}") @RequestBody String param,
                                @AccessToken AccessInfo info){
        purchaseManager.reject(JSONObject.parseObject(param).get("id").toString(),info.getUser());
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
    @PostMapping("/purchase/batch")
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
