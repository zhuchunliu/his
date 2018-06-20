package com.acmed.his.api;

import com.acmed.his.constants.StatusCode;
import com.acmed.his.model.MedicalRecordTpl;
import com.acmed.his.model.dto.MedicalRecordTplDto;
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

import java.time.LocalDateTime;
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
    @GetMapping("/del")
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

    @ApiOperation(value = "恢复病例模板",hidden = true)
    @GetMapping("/recover")
    public ResponseResult recoverMedicalRecordTpl(@ApiParam("模板主键") @RequestParam("id") Integer id,
                                              @AccessToken AccessInfo info){

        MedicalRecordTpl medicalRecordTpl1 = medicalRecordTplManager.medicalRecordTplDetail(id);
        if (!StringUtils.equals(info.getUserId().toString(),medicalRecordTpl1.getCreateBy())){
            return ResponseUtil.setErrorMeg(StatusCode.ERROR_PREMISSION,"不能恢复他人模板");
        }
        MedicalRecordTpl medicalRecordTpl = new MedicalRecordTpl();
        medicalRecordTpl.setId(id);
        medicalRecordTpl.setModifyBy(info.getUserId().toString());
        medicalRecordTpl.setIsValid("0");
        medicalRecordTplManager.updateMedicalRecordTpl(medicalRecordTpl);
        return ResponseUtil.setSuccessResult();
    }

    @ApiOperation(value = "禁用病例模板")
    @GetMapping("/forbidden")
    public ResponseResult forbiddenMedicalRecordTpl(@ApiParam("模板主键") @RequestParam("id") Integer id,
                                              @AccessToken AccessInfo info){

        MedicalRecordTpl medicalRecordTpl1 = medicalRecordTplManager.medicalRecordTplDetail(id);
        if (!StringUtils.equals(info.getUserId().toString(),medicalRecordTpl1.getCreateBy())){
            return ResponseUtil.setErrorMeg(StatusCode.ERROR_PREMISSION,"不能操作他人模板");
        }
        MedicalRecordTpl medicalRecordTpl = new MedicalRecordTpl();
        medicalRecordTpl.setId(id);
        medicalRecordTpl.setModifyBy(info.getUserId().toString());
        medicalRecordTpl.setIsValid("0");
        medicalRecordTplManager.updateMedicalRecordTpl(medicalRecordTpl);
        return ResponseUtil.setSuccessResult();
    }


    @ApiOperation(value = "启用病例模板")
    @GetMapping("/using")
    public ResponseResult usingMedicalRecordTpl(@ApiParam("模板主键") @RequestParam("id") Integer id,
                                              @AccessToken AccessInfo info){
        MedicalRecordTpl medicalRecordTpl1 = medicalRecordTplManager.medicalRecordTplDetail(id);
        if (!StringUtils.equals(info.getUserId().toString(),medicalRecordTpl1.getCreateBy())){
            return ResponseUtil.setErrorMeg(StatusCode.ERROR_PREMISSION,"不能操作他人模板");
        }
        MedicalRecordTpl medicalRecordTpl = new MedicalRecordTpl();
        medicalRecordTpl.setId(id);
        medicalRecordTpl.setModifyBy(info.getUserId().toString());
        medicalRecordTpl.setIsValid("1");
        medicalRecordTplManager.updateMedicalRecordTpl(medicalRecordTpl);
        return ResponseUtil.setSuccessResult();
    }



    @ApiOperation(value = "添加/编辑 病例模板")
    @PostMapping("/save")
    public ResponseResult<Integer> saveMedicalRecordTpl(@ApiParam("传id  表示修改，不传表示新增  新增的时候会返回模板id") @RequestBody AddMedicalRecordTplMo param, @AccessToken AccessInfo info){
        Integer id = param.getId();
        if (id == null){
            MedicalRecordTpl medicalRecordTpl = new MedicalRecordTpl();
            BeanUtils.copyProperties(param,medicalRecordTpl);
            medicalRecordTpl.setUserId(info.getUserId());
            medicalRecordTpl.setDept(info.getUser().getDept());
            medicalRecordTpl.setOrgCode(info.getUser().getOrgCode());
            medicalRecordTpl.setCreateBy(info.getUserId().toString());
            medicalRecordTpl.setModifyBy(info.getUserId().toString());
            return ResponseUtil.setSuccessResult(medicalRecordTplManager.add(medicalRecordTpl));
        }else {
            //修改
            MedicalRecordTpl medicalRecordTpl = medicalRecordTplManager.medicalRecordTplDetail(id);
//            if (!StringUtils.equals(info.getUserId().toString(),medicalRecordTpl1.getCreateBy())){
//                return ResponseUtil.setErrorMeg(StatusCode.ERROR_PREMISSION,"不能编辑他人模板");
//            }
//            MedicalRecordTpl medicalRecordTpl = new MedicalRecordTpl();
//            medicalRecordTpl.setUserId(info.getUserId());
//            medicalRecordTpl.setId(id);
//            List<MedicalRecordTplDto> byParam = medicalRecordTplManager.getByParam(medicalRecordTpl);
//            if (byParam.size() != 0){
                BeanUtils.copyProperties(param,medicalRecordTpl);
                medicalRecordTpl.setModifyBy(info.getUserId().toString());
                medicalRecordTpl.setModifyAt(LocalDateTime.now().toString());
                medicalRecordTpl.setUserId(null);
                medicalRecordTpl.setDept(null);
                medicalRecordTpl.setOrgCode(null);

                medicalRecordTplManager.updateMedicalRecordTpl(medicalRecordTpl);
//            }
            return ResponseUtil.setSuccessResult();
        }

    }


    @ApiOperation(value = "禁用病例模板总数")
    @GetMapping("/forbiddennum")
    public ResponseResult forbiddenMedicalRecordTpl(@AccessToken AccessInfo info){
        return ResponseUtil.setSuccessResult(medicalRecordTplManager.getForbiddenMedicalRecordTotal(info.getUserId()));
    }


    @ApiOperation(value = "病例模板列表")
    @PostMapping("/list")
    public ResponseResult<PageResult<MedicalRecordTplVo>> medicalRecordTplList(@ApiParam("条件") @RequestBody PageBase<GetMedicalRecordTplMo> pageBase, @AccessToken AccessInfo info){
        GetMedicalRecordTplMo param = pageBase.getParam();
        MedicalRecordTpl medicalRecordTpl = new MedicalRecordTpl();
        if (param == null) {
            medicalRecordTpl.setDept(info.getUser().getDept());
            medicalRecordTpl.setOrgCode(info.getUser().getOrgCode());
            medicalRecordTpl.setDept(info.getUser().getDept());
            medicalRecordTpl.setIsPublic("1");
        }else {
            Integer dept = param.getDept();
            medicalRecordTpl.setUserId(info.getUserId());
            medicalRecordTpl.setOrgCode(info.getUser().getOrgCode());

            if (dept == null || dept == 0 || param.getIsValid().equals("0")){
                medicalRecordTpl.setDept(null);
            }else {
                medicalRecordTpl.setDept(dept);
            }

            medicalRecordTpl.setCategory(param.getCategory());
            medicalRecordTpl.setTplName(param.getTplName());
            medicalRecordTpl.setIsValid(param.getIsValid());
            medicalRecordTpl.setIsPublic(param.getIsPublic());
        }
        medicalRecordTpl.setRemoved("0");
        PageResult<MedicalRecordTplDto> byParamByPage = medicalRecordTplManager.getByParamByPage(medicalRecordTpl, pageBase.getPageNum(), pageBase.getPageSize());
        PageResult<MedicalRecordTplVo> result = new PageResult<>();
        result.setPageSize(byParamByPage.getPageSize());
        result.setPageNum(byParamByPage.getPageNum());
        result.setTotal(byParamByPage.getTotal());

        List<MedicalRecordTplDto> data = byParamByPage.getData();
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


    @ApiOperation(value = "病例模板详情")
    @GetMapping("/id")
    public ResponseResult<MedicalRecordTplVo> getMedicalRecordTplVoById(@ApiParam("id") @RequestParam("id") Integer id){
        MedicalRecordTpl medicalRecordTpl = new MedicalRecordTpl();
        medicalRecordTpl.setId(id);
        return ResponseUtil.setSuccessResult(medicalRecordTplManager.getByParam(medicalRecordTpl));
    }


}
