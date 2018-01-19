package com.acmed.his.api;

import com.acmed.his.constants.StatusCode;
import com.acmed.his.consts.DicTypeEnum;
import com.acmed.his.model.*;
import com.acmed.his.pojo.mo.*;
import com.acmed.his.pojo.vo.AdviceTplVo;
import com.acmed.his.pojo.vo.DiagnosisTplVo;
import com.acmed.his.pojo.vo.PrescriptionTplVo;
import com.acmed.his.service.BaseInfoManager;
import com.acmed.his.service.DrugManager;
import com.acmed.his.service.MedicalRecordTplManager;
import com.acmed.his.service.TemplateManager;
import com.acmed.his.support.AccessInfo;
import com.acmed.his.support.AccessToken;
import com.acmed.his.util.PageBase;
import com.acmed.his.util.ResponseResult;
import com.acmed.his.util.ResponseUtil;
import io.swagger.annotations.*;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
    private MedicalRecordTplManager medicalRecordTplManager;

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
    @DeleteMapping("/diagnosis/del")
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
    @GetMapping("/prescripTpl/list")
    public ResponseResult<List<PrescriptionTplVo>> getPrescripTplList(@AccessToken AccessInfo info,
                                                                      @ApiParam("模板类型") @RequestParam(value = "category",required = false) String category){
        List<PrescriptionTplVo> list = new ArrayList<>();
        templateManager.getPrescripTplList(info.getUser().getOrgCode(),category).forEach((obj)->{
            PrescriptionTplVo mo = new PrescriptionTplVo();
            BeanUtils.copyProperties(obj,mo);
            mo.setCategoryName("1".equals(mo.getCategory())?"药品处方":"检查处方");
            list.add(mo);
        });
        return ResponseUtil.setSuccessResult(list);
    }

    @ApiOperation(value = "获取处方模板")
    @GetMapping("/prescripTpl/detail")
    public ResponseResult<PrescriptionTplVo> getPrescripTplDetail(@ApiParam("模板主键") @RequestParam("id") Integer id){
        PrescriptionTplVo mo = new PrescriptionTplVo();
        BeanUtils.copyProperties(templateManager.getPrescripTpl(id),mo);

        List<PrescriptionTplVo.Item> itemList = new ArrayList<>();
        templateManager.getPrescripTplItemList(mo.getId()).forEach(obj->{
            PrescriptionTplVo.Item item = new PrescriptionTplVo().new Item();
            BeanUtils.copyProperties(obj,item);
            Drug drug = Optional.ofNullable(item.getDrugId()).map(drugManager::getDrugById).orElse(null);
            if(null != drug) {
                item.setFee(Optional.ofNullable(drug.getRetailPrice()).orElse(0d));
                item.setSpec(drug.getSpec());
            }
            itemList.add(item);
        });
        mo.setItemList(itemList);

        List<PrescriptionTplVo.InspectDetail> inspectList = new ArrayList<>();
        templateManager.getInspectTplList(mo.getId()).forEach(obj->{
            PrescriptionTplVo.InspectDetail item = new PrescriptionTplVo().new InspectDetail();
            BeanUtils.copyProperties(obj,item);
            inspectList.add(item);
        });
        mo.setInspectList(inspectList);
        return ResponseUtil.setSuccessResult(mo);
    }

    @ApiOperation(value = "删除处方模板")
    @DeleteMapping("/prescripTpl/del")
    public ResponseResult delPrescripTpl(@ApiParam("模板主键") @RequestParam("id") Integer id,
                                         @AccessToken AccessInfo info){
        templateManager.delPrescripTpl(id,info.getUser());
        return ResponseUtil.setSuccessResult();
    }



    @ApiOperation(value = "删除病例模板")
    @GetMapping("/medicalRecordTpl/del")
    public ResponseResult delMedicalRecordTpl(@ApiParam("模板主键") @RequestParam("id") Integer id,
                                         @AccessToken AccessInfo info){
        MedicalRecordTpl medicalRecordTpl = new MedicalRecordTpl();
        medicalRecordTpl.setId(id);
        medicalRecordTpl.setModifyBy(info.getUserId().toString());
        medicalRecordTpl.setRemoved("1");
        medicalRecordTplManager.updateMedicalRecordTpl(medicalRecordTpl);
        return ResponseUtil.setSuccessResult();
    }



    @ApiOperation(value = "添加/编辑 病例模板")
    @PostMapping("/medicalRecordTpl/save")
    public ResponseResult saveMedicalRecordTpl(@ApiParam("传id  表示修改，不传表示新增") @RequestBody AddMedicalRecordTplMo param,@AccessToken AccessInfo info){
        Integer id = param.getId();
        if (id == null){
            MedicalRecordTpl medicalRecordTpl = new MedicalRecordTpl();
            BeanUtils.copyProperties(param,medicalRecordTpl);
            medicalRecordTpl.setUserId(info.getUserId());
            medicalRecordTpl.setDept(info.getUser().getDept());
            medicalRecordTpl.setOrgCode(info.getUser().getOrgCode());
            medicalRecordTpl.setCreateBy(info.getUserId().toString());
            medicalRecordTplManager.add(medicalRecordTpl);
        }else {
            //修改
            MedicalRecordTpl medicalRecordTpl = new MedicalRecordTpl();
            medicalRecordTpl.setUserId(info.getUserId());
            medicalRecordTpl.setId(id);
            List<MedicalRecordTpl> byParam = medicalRecordTplManager.getByParam(medicalRecordTpl);
            if (byParam.size() != 0){
                BeanUtils.copyProperties(param,medicalRecordTpl);
                medicalRecordTpl.setModifyBy(info.getUserId().toString());
                medicalRecordTplManager.updateMedicalRecordTpl(medicalRecordTpl);
            }
        }
        return ResponseUtil.setSuccessResult();
    }

    @ApiOperation(value = "病例模板列表")
    @PostMapping("/medicalRecordTpl/list")
    public ResponseResult medicalRecordTplList(@ApiParam("条件") @RequestBody PageBase<GetMedicalRecordTplMo> pageBase, @AccessToken AccessInfo info){
        GetMedicalRecordTplMo param = pageBase.getParam();
        MedicalRecordTpl medicalRecordTpl = new MedicalRecordTpl();
        Integer dept = param.getDept();
        Integer orgCode = param.getOrgCode();
        Integer isSelf = param.getIsSelf();
        if (isSelf == 1){
            medicalRecordTpl.setUserId(info.getUserId());
        }
        if (orgCode == null){
            param.setOrgCode(info.getUser().getOrgCode());
        }else if(orgCode == 0){
            param.setOrgCode(null);
        }else {
            param.setOrgCode(orgCode);
        }
        if (dept == null){
            param.setDept(info.getUser().getDept());
        }else if(dept == 0){
            param.setDept(null);
        }else {
            param.setDept(orgCode);
        }
        return ResponseUtil.setSuccessResult(medicalRecordTplManager.getByParamByPage(medicalRecordTpl,pageBase.getPageNum(),pageBase.getPageSize()));
    }
}
