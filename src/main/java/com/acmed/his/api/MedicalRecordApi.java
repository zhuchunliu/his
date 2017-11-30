package com.acmed.his.api;

import com.acmed.his.model.MedicalRecord;
import com.acmed.his.service.MedicalRecordManager;
import com.acmed.his.util.ResponseResult;
import com.acmed.his.util.ResponseUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * MedicalRecordApi
 *
 * @author jimson
 * @date 2017/11/21
 */
@Api(tags = "病例")
@RestController
@RequestMapping("medicalRecord")
public class MedicalRecordApi {
    @Autowired
    private MedicalRecordManager medicalRecordManager;

    @ApiOperation(value = "添加病例")
    @PostMapping("add")
    public ResponseResult addMedicalRecord(@ApiParam("病例参数") @RequestBody MedicalRecord medicalRecord){
        return ResponseUtil.setSuccessResult(medicalRecordManager.addMedicalRecord(medicalRecord));
    }

    @ApiOperation(value = "查询病人病例列表")
    @GetMapping("patientId")
    public ResponseResult<List<MedicalRecord>> addMedicalRecord(@ApiParam("参数")  Integer patientId){
        return ResponseUtil.setSuccessResult(medicalRecordManager.getMedicalRecordListByPatientId(patientId,null));
    }
}
