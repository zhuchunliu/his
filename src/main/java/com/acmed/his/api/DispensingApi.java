package com.acmed.his.api;

import com.acmed.his.constants.CommonConstants;
import com.acmed.his.dao.ChargeMapper;
import com.acmed.his.dao.PrescriptionItemMapper;
import com.acmed.his.dao.PrescriptionMapper;
import com.acmed.his.model.*;
import com.acmed.his.pojo.vo.DispensingPreVo;
import com.acmed.his.pojo.vo.DispensingVo;
import com.acmed.his.service.*;
import com.acmed.his.support.AccessInfo;
import com.acmed.his.support.AccessToken;
import com.acmed.his.util.ResponseResult;
import com.acmed.his.util.ResponseUtil;
import com.alibaba.fastjson.JSONObject;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import tk.mybatis.mapper.entity.Example;

import java.util.ArrayList;
import java.util.List;

/**
 * 发药接口
 *
 * Created by Darren on 2017-12-05
 **/
@RestController
@Api(tags = "发药管理")
@RequestMapping("/dispens")
public class DispensingApi {

    @Autowired
    private ApplyManager applyManager;

    @Autowired
    private MedicalRecordManager medicalRecordManager;

    @Autowired
    private UserManager userManager;

    @Autowired
    private PrescriptionManager preManager;

    @Autowired
    private ChargeMapper chargeMapper;

    @Autowired
    private PrescriptionMapper preMapper;

    @Autowired
    private PrescriptionItemMapper preItemMapper;

    @Autowired
    private BaseInfoManager baseInfoManager;

    @ApiOperation(value = "根据挂号id获取处方列表")
    @ApiImplicitParam(paramType = "header", dataType = "String", name = CommonConstants.USER_HEADER_TOKEN, value = "token", required = true)
    @GetMapping("/list")
    public ResponseResult<DispensingVo> getDispensingList(
            @ApiParam("患者姓名或者编号") @RequestParam(value = "name",required = false)String name,
            @ApiParam("有效期") @RequestParam(value = "date",required = false)String date,
            @ApiParam("1:已经发药,0:未发药") @RequestParam(value = "status",required = false)String status,
            @AccessToken AccessInfo info){
        List<DispensingVo> list = new ArrayList<>();
        List<Apply> applyList = applyManager.getApply(info.getUser().getOrgCode(),name,date,status);
        applyList.forEach(obj->{
            DispensingVo mo = new DispensingVo();
            BeanUtils.copyProperties(obj,mo);

            MedicalRecord record = medicalRecordManager.getMedicalRecordByApplyId(mo.getId());
            if(null != record){
                mo.setDoctorName(userManager.getUserDetail(Integer.parseInt(record.getCreateBy())).getNickName());
                mo.setDiagnosis(record.getDiagnosis());
            }
            list.add(mo);
        });
        return ResponseUtil.setSuccessResult(list);
    }

    @ApiOperation(value = "根据挂号id获取处方列表")
    @GetMapping("/pre")
    public ResponseResult<DispensingPreVo> getDispensingDetail(@ApiParam("挂号主键") @RequestParam("applyId") Integer applyId){
        List<DispensingPreVo> list = new ArrayList<>();
        preManager.getPreByApply(applyId).forEach(obj->{

            Prescription prescription = preMapper.selectByPrimaryKey(obj.getId());
            if(prescription.getCategory().equals("1")){//只处理药品处方
                Example example = new Example(Charge.class);
                example.createCriteria().andEqualTo("prescriptionId",obj.getId());
                List<Charge> chargeList = chargeMapper.selectByExample(example);

                example = new Example(Inspect.class);
                example.createCriteria().andEqualTo("prescriptionId",obj.getId());


                example = new Example(PrescriptionItem.class);
                example.createCriteria().andEqualTo("prescriptionId",obj.getId());
                List<PrescriptionItem> preItemList = preItemMapper.selectByExample(example);
                list.add(new DispensingPreVo(prescription,chargeList,preItemList,baseInfoManager));
            }


        });
        return ResponseUtil.setSuccessResult(list);
    }

    @ApiOperation(value = "确认发药")
    @ApiImplicitParam(paramType = "header", dataType = "String", name = CommonConstants.USER_HEADER_TOKEN, value = "token", required = true)
    @PostMapping
    public ResponseResult dispensing(@ApiParam("{\"id\":},id：处方主键") @RequestBody String param,
                                        @AccessToken AccessInfo info){
        if(StringUtils.isEmpty(param) || null == JSONObject.parseObject(param).get("id")){
            return ResponseUtil.setParamEmptyError("id");
        }
        preManager.dispensing(JSONObject.parseObject(param).getInteger("id"),info.getUser());
        return ResponseUtil.setSuccessResult();
    }


    @ApiOperation(value = "付费")
    @ApiImplicitParam(paramType = "header", dataType = "String", name = CommonConstants.USER_HEADER_TOKEN, value = "token", required = true)
    @PostMapping("/pay")
    public ResponseResult pay(@ApiParam("{\"id\":\"\";\"fee\":\"\",\"feeType\":\"\"},id：处方主键、fee：实际付费金额、feeType：付费类型") @RequestBody String param,
                                     @AccessToken AccessInfo info){
        if(StringUtils.isEmpty(param) || null == JSONObject.parseObject(param).get("id")){
            return ResponseUtil.setParamEmptyError("id");
        }
        applyManager.pay(JSONObject.parseObject(param).getInteger("id"),
                JSONObject.parseObject(param).getDouble("fee"),
                JSONObject.parseObject(param).getString("feeType"),info.getUser());
        return ResponseUtil.setSuccessResult();
    }
}
