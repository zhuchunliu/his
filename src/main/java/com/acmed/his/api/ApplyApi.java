package com.acmed.his.api;

import com.acmed.his.constants.CommonConstants;
import com.acmed.his.model.Apply;
import com.acmed.his.pojo.mo.ApplyIdAndStatus;
import com.acmed.his.pojo.mo.ApplyMo;
import com.acmed.his.pojo.mo.DeptIdAndStatus;
import com.acmed.his.pojo.mo.DeptIdAndStatusAndDate;
import com.acmed.his.pojo.vo.ApplyDoctorVo;
import com.acmed.his.service.ApplyManager;
import com.acmed.his.support.AccessInfo;
import com.acmed.his.support.AccessToken;
import com.acmed.his.util.ResponseResult;
import com.acmed.his.util.ResponseUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
    @ApiImplicitParam(paramType = "header", dataType = "String", name = CommonConstants.USER_HEADER_TOKEN, value = "token", required = true)
    @PostMapping("add")
    public ResponseResult add(@RequestBody ApplyMo mo,
                              @AccessToken AccessInfo info){
        applyManager.addApply(mo,info.getPatientId());
        return ResponseUtil.setSuccessResult();
    }

    @ApiOperation(value = "根据患者id 查询列表")
    @GetMapping("patientId")
    public ResponseResult patientId(@ApiParam("患者id") @RequestParam(value = "patientId") Integer patientId){
        return ResponseUtil.setSuccessResult(applyManager.getApplyByPatientId(patientId));
    }


    @ApiOperation(value = "根据orgCode查询列表")
    @GetMapping("orgCode")
    public ResponseResult<List<Apply>> orgCode(@ApiParam("机构code") @RequestParam(value = "orgCode" )Integer orgCode){
        List<Apply> applyByOrgCode = applyManager.getApplyByOrgCode(orgCode);
        return ResponseUtil.setSuccessResult(applyByOrgCode);
    }

    @ApiOperation(value = "根据挂号单id查询")
    @GetMapping("id")
    public ResponseResult<Apply> id(@ApiParam("挂号单id") @RequestParam(value = "id" ) Integer id){
        Apply applyById = applyManager.getApplyById(id);
        return ResponseUtil.setSuccessResult(applyById);
    }


    @ApiOperation(value = "根据科室id和状态查询当天的就诊列表")
    @GetMapping("idandstatus")
    public ResponseResult<List<Apply>> idandstatus(@ApiParam("条件查询")DeptIdAndStatus model){
        Integer id = model.getDeptId();
        String status = model.getStatus();
        List<Apply> applyByDeptIdAndStatus = applyManager.getApplyByDeptIdAndStatus(id, status);
        return ResponseUtil.setSuccessResult(applyByDeptIdAndStatus);
    }

    @ApiOperation(value = "修改状态")
    @PutMapping("status")
    public ResponseResult updateStatus(ApplyIdAndStatus model,@AccessToken AccessInfo info){
        Integer id = model.getId();
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
    public ResponseResult<List<ApplyDoctorVo>> idandstatus(DeptIdAndStatusAndDate model){
        Integer id = model.getDeptId();
        String status = model.getStatus();
        String date = model.getDate();
        return ResponseUtil.setSuccessResult(applyManager.getApplyDoctorVoList(id,status,date));
    }

    @ApiOperation(value = "模糊查询当前科室挂号列表（姓名或者拼音）")
    @GetMapping("mohu")
    public ResponseResult<List<ApplyDoctorVo>> mohu(
            @ApiParam("姓名或者拼音首字母") @RequestParam(value = "param" )String param,
            @ApiParam("挂号日期，不传就是默认今天") @RequestParam(value = "id" )Date date,
            @ApiParam("状态  不传就是全部") @RequestParam(value = "id" )String status,
            @AccessToken AccessInfo info){
        Integer dept = info.getUser().getDept();
        return ResponseUtil.setSuccessResult(applyManager.getByPinyinOrName(param,status,dept,date));
    }
}
