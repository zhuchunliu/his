package com.acmed.his.api;

import com.acmed.his.consts.DicTypeEnum;
import com.acmed.his.dao.ManufacturerMapper;
import com.acmed.his.model.Drug;
import com.acmed.his.model.DrugDict;
import com.acmed.his.pojo.mo.DrugMo;
import com.acmed.his.pojo.mo.DrugQueryMo;
import com.acmed.his.pojo.vo.DrugDictVo;
import com.acmed.his.pojo.vo.DrugListVo;
import com.acmed.his.pojo.vo.DrugVo;
import com.acmed.his.service.BaseInfoManager;
import com.acmed.his.service.DrugManager;
import com.acmed.his.support.AccessInfo;
import com.acmed.his.support.AccessToken;
import com.acmed.his.util.PageBase;
import com.acmed.his.util.PageResult;
import com.acmed.his.util.ResponseResult;
import com.acmed.his.util.ResponseUtil;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

/**
 * DrugApi
 *
 * @author jimson
 * @date 2017/11/23
 */
@Api(tags = "药品管理")
@RequestMapping("/drug")
@RestController
public class DrugApi {
    @Autowired
    private DrugManager drugManager;

    @Autowired
    private ManufacturerMapper manufacturerMapper;

    @Autowired
    private BaseInfoManager baseInfoManager;

    @ApiOperation(value = "药品信息列表")
    @PostMapping("/list")
    public ResponseResult<PageResult<DrugListVo>> getDrugList(@RequestBody(required = false) PageBase<DrugQueryMo> pageBase,
                                                              @AccessToken AccessInfo info){
        List<Drug> list = drugManager.getDrugList(info.getUser().getOrgCode(),
                Optional.ofNullable(pageBase.getParam()).map(DrugQueryMo::getName).orElse(null),
                Optional.ofNullable(pageBase.getParam()).map(DrugQueryMo::getCategory).orElse(null),
                Optional.ofNullable(pageBase.getParam()).map(DrugQueryMo::getIsValid).orElse(null),
                pageBase.getPageNum(), pageBase.getPageSize());
        int total = drugManager.getDrugTotal(info.getUser().getOrgCode(),
                Optional.ofNullable(pageBase.getParam()).map(DrugQueryMo::getName).orElse(null),
                Optional.ofNullable(pageBase.getParam()).map(DrugQueryMo::getCategory).orElse(null),
                Optional.ofNullable(pageBase.getParam()).map(DrugQueryMo::getIsValid).orElse(null));

        List<DrugVo> voList = Lists.newArrayList();
        list.forEach(drug->{
            DrugVo vo = new DrugVo();
            BeanUtils.copyProperties(drug,vo);
            vo.setCategoryName(null == drug.getCategory()?"":baseInfoManager.getDicItem(DicTypeEnum.DRUG_CLASSIFICATION.getCode(),drug.getCategory().toString()).getDicItemName());
            vo.setDrugFormName(null == drug.getDrugForm()?"":baseInfoManager.getDicItem(DicTypeEnum.DRUG_FORM.getCode(),drug.getDrugForm().toString()).getDicItemName());
            vo.setUnitName(null == drug.getUnit()?"":baseInfoManager.getDicItem(DicTypeEnum.UNIT.getCode(),drug.getUnit().toString()).getDicItemName());
            vo.setMinUnitName(null==drug.getMinUnit()?"":baseInfoManager.getDicItem(DicTypeEnum.UNIT.getCode(),drug.getMinUnit().toString()).getDicItemName());
            vo.setDoseUnitName(null==drug.getDoseUnit()?"":baseInfoManager.getDicItem(DicTypeEnum.UNIT.getCode(),drug.getDoseUnit().toString()).getDicItemName());
            vo.setUseageName(null == drug.getUseage()?"":baseInfoManager.getDicItem(DicTypeEnum.USEAGE.getCode(),drug.getUseage().toString()).getDicItemName());

            vo.setManufacturerName(Optional.ofNullable(drug.getManufacturer()).map(obj->manufacturerMapper.selectByPrimaryKey(obj)).
                    map(obj->obj.getName()).orElse(""));
            voList.add(vo);
        });

        PageResult pageResult = new PageResult();
        BeanUtils.copyProperties(pageBase,pageResult);
        pageResult.setTotal((long)total);
        pageResult.setData(voList);
        return ResponseUtil.setSuccessResult(pageResult);

    }

    @ApiOperation(value = "获取药品字典表")
    @PostMapping("/dict")
    public ResponseResult<PageResult<DrugDictVo>> getDrugDictList(@RequestBody(required = false) PageBase<DrugQueryMo> pageBase,
                                                                  @AccessToken AccessInfo info){
        List<DrugDict> list = drugManager.getDrugDictList(info.getUser().getOrgCode(),
                Optional.ofNullable(pageBase.getParam()).map(DrugQueryMo::getName).orElse(null),
                Optional.ofNullable(pageBase.getParam()).map(DrugQueryMo::getCategory).orElse(null),
                pageBase.getPageNum(), pageBase.getPageSize());
        int total = drugManager.getDrugDictTotal(info.getUser().getOrgCode(),
                Optional.ofNullable(pageBase.getParam()).map(DrugQueryMo::getName).orElse(null),
                Optional.ofNullable(pageBase.getParam()).map(DrugQueryMo::getCategory).orElse(null));

        List<DrugDictVo> voList = Lists.newArrayList();
        list.forEach(obj->{
            DrugDictVo vo = new DrugDictVo();
            BeanUtils.copyProperties(obj,vo);
            voList.add(vo);
        });

        PageResult pageResult = new PageResult();
        BeanUtils.copyProperties(pageBase,pageResult);
        pageResult.setTotal((long)total);
        pageResult.setData(voList);
        return ResponseUtil.setSuccessResult(pageResult);

    }



    @ApiOperation(value = "删除")
    @DeleteMapping("/del")
    public ResponseResult delDrug(@ApiParam("药品主键")@RequestParam(value = "id") Integer id,
                                  @AccessToken AccessInfo info){
        drugManager.delDrug(id,info.getUser());
        return ResponseUtil.setSuccessResult();
    }

    @ApiOperation(value = "启用、禁用药品信息")
    @PostMapping("/switch")
    public ResponseResult swithDrug(@ApiParam("{\"id\":\"\"} id：药品主键") @RequestBody String param,
                                  @AccessToken AccessInfo info){
        if(org.apache.commons.lang3.StringUtils.isEmpty(param) || null == JSONObject.parseObject(param).get("id")){
            return ResponseUtil.setParamEmptyError("id");
        }
        drugManager.switchDrug(JSONObject.parseObject(param).getInteger("id"),info.getUser());
        return ResponseUtil.setSuccessResult();
    }

    @ApiOperation(value = "批量添加药品信息")
    @PostMapping("/batch")
    public ResponseResult saveDrug(@ApiParam("{\"codes\":\"XXX,XXX\"}  code为药品编码集合，逗号间隔") @RequestBody String param,
                                   @AccessToken AccessInfo info){

        if(StringUtils.isEmpty(param) || null == JSONObject.parseObject(param).get("codes")){
            return ResponseUtil.setParamEmptyError("codes");
        }
        drugManager.saveDrugByDict(JSONObject.parseObject(param).get("codes").toString().split(","),info.getUser());
        return ResponseUtil.setSuccessResult();
    }


    @ApiOperation(value = "更新药品信息")
    @PostMapping("/update")
    public ResponseResult saveDrug(@RequestBody DrugMo mo,@AccessToken AccessInfo info){
        drugManager.saveDrug(mo,info.getUser());
        return ResponseUtil.setSuccessResult();
    }


    @ApiOperation(value = "根据id查询药品详情")
    @GetMapping("/detail")
    public ResponseResult<DrugVo> selectDrugsById(@ApiParam("药品id") @RequestParam("id") Integer id){
        Drug drug = drugManager.getDrugById(id);
        DrugVo vo = new DrugVo();
        BeanUtils.copyProperties(drug,vo);
        vo.setCategoryName(null==drug.getCategory()?"":baseInfoManager.getDicItem(DicTypeEnum.DRUG_CLASSIFICATION.getCode(),drug.getCategory().toString()).getDicItemName());
        vo.setDrugFormName(null==drug.getDrugForm()?"":baseInfoManager.getDicItem(DicTypeEnum.DRUG_FORM.getCode(),drug.getDrugForm().toString()).getDicItemName());
        vo.setUnitName(null==drug.getUnit()?"":baseInfoManager.getDicItem(DicTypeEnum.UNIT.getCode(),drug.getUnit().toString()).getDicItemName());
        vo.setMinUnitName(null==drug.getMinUnit()?"":baseInfoManager.getDicItem(DicTypeEnum.UNIT.getCode(),drug.getMinUnit().toString()).getDicItemName());
        vo.setDoseUnitName(null==drug.getDoseUnit()?"":baseInfoManager.getDicItem(DicTypeEnum.UNIT.getCode(),drug.getDoseUnit().toString()).getDicItemName());
        vo.setUseageName(null==drug.getUseage()?"":baseInfoManager.getDicItem(DicTypeEnum.USEAGE.getCode(),drug.getUseage().toString()).getDicItemName());
        vo.setFrequencyName(null == drug.getFrequency()?"":baseInfoManager.getDicItem(DicTypeEnum.DRUG_FREQUENCY.getCode(),drug.getFrequency().toString()).getDicItemName());

        vo.setManufacturerName(Optional.ofNullable(drug.getManufacturer()).map(obj->manufacturerMapper.selectByPrimaryKey(obj)).
                map(obj->obj.getName()).orElse(""));
        return ResponseUtil.setSuccessResult(vo);
    }



}
