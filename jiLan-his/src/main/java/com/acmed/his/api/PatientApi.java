package com.acmed.his.api;

import com.acmed.his.model.Patient;
import com.acmed.his.model.dto.OrgPatientNumDto;
import com.acmed.his.model.dto.PatientCountDto;
import com.acmed.his.pojo.mo.PatientMo;
import com.acmed.his.pojo.mo.WxRegistPatientMo;
import com.acmed.his.pojo.vo.PatientInfoVo;
import com.acmed.his.service.PatientManager;
import com.acmed.his.support.AccessInfo;
import com.acmed.his.support.AccessToken;
import com.acmed.his.util.ResponseResult;
import com.acmed.his.util.ResponseUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
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
    @ApiOperation(value = "第三方添加患者信息")
    @PostMapping("add")
    public ResponseResult addPatient(@RequestBody PatientMo patient){
        Patient patient1 = new Patient();
        BeanUtils.copyProperties(patient,patient1);
        int add = patientManager.add(patient1);
        return ResponseUtil.setSuccessResult();
    }

    @ApiOperation(value = "微信注册")
    @PostMapping("wxregister")
    public ResponseResult<PatientInfoVo> wxRegister(@RequestBody WxRegistPatientMo wxRegistPatientMo){
        return patientManager.wxRegistPatient(wxRegistPatientMo);
    }

    @ApiOperation(value = "根据id查询病患信息")
    @GetMapping("id")
    public ResponseResult<Patient> getPatientById(@ApiParam("患者id") @RequestParam(value = "id" )String id){
        return ResponseUtil.setSuccessResult(patientManager.getPatientById(id));
    }

    @ApiOperation(value = "根据身份证号查询病患信息")
    @GetMapping("idCard")
    public ResponseResult<Patient> getPatientByIdCard(@ApiParam("患者身份证号") @RequestParam(value = "idCard" )String idCard){
        return ResponseUtil.setSuccessResult(patientManager.getPatientByIdCard(idCard));
    }

    @ApiOperation(value = "根据姓名查询")
    @GetMapping("name")
    public ResponseResult<List<Patient>> getPatientByName(@ApiParam("患者姓名") @RequestParam(value = "name" )String name){
        return ResponseUtil.setSuccessResult(patientManager.getPatientByUserName(name));
    }

    @ApiOperation(value = "根据拼音模糊查询")
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
}
