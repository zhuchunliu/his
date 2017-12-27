package com.acmed.his.api;

import com.acmed.his.constants.CommonConstants;
import com.acmed.his.model.Org;
import com.acmed.his.pojo.mo.OrgMo;
import com.acmed.his.pojo.vo.OrgVo;
import com.acmed.his.service.ApplyManager;
import com.acmed.his.service.OrgManager;
import com.acmed.his.support.AccessInfo;
import com.acmed.his.support.AccessToken;
import com.acmed.his.support.WithoutToken;
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
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by Darren on 2017-12-25
 **/
@RestController
@Api(tags = "机构管理")
@RequestMapping("/org")
public class OrgApi {

    @Autowired
    private OrgManager orgManager;

    @Autowired
    private ApplyManager applyManager;

    @ApiOperation(value = "新增/编辑 机构信息")
    @ApiImplicitParam(paramType = "header", dataType = "String", name = CommonConstants.USER_HEADER_TOKEN, value = "token", required = true)
    @PostMapping("/save")
    public ResponseResult saveOrg(@ApiParam("orgCode等于null:新增; orgCode不等于null：编辑") @RequestBody OrgMo orgMo,
                                  @AccessToken AccessInfo info){
        orgManager.saveOrg(orgMo,info.getUser());
        return ResponseUtil.setSuccessResult();
    }

    @ApiOperation(value = "获取就医北上广机构列表")
    @GetMapping("/getBSGList")
    public ResponseResult<List<OrgVo>> getBSGList(@ApiParam("市区id null:获取所有的机构信息")@RequestParam(value = "city",required = true) Integer cityId){
        List<OrgVo> list = new ArrayList<>();
        Org org = new Org();
        org.setIsRecommend("1");
        org.setCity(cityId+"");
        orgManager.getList(org).forEach((obj)->{

            OrgVo orgMo = new OrgVo();
            BeanUtils.copyProperties(obj,orgMo);
            list.add(orgMo);
        });
        return ResponseUtil.setSuccessResult(list);
    }

    @ApiOperation(value = "获取机构列表")
    @GetMapping("/list")
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
    @GetMapping("/recent")
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
    @GetMapping("/detail")
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
    @DeleteMapping("/del")
    public ResponseResult delRole(@ApiParam("机构主键") @RequestParam("orgCode") Integer orgCode,
                                  @AccessToken AccessInfo info){
        orgManager.delOrg(orgCode,info.getUser());
        return ResponseUtil.setSuccessResult();
    }
}
