package com.acmed.his.api;

import com.acmed.his.model.MedicalRecord;
import com.acmed.his.service.MedicalRecordManager;
import com.acmed.his.util.ResponseResult;
import com.acmed.his.util.ResponseUtil;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * MedicalRecordApi
 *
 * @author jimson
 * @date 2017/11/21
 */
@RestController
public class MedicalRecordApi {
    @Autowired
    private MedicalRecordManager medicalRecordManager;

    @ApiOperation(value = "添加病例")
    @PostMapping("medicalRecord/add")
    public ResponseResult addMedicalRecord(@ApiParam("病例参数") @RequestBody MedicalRecord medicalRecord){
        return ResponseUtil.setSuccessResult(medicalRecordManager.addMedicalRecord(medicalRecord));
    }

    @ApiOperation(value = "查询病例")
    @GetMapping("medicalRecords/patientId")
    public ResponseResult addMedicalRecord(@ApiParam("参数")  Integer patientId){
        return ResponseUtil.setSuccessResult(medicalRecordManager.getMedicalRecordListByPatientId(patientId,null));
    }
}