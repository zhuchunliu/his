package com.acmed.his.api;

import com.acmed.his.constants.StatusCode;
import com.acmed.his.pojo.mo.DeptMo;
import com.acmed.his.pojo.vo.DeptVo;
import com.acmed.his.service.DeptManager;
import com.acmed.his.service.UserManager;
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
import java.util.List;
import java.util.Optional;

/**
 * Created by Darren on 2017-11-22
 **/
@RestController
@Api(tags = "科室管理")
@RequestMapping("dept")
public class DeptApi {

    @Autowired
    private DeptManager deptManager;

    @Autowired
    private UserManager userManager;

    @ApiOperation(value = "新增/编辑 科室信息")
    @PostMapping("/save")
    public ResponseResult saveDept(@ApiParam("id等于null:新增; id不等于null：编辑") @RequestBody DeptMo deptMo,
                                   @AccessToken AccessInfo info){
        if(StringUtils.isEmpty(deptMo.getDept())){
            return ResponseUtil.setErrorMeg(StatusCode.FAIL,"科室名称不能为空!");

        }
        deptManager.saveDept(deptMo,info.getUser());
        return ResponseUtil.setSuccessResult();
    }

    @ApiOperation(value = "获取科室列表")
    @GetMapping("/list")
    public ResponseResult<List<DeptVo>> getDeptList(@AccessToken AccessInfo info,@ApiParam("科室主键") @RequestParam("orgCode") Integer orgCode){
        List<DeptVo> list = new ArrayList<>();
        Integer org = null;
        if (orgCode == null){
            org = info.getUser().getOrgCode();
        }else {
            org = orgCode;
        }
        deptManager.getDeptList(org).forEach(obj->{
            DeptVo deptVo = new DeptVo();
            if(!StringUtils.isEmpty(obj.getCreateBy())){
                deptVo.setCreateUserName(Optional.ofNullable(userManager.getUserDetail(Integer.parseInt(obj.getCreateBy())))
                        .map(user->user.getUserName()).orElse(null));
            }
            BeanUtils.copyProperties(obj,deptVo);
            list.add(deptVo);
        });
        return ResponseUtil.setSuccessResult(list);
    }

    @ApiOperation(value = "获取科室详情")
    @GetMapping("/detail")
    public ResponseResult<DeptVo> getDeptDetail(@ApiParam("科室主键") @RequestParam("id") Integer id){
        DeptVo deptMo = new DeptVo();
        BeanUtils.copyProperties(deptManager.getDeptDetail(id),deptMo);
        return ResponseUtil.setSuccessResult(deptMo);
    }

    @ApiOperation(value = "删除科室信息")
    @DeleteMapping("/del")
    public ResponseResult delDept(@ApiParam("科室主键") @RequestParam("id") Integer id,
                                  @AccessToken AccessInfo info){
        deptManager.delDept(id,info.getUser());
        return ResponseUtil.setSuccessResult();
    }



}
