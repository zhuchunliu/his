package com.acmed.his.api;

import com.acmed.his.constants.StatusCode;
import com.acmed.his.consts.DicTypeEnum;
import com.acmed.his.dao.InspectTplMapper;
import com.acmed.his.dao.PrescriptionTplItemMapper;
import com.acmed.his.dao.PrescriptionTplMapper;
import com.acmed.his.model.*;
import com.acmed.his.model.dto.PrescriptionTplDto;
import com.acmed.his.pojo.mo.*;
import com.acmed.his.pojo.vo.AdviceTplVo;
import com.acmed.his.pojo.vo.DiagnosisTplVo;
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
@Api(tags = "模板信息",description = "诊断模板、医嘱模板、处方模板、病例模板")
@RestController
@RequestMapping("/tpl")
public class TemplateApi {

    @Autowired
    private TemplateManager templateManager;

    @Autowired
    private BaseInfoManager baseInfoManager;

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

    @ApiOperation(value = "获取指定机构下的诊断模板")
    @GetMapping("/diagnosis/list")
    public ResponseResult<List<DiagnosisTplVo>> getDiagnosisList(@AccessToken AccessInfo info){
        List<DiagnosisTplVo> list = new ArrayList<>();


        List<DiagnosisTpl> sourceList = templateManager.getDiagnosisTplList(info.getUser().getOrgCode());
        List<DicItem> dicItemList = baseInfoManager.getDicItemsByDicTypeCode(DicTypeEnum.DIAGNOSIS_TPL.getCode());
        Map<String,DicItem> dicMap= new HashMap<>();
        for(DicItem dicItem :dicItemList){
            dicMap.put(dicItem.getDicItemCode(),dicItem);
        }

        String preCategory = null;
        List<DiagnosisTpl> detailList = new ArrayList<>();
        Set<String> categorySet = new HashSet<>();
        for(DiagnosisTpl tpl :sourceList){
            preCategory = Optional.ofNullable(preCategory).orElse(tpl.getCategory());
            categorySet.add(preCategory);
            if(preCategory.equals(tpl.getCategory())){
                detailList.add(tpl);
            }else{
                list.add(new DiagnosisTplVo(dicMap.get(preCategory),detailList));
                dicMap.remove(preCategory);
                detailList.clear();
                detailList.add(tpl);
                preCategory = tpl.getCategory();
            }
        }

        list.add(new DiagnosisTplVo(dicMap.get(preCategory),detailList));
        dicMap.remove(preCategory);

        for(String code : dicMap.keySet()){
            list.add(new DiagnosisTplVo(dicMap.get(code)));
        }
        return ResponseUtil.setSuccessResult(list);
    }


    @ApiOperation(value = "获取诊断模板详情")
    @GetMapping("/diagnosis/detail")
    public ResponseResult<DiagnosisTplMo> getDiagnosisDetail(@ApiParam("模板主键") @RequestParam("id") Integer id){
        DiagnosisTplMo mo = new DiagnosisTplMo();
        BeanUtils.copyProperties(templateManager.getDiagnosisTpl(id),mo);
        return ResponseUtil.setSuccessResult(mo);
    }

    @ApiOperation(value = "删除诊断模板")
    @GetMapping("/diagnosis/del")
    public ResponseResult delDiagnosis(@ApiParam("模板主键") @RequestParam("id") Integer id,
                                       @AccessToken AccessInfo info){
        templateManager.delDiagnosisTpl(id,info.getUser());
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

    @ApiOperation(value = "获取指定机构下的医嘱模板")
    @GetMapping("/advice/list")
    public ResponseResult<List<AdviceTplVo>> getAdviceList(@AccessToken AccessInfo info){

        List<AdviceTplVo> list = new ArrayList<>();

        List<AdviceTpl> sourceList = templateManager.getAdviceTplList(info.getUser().getOrgCode());

        List<DicItem> dicItemList = baseInfoManager.getDicItemsByDicTypeCode(DicTypeEnum.ADVICE_TPL.getCode());
        Map<String,DicItem> dicMap= new HashMap<>();
        for(DicItem dicItem :dicItemList){
            dicMap.put(dicItem.getDicItemCode(),dicItem);
        }

        String preCategory = null;
        List<AdviceTpl> detailList = new ArrayList<>();
        for(AdviceTpl tpl :sourceList){
            preCategory = Optional.ofNullable(preCategory).orElse(tpl.getCategory());
            if(preCategory.equals(tpl.getCategory())){
                detailList.add(tpl);
            }else{
                list.add(new AdviceTplVo(dicMap.get(preCategory),detailList));
                dicMap.remove(preCategory);
                detailList.clear();
                detailList.add(tpl);
                preCategory = tpl.getCategory();
            }
        }
        list.add(new AdviceTplVo(dicMap.get(preCategory),detailList));
        dicMap.remove(preCategory);

        for(String code : dicMap.keySet()){
            list.add(new AdviceTplVo(dicMap.get(code)));
        }
        return ResponseUtil.setSuccessResult(list);
    }

    @ApiOperation(value = "获取医嘱模板详情")
    @GetMapping("/advice/detail")
    public ResponseResult<AdviceTplMo> getAdviceDetail(@ApiParam("模板主键") @RequestParam("id") Integer id){
        AdviceTplMo mo = new AdviceTplMo();
        BeanUtils.copyProperties(templateManager.getAdviceTpl(id),mo);
        return ResponseUtil.setSuccessResult(mo);
    }

    @ApiOperation(value = "删除医嘱模板")
    @DeleteMapping("/advice/del")
    public ResponseResult delAdviceDetail(@ApiParam("模板主键") @RequestParam("id") Integer id,
                                          @AccessToken AccessInfo info){
        templateManager.delAdviceTpl(id,info.getUser());
        return ResponseUtil.setSuccessResult();
    }


    @ApiOperation(value = "新增/编辑 处方模板")
    @PostMapping("/prescripTpl/save")
    public ResponseResult savePrescripTpl(@ApiParam("id等于null:新增; id不等于null：编辑") @RequestBody PrescriptionTplMo mo,
                                          @AccessToken AccessInfo info){

        boolean flag = templateManager.savePrescripTpl(mo,info.getUser());
        return flag?ResponseUtil.setSuccessResult():ResponseUtil.setErrorMeg(StatusCode.FAIL,"新增处方模板失败");
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
                BeanUtils.copyProperties(obj,vo);
                Drug drug = drugManager.getDrugById(obj.getDrugId());
                if(null != drug){
                    vo.setDrugName(Optional.ofNullable(drug.getGoodsName()).orElse(drug.getName()));
                    vo.setFee(drug.getRetailPrice());
                }
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
