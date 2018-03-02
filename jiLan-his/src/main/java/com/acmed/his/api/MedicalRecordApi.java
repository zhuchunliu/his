package com.acmed.his.api;

import com.acmed.his.constants.StatusCode;
import com.acmed.his.model.Apply;
import com.acmed.his.model.MedicalRecord;
import com.acmed.his.model.dto.MedicalReDto;
import com.acmed.his.pojo.mo.MedicalRecordAddMo;
import com.acmed.his.pojo.vo.MedicalRecordDetailVo;
import com.acmed.his.pojo.vo.MedicalRecordDoctorVo;
import com.acmed.his.pojo.vo.PrescriptionVo;
import com.acmed.his.pojo.vo.UserInfo;
import com.acmed.his.service.ApplyManager;
import com.acmed.his.service.MedicalRecordManager;
import com.acmed.his.service.PrescriptionManager;
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

import java.util.ArrayList;
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

    @Autowired
    private ApplyManager applyManager;

    @Autowired
    private PrescriptionManager prescriptionManager;

    @ApiOperation(value = "保存病例")
    @PostMapping("save")
    public ResponseResult addMedicalRecord(@ApiParam("病例参数") @RequestBody MedicalRecordAddMo medicalRecordAddMoo, @AccessToken AccessInfo info){
        MedicalRecord medicalRecord = new MedicalRecord();
        BeanUtils.copyProperties(medicalRecordAddMoo,medicalRecord);
        medicalRecord.setCreateBy(info.getUserId().toString());
        medicalRecord.setModifyBy(info.getUserId().toString());
        Apply applyById = applyManager.getApplyById(medicalRecord.getApplyId());
        if (applyById == null){
            return ResponseUtil.setErrorMeg(StatusCode.ERROR_ORDER,"挂号单不存在");
        }
        medicalRecord.setPatientItemId(applyById.getPatientItemId());
        UserInfo user = info.getUser();
        medicalRecord.setDept(user.getDept());
        medicalRecord.setDeptName(user.getDeptName());
        medicalRecord.setOrgCode(user.getOrgCode());
        return ResponseUtil.setSuccessResult(medicalRecordManager.saveMedicalRecord(medicalRecord));
    }

    @ApiOperation(value = "醫生查询病人病例列表")
    @GetMapping("patientItemId")
    public ResponseResult<List<MedicalRecordDoctorVo>> addMedicalRecord(@ApiParam("患者庫id") @RequestParam("patientItemId") String patientItemId, @AccessToken AccessInfo info){
        MedicalRecord medicalRecord = new MedicalRecord();
        medicalRecord.setOrgCode(info.getUser().getOrgCode());
        medicalRecord.setPatientItemId(patientItemId);
        List<MedicalRecord> medicalRecordListByMedicalRecord = medicalRecordManager.getMedicalRecordListByMedicalRecord(medicalRecord);
        List<MedicalRecordDoctorVo> list = new ArrayList<>();
        if (medicalRecordListByMedicalRecord.size()!=0){
            for(MedicalRecord param : medicalRecordListByMedicalRecord){
                MedicalRecordDoctorVo medicalRecordDoctorVo = new MedicalRecordDoctorVo();
                BeanUtils.copyProperties(param,medicalRecordDoctorVo);
                list.add(medicalRecordDoctorVo);
            }
        }
        return ResponseUtil.setSuccessResult(list);
    }

    @ApiOperation(value = "查询患者就诊列表 记录",hidden = true)
    @GetMapping("getMedicalReList")
    public ResponseResult<List<MedicalReDto>> getMedicalReList(@ApiParam("患者id") @RequestParam("patientId") Integer patientId){
        return ResponseUtil.setSuccessResult(medicalRecordManager.getMedicalReDtoList(patientId));
    }





    @ApiOperation(value = "根据病历id查询病历详情")
    @GetMapping("id")
    public ResponseResult<MedicalRecordDetailVo> get(@ApiParam("病历id") @RequestParam("id")  String id){
        MedicalRecord medicalRecordById = medicalRecordManager.getMedicalRecordById(id);
        if (medicalRecordById == null){
            return ResponseUtil.setErrorMeg(StatusCode.ERROR_ORDER,"病例不存在");
        }
        List<PrescriptionVo> preByApply = prescriptionManager.getPreByApplyId(medicalRecordById.getApplyId());
        MedicalRecordDetailVo medicalRecordDetailVo = new MedicalRecordDetailVo();
        BeanUtils.copyProperties(medicalRecordById,medicalRecordDetailVo);
        medicalRecordDetailVo.setPrescriptionVoList(preByApply);
        return ResponseUtil.setSuccessResult(medicalRecordDetailVo);
    }

    @ApiOperation(value = "根据挂号单id查询病历详情")
    @GetMapping("applyId")
    public ResponseResult<MedicalRecordDetailVo> getByApplyId(@ApiParam("挂号单id") @RequestParam("applyId")  String applyId){
        MedicalRecord medicalRecordByApplyId = medicalRecordManager.getMedicalRecordByApplyId(applyId);
        if (medicalRecordByApplyId == null){
            return ResponseUtil.setErrorMeg(StatusCode.ERROR_ORDER,"病例不存在");
        }
        List<PrescriptionVo> preByApply = prescriptionManager.getPreByApplyId(medicalRecordByApplyId.getApplyId());
        MedicalRecordDetailVo medicalRecordDetailVo = new MedicalRecordDetailVo();
        BeanUtils.copyProperties(medicalRecordByApplyId,medicalRecordDetailVo);
        medicalRecordDetailVo.setPrescriptionVoList(preByApply);
        return ResponseUtil.setSuccessResult(medicalRecordDetailVo);
    }
}
