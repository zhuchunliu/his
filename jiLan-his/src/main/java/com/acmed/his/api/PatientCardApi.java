package com.acmed.his.api;

import com.acmed.his.model.PatientCard;
import com.acmed.his.service.PatientCardManager;
import com.acmed.his.support.AccessInfo;
import com.acmed.his.support.AccessToken;
import com.acmed.his.util.ResponseResult;
import com.acmed.his.util.ResponseUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * PatientCardApi
 *
 * @author jimson
 * @date 2018/1/24
 */
@Api(tags = "就诊人")
@RestController
@RequestMapping("patientCard")
public class PatientCardApi {
    @Autowired
    private PatientCardManager patientCardManager;

    @ApiOperation(value = "获取自己的就诊人列表")
    @GetMapping("getSelfPatientCards")
    public ResponseResult<List<PatientCard>> getSelfPatientCards(@AccessToken AccessInfo info){
        PatientCard patientCard = new PatientCard();
        patientCard.setRemoved("0");
        patientCard.setCreateBy(info.getPatientId());
        return ResponseUtil.setSuccessResult(patientCardManager.getPatientCardList(patientCard));
    }

    @ApiOperation(value = "删除自己的就诊人")
    @GetMapping("delSelfPatientCards")
    public ResponseResult delSelfPatientCards(@AccessToken AccessInfo info,@ApiParam("id") @RequestParam("id") String id){
        PatientCard patientCard = patientCardManager.patientCardDetail(id);
        if (patientCard == null){
            return ResponseUtil.setSuccessResult();
        }
        patientCard.setRemoved("1");
        patientCard.setModifyBy(info.getPatientId());
        return ResponseUtil.setSuccessResult(patientCardManager.update(patientCard));
    }


}
