package com.acmed.his.api;

import com.acmed.his.constants.StatusCode;
import com.acmed.his.model.PatientItem;
import com.acmed.his.pojo.mo.PatientItemMo;
import com.acmed.his.pojo.mo.PatientItemUpMo;
import com.acmed.his.service.PatientItemManager;
import com.acmed.his.support.AccessInfo;
import com.acmed.his.support.AccessToken;
import com.acmed.his.util.PageBase;
import com.acmed.his.util.PageResult;
import com.acmed.his.util.ResponseResult;
import com.acmed.his.util.ResponseUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * PatientItemApi
 *
 * @author jimson
 * @date 2018/1/23
 */
@Api(tags = "医院患者")
@RestController
@RequestMapping("patientItem")
public class PatientItemApi {
    @Autowired
    private PatientItemManager patientItemManager;

    @ApiOperation(value = "分页查询机构患者库")
    @PostMapping("getPatientPoolByPage")
    public ResponseResult<PageResult<PatientItem>> getPatientPoolByPage(@AccessToken AccessInfo info,@RequestBody PageBase<PatientItemMo> pageBase){
        Integer orgCode = pageBase.getParam().getOrgCode();
        if (orgCode == null){
            pageBase.getParam().setOrgCode(info.getUser().getOrgCode());
        }
        return ResponseUtil.setSuccessResult(patientItemManager.getPatientBlacklistByPage(pageBase,0));
    }


    @ApiOperation(value = "添加黑名单")
    @GetMapping("addPatientBlacklist")
    public ResponseResult addPatientBlacklist(@AccessToken AccessInfo info,
                                              @ApiParam("患者库id") @RequestParam(value = "id" )String id){
        return ResponseUtil.setSuccessResult(patientItemManager.updatePatientItemBlackFlag(info.getUser().getOrgCode(),info.getUser().getId(),id,1));
    }

    @ApiOperation(value = "移除黑名单")
    @GetMapping("removedPatientBlacklist")
    public ResponseResult removedPatientBlacklist(@AccessToken AccessInfo info,
                                                  @ApiParam("患者库id") @RequestParam(value = "id" )String id){
        return ResponseUtil.setSuccessResult(patientItemManager.updatePatientItemBlackFlag(info.getUser().getOrgCode(),info.getUser().getId(),id,0));
    }

    @ApiOperation(value = "修改患者库信息,只支持修改本机构的")
    @PostMapping("update")
    public ResponseResult update(@AccessToken AccessInfo info, @RequestBody PatientItemUpMo patientItemUpMo){
        Integer userOrgCode = info.getUser().getOrgCode();
        PatientItem byId = patientItemManager.getById(patientItemUpMo.getId());
        Integer orgCode = byId.getOrgCode();
        if (orgCode.equals(userOrgCode)){
            PatientItem patientItem = new PatientItem();
            BeanUtils.copyProperties(patientItemUpMo,patientItem);
            patientItem.setModifyBy(info.getUserId().toString());
            patientItemManager.updatePatientItem(patientItem);
            return ResponseUtil.setSuccessResult();
        }else{
            return ResponseUtil.setErrorMeg(StatusCode.FAIL,"权限不足");
        }
    }
}
