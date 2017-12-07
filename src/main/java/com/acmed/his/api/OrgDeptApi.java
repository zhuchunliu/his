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
@Api(tags = "机构/科室管理")
public class OrgDeptApi {
    @Autowired
    private OrgManager orgManager;

    @Autowired
    private DeptManager deptManager;

    @Autowired
    private ApplyManager applyManager;

    @ApiOperation(value = "新增/编辑 机构信息")
    @ApiImplicitParam(paramType = "header", dataType = "String", name = CommonConstants.USER_HEADER_TOKEN, value = "token", required = true)
    @PostMapping("/org/save")
    public ResponseResult saveOrg(@ApiParam("orgCode等于null:新增; orgCode不等于null：编辑") @RequestBody OrgMo orgMo,
                                   @AccessToken AccessInfo info){
        orgManager.saveOrg(orgMo,info.getUser());
        return ResponseUtil.setSuccessResult();
    }

    @ApiOperation(value = "获取机构列表")
    @GetMapping("/org/list")
    public ResponseResult<List<OrgVo>> getOrgList(@ApiParam("市区id null:获取所有的机构信息")@RequestParam(value = "city",required = false) Integer cityId){
        List<OrgVo> list = new ArrayList<>();
        orgManager.getOrgList(cityId).forEach((obj)->{

            OrgVo orgMo = new OrgVo();
            BeanUtils.copyProperties(obj,orgMo);
            list.add(orgMo);
        });
        return ResponseUtil.setSuccessResult(list);
    }

    @ApiOperation(value = "获取最近的机构列表")
    @GetMapping("/org/recent")
    public ResponseResult<List<OrgVo>> getRecentOrgList(@ApiParam("经度") @RequestParam(value = "lng") Double lng,
                                                        @ApiParam("纬度") @RequestParam(value = "lat") Double lat,
                                                        @ApiParam("医院名称") @RequestParam(value="orgName",required = false) String orgName,
                                                        @ApiParam("直线距离 默认100Km") @RequestParam(value = "range",defaultValue = "100") Double range){
        List<OrgVo> list = new ArrayList<>();
        DecimalFormat format =  new DecimalFormat("#.00");
        orgManager.getOrgList(lng,lat,range,orgName).forEach((obj)->{

            OrgVo vo = new OrgVo();
            BeanUtils.copyProperties(obj,vo);
            Double distance = LngLatUtil.Distance(lng,lat,Double.parseDouble(obj.getLng()),Double.parseDouble(obj.getLat()));
            if(distance <= range) {
                vo.setApplyNum(applyManager.getApplyNum(vo.getOrgCode()));
                vo.setDistanceUn(distance);
                vo.setDistance(format.format(distance));
                list.add(vo);
            }
        });
        Collections.sort(list, new Comparator<OrgVo>() {
            @Override
            public int compare(OrgVo t1, OrgVo t2) {
                Double offset = t1.getDistanceUn() - t2.getDistanceUn();
                return offset<0?-1:offset==0?0:1;
            }
        });

        return ResponseUtil.setSuccessResult(list);
    }


    @ApiOperation(value = "获取机构详情")
    @ApiImplicitParam(paramType = "header", dataType = "String", name = CommonConstants.USER_HEADER_TOKEN, value = "token", required = true)
    @GetMapping("/org/detail")
    public ResponseResult<OrgVo> getOrgDetail(@ApiParam("机构主键 null:获取当前登录人的机构信息") @RequestParam(value = "orgCode",required = false) Integer orgCode,
                                              @AccessToken AccessInfo info){
        OrgVo mo = new OrgVo();
        if(null == orgCode){
            orgCode = info.getUser().getOrgCode();
        }
        BeanUtils.copyProperties(orgManager.getOrgDetail(orgCode),mo);
        mo.setApplyNum(applyManager.getApplyNum(mo.getOrgCode()));
        return ResponseUtil.setSuccessResult(mo);
    }

    @ApiOperation(value = "删除机构信息")
    @ApiImplicitParam(paramType = "header", dataType = "String", name = CommonConstants.USER_HEADER_TOKEN, value = "token", required = true)
    @DeleteMapping("/org/del")
    public ResponseResult delRole(@ApiParam("机构主键") @RequestParam("orgCode") Integer orgCode,
                                  @AccessToken AccessInfo info){
        orgManager.delOrg(orgCode,info.getUser());
        return ResponseUtil.setSuccessResult();
    }


    @ApiOperation(value = "新增/编辑 科室信息")
    @ApiImplicitParam(paramType = "header", dataType = "String", name = CommonConstants.USER_HEADER_TOKEN, value = "token", required = true)
    @PostMapping("/dept/save")
    public ResponseResult saveDept(@ApiParam("id等于null:新增; id不等于null：编辑") @RequestBody DeptMo deptMo,
                                   @AccessToken AccessInfo info){
        deptManager.saveDept(deptMo,info.getUser());
        return ResponseUtil.setSuccessResult();
    }

    @ApiOperation(value = "获取科室列表")
    @GetMapping("/dept/list")
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
    @GetMapping("/dept/detail")
    public ResponseResult<DeptMo> getDeptDetail(@ApiParam("科室主键") @RequestParam("id") Integer id){
        DeptMo deptMo = new DeptMo();
        BeanUtils.copyProperties(deptManager.getDeptDetail(id),deptMo);
        return ResponseUtil.setSuccessResult(deptMo);
    }

    @ApiOperation(value = "删除机构信息")
    @ApiImplicitParam(paramType = "header", dataType = "String", name = CommonConstants.USER_HEADER_TOKEN, value = "token", required = true)
    @DeleteMapping("/dept/del")
    public ResponseResult delDept(@ApiParam("科室主键") @RequestParam("id") Integer id,
                                  @AccessToken AccessInfo info){
        deptManager.delDept(id,info.getUser());
        return ResponseUtil.setSuccessResult();
    }



}
