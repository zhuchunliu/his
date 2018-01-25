package com.acmed.his.api;

import com.acmed.his.constants.StatusCode;
import com.acmed.his.model.MedicalRecordTpl;
import com.acmed.his.pojo.mo.AddMedicalRecordTplMo;
import com.acmed.his.pojo.mo.GetMedicalRecordTplMo;
import com.acmed.his.pojo.vo.MedicalRecordTplVo;
import com.acmed.his.service.MedicalRecordTplManager;
import com.acmed.his.support.AccessInfo;
import com.acmed.his.support.AccessToken;
import com.acmed.his.util.PageBase;
import com.acmed.his.util.PageResult;
import com.acmed.his.util.ResponseResult;
import com.acmed.his.util.ResponseUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * MedicalRecordTplApi
 *
 * @author jimson
 * @date 2018/1/25
 */
@Api(tags = "病例模板")
@RestController
@RequestMapping("medicalRecordTpl")
public class MedicalRecordTplApi {

    @Autowired
    private MedicalRecordTplManager medicalRecordTplManager;



    @ApiOperation(value = "删除病例模板")
    @GetMapping("/medicalRecordTpl/del")
    public ResponseResult delMedicalRecordTpl(@ApiParam("模板主键") @RequestParam("id") Integer id,
                                              @AccessToken AccessInfo info){

        MedicalRecordTpl medicalRecordTpl1 = medicalRecordTplManager.medicalRecordTplDetail(id);
        if (!StringUtils.equals(info.getUserId().toString(),medicalRecordTpl1.getCreateBy())){
            return ResponseUtil.setErrorMeg(StatusCode.ERROR_PREMISSION,"不能删除他人模板");
        }
        MedicalRecordTpl medicalRecordTpl = new MedicalRecordTpl();
        medicalRecordTpl.setId(id);
        medicalRecordTpl.setModifyBy(info.getUserId().toString());
        medicalRecordTpl.setRemoved("1");
        medicalRecordTplManager.updateMedicalRecordTpl(medicalRecordTpl);
        return ResponseUtil.setSuccessResult();
    }


    @ApiOperation(value = "添加/编辑 病例模板")
    @PostMapping("/medicalRecordTpl/save")
    public ResponseResult saveMedicalRecordTpl(@ApiParam("传id  表示修改，不传表示新增") @RequestBody AddMedicalRecordTplMo param, @AccessToken AccessInfo info){
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
            MedicalRecordTpl medicalRecordTpl1 = medicalRecordTplManager.medicalRecordTplDetail(id);
            if (!StringUtils.equals(info.getUserId().toString(),medicalRecordTpl1.getCreateBy())){
                return ResponseUtil.setErrorMeg(StatusCode.ERROR_PREMISSION,"不能编辑他人模板");
            }
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
    public ResponseResult<PageResult<MedicalRecordTplVo>> medicalRecordTplList(@ApiParam("条件") @RequestBody PageBase<GetMedicalRecordTplMo> pageBase, @AccessToken AccessInfo info){
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
        PageResult<MedicalRecordTpl> byParamByPage = medicalRecordTplManager.getByParamByPage(medicalRecordTpl, pageBase.getPageNum(), pageBase.getPageSize());

        PageResult<MedicalRecordTplVo> result = new PageResult<>();
        result.setPageSize(byParamByPage.getPageSize());
        result.setPageNum(byParamByPage.getPageNum());
        result.setTotal(byParamByPage.getTotal());

        List<MedicalRecordTpl> data = byParamByPage.getData();
        List<MedicalRecordTplVo> medicalRecordTplVos = new ArrayList<>();
        if (data.size()!=0){
            for (MedicalRecordTpl source : data){
                MedicalRecordTplVo medicalRecordTplVo = new MedicalRecordTplVo();
                BeanUtils.copyProperties(source,medicalRecordTplVo);
                if (StringUtils.equals(info.getUserId().toString(),source.getCreateBy())){
                    medicalRecordTplVo.setIsSelf(1);
                }else {
                    medicalRecordTplVo.setIsSelf(0);
                }
                medicalRecordTplVos.add(medicalRecordTplVo);
            }
        }
        result.setData(medicalRecordTplVos);
        return ResponseUtil.setSuccessResult(result);
    }


}
