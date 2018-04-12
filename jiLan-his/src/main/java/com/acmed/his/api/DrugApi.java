package com.acmed.his.api;

import com.acmed.his.constants.StatusCode;
import com.acmed.his.consts.DicTypeEnum;
import com.acmed.his.dao.ManufacturerMapper;
import com.acmed.his.exceptions.BaseException;
import com.acmed.his.model.DicItem;
import com.acmed.his.model.Drug;
import com.acmed.his.model.Org;
import com.acmed.his.pojo.mo.DrugMo;
import com.acmed.his.pojo.mo.DrugQueryMo;
import com.acmed.his.pojo.mo.DrugZYQueryMo;
import com.acmed.his.pojo.vo.DrugDictVo;
import com.acmed.his.pojo.vo.DrugListVo;
import com.acmed.his.pojo.vo.DrugVo;
import com.acmed.his.pojo.zy.ZYDrugVo;
import com.acmed.his.service.BaseInfoManager;
import com.acmed.his.service.DrugManager;
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
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
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

    @Autowired
    private ZhangYaoManager zhangYaoManager;

    @Autowired
    private OrgManager orgManager;

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

        List<DicItem> dicItemList = baseInfoManager.getDicItemsByDicTypeCode(DicTypeEnum.UNIT.getCode());
        Map<String,String> dicItemName = Maps.newHashMap();
        dicItemList.forEach(obj->{
            dicItemName.put(obj.getDicItemCode(),obj.getDicItemName());
        });

        List<DrugListVo> voList = Lists.newArrayList();
        list.forEach(drug->{
            DrugListVo vo = new DrugListVo();
            BeanUtils.copyProperties(drug,vo);
            vo.setCategoryName(null == drug.getCategory()?"":baseInfoManager.getDicItem(DicTypeEnum.DRUG_CLASSIFICATION.getCode(),drug.getCategory().toString()).getDicItemName());
            vo.setDrugFormName(null == drug.getDrugForm()?"":baseInfoManager.getDicItem(DicTypeEnum.DRUG_FORM.getCode(),drug.getDrugForm().toString()).getDicItemName());
            vo.setUnitName(null == drug.getUnit()?"":baseInfoManager.getDicItem(DicTypeEnum.UNIT.getCode(),drug.getUnit().toString()).getDicItemName());
            vo.setMinUnitName(null==drug.getMinUnit()?"":baseInfoManager.getDicItem(DicTypeEnum.UNIT.getCode(),drug.getMinUnit().toString()).getDicItemName());
            vo.setDoseUnitName(null==drug.getDoseUnit()?"":baseInfoManager.getDicItem(DicTypeEnum.UNIT.getCode(),drug.getDoseUnit().toString()).getDicItemName());
            vo.setUseageName(null == drug.getUseage()?"":baseInfoManager.getDicItem(DicTypeEnum.USEAGE.getCode(),drug.getUseage().toString()).getDicItemName());
            vo.setPrescriptionTypeName(null == drug.getPrescriptionType()?"":baseInfoManager.getDicItem(DicTypeEnum.PRESCRIPTION_TYPE.getCode(),drug.getPrescriptionType().toString()).getDicItemName());
            vo.setManufacturerName(Optional.ofNullable(drug.getManufacturer()).map(obj->manufacturerMapper.selectByPrimaryKey(obj)).
                    map(obj->obj.getName()).orElse(""));

            if(null != drug.getNum() && 0 != drug.getNum()){
                vo.setNumName(Optional.ofNullable(vo.getNumName()).orElse("")+drug.getNum()+
                        (null == drug.getUnit()?"":dicItemName.get(drug.getUnit().toString())));
            }
            if(null != drug.getMinNum() && 0 != drug.getMinNum()){
                vo.setNumName(Optional.ofNullable(vo.getNumName()).orElse("")+drug.getMinNum()+
                        (null == drug.getMinUnit()?"":dicItemName.get(drug.getMinUnit().toString())));
            }
            if(null != drug.getDoseNum() && 0 != drug.getDoseNum()){
                vo.setNumName(Optional.ofNullable(vo.getNumName()).orElse("")+
                        (0==drug.getDoseNum()*10%1? String.valueOf((int)Math.floor(drug.getDoseNum())):String.valueOf(drug.getDoseNum()))+
                        (null == drug.getDoseUnit()?"":dicItemName.get(drug.getDoseUnit().toString())));
            }
            vo.setNeedSupplement(null == drug.getConversion()?1:0);
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

        return ResponseUtil.setSuccessResult(drugManager.getDrugDictList(info.getUser().getOrgCode(),
                Optional.ofNullable(pageBase.getParam()).map(DrugQueryMo::getName).orElse(null),
                Optional.ofNullable(pageBase.getParam()).map(DrugQueryMo::getCategory).orElse(null),
                pageBase.getPageNum(), pageBase.getPageSize()));

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



    @ApiOperation(value = "掌药药品信息列表")
    @PostMapping("/zy/list")
    public ResponseResult<PageResult<ZYDrugVo>> getZYDrugList(@RequestBody(required = false) PageBase<DrugZYQueryMo> pageBase,
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

        PageResult<ZYDrugVo> pageResult = zhangYaoManager.getDrugList(pageBase);
        return ResponseUtil.setSuccessResult(pageResult);

    }

}
