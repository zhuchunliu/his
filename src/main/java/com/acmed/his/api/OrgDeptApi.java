package com.acmed.his.api;

import com.acmed.his.pojo.mo.DeptMo;
import com.acmed.his.pojo.mo.OrgMo;
import com.acmed.his.service.DeptManager;
import com.acmed.his.service.OrgManager;
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
 * Created by Darren on 2017-11-22
 **/
@RestController
@Api("机构管理")
public class OrgDeptApi {
    @Autowired
    private OrgManager orgManager;

    @Autowired
    private DeptManager deptManager;

    @ApiOperation(value = "新增/编辑 机构信息")
    @PostMapping("/org/save")
    public ResponseResult saveRole(@ApiParam("orgCode等于null:新增; orgCode不等于null：编辑") @RequestBody OrgMo orgMo,
                                   @AccessToken AccessInfo info){
        orgManager.saveOrg(orgMo,info.getUser());
        return ResponseUtil.setSuccessResult();
    }

    @ApiOperation(value = "获取机构列表")
    @GetMapping("/org/list")
    public ResponseResult<List<OrgMo>> getRoleList(){
        List<OrgMo> list = new ArrayList<>();
        OrgMo orgMo = new OrgMo();
        orgManager.getOrgList().forEach((obj)->{
            BeanUtils.copyProperties(obj,orgMo);list.add(orgMo);});
        return ResponseUtil.setSuccessResult(list);
    }

    @ApiOperation(value = "获取机构详情")
    @GetMapping("/org/detail")
    public ResponseResult<OrgMo> getRoleDetail(@ApiParam("机构主键") @RequestParam("orgCode") Integer orgCode){
        OrgMo orgMo = new OrgMo();
        BeanUtils.copyProperties(orgManager.getOrgDetail(orgCode),orgMo);
        return ResponseUtil.setSuccessResult(orgMo);
    }

    @ApiOperation(value = "删除机构信息")
    @DeleteMapping("/org/del")
    public ResponseResult delRole(@ApiParam("机构主键") @RequestParam("orgCode") Integer orgCode,
                                  @AccessToken AccessInfo info){
        orgManager.delOrg(orgCode,info.getUser());
        return ResponseUtil.setSuccessResult();
    }


    @ApiOperation(value = "新增/编辑 科室信息")
    @PostMapping("/dept/save")
    public ResponseResult saveDept(@ApiParam("id等于null:新增; id不等于null：编辑") @RequestBody DeptMo deptMo){
        deptManager.saveDept(deptMo);
        return ResponseUtil.setSuccessResult();
    }

    @ApiOperation(value = "获取科室列表")
    @GetMapping("/dept/list")
    public ResponseResult<List<DeptMo>> getDeptList(@ApiParam("机构主键") @RequestParam("orgCode") Integer orgCode){
        List<DeptMo> list = new ArrayList<>();
        DeptMo deptMo = new DeptMo();
        deptManager.getDeptList(orgCode).forEach((obj)->{
            BeanUtils.copyProperties(obj,deptMo);list.add(deptMo);});
        return ResponseUtil.setSuccessResult(list);
    }

    @ApiOperation(value = "获取科室详情")
    @GetMapping("/dept/detail")
    public ResponseResult<DeptMo> getDeptDetail(@ApiParam("科室主键") @RequestParam("id") Integer id){
        DeptMo deptMo = new DeptMo();
        BeanUtils.copyProperties(deptManager.getDeptDetail(id),deptMo);
        return ResponseUtil.setSuccessResult(deptMo);
    }

    @ApiOperation(value = "删除机构信息")
    @DeleteMapping("/dept/del")
    public ResponseResult delDept(@ApiParam("科室主键") @RequestParam("id") Integer id){
        deptManager.delDept(id);
        return ResponseUtil.setSuccessResult();
    }
}
