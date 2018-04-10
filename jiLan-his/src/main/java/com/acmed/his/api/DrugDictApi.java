package com.acmed.his.api;

import com.acmed.his.consts.DicTypeEnum;
import com.acmed.his.dao.DrugDictMapper;
import com.acmed.his.dao.ManufacturerMapper;
import com.acmed.his.model.DicItem;
import com.acmed.his.model.DrugDict;
import com.acmed.his.pojo.mo.DrugDictMo;
import com.acmed.his.pojo.mo.DrugDictQueryMo;
import com.acmed.his.pojo.vo.DrugDictDetailVo;
import com.acmed.his.pojo.vo.DrugDictListVo;
import com.acmed.his.pojo.vo.DrugListVo;
import com.acmed.his.service.BaseInfoManager;
import com.acmed.his.service.DrugDictManager;
import com.acmed.his.support.AccessInfo;
import com.acmed.his.support.AccessToken;
import com.acmed.his.util.PageBase;
import com.acmed.his.util.PageResult;
import com.acmed.his.util.ResponseResult;
import com.acmed.his.util.ResponseUtil;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Created by Darren on 2018-03-22
 **/
@Api(tags = "药品字典管理")
@RequestMapping("/drugdict")
@RestController
public class DrugDictApi {

    @Autowired
    private DrugDictManager drugDictManager;

    @Autowired
    private DrugDictMapper drugDictMapper;

    @Autowired
    private BaseInfoManager baseInfoManager;

    @Autowired
    private ManufacturerMapper manufacturerMapper;

    @ApiOperation(value = "药品信息列表")
    @PostMapping("/list")
    public ResponseResult<PageResult<DrugDictListVo>> getDrugList(@RequestBody(required = false) PageBase<DrugDictQueryMo> pageBase){

        PageResult<DrugDict> pageResult = drugDictManager.getDrugDictList(Optional.ofNullable(pageBase.getParam()).map(DrugDictQueryMo::getName).orElse(null),
                Optional.ofNullable(pageBase.getParam()).map(DrugDictQueryMo::getCategory).orElse(null),
                Optional.ofNullable(pageBase.getParam()).map(DrugDictQueryMo::getIsHandle).orElse(null),
                pageBase.getPageNum(), pageBase.getPageSize());

        List<DicItem> dicItemList = baseInfoManager.getDicItemsByDicTypeCode(DicTypeEnum.UNIT.getCode());
        Map<String,String> dicItemName = Maps.newHashMap();
        dicItemList.forEach(obj->{
            dicItemName.put(obj.getDicItemCode(),obj.getDicItemName());
        });

        List<DrugDictListVo> voList = Lists.newArrayList();
        pageResult.getData().forEach(dict->{
            DrugDictListVo vo = new DrugDictListVo();
            BeanUtils.copyProperties(dict,vo);
            vo.setCategoryName(null == dict.getCategory()?"":baseInfoManager.getDicItem(DicTypeEnum.DRUG_CLASSIFICATION.getCode(),dict.getCategory().toString()).getDicItemName());
            vo.setDrugFormName(null == dict.getDrugForm()?"":baseInfoManager.getDicItem(DicTypeEnum.DRUG_FORM.getCode(),dict.getDrugForm().toString()).getDicItemName());
            vo.setUnitName(null == dict.getUnit()?"":baseInfoManager.getDicItem(DicTypeEnum.UNIT.getCode(),dict.getUnit().toString()).getDicItemName());
            vo.setMinUnitName(null==dict.getMinUnit()?"":baseInfoManager.getDicItem(DicTypeEnum.UNIT.getCode(),dict.getMinUnit().toString()).getDicItemName());
            vo.setDoseUnitName(null==dict.getDoseUnit()?"":baseInfoManager.getDicItem(DicTypeEnum.UNIT.getCode(),dict.getDoseUnit().toString()).getDicItemName());
            vo.setUseageName(null == dict.getUseage()?"":baseInfoManager.getDicItem(DicTypeEnum.USEAGE.getCode(),dict.getUseage().toString()).getDicItemName());
            vo.setPrescriptionTypeName(null == dict.getPrescriptionType()?"":baseInfoManager.getDicItem(DicTypeEnum.PRESCRIPTION_TYPE.getCode(),dict.getPrescriptionType().toString()).getDicItemName());
            vo.setManufacturerName(Optional.ofNullable(dict.getManufacturer()).map(obj->manufacturerMapper.selectByPrimaryKey(obj)).
                    map(obj->obj.getName()).orElse(""));

            voList.add(vo);
        });
        PageResult result = new PageResult();
        result.setTotal(pageResult.getTotal());
        result.setData(voList);
        return ResponseUtil.setSuccessResult(result);

    }


    @ApiOperation(value = "删除")
    @DeleteMapping("/del")
    public ResponseResult delDrugDict(@ApiParam("药品字典主键")@RequestParam(value = "id") Integer id,
                                  @AccessToken AccessInfo info){
        drugDictManager.delDrugDict(id);
        return ResponseUtil.setSuccessResult();
    }


    @ApiOperation(value = "更新药品信息")
    @PostMapping("/update")
    public ResponseResult saveDrugDict(@RequestBody DrugDictMo mo, @AccessToken AccessInfo info){
        drugDictManager.saveDrugDict(mo);
        return ResponseUtil.setSuccessResult();
    }


    @ApiOperation(value = "根据id查询药品详情")
    @GetMapping("/detail")
    public ResponseResult<DrugDictDetailVo> selectDrugsById(@ApiParam("药品id") @RequestParam("id") Integer id){
        DrugDict drug = drugDictMapper.selectByPrimaryKey(id);
        DrugDictDetailVo vo = new DrugDictDetailVo();
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
