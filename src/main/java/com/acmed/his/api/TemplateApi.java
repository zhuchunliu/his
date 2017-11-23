package com.acmed.his.api;

import com.acmed.his.model.AdviceTpl;
import com.acmed.his.model.DiagnosisTpl;
import com.acmed.his.model.PrescriptionTpl;
import com.acmed.his.model.PrescriptionTplItem;
import com.acmed.his.service.TemplateManager;
import com.acmed.his.util.ResponseResult;
import com.acmed.his.util.ResponseUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

/**
 * Created by Darren on 2017-11-20
 **/
@Api(tags = "模板信息",description = "诊断模板、医嘱模板、处方模板")
@RestController
@RequestMapping("/tpl")
public class TemplateApi {

    @Autowired
    private TemplateManager templateManager;

    @ApiOperation(value = "新增/编辑 诊断模板")
    @PostMapping("/diagnosis/save")
    public ResponseResult saveDiagnosisList(@ApiParam("id等于null:新增; id不等于null：编辑") @RequestBody DiagnosisTpl diagnosisTpl){
        templateManager.saveDiagnosisTpl(diagnosisTpl);
        return ResponseUtil.setSuccessResult();
    }

    @ApiOperation(value = "获取指定机构下的诊断模板")
    @GetMapping("/diagnosis/list")
    public ResponseResult<List<DiagnosisTpl>> getDiagnosisList(@ApiParam("机构编码") @RequestParam("orgCode") Integer orgCode){
        return ResponseUtil.setSuccessResult(templateManager.getDiagnosisTplList(orgCode));
    }

    @ApiOperation(value = "获取诊断模板详情")
    @GetMapping("/diagnosis/detail")
    public ResponseResult<DiagnosisTpl> getDiagnosisDetail(@ApiParam("模板主键") @RequestParam("id") Integer id){
        return ResponseUtil.setSuccessResult(templateManager.getDiagnosisTpl(id));
    }

    @ApiOperation(value = "删除诊断模板")
    @DeleteMapping("/diagnosis/del")
    public ResponseResult delDiagnosis(@ApiParam("模板主键") @RequestParam("id") Integer id){
        templateManager.delDiagnosisTpl(id);
        return ResponseUtil.setSuccessResult();
    }


    @ApiOperation(value = "新增/编辑 医嘱模板")
    @PostMapping("/advice/save")
    public ResponseResult saveAdviceTpl(@ApiParam("id等于null:新增; id不等于null：编辑") @RequestBody AdviceTpl adviceTpl){
        templateManager.saveAdviceTpl(adviceTpl);
        return ResponseUtil.setSuccessResult();
    }

    @ApiOperation(value = "获取指定机构下的医嘱模板")
    @GetMapping("/advice/list")
    public ResponseResult<List<AdviceTpl>> getAdviceList(@ApiParam("机构编码") @RequestParam("orgCode") Integer orgCode){
        return ResponseUtil.setSuccessResult(templateManager.getAdviceTplList(orgCode));
    }

    @ApiOperation(value = "获取医嘱模板详情")
    @GetMapping("/advice/detail")
    public ResponseResult<AdviceTpl> getAdviceDetail(@ApiParam("模板主键") @RequestParam("id") Integer id){
        return ResponseUtil.setSuccessResult(templateManager.getAdviceTpl(id));
    }

    @ApiOperation(value = "删除医嘱模板")
    @DeleteMapping("/advice/del")
    public ResponseResult delAdviceDetail(@ApiParam("模板主键") @RequestParam("id") Integer id){
        templateManager.delAdviceTpl(id);
        return ResponseUtil.setSuccessResult();
    }


    @ApiOperation(value = "新增/编辑 处方模板")
    @PostMapping("/prescripTpl/save")
    public ResponseResult savePrescripTpl(@ApiParam("id等于null:新增; id不等于null：编辑") @RequestBody PrescriptionTpl prescriptionTpl){
        templateManager.savePrescripTpl(prescriptionTpl);
        return ResponseUtil.setSuccessResult();
    }

    @ApiOperation(value = "获取指定机构下的处方模板")
    @GetMapping("/prescripTpl/list")
    public ResponseResult<List<PrescriptionTpl>> getPrescripTplList(@ApiParam("机构编码") @RequestParam("orgCode") Integer orgCode){
        return ResponseUtil.setSuccessResult(templateManager.getPrescripTplList(orgCode));
    }

    @ApiOperation(value = "获取处方模板")
    @GetMapping("/prescripTpl/detail")
    public ResponseResult<PrescriptionTpl> getPrescripTplDetail(@ApiParam("模板主键") @RequestParam("id") Integer id){
        return ResponseUtil.setSuccessResult(templateManager.getPrescripTpl(id));
    }

    @ApiOperation(value = "删除处方模板")
    @DeleteMapping("/prescripTpl/del")
    public ResponseResult delPrescripTpl(@ApiParam("模板主键") @RequestParam("id") Integer id){
        templateManager.delPrescripTpl(id);
        return ResponseUtil.setSuccessResult();
    }


    @ApiOperation(value = "新增/编辑 处方模板详情")
    @PostMapping("/prescripTplItem/save")
    public ResponseResult savePrescriptionTplItem(@ApiParam("id等于null:新增; id不等于null：编辑; tplId 不能为null") @RequestBody PrescriptionTplItem prescriptionTplItem){
        if(null == prescriptionTplItem.getTplId()){
            return ResponseUtil.setParamEmptyError("tplId");
        }
        templateManager.savePrescriptionTplItem(prescriptionTplItem);
        return ResponseUtil.setSuccessResult();
    }

    @ApiOperation(value = "获取处方模板详情列表信息")
    @GetMapping("/prescripTplItem/list")
    public ResponseResult<List<PrescriptionTplItem>> getPrescripTplItemList(@ApiParam("处方模板主键") @RequestParam("tplId") Integer tplId){
        return ResponseUtil.setSuccessResult(templateManager.getPrescripTplItemList(tplId));
    }

    @ApiOperation(value = "获取处方模板详情")
    @GetMapping("/prescripTplItem/detail")
    public ResponseResult<PrescriptionTplItem> getPrescripTplItemDetail(@ApiParam("处方详情主键") @RequestParam("id") Integer id){
        return ResponseUtil.setSuccessResult(templateManager.getPrescripTplItem(id));
    }

    @ApiOperation(value = "删除处方模板详情")
    @DeleteMapping("/prescripTplItem/del")
    public ResponseResult delPrescripTplItem(@ApiParam("处方详情主键") @RequestParam("id") Integer id){
        templateManager.delPrescripTplItem(id);
        return ResponseUtil.setSuccessResult();
    }


}
