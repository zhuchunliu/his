package com.acmed.his.api;

import com.acmed.his.constants.StatusCode;
import com.acmed.his.exceptions.BaseException;
import com.acmed.his.model.Patient;
import com.acmed.his.model.PatientItem;
import com.acmed.his.model.dto.OrgPatientNumDto;
import com.acmed.his.model.dto.PatientCountDto;
import com.acmed.his.pojo.mo.PatientMo;
import com.acmed.his.pojo.mo.PatientMobileUpMo;
import com.acmed.his.pojo.mo.WxRegistPatientMo;
import com.acmed.his.pojo.vo.PatientInfoVo;
import com.acmed.his.pojo.vo.PatientVoP;
import com.acmed.his.service.PatientManager;
import com.acmed.his.support.AccessInfo;
import com.acmed.his.support.AccessToken;
import com.acmed.his.util.*;
import com.alibaba.fastjson.JSONObject;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * PatientApi
 *
 * @author jimson
 * @date 2017/11/22
 */
@Api(tags = "患者")
@RestController
@RequestMapping("patient")
public class PatientApi {
    @Autowired
    private PatientManager patientManager;

    /**
     * 第三方添加患者信息
     * @param patient 患者信息
     * @return ResponseResult
     */
    //@ApiOperation(value = "第三方添加患者信息",hidden = true)// 暂时没有了
    //@PostMapping("add")
    public ResponseResult addPatient(@RequestBody PatientMo patient){
        Patient patient1 = new Patient();
        BeanUtils.copyProperties(patient,patient1);
        patientManager.add(patient1);
        return ResponseUtil.setSuccessResult();
    }

    @ApiOperation(value = "微信注册")
    @PostMapping("wxregister")
    public ResponseResult<PatientInfoVo> wxRegister(@RequestBody WxRegistPatientMo wxRegistPatientMo){
        return patientManager.wxRegistPatient(wxRegistPatientMo);
    }

    @ApiOperation(value = "根据id查询病患信息",hidden = true)
    @GetMapping("id")
    public ResponseResult<Patient> getPatientById(@ApiParam("患者id") @RequestParam(value = "id" )String id){
        return ResponseUtil.setSuccessResult(patientManager.getPatientById(id));
    }

    @ApiOperation(value = "根据身份证号查询病患信息",hidden = true)
    @GetMapping("idCard")
    public ResponseResult<Patient> getPatientByIdCard(@ApiParam("患者身份证号") @RequestParam(value = "idCard" )String idCard){
        return ResponseUtil.setSuccessResult(patientManager.getPatientByIdCard(idCard));
    }

    @ApiOperation(value = "根据姓名查询",hidden = true)
    @GetMapping("name")
    public ResponseResult<List<Patient>> getPatientByName(@ApiParam("患者姓名") @RequestParam(value = "name" )String name){
        return ResponseUtil.setSuccessResult(patientManager.getPatientByRealName(name));
    }

    @ApiOperation(value = "根据拼音模糊查询",hidden = true)
    @GetMapping("pinyin")
    public ResponseResult<List<Patient>> getPatientLikePinYin(@ApiParam("患者姓名拼音") @RequestParam(value = "pinYin" )String pinYin){
        return ResponseUtil.setSuccessResult(patientManager.getPatientLikePinYin(pinYin));
    }

    @ApiOperation(value = "机构患者年龄分布统计")
    @GetMapping("getPatientCount")
    public ResponseResult<List<PatientCountDto>> getPatientCount(@AccessToken AccessInfo info){
        return ResponseUtil.setSuccessResult(patientManager.getPatientCount(info.getUser().getOrgCode()));
    }

    @ApiOperation(value = "机构患者人数统计")
    @GetMapping("getDayNumAnTotalNum")
    public ResponseResult<OrgPatientNumDto> getDayNumAnTotalNum(@AccessToken AccessInfo info){
        return ResponseUtil.setSuccessResult(patientManager.getDayNumAnTotalNum(info.getUser().getOrgCode(), LocalDate.now().toString()));
    }

    @ApiOperation("修改密码")
    @PostMapping(value = "/changePasswd")
    public ResponseResult changePasswd(@ApiParam("{\"oldPasswd\":\"\",\"newPasswd\":\"\"},oldPasswd：老密码、newPasswd：新密码")  @RequestBody String param,
                                       @AccessToken AccessInfo info){
        if(org.apache.commons.lang3.StringUtils.isEmpty(param) || null == JSONObject.parseObject(param).get("oldPasswd")){
            return ResponseUtil.setParamEmptyError("oldPasswd");
        }
        if(org.apache.commons.lang3.StringUtils.isEmpty(param) || null == JSONObject.parseObject(param).get("newPasswd")){
            return ResponseUtil.setParamEmptyError("newPasswd");
        }
        String patientId = info.getPatientId();
        String oldPasswd = JSONObject.parseObject(param).get("oldPasswd").toString();
        String newPasswd = JSONObject.parseObject(param).get("newPasswd").toString();

        Patient patientById = patientManager.getPatientById(patientId);
        if(!patientById.getPassWd().equalsIgnoreCase(MD5Util.encode(oldPasswd))){
            throw new BaseException(StatusCode.ERROR_PASSWD);
        }
        patientById.setPassWd(MD5Util.encode(newPasswd));
        patientById.setModifyBy(patientId);
        patientById.setModifyAt(LocalDateTime.now().toString());
        patientManager.update(patientById);
        return ResponseUtil.setSuccessResult();
    }

    @ApiOperation("设置密码")
    @PostMapping(value = "/addpwd")
    public ResponseResult addpwd(@ApiParam("{\"newPasswd\":\"\"}newPasswd：新密码")  @RequestBody String param,
                                       @AccessToken AccessInfo info){
        if(org.apache.commons.lang3.StringUtils.isEmpty(param) || null == JSONObject.parseObject(param).get("newPasswd")){
            return ResponseUtil.setParamEmptyError("newPasswd");
        }
        String patientId = info.getPatientId();
        String newPasswd = JSONObject.parseObject(param).get("newPasswd").toString();
        Patient patientById = patientManager.getPatientById(patientId);
        if(StringUtils.isNotEmpty(patientById.getPassWd())){
            return ResponseUtil.setErrorMeg(StatusCode.ERROR_PASSWD,"已经设置过密码");
        }
        patientById.setPassWd(MD5Util.encode(newPasswd));
        patientById.setModifyBy(patientId);
        patientManager.update(patientById);
        return ResponseUtil.setSuccessResult();
    }

    @ApiOperation("自己更新基础信息")
    @PostMapping(value = "/updateBaseInfo")
    public ResponseResult updateBaseInfo(@RequestBody PatientMobileUpMo param,
                                         @AccessToken AccessInfo info){
        Patient patient = new Patient();
        BeanUtils.copyProperties(param,patient);
        patient.setId(info.getPatientId());
        patient.setModifyBy(info.getPatientId());
        patientManager.update(patient);
        return ResponseUtil.setSuccessResult();
    }

    @ApiOperation("自己个人信息")
    @GetMapping(value = "/selfinfo")
    public ResponseResult<PatientVoP> selfinfo(@AccessToken AccessInfo info){
        Patient patientById = patientManager.getPatientById(info.getPatientId());
        PatientVoP patientVoP = new PatientVoP();
        BeanUtils.copyProperties(patientById,patientVoP);
        patientVoP.setNickName(EmojiUtil.emojiRecovery(patientById.getNickName()));
        return ResponseUtil.setSuccessResult(patientVoP);
    }
}
