package com.acmed.his.api;

import com.acmed.his.model.PatientCard;
import com.acmed.his.pojo.mo.PatientCardMo;
import com.acmed.his.service.PatientCardManager;
import com.acmed.his.support.AccessInfo;
import com.acmed.his.support.AccessToken;
import com.acmed.his.util.ResponseResult;
import com.acmed.his.util.ResponseUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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


    @ApiOperation(value = "就诊人详情")
    @GetMapping("detail")
    public ResponseResult<PatientCard> getSelfPatientCards(@AccessToken AccessInfo info,@ApiParam("id") @RequestParam("id") String id){
        return ResponseUtil.setSuccessResult(patientCardManager.patientCardDetail(id));
    }

    @ApiOperation(value = "添加/编辑")
    @PostMapping("save")
    public ResponseResult delSelfPatientCards(@AccessToken AccessInfo info, @RequestBody PatientCardMo patientCardMo){
        String id = patientCardMo.getId();
        if (StringUtils.isEmpty(id)){
            //新增
            PatientCard patientCard1 = new PatientCard();
            patientCard1.setCreateBy(info.getPatientId());
            patientCard1.setIdCard(patientCardMo.getIdCard());
            List<PatientCard> patientCardList = patientCardManager.getPatientCardList(patientCard1);
            if (patientCardList.size() == 0){
                PatientCard patientCard = new PatientCard();
                BeanUtils.copyProperties(patientCardMo,patientCard);
                patientCard.setCreateBy(info.getPatientId());
                patientCardManager.add(patientCard);
            }else {
                PatientCard patientCard = patientCardList.get(0);
                patientCard.setPatientName(patientCardMo.getPatientName());
                patientCard.setRemoved("0");
                patientCard.setRelation(patientCardMo.getRelation());
                patientCard.setSocialCard(patientCardMo.getSocialCard());
                patientCardManager.update(patientCard);
            }
        }else {
            PatientCard patientCard = new PatientCard();
            BeanUtils.copyProperties(patientCardMo,patientCard);
            patientCard.setModifyBy(info.getPatientId());
            patientCardManager.update(patientCard);
        }
        return ResponseUtil.setSuccessResult();
    }
}
