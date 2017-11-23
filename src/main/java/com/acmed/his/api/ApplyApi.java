package com.acmed.his.api;

import com.acmed.his.model.Apply;
import com.acmed.his.pojo.mo.ApplyIdAndStatus;
import com.acmed.his.pojo.mo.DeptIdAndStatus;
import com.acmed.his.service.ApplyManager;
import com.acmed.his.util.ResponseResult;
import com.acmed.his.util.ResponseUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


/**
 * ApplyApi
 *
 * @author jimson
 * @date 2017/11/20
 */
@RestController
@Api("挂号")
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
    public ResponseResult orgCode(Integer orgCode){
        return ResponseUtil.setSuccessResult(applyManager.getApplyByOrgCode(orgCode));
    }

    @ApiOperation(value = "根据orgCode查询列表")
    @GetMapping("id")
    public ResponseResult id(Integer id){
        return ResponseUtil.setSuccessResult(applyManager.getApplyById(id));
    }


    @ApiOperation(value = "根据科室id和状态查询当天的就诊列表")
    @GetMapping("idandstatus")
    public ResponseResult idandstatus(DeptIdAndStatus model){
        Integer id = model.getDeptId();
        String status = model.getStatus();
        return ResponseUtil.setSuccessResult(applyManager.getApplyByDeptIdAndStatus(id,status));
    }

    @ApiOperation(value = "修改状态")
    @PutMapping("status")
    public ResponseResult updateStatus(ApplyIdAndStatus model){
        Integer id = model.getId();
        String status = model.getStatus();
        Apply apply = new Apply();
        apply.setId(id);
        apply.setStatus(status);
        return ResponseUtil.setSuccessResult(applyManager.updateApply(apply));
    }

}
