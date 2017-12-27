package com.acmed.his.api;

import com.acmed.his.constants.CommonConstants;
import com.acmed.his.pojo.mo.DeptMo;
import com.acmed.his.pojo.mo.OrgMo;
import com.acmed.his.pojo.vo.OrgVo;
import com.acmed.his.service.ApplyManager;
import com.acmed.his.service.DeptManager;
import com.acmed.his.service.OrgManager;
import com.acmed.his.support.AccessInfo;
import com.acmed.his.support.AccessToken;
import com.acmed.his.util.LngLatUtil;
import com.acmed.his.util.ResponseResult;
import com.acmed.his.util.ResponseUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.text.DecimalFormat;
import java.util.*;

/**
 * Created by Darren on 2017-11-22
 **/
@RestController
@Api(tags = "科室管理")
@RequestMapping("dept")
public class DeptApi {

    @Autowired
    private DeptManager deptManager;

    @ApiOperation(value = "新增/编辑 科室信息")
    @ApiImplicitParam(paramType = "header", dataType = "String", name = CommonConstants.USER_HEADER_TOKEN, value = "token", required = true)
    @PostMapping("/save")
    public ResponseResult saveDept(@ApiParam("id等于null:新增; id不等于null：编辑") @RequestBody DeptMo deptMo,
                                   @AccessToken AccessInfo info){
        deptManager.saveDept(deptMo,info.getUser());
        return ResponseUtil.setSuccessResult();
    }

    @ApiOperation(value = "获取科室列表")
    @GetMapping("/list")
    public ResponseResult<List<DeptMo>> getDeptList(@ApiParam("机构主键") @RequestParam("orgCode") Integer orgCode){
        List<DeptMo> list = new ArrayList<>();

        deptManager.getDeptList(orgCode).forEach(obj->{
            DeptMo deptMo = new DeptMo();
            BeanUtils.copyProperties(obj,deptMo);
            list.add(deptMo);
        });
        return ResponseUtil.setSuccessResult(list);
    }

    @ApiOperation(value = "获取科室详情")
    @GetMapping("/detail")
    public ResponseResult<DeptMo> getDeptDetail(@ApiParam("科室主键") @RequestParam("id") Integer id){
        DeptMo deptMo = new DeptMo();
        BeanUtils.copyProperties(deptManager.getDeptDetail(id),deptMo);
        return ResponseUtil.setSuccessResult(deptMo);
    }

    @ApiOperation(value = "删除机构信息")
    @ApiImplicitParam(paramType = "header", dataType = "String", name = CommonConstants.USER_HEADER_TOKEN, value = "token", required = true)
    @DeleteMapping("/del")
    public ResponseResult delDept(@ApiParam("科室主键") @RequestParam("id") Integer id,
                                  @AccessToken AccessInfo info){
        deptManager.delDept(id,info.getUser());
        return ResponseUtil.setSuccessResult();
    }



}
