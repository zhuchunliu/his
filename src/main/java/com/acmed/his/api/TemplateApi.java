package com.acmed.his.api;

import com.acmed.his.constants.CommonConstants;
import com.acmed.his.constants.StatusCode;
import com.acmed.his.model.AdviceTpl;
import com.acmed.his.model.PrescriptionTpl;
import com.acmed.his.model.PrescriptionTplItem;
import com.acmed.his.pojo.mo.AdviceTplMo;
import com.acmed.his.pojo.mo.DiagnosisTplMo;
import com.acmed.his.pojo.mo.PrescriptionTplMo;
import com.acmed.his.service.TemplateManager;
import com.acmed.his.support.AccessInfo;
import com.acmed.his.support.AccessToken;
import com.acmed.his.util.ResponseResult;
import com.acmed.his.util.ResponseUtil;
import io.swagger.annotations.*;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

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
    @ApiImplicitParam(paramType = "header", dataType = "String", name = CommonConstants.USER_HEADER_TOKEN, value = "token", required = true)
    @PostMapping("/diagnosis/save")
    public ResponseResult saveDiagnosisList(@ApiParam("id等于null:新增; id不等于null：编辑") @RequestBody DiagnosisTplMo mo,
                                            @AccessToken AccessInfo info){
        templateManager.saveDiagnosisTpl(mo,info.getUser());
        return ResponseUtil.setSuccessResult();
    }

    @ApiOperation(value = "获取指定机构下的诊断模板")
    @ApiImplicitParam(paramType = "header", dataType = "String", name = CommonConstants.USER_HEADER_TOKEN, value = "token", required = true)
    @GetMapping("/diagnosis/list")
    public ResponseResult<List<DiagnosisTplMo>> getDiagnosisList(@AccessToken AccessInfo info){
        List<DiagnosisTplMo> list = new ArrayList<>();
        templateManager.getDiagnosisTplList(info.getUser().getOrgCode()).forEach((obj)->{
            DiagnosisTplMo mo = new DiagnosisTplMo();
            BeanUtils.copyProperties(obj,mo);
            list.add(mo);
        });
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
    @ApiImplicitParam(paramType = "header", dataType = "String", name = CommonConstants.USER_HEADER_TOKEN, value = "token", required = true)
    @DeleteMapping("/diagnosis/del")
    public ResponseResult delDiagnosis(@ApiParam("模板主键") @RequestParam("id") Integer id,
                                       @AccessToken AccessInfo info){
        templateManager.delDiagnosisTpl(id,info.getUser());
        return ResponseUtil.setSuccessResult();
    }


    @ApiOperation(value = "新增/编辑 医嘱模板")
    @ApiImplicitParam(paramType = "header", dataType = "String", name = CommonConstants.USER_HEADER_TOKEN, value = "token", required = true)
    @PostMapping("/advice/save")
    public ResponseResult saveAdviceTpl(@ApiParam("id等于null:新增; id不等于null：编辑") @RequestBody AdviceTplMo mo,
                                        @AccessToken AccessInfo info){
        templateManager.saveAdviceTpl(mo,info.getUser());
        return ResponseUtil.setSuccessResult();
    }

    @ApiOperation(value = "获取指定机构下的医嘱模板")
    @ApiImplicitParam(paramType = "header", dataType = "String", name = CommonConstants.USER_HEADER_TOKEN, value = "token", required = true)
    @GetMapping("/advice/list")
    public ResponseResult<List<AdviceTplMo>> getAdviceList(@AccessToken AccessInfo info){
        List<AdviceTplMo> list = new ArrayList<>();
        templateManager.getAdviceTplList(info.getUser().getOrgCode()).forEach((obj)->{
            AdviceTplMo mo = new AdviceTplMo();
            BeanUtils.copyProperties(obj,mo);
            list.add(mo);
        });
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
    @ApiImplicitParam(paramType = "header", dataType = "String", name = CommonConstants.USER_HEADER_TOKEN, value = "token", required = true)
    @DeleteMapping("/advice/del")
    public ResponseResult delAdviceDetail(@ApiParam("模板主键") @RequestParam("id") Integer id,
                                          @AccessToken AccessInfo info){
        templateManager.delAdviceTpl(id,info.getUser());
        return ResponseUtil.setSuccessResult();
    }


    @ApiOperation(value = "新增/编辑 处方模板")
    @ApiImplicitParam(paramType = "header", dataType = "String", name = CommonConstants.USER_HEADER_TOKEN, value = "token", required = true)
    @PostMapping("/prescripTpl/save")
    public ResponseResult savePrescripTpl(@ApiParam("id等于null:新增; id不等于null：编辑") @RequestBody PrescriptionTplMo mo,
                                          @AccessToken AccessInfo info){
        boolean flag = templateManager.savePrescripTpl(mo,info.getUser());
        return flag?ResponseUtil.setSuccessResult():ResponseUtil.setErrorMeg(StatusCode.FAIL,"新增处方模板失败");
    }

    @ApiOperation(value = "获取指定机构下的处方模板")
    @ApiImplicitParam(paramType = "header", dataType = "String", name = CommonConstants.USER_HEADER_TOKEN, value = "token", required = true)
    @GetMapping("/prescripTpl/list")
    public ResponseResult<List<PrescriptionTplMo>> getPrescripTplList(@AccessToken AccessInfo info){
        List<PrescriptionTplMo> list = new ArrayList<>();
        templateManager.getPrescripTplList(info.getUser().getOrgCode()).forEach((obj)->{
            PrescriptionTplMo mo = new PrescriptionTplMo();
            BeanUtils.copyProperties(obj,mo);
            list.add(mo);
        });
        return ResponseUtil.setSuccessResult(list);
    }

    @ApiOperation(value = "获取处方模板")
    @GetMapping("/prescripTpl/detail")
    public ResponseResult<PrescriptionTplMo> getPrescripTplDetail(@ApiParam("模板主键") @RequestParam("id") Integer id){
        PrescriptionTplMo mo = new PrescriptionTplMo();
        BeanUtils.copyProperties(templateManager.getPrescripTpl(id),mo);

        List<PrescriptionTplMo.Item> itemList = new ArrayList<>();
        templateManager.getPrescripTplItemList(mo.getId()).forEach(obj->{
            PrescriptionTplMo.Item item = new PrescriptionTplMo().new Item();
            BeanUtils.copyProperties(obj,item);
            itemList.add(item);
        });
        mo.setItemList(itemList);
        return ResponseUtil.setSuccessResult(mo);
    }

    @ApiOperation(value = "删除处方模板")
    @ApiImplicitParam(paramType = "header", dataType = "String", name = CommonConstants.USER_HEADER_TOKEN, value = "token", required = true)
    @DeleteMapping("/prescripTpl/del")
    public ResponseResult delPrescripTpl(@ApiParam("模板主键") @RequestParam("id") Integer id,
                                         @AccessToken AccessInfo info){
        templateManager.delPrescripTpl(id,info.getUser());
        return ResponseUtil.setSuccessResult();
    }

}
