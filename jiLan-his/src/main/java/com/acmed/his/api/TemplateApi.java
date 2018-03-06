package com.acmed.his.api;

import com.acmed.his.constants.StatusCode;
import com.acmed.his.consts.DicTypeEnum;
import com.acmed.his.dao.InspectTplMapper;
import com.acmed.his.dao.ManufacturerMapper;
import com.acmed.his.dao.PrescriptionTplItemMapper;
import com.acmed.his.dao.PrescriptionTplMapper;
import com.acmed.his.model.*;
import com.acmed.his.model.dto.AdviceTplDto;
import com.acmed.his.model.dto.DiagnosisTplDto;
import com.acmed.his.model.dto.PrescriptionTplDto;
import com.acmed.his.pojo.mo.*;
import com.acmed.his.pojo.vo.InspectTplVo;
import com.acmed.his.pojo.vo.PrescriptionTplItemVo;
import com.acmed.his.service.*;
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
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import tk.mybatis.mapper.entity.Example;

import java.util.*;

/**
 * Created by Darren on 2017-11-20
 **/
@Api(tags = "模板信息",description = "诊断模板、医嘱模板、处方模板")
@RestController
@RequestMapping("/tpl")
public class TemplateApi {

    @Autowired
    private TemplateManager templateManager;

    @Autowired
    private DrugManager drugManager;

    @Autowired
    private PrescriptionTplMapper preTplMapper;

    @Autowired
    private PrescriptionTplItemMapper preTplItemMapper;

    @Autowired
    private InspectTplMapper inspectTplMapper;

    @Autowired
    private FeeItemManager feeItemManager;

    @Autowired
    private ManufacturerMapper manufacturerMapper;

    @Autowired
    private BaseInfoManager baseInfoManager;

    @ApiOperation(value = "新增/编辑 诊断模板")
    @PostMapping("/diagnosis/save")
    public ResponseResult saveDiagnosisList(@ApiParam("id等于null:新增; id不等于null：编辑") @RequestBody DiagnosisTplMo mo,
                                            @AccessToken AccessInfo info){
        if(StringUtils.isEmpty(mo.getCategory())){
            return ResponseUtil.setParamEmptyError("category");
        }
        templateManager.saveDiagnosisTpl(mo,info.getUser());
        return ResponseUtil.setSuccessResult();
    }

    @ApiOperation(value = "诊断模板列表")
    @PostMapping("/diagnosis/list")
    public ResponseResult<PageResult<DiagnosisTplDto>> getDiagnosisList(@RequestBody(required = false) PageBase<TplQueryMo> pageBase,
                                                                        @AccessToken AccessInfo info){
        PageResult result = new PageResult();
        List<DiagnosisTplDto> list = templateManager.getDiagnosisTplList(pageBase.getParam(),pageBase.getPageNum(),
                pageBase.getPageSize(),info.getUser());
        result.setData(list);
        result.setTotal((long)templateManager.getDiagnosisTplTotal(pageBase.getParam(),info.getUser()));
        return ResponseUtil.setSuccessResult(result);
    }

    @ApiOperation(value = "诊断模板-禁用数")
    @GetMapping("/diagnosis/disable/num")
    public ResponseResult<PageResult<DiagnosisTplDto>> getDiagnosisDisableNum(@AccessToken AccessInfo info){
        TplQueryMo mo = new TplQueryMo();
        mo.setIsValid("0");
        return ResponseUtil.setSuccessResult(ImmutableMap.of("num",templateManager.getDiagnosisTplTotal(mo,info.getUser())));
    }


    @ApiOperation(value = "删除诊断模板")
    @DeleteMapping("/diagnosis/del")
    public ResponseResult delDiagnosis(@ApiParam("模板主键") @RequestParam Integer id,
                                       @AccessToken AccessInfo info){
        templateManager.delDiagnosisTpl(id,info.getUser());
        return ResponseUtil.setSuccessResult();
    }

    @ApiOperation(value = "禁用/启用 诊断模板")
    @PostMapping("/diagnosis/switch")
    public ResponseResult switchDiagnosisTpl(@ApiParam("{\"id\":\"\"} id：模板主键") @RequestBody String param,
                                            @AccessToken AccessInfo info){
        if(org.apache.commons.lang3.StringUtils.isEmpty(param) || null == JSONObject.parseObject(param).get("id")){
            return ResponseUtil.setParamEmptyError("id");
        }
        templateManager.switchDiagnosisTpl(JSONObject.parseObject(param).getInteger("id"),info.getUser());
        return ResponseUtil.setSuccessResult();
    }


    @ApiOperation(value = "新增/编辑 医嘱模板")
    @PostMapping("/advice/save")
    public ResponseResult saveAdviceTpl(@ApiParam("id等于null:新增; id不等于null：编辑") @RequestBody AdviceTplMo mo,
                                        @AccessToken AccessInfo info){
        if(StringUtils.isEmpty(mo.getCategory())){
            return ResponseUtil.setParamEmptyError("category");
        }
        templateManager.saveAdviceTpl(mo,info.getUser());
        return ResponseUtil.setSuccessResult();
    }

    @ApiOperation(value = "医嘱模板列表")
    @PostMapping("/advice/list")
    public ResponseResult<PageResult<DiagnosisTplDto>> getAdviceTplList(@RequestBody(required = false) PageBase<TplQueryMo> pageBase,
                                                                        @AccessToken AccessInfo info){
        PageResult result = new PageResult();
        List<AdviceTplDto> list = templateManager.getAdviceTplList(pageBase.getParam(),pageBase.getPageNum(),
                pageBase.getPageSize(),info.getUser());
        result.setData(list);
        result.setTotal((long)templateManager.getAdviceTplTotal(pageBase.getParam(),info.getUser()));
        return ResponseUtil.setSuccessResult(result);
    }

    @ApiOperation(value = "医嘱模板-禁用数")
    @GetMapping("/advice/disable/num")
    public ResponseResult<PageResult<DiagnosisTplDto>> getAdviceDisableNum(@AccessToken AccessInfo info){
        TplQueryMo mo = new TplQueryMo();
        mo.setIsValid("0");
        return ResponseUtil.setSuccessResult(ImmutableMap.of("num",templateManager.getAdviceTplTotal(mo,info.getUser())));
    }


    @ApiOperation(value = "删除医嘱模板")
    @DeleteMapping("/advice/del")
    public ResponseResult delAdviceDetail(@ApiParam("模板主键") @RequestParam Integer id,
                                          @AccessToken AccessInfo info){

        templateManager.delAdviceTpl(id,info.getUser());
        return ResponseUtil.setSuccessResult();
    }

    @ApiOperation(value = "禁用/启用 医嘱模板")
    @PostMapping("/advice/switch")
    public ResponseResult switchAdviceTpl(@ApiParam("{\"id\":\"\"} id：模板主键") @RequestBody String param,
                                             @AccessToken AccessInfo info){
        if(org.apache.commons.lang3.StringUtils.isEmpty(param) || null == JSONObject.parseObject(param).get("id")){
            return ResponseUtil.setParamEmptyError("id");
        }
        templateManager.switchAdviceTpl(JSONObject.parseObject(param).getInteger("id"),info.getUser());
        return ResponseUtil.setSuccessResult();
    }


    @ApiOperation(value = "新增/编辑 处方模板")
    @PostMapping("/prescripTpl/save")
    public ResponseResult savePrescripTpl(@ApiParam("id等于null:新增; id不等于null：编辑") @RequestBody PrescriptionTplMo mo,
                                          @AccessToken AccessInfo info){

        Integer id = templateManager.savePrescripTpl(mo,info.getUser());
        return ResponseUtil.setSuccessResult(ImmutableMap.of("id",id));
    }

    @ApiOperation(value = "获取指定机构下的处方模板")
    @PostMapping("/prescripTpl/list")
    public ResponseResult<PageResult<PrescriptionTplDto>> getPrescripTplList(@RequestBody(required = false) PageBase<PrescriptionQueryTplMo> page,
                                                                             @AccessToken AccessInfo info){
        PageResult result = new PageResult();
        result.setData(templateManager.getPrescripTplList(page.getParam(),page.getPageNum(),page.getPageSize(), info.getUser()));
        result.setTotal((long)templateManager.getPrescripTplTotal(page.getParam(), info.getUser()));
        return ResponseUtil.setSuccessResult(result);
    }

    @ApiOperation(value = "处方模板-禁用数")
    @GetMapping("/prescripTpl/disable/num")
    public ResponseResult<PageResult<DiagnosisTplDto>> getPrescripDisableNum(@AccessToken AccessInfo info){
        PrescriptionQueryTplMo mo = new PrescriptionQueryTplMo();
        mo.setIsValid("0");
        return ResponseUtil.setSuccessResult(ImmutableMap.of("num",templateManager.getPrescripTplTotal(mo,info.getUser())));
    }

    @ApiOperation(value = "禁用/启用 处方模板")
    @PostMapping("/prescripTpl/switch")
    public ResponseResult switchPrescripTpl(@ApiParam("{\"id\":\"\"} id：模板主键") @RequestBody String param,
                                         @AccessToken AccessInfo info){
        if(org.apache.commons.lang3.StringUtils.isEmpty(param) || null == JSONObject.parseObject(param).get("id")){
            return ResponseUtil.setParamEmptyError("id");
        }
        templateManager.switchPrescripTpl(JSONObject.parseObject(param).getInteger("id"),info.getUser());
        return ResponseUtil.setSuccessResult();
    }

    @ApiOperation(value = "删除 处方模板")
    @DeleteMapping("/prescripTpl/del")
    public ResponseResult delPrescripTpl(@ApiParam("模板主键") @RequestParam Integer id,
                                            @AccessToken AccessInfo info){

        templateManager.delPrescripTpl(id,info.getUser());
        return ResponseUtil.setSuccessResult();
    }

    @ApiOperation(value = "获取处方配置详情")
    @GetMapping("/prescripTpl/detail")
    public ResponseResult getPrescripTplDetail(@ApiParam("模板主键") @RequestParam("id") Integer id,
                                               @AccessToken AccessInfo info){
        PrescriptionTpl prescriptionTpl = preTplMapper.selectByPrimaryKey(id);
        if(prescriptionTpl.getCategory().equals("1")){//药品处方
            Example example = new Example(PrescriptionTplItem.class);
            example.createCriteria().andEqualTo("tplId",id);
            List<PrescriptionTplItemVo> list = Lists.newArrayList();
            preTplItemMapper.selectByExample(example).forEach(obj->{
                PrescriptionTplItemVo vo = new PrescriptionTplItemVo();
                Drug drug = drugManager.getDrugById(obj.getDrugId());

                BeanUtils.copyProperties(obj,vo);
                vo.setDrugId(drug.getId());
                vo.setDrugName(Optional.ofNullable(drug.getGoodsName()).orElse(drug.getName()));
                vo.setCategory(drug.getCategory());
                vo.setCategoryName(null==drug.getCategory()?"":baseInfoManager.getDicItem(DicTypeEnum.DRUG_CLASSIFICATION.getCode(),drug.getCategory().toString()).getDicItemName());
                vo.setSpec(drug.getSpec());
                vo.setManufacturerName(Optional.ofNullable(drug.getManufacturer()).map(manu->manufacturerMapper.selectByPrimaryKey(manu)).
                        filter(manu->manu!=null).map(manu->manu.getName()).orElse(""));
                vo.setFrequencyName(Optional.ofNullable(baseInfoManager.getDicItem(DicTypeEnum.DRUG_FREQUENCY.getCode(),obj.getFrequency())).map(item->item.getDicItemName()).orElse(null));

                vo.setUnitName(null == drug.getUnit()?"":baseInfoManager.getDicItem(DicTypeEnum.UNIT.getCode(),drug.getUnit().toString()).getDicItemName());
                if(null != drug.getMinPriceUnitType()) {
                    vo.setMinOrDoseUnitName(1 == drug.getMinPriceUnitType() ? baseInfoManager.getDicItem(DicTypeEnum.UNIT.getCode(), drug.getMinUnit().toString()).getDicItemName() :
                            baseInfoManager.getDicItem(DicTypeEnum.UNIT.getCode(), drug.getDoseUnit().toString()).getDicItemName());
                }
                vo.setDoseUnitName(org.apache.commons.lang3.StringUtils.isEmpty(drug.getDoseUnit().toString())?"":baseInfoManager.getDicItem(DicTypeEnum.UNIT.getCode(),drug.getDoseUnit().toString()).getDicItemName());
                list.add(vo);

            });
            return ResponseUtil.setSuccessResult(list);
        }else{//检查处方
            Example example = new Example(InspectTpl.class);
            example.createCriteria().andEqualTo("tplId",id);
            List<InspectTplVo> list = Lists.newArrayList();
            inspectTplMapper.selectByExample(example).forEach(obj->{
                InspectTplVo vo = new InspectTplVo();
                BeanUtils.copyProperties(obj,vo);
                vo.setCategoryName(baseInfoManager.getDicItem(DicTypeEnum.INSPECT_CATEGORY.getCode(),obj.getCategory()).getDicItemName());
                vo.setFee(Optional.ofNullable(feeItemManager.getFeeItemDetail(info.getUser().getOrgCode(),DicTypeEnum.INSPECT_CATEGORY.getCode(),vo.getCategory())).
                        map(fee->Double.parseDouble(fee.getItemPrice().toString())).orElse(0d));
                list.add(vo);
            });
            return ResponseUtil.setSuccessResult(list);
        }

    }


    @ApiOperation(value = "编辑 处方模板-药品")
    @PostMapping("/prescripTpl/item/save")
    public ResponseResult savePrescripItemTpl(@ApiParam("id等于null:新增; id不等于null：编辑") @RequestBody PrescriptionTplItemMo mo,
                                          @AccessToken AccessInfo info){

        boolean flag = templateManager.savePrescripItemTpl(mo);
        return flag?ResponseUtil.setSuccessResult():ResponseUtil.setErrorMeg(StatusCode.FAIL,"新增处方模板失败");
    }

    @ApiOperation(value = "编辑 处方模板-检查")
    @PostMapping("/prescripTpl/inspect/save")
    public ResponseResult savInspectTpl(@ApiParam("id等于null:新增; id不等于null：编辑") @RequestBody InspectTplMo mo,
                                          @AccessToken AccessInfo info){

        boolean flag = templateManager.savInspectTpl(mo);
        return flag?ResponseUtil.setSuccessResult():ResponseUtil.setErrorMeg(StatusCode.FAIL,"新增处方模板失败");
    }
}
