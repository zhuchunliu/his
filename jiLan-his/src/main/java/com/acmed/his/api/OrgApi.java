package com.acmed.his.api;

import com.acmed.his.consts.DicTypeEnum;
import com.acmed.his.model.*;
import com.acmed.his.model.dto.OrgDto;
import com.acmed.his.pojo.mo.BsgOrgMo;
import com.acmed.his.pojo.mo.OrgMo;
import com.acmed.his.pojo.vo.OrgPatientVo;
import com.acmed.his.pojo.vo.OrgVo;
import com.acmed.his.service.*;
import com.acmed.his.support.AccessInfo;
import com.acmed.his.support.AccessToken;
import com.acmed.his.util.*;
import com.alibaba.fastjson.JSONObject;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang3.StringUtils;
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

    @Autowired
    private DeptManager deptManager;

    @Autowired
    private PatientItemManager patientItemManager;

    @Autowired
    private UserManager userManager;

    @Autowired
    private BaseInfoManager baseInfoManager;


    @ApiOperation(value = "新增/编辑 机构信息")
    @PostMapping("/save")
    public ResponseResult saveOrg(@ApiParam("orgCode等于null:新增; orgCode不等于null：编辑") @RequestBody OrgMo orgMo,
                                  @AccessToken AccessInfo info){
        orgManager.saveOrg(orgMo,info.getUser());
        return ResponseUtil.setSuccessResult();
    }

    @ApiOperation(value = "获取就医北上广机构列表")
    @GetMapping("/getBSGList")
    public ResponseResult<List<OrgVo>> getBSGList(@ApiParam("市区id null:获取所有的机构信息")@RequestParam(value = "city",required = false) Integer cityId,
                                                  @ApiParam("医院名称") @RequestParam(value="orgName",required = false) String orgName,
                                                  @ApiParam("级别") @RequestParam(value="level",required = false)String level){
        List<OrgVo> list = new ArrayList<>();
        Org org = new Org();
        org.setIsRecommend("1");
        if (cityId!=null){
            org.setCity(cityId.toString());
        }
        if (StringUtils.isNotEmpty(orgName)){
            org.setOrgName(orgName);
        }
        if (StringUtils.isNotEmpty(level)){
            org.setLevel(level);
        }
        List<DicItem> dicItems = baseInfoManager.getDicItems(DicTypeEnum.ORG_LEVEL.getCode());
        orgManager.getList(org).forEach((obj)->{
            OrgVo orgMo = new OrgVo();
            BeanUtils.copyProperties(obj,orgMo);
            if (dicItems.size()!=0){
                for (DicItem item : dicItems){
                    if (item.getDicItemCode().equals(obj.getLevel())){
                        orgMo.setLevelStr(item.getDicItemName());
                    }
                }
            }
            list.add(orgMo);
        });
        return ResponseUtil.setSuccessResult(list);
    }

    @ApiOperation(value = "获取机构列表")
    @GetMapping("/list")
    public ResponseResult<List<OrgVo>> getOrgList(@ApiParam("市区id null:获取所有的机构信息")@RequestParam(value = "city",required = false) Integer cityId,
                                                  @ApiParam("医院名称") @RequestParam(value="orgName",required = false) String orgName,
                                                  @ApiParam("是否就医北上广医院  不传表示全部  1推荐") @RequestParam(value="isRecommend",required = false) String isRecommend,
                                                  @ApiParam("级别") @RequestParam(value="level",required = false)String level
    ){
        List<OrgVo> list = new ArrayList<>();
        orgManager.getOrgList(cityId,orgName,level,isRecommend).forEach((obj)->{
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
                                                        @ApiParam("是否就医北上广医院  不传表示全部  1推荐") @RequestParam(value="isRecommend",required = false) String isRecommend,
                                                        @ApiParam("直线距离 默认100Km") @RequestParam(value = "range",defaultValue = "100") Double range){
        List<OrgVo> list = new ArrayList<>();
        DecimalFormat format =  new DecimalFormat("#0.00");
        orgManager.getOrgList(lng,lat,range,orgName,isRecommend).forEach((obj)->{

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

    @ApiOperation(value = "禁用/启用 诊断模板")
    @PostMapping("/switch")
    public ResponseResult switchOrg(@ApiParam("{\"id\":\"\"} id：机构主键") @RequestBody String param,
                                             @AccessToken AccessInfo info){
        if(org.apache.commons.lang3.StringUtils.isEmpty(param) || null == JSONObject.parseObject(param).get("id")){
            return ResponseUtil.setParamEmptyError("id");
        }
        orgManager.switchOrg(JSONObject.parseObject(param).getInteger("id"),info.getUser());
        return ResponseUtil.setSuccessResult();
    }


    @ApiOperation(value = "获取机构详情")
    @GetMapping("/detail")
    public ResponseResult<OrgVo> getOrgDetail(@ApiParam("机构主键 null:获取当前登录人的机构信息") @RequestParam(value = "orgCode",required = false) Integer orgCode,
                                              @ApiParam(value = "是否优势科室  不填显示全部科室  0表示显示非优势科室  1显示优势科室")@RequestParam(value = "superiorityFlag",required = false) Integer superiorityFlag,
                                              @AccessToken AccessInfo info){
        OrgVo mo = new OrgVo();
        if(null == orgCode){
            orgCode = info.getUser().getOrgCode();
        }
        BeanUtils.copyProperties(orgManager.getOrgDetail(orgCode),mo);
        mo.setApplyNum(applyManager.getApplyNum(mo.getOrgCode()));
        Dept dept = new Dept();
        dept.setRemoved("0");
        dept.setOrgCode(orgCode);
        dept.setSuperiorityFlag(superiorityFlag);
        mo.setDeptList(deptManager.getDeptList(dept));
        if(StringUtils.isNotEmpty(mo.getManager())){
            User user = userManager.getUserDetail(Integer.parseInt(mo.getManager()));
            if(null != user){
                mo.setManager(user.getUserName());
            }
        }
        return ResponseUtil.setSuccessResult(mo);
    }

    @ApiOperation(value = "删除机构信息")
    @DeleteMapping("/del")
    public ResponseResult delRole(@ApiParam("机构主键") @RequestParam("orgCode") Integer orgCode,
                                  @AccessToken AccessInfo info){
        orgManager.delOrg(orgCode,info.getUser());
        return ResponseUtil.setSuccessResult();
    }

    @ApiOperation(value = "患者就诊过的机构列表")
    @GetMapping("/orgPatientList")
    public ResponseResult<List<OrgPatientVo>> delRole(@AccessToken AccessInfo info){
        String patientId = info.getPatientId();
        PatientItem patientItem = new PatientItem();
        patientItem.setPatientId(patientId);
        List<PatientItem> patientItems = patientItemManager.patientItems(patientItem);
        List<Integer> orgCodes = new ArrayList<>();
        for (PatientItem item : patientItems){
            orgCodes.add(item.getOrgCode());
        }
        List<OrgPatientVo> orgPatientVoList = new ArrayList<>();
        if (orgCodes.size() != 0){
            List<Org> orgList = orgManager.getOrgsByIdList(orgCodes);
            for (Org org: orgList){
                OrgPatientVo orgPatientVo = new OrgPatientVo();
                orgPatientVo.setApplyNum(applyManager.getApplyNum(org.getOrgCode()));
                orgPatientVo.setImgUrl(org.getImgUrl());
                orgPatientVo.setOrgCode(org.getOrgCode());
                orgPatientVo.setIntroduction(org.getIntroduction());
                orgPatientVo.setOrgName(org.getOrgName());
                orgPatientVoList.add(orgPatientVo);
            }
        }
        return ResponseUtil.setSuccessResult(orgPatientVoList);
    }


    @ApiOperation(value = "获取机构列表分頁(不包括就医北上广的医院)  param 可以是机构名称")
    @PostMapping("/listbyPage")
    public ResponseResult<PageResult<OrgDto>> getOrgList(@RequestBody PageBase<String> pageBase){
        PageResult<OrgDto> orgDtoByPage = orgManager.getOrgDtoByPage(pageBase,"0");
        return ResponseUtil.setSuccessResult(orgDtoByPage);
    }


    @ApiOperation(value = "就医北上广 新增/编辑 机构信息")
    @PostMapping("/bsgsave")
    public ResponseResult bsgSaveOrg(@ApiParam("orgCode等于null:新增; orgCode不等于null：编辑") @RequestBody BsgOrgMo orgMo,
                                     @AccessToken AccessInfo info){
        orgManager.saveBsgOrg(orgMo,info.getUser());
        return ResponseUtil.setSuccessResult();
    }


    @ApiOperation(value = "pc端北上广医院列表")
    @GetMapping("/bsglistpagepc")
    public ResponseResult<PageResult<OrgDto>> bsglistpagepc(
            @ApiParam("页码") @RequestParam("pageNum") Integer pageNum,
            @ApiParam("记录数") @RequestParam("pageSize") Integer pageSize,
            @ApiParam("机构名") @RequestParam(value = "orgName",required = false) String orgName,
            @ApiParam("城市id") @RequestParam(value = "cityId",required = false) String cityId){
        PageResult<OrgDto> orgDtoByPage = orgManager.getOrgDtoByPage(pageNum,pageSize,orgName,cityId,"1");
        return ResponseUtil.setSuccessResult(orgDtoByPage);
    }
}
