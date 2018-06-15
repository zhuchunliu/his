package com.acmed.his.api;

import com.acmed.his.constants.StatusCode;
import com.acmed.his.consts.DicTypeEnum;
import com.acmed.his.model.Prescription;
import com.acmed.his.pojo.mo.PreMo;
import com.acmed.his.pojo.vo.PreDrugVo;
import com.acmed.his.pojo.vo.PreTitleVo;
import com.acmed.his.pojo.vo.PreVo;
import com.acmed.his.service.CommonManager;
import com.acmed.his.service.FeeItemManager;
import com.acmed.his.service.PrescriptionManager;
import com.acmed.his.support.AccessInfo;
import com.acmed.his.support.AccessToken;
import com.acmed.his.util.ResponseResult;
import com.acmed.his.util.ResponseUtil;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.ImmutableMap;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Created by Darren on 2017-11-22
 **/
@RestController
@RequestMapping("/pre")
@Api(tags = "新开就诊")
public class PrescriptionApi {

    @Autowired
    private PrescriptionManager preManager;

    @Autowired
    private CommonManager commonManager;

    @ApiOperation(value = "获取门诊编号、处方编号")
    @GetMapping("/getNo")
    public ResponseResult getNo(@ApiParam("0:全部,1:门诊编号,2:处方编号；默认为0") @RequestParam("type") Integer type,
                                @AccessToken AccessInfo info){
        switch (type){
            case 0:
                return ResponseUtil.setSuccessResult(ImmutableMap.of("prescriptionNo",
                        commonManager.getPrescriptionNo(info.getUser().getOrgCode()),
                        "clinicNo",commonManager.getClinicNo(info.getUser().getOrgCode(),null)));
            case 1:
                return ResponseUtil.setSuccessResult(ImmutableMap.of("clinicNo",
                        commonManager.getClinicNo(info.getUser().getOrgCode(),null)));
            default:
                return ResponseUtil.setSuccessResult(ImmutableMap.of("prescriptionNo",
                        commonManager.getPrescriptionNo(info.getUser().getOrgCode())));

        }

    }

    @ApiOperation(value = "保存处方")
    @PostMapping
    public ResponseResult savePre(@ApiParam("id等于null:新增; id不等于null：编辑") @RequestBody PreMo mo,
                                  @AccessToken AccessInfo info){

        if(StringUtils.isEmpty(mo.getApplyId())) {//挂号单为null验证用户信息

            if (null == mo.getPatient() || StringUtils.isEmpty(mo.getPatient().getRealName())) {
                return ResponseUtil.setParamEmptyError("患者姓名不能为空!");
            }

            if (StringUtils.isNotEmpty(mo.getPatient().getIdCard()) && mo.getPatient().getIdCard().length() == 8){
                // 表示传的是生日
                String orgCode = "0000000"+info.getUser().getOrgCode();
                long l = System.currentTimeMillis();
                String s = l + "";
                mo.getPatient().setIdCard(orgCode.substring(orgCode.length()-6)+mo.getPatient().getIdCard()+s.substring(s.length()-4));
            }
        }

        Prescription prescription = preManager.savePre(mo,info.getUser());
        return null == prescription?ResponseUtil.setErrorMeg(StatusCode.FAIL,"新增处方失败"):
                ResponseUtil.setSuccessResult(ImmutableMap.of("id",prescription.getId(),"applyId",prescription.getApplyId()));
    }

    @ApiOperation(value = "获取处方")
    @GetMapping
    public ResponseResult<PreVo> getPre(@ApiParam("挂号单主键") @RequestParam("applyId") String applyId,
                                        @AccessToken AccessInfo info){
        return ResponseUtil.setSuccessResult(preManager.getPre(applyId,info.getUser()));
    }


    @ApiOperation(value = "获取药品处方")
    @GetMapping("/drug")
    public ResponseResult<PreDrugVo> getPreDrug(@ApiParam("挂号单主键") @RequestParam("applyId") String applyId){
        return ResponseUtil.setSuccessResult(preManager.getPreDrug(applyId));
    }




    @ApiOperation(value = "根据挂号id获取处方列表",hidden = true)
    @PostMapping("/list")
    public ResponseResult<PreTitleVo> getPreByApply(@ApiParam("挂号主键") @RequestParam("applyId") String applyId){
        List<PreTitleVo> list = new ArrayList<>();
        preManager.getPreByApply(applyId).forEach(obj->list.add(new PreTitleVo(obj)));
        return ResponseUtil.setSuccessResult(list);
    }

//    @ApiOperation(value = "已完成")
//    @PostMapping("/finish")
//    public ResponseResult<PreTitleVo> finish(@ApiParam("{\"applyId\":\"\"},applyId：处方主键") @RequestBody String param,
//                                             @AccessToken AccessInfo info){
//        if(StringUtils.isEmpty(param) || null == JSONObject.parseObject(param).get("applyId")){
//            return ResponseUtil.setParamEmptyError("applyId");
//        }
//        preManager.finish(JSONObject.parseObject(param).getString("applyId"),info.getUser());
//        return ResponseUtil.setSuccessResult();
//    }





}
