package com.acmed.his.api;

import com.acmed.his.model.Apply;
import com.acmed.his.pojo.mo.ApplyIdAndStatus;
import com.acmed.his.pojo.mo.DeptIdAndStatus;
import com.acmed.his.pojo.mo.DeptIdAndStatusAndDate;
import com.acmed.his.pojo.vo.ApplyDoctorVo;
import com.acmed.his.service.ApplyManager;
import com.acmed.his.support.AccessInfo;
import com.acmed.his.support.AccessToken;
import com.acmed.his.util.ResponseResult;
import com.acmed.his.util.ResponseUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
    public ResponseResult add(@RequestBody Apply apply){
        applyManager.addApply(apply);
        return ResponseUtil.setSuccessResult();
    }

    @ApiOperation(value = "根据患者id 查询列表")
    @GetMapping("patientId")
    public ResponseResult patientId(Integer patientId){
        return ResponseUtil.setSuccessResult(applyManager.getApplyByPatientId(patientId));
    }


    @ApiOperation(value = "根据orgCode查询列表")
    @GetMapping("orgCode")
    public ResponseResult<List<Apply>> orgCode(Integer orgCode){
        List<Apply> applyByOrgCode = applyManager.getApplyByOrgCode(orgCode);
        return ResponseUtil.setSuccessResult(applyByOrgCode);
    }

    @ApiOperation(value = "根据挂号单id查询")
    @GetMapping("id")
    public ResponseResult<Apply> id(Integer id){
        Apply applyById = applyManager.getApplyById(id);
        return ResponseUtil.setSuccessResult(applyById);
    }


    @ApiOperation(value = "根据科室id和状态查询当天的就诊列表")
    @GetMapping("idandstatus")
    public ResponseResult<List<Apply>> idandstatus(DeptIdAndStatus model){
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
}
