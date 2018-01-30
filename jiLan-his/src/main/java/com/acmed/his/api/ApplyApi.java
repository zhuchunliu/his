package com.acmed.his.api;

import com.acmed.his.constants.StatusCode;
import com.acmed.his.model.Apply;
import com.acmed.his.model.Patient;
import com.acmed.his.model.PatientItem;
import com.acmed.his.model.PayStatements;
import com.acmed.his.model.dto.ChuZhenFuZhenCountDto;
import com.acmed.his.pojo.mo.ApplyMo;
import com.acmed.his.pojo.vo.ApplyDoctorVo;
import com.acmed.his.pojo.vo.ApplyPatientVo;
import com.acmed.his.pojo.vo.ApplyVo;
import com.acmed.his.service.ApplyManager;
import com.acmed.his.service.PatientItemManager;
import com.acmed.his.service.PayManager;
import com.acmed.his.support.AccessInfo;
import com.acmed.his.support.AccessToken;
import com.acmed.his.util.PageBase;
import com.acmed.his.util.PageResult;
import com.acmed.his.util.ResponseResult;
import com.acmed.his.util.ResponseUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.Info;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


/**
 * ApplyApi
 *
 * @author jimson
 * @date 2017/11/20
 */
@RestController
@Api(tags = "挂号")
@RequestMapping("/apply")
public class ApplyApi {
    @Autowired
    private ApplyManager applyManager;

    @Autowired
    private PayManager payManager;

    @Autowired
    private PatientItemManager patientItemManager;

    @ApiOperation(value = "患者添加挂号信息")
    @PostMapping("addByPatient")
    public ResponseResult add(@RequestBody ApplyMo mo,
                              @AccessToken AccessInfo info){
        return applyManager.addApply(mo,info.getPatientId(),null);
    }

    @ApiOperation(value = "医生添加挂号信息")
    @PostMapping("addByDoctor")
    public ResponseResult addByDoctor(@RequestBody ApplyMo mo,
                              @AccessToken AccessInfo info){
        return applyManager.addApply(mo,null,info.getUser());
    }

    @ApiOperation(value = "医生查看患者在本医院的挂号列表")
    @GetMapping("patientItemId")
    public ResponseResult<ApplyVo> patientId(@ApiParam("患者库id") @RequestParam(value = "patientItemId") String patientItemId,@AccessToken AccessInfo info){
        PatientItem byId = patientItemManager.getById(patientItemId);
        if (byId == null){
            return ResponseUtil.setErrorMeg(StatusCode.ERROR_DATA_EMPTY,"没有找到对应的患者");
        }
        if (!byId.getOrgCode().equals(info.getUser().getOrgCode())){
            return ResponseUtil.setErrorMeg(StatusCode.ERROR_DATA_EMPTY,"没有找到对应的患者");
        }
        List<ApplyVo> list = new ArrayList<>();
        Apply apply = new Apply();
        apply.setPatientItemId(patientItemId);
        apply.setOrgCode(info.getUser().getOrgCode());
        applyManager.getApplys(apply).forEach(obj->{
            ApplyVo applyVo = new ApplyVo();
            BeanUtils.copyProperties(obj,applyVo);
            list.add(applyVo);
        });
        return ResponseUtil.setSuccessResult(list);
    }

    @ApiOperation(value = "根据挂号单id查询")
    @GetMapping("id")
    public ResponseResult<ApplyDoctorVo> id(@ApiParam("挂号单id") @RequestParam(value = "id" ) String id){
        Apply applyById = applyManager.getApplyById(id);
        ApplyDoctorVo result = new ApplyDoctorVo();
        BeanUtils.copyProperties(applyById,result);
        return ResponseUtil.setSuccessResult(result);
    }

    @ApiOperation(value = "查询某机构的初诊数和就诊数")
    @GetMapping("chuZhenAndFuZhenTongJi")
    public ResponseResult<ChuZhenFuZhenCountDto> chuZhenAndFuZhenTongJi(@AccessToken AccessInfo info){
        return ResponseUtil.setSuccessResult(applyManager.chuZhenAndFuZhenTongJi(info.getUser().getOrgCode()));
    }

    @ApiOperation(value = "现金收挂号费")
    @GetMapping("cashcollection")
    public ResponseResult collection(@AccessToken AccessInfo info,
                                     @ApiParam("挂号单id") @RequestParam(value = "applyId" )String applyId){
        Apply applyById = applyManager.getApplyById(applyId);
        return applyManager.pay(applyId,applyById.getFee(),"0",info.getUser());
    }

    @ApiOperation(value = "条件查询 医院使用")
    @GetMapping("tiaojianchaxun")
    public ResponseResult<PageResult<ApplyDoctorVo>> getByPinyinOrNameOrClinicnoTiaojianByPage(@AccessToken AccessInfo info,
                                                                                                @ApiParam("机构id 0表示全部  传表示指定 不传表示自己所在机构") @RequestParam(value = "orgCode" ,required = false)Integer orgCode,
                                                                                                @ApiParam("科室id 0表示全部  传表示指定 不传表示自己所在科室") @RequestParam(value = "dept" ,required = false)Integer dept,
                                                                                                @ApiParam("开始时间 如2018-01-02 不传表示今天") @RequestParam(value = "startTime" ,required = false)String startTime,
                                                                                                @ApiParam("结束时间 如2018-01-02 不传表示今天") @RequestParam(value = "endTime" ,required = false)String endTime,
                                                                                                @ApiParam("状态 0:未就诊;1:已就诊,2:已取消") @RequestParam(value = "status" ,required = false)String status,
                                                                                                @ApiParam("姓名。拼音。挂号单号全模糊查询") @RequestParam(value = "param" ,required = false)String param,
                                                                                                @ApiParam("是否已付费 0:否; 1:是") @RequestParam(value = "isPaid" ,required = false)String isPaid,
                                                                                                @ApiParam("页码") @RequestParam(value = "pageNum" )Integer pageNum,
                                                                                                @ApiParam("每页记录数") @RequestParam(value = "pageSize" )Integer pageSize){
        if (orgCode == null){
            orgCode = info.getUser().getOrgCode();
        }
        if (orgCode == 0){
            orgCode = null;
        }
        if (dept == null){
            dept = info.getUser().getDept();
        }
        if (dept == 0){
            dept = null;
        }
        if (StringUtils.isEmpty(startTime)){
            startTime = LocalDate.now().toString();
        }
        if (StringUtils.isEmpty(endTime)){
            endTime = LocalDate.now().toString();
        }
        PageResult<ApplyDoctorVo> byPinyinOrNameOrClinicnoTiaojianByPage = applyManager.getByPinyinOrNameOrClinicnoTiaojianByPage(orgCode, dept, startTime, endTime, status, param, isPaid, pageNum, pageSize);
        return ResponseUtil.setSuccessResult(byPinyinOrNameOrClinicnoTiaojianByPage);
    }
    @ApiOperation(value = "现金退挂号费")
    @GetMapping("cashrefund")
    public ResponseResult refund(@AccessToken AccessInfo info,
                                 @ApiParam("挂号单id") @RequestParam(value = "applyId" )String applyId,
                                 @ApiParam("金额") @RequestParam(value = "fee" )Double fee){
        return applyManager.refund(applyId,fee,"0",info.getUser());
    }

    @ApiOperation(value = "用户退线上支付挂号费号费")
    @GetMapping("refundonline")
    public ResponseResult refundonline(@AccessToken AccessInfo info,
                                 @ApiParam("挂号单id") @RequestParam(value = "applyId" )String applyId){
        PayStatements payStatements = new PayStatements();
        payStatements.setSource("1");
        payStatements.setApplyId(applyId);
        List<PayStatements> byPayStatements = payManager.getByPayStatements(payStatements);
        if (byPayStatements.size()!=1){
            return ResponseUtil.setErrorMeg(StatusCode.FAIL,"还未支付");
        }
        return applyManager.refund(applyId,payStatements.getFee().doubleValue(),payStatements.getFeeType(),info.getUser());
    }
    @ApiOperation(value = "自己的挂号列表  ")
    @PostMapping("selfapplys")
    public ResponseResult getselfapplylist(@AccessToken AccessInfo info, @RequestBody PageBase pageBase){
        String patientId = info.getPatientId();
        Apply apply = new Apply();
        apply.setCreateBy(patientId);
        PageBase<Apply> applyPageBase = new PageBase<>();
        Integer pageNum = pageBase.getPageNum();
        applyPageBase.setPageNum(pageNum);
        Integer pageSize = pageBase.getPageSize();
        applyPageBase.setPageSize(pageSize);
        applyPageBase.setParam(apply);
        PageResult<Apply> applysByPage = applyManager.getApplysByPage(applyPageBase);
        Long total = applysByPage.getTotal();
        PageResult<ApplyPatientVo> result = new PageResult<>();
        result.setTotal(total);
        result.setPageNum(pageNum);
        result.setPageSize(pageSize);
        List<ApplyPatientVo> list = new ArrayList<>();
        List<Apply> data = applysByPage.getData();
        if (data.size()!=0){
            for (Apply apply1 : data){
                ApplyPatientVo applyPatientVo = new ApplyPatientVo();
                BeanUtils.copyProperties(apply1,applyPatientVo);
                list.add(applyPatientVo);
            }
        }
        result.setData(list);
        return ResponseUtil.setSuccessResult(result);
    }
}
