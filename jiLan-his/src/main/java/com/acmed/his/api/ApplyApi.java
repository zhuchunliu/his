package com.acmed.his.api;

import com.acmed.his.model.Apply;
import com.acmed.his.model.dto.ChuZhenFuZhenCountDto;
import com.acmed.his.pojo.mo.ApplyIdAndStatus;
import com.acmed.his.pojo.mo.ApplyMo;
import com.acmed.his.pojo.vo.ApplyDoctorVo;
import com.acmed.his.pojo.vo.ApplyVo;
import com.acmed.his.service.ApplyManager;
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

import java.util.ArrayList;
import java.util.Date;
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

    @ApiOperation(value = "添加挂号信息")
    @PostMapping("add")
    public ResponseResult add(@RequestBody ApplyMo mo,
                              @AccessToken AccessInfo info){
        applyManager.addApply(mo,info.getPatientId());
        return ResponseUtil.setSuccessResult();
    }

    @ApiOperation(value = "根据患者id 查询列表,传患者id就是查询患者的挂号列表  如果不传就是差自己的挂号列表")
    @GetMapping("patientId")
    public ResponseResult<ApplyVo> patientId(@ApiParam("患者id") @RequestParam(value = "patientId",required = false) String patientId,
                                    @AccessToken AccessInfo info){
        if(StringUtils.isEmpty(patientId)){
            patientId = info.getPatientId();
        }
        List<ApplyVo> list = new ArrayList<>();
        applyManager.getApplyByPatientId(patientId).forEach(obj->{
            ApplyVo applyVo = new ApplyVo();
            BeanUtils.copyProperties(obj,applyVo);
            list.add(applyVo);
        });
        return ResponseUtil.setSuccessResult(list);
    }


    @ApiOperation(value = "根据orgCode查询列表")
    @GetMapping("orgCode")
    public ResponseResult<List<Apply>> orgCode(@ApiParam("机构code") @RequestParam(value = "orgCode" )Integer orgCode){
        List<Apply> applyByOrgCode = applyManager.getApplyByOrgCode(orgCode);
        return ResponseUtil.setSuccessResult(applyByOrgCode);
    }

    @ApiOperation(value = "根据挂号单id查询")
    @GetMapping("id")
    public ResponseResult<Apply> id(@ApiParam("挂号单id") @RequestParam(value = "id" ) String id){
        Apply applyById = applyManager.getApplyById(id);
        return ResponseUtil.setSuccessResult(applyById);
    }


    @ApiOperation(value = "根据科室id和状态查询当天的就诊列表")
    @GetMapping("idandstatus")
    public ResponseResult<List<Apply>> idandstatus(
            @ApiParam("科室id") @RequestParam(value = "deptId",required = false) Integer deptId,
            @ApiParam("状态码 状态 0:未就诊;1:已就诊,2:已取消  不填表示全部付费列表") @RequestParam(value = "status",required = false) String status){
        List<Apply> applyByDeptIdAndStatus = applyManager.getApplyByDeptIdAndStatus(deptId, status);
        return ResponseUtil.setSuccessResult(applyByDeptIdAndStatus);
    }

    @ApiOperation(value = "修改状态")
    @PostMapping("status")
    public ResponseResult updateStatus(@RequestBody ApplyIdAndStatus model,@AccessToken AccessInfo info){
        String id = model.getId();
        String status = model.getStatus();
        Apply apply = new Apply();
        apply.setId(id);
        apply.setStatus(status);
        // 添加修改人的id
        apply.setModifyBy(info.getUserId().toString());
        applyManager.updateApply(apply);
        return ResponseUtil.setSuccessResult();
    }

    @ApiOperation(value = "根据科室id和状态查询当天的就诊列表")
    @GetMapping("idandstatusanddate")
    public ResponseResult<List<ApplyDoctorVo>> idandstatus(
                                                            @ApiParam("科室id") @RequestParam(value = "deptId",required = false) Integer deptId,
                                                           @ApiParam("状态码 状态 0:未就诊;1:已就诊,2:已取消  不填表示全部付费列表") @RequestParam(value = "status",required = false) String status,
                                                           @ApiParam("日期   yyyy-MM-dd") @RequestParam(value = "date",required = false) String date){
        return ResponseUtil.setSuccessResult(applyManager.getApplyDoctorVoList(deptId,date,status));
    }

    @ApiOperation(value = "模糊查询当前科室挂号列表（姓名或者拼音）")
    @GetMapping("mohu")
    public ResponseResult<List<ApplyDoctorVo>> mohu(
            @ApiParam("姓名或者拼音首字母") @RequestParam(value = "param" )String param,
            @ApiParam("挂号日期，不传就是默认今天  2018-01-10") @RequestParam(value = "date",required = false )String date,
            @ApiParam("状态码 状态 0:未就诊;1:已就诊,2:已取消  不填表示全部付费列表") @RequestParam(value = "status" ,required = false)String status,
            @AccessToken AccessInfo info){
        Integer dept = info.getUser().getDept();
        return ResponseUtil.setSuccessResult(applyManager.getByPinyinOrName(param,status,dept,date));
    }

    @ApiOperation(value = "查询某机构的初诊数和就诊数")
    @GetMapping("chuZhenAndFuZhenTongJi")
    public ResponseResult<ChuZhenFuZhenCountDto> chuZhenAndFuZhenTongJi(@AccessToken AccessInfo info){
        return ResponseUtil.setSuccessResult(applyManager.chuZhenAndFuZhenTongJi(info.getUser().getOrgCode()));
    }

    @ApiOperation(value = "现金收挂号费")
    @GetMapping("cashcollection")
    public ResponseResult collection(@AccessToken AccessInfo info,
                                     @ApiParam("挂号单id") @RequestParam(value = "applyId" )String applyId,
                                     @ApiParam("金额") @RequestParam(value = "fee" )Double fee){
        return applyManager.pay(applyId,fee,"0",info.getUser());
    }
}
