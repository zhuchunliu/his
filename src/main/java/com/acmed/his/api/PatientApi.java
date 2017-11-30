package com.acmed.his.api;

import com.acmed.his.model.Patient;
import com.acmed.his.pojo.mo.WxRegistPatientMo;
import com.acmed.his.pojo.vo.PatientInfoVo;
import com.acmed.his.service.PatientManager;
import com.acmed.his.util.ResponseResult;
import com.acmed.his.util.ResponseUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
    public ResponseResult addPatient(@RequestBody Patient patient){
        int add = patientManager.add(patient);
        return ResponseUtil.setSuccessResult();
    }

    @ApiOperation(value = "微信注册")
    @PostMapping("wxregister")
    public ResponseResult<PatientInfoVo> wxRegister(@RequestBody WxRegistPatientMo wxRegistPatientMo){
        return patientManager.wxRegistPatient(wxRegistPatientMo);
    }

    @ApiOperation(value = "根据id查询病患信息")
    @GetMapping("id")
    public ResponseResult<Patient> getPatientById(Integer id){
        return ResponseUtil.setSuccessResult(patientManager.getPatientById(id));
    }

    @ApiOperation(value = "根据身份证号查询病患信息")
    @GetMapping("idCard")
    public ResponseResult<Patient> getPatientByIdCard(String idCard){
        return ResponseUtil.setSuccessResult(patientManager.getPatientByIdCard(idCard));
    }

    @ApiOperation(value = "根据姓名查询")
    @GetMapping("name")
    public ResponseResult<List<Patient>> getPatientByName(String name){
        return ResponseUtil.setSuccessResult(patientManager.getPatientByUserName(name));
    }


}
