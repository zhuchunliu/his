package com.acmed.his.api;

import com.acmed.his.constants.StatusCode;
import com.acmed.his.model.PatientItem;
import com.acmed.his.model.dto.PatientItemDto;
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
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

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
    public ResponseResult<PageResult<PatientItemDto>> getPatientPoolByPage(@AccessToken AccessInfo info, @RequestBody PageBase<PatientItemMo> pageBase){
        Integer orgCode = pageBase.getParam().getOrgCode();
        if (orgCode == null){
            pageBase.getParam().setOrgCode(info.getUser().getOrgCode());
        }
        PageResult<PatientItemDto> patientBlacklistByPage = patientItemManager.getPatientBlacklistByPage(pageBase);
        List<PatientItemDto> data = patientBlacklistByPage.getData();
        if (data.size()!=0){
            List<PatientItemDto> list = new ArrayList<>();
            for (PatientItemDto item :data){
                String idCard = item.getIdCard();
                if (StringUtils.isNotEmpty(idCard)){
                    if (StringUtils.equals("00",idCard.substring(0,2))){
                        item.setIdCard(null);
                    }
                }
                list.add(item);
            }
            patientBlacklistByPage.setData(list);
        }
        return ResponseUtil.setSuccessResult(patientBlacklistByPage);
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

    @ApiOperation(value = "根据id 查询患者库库内患者详情   医生使用")
    @GetMapping("id")
    public ResponseResult<PatientItemDto> getPatientItemDtoById(@AccessToken AccessInfo info, @ApiParam("患者库id") @RequestParam(value = "id" )String id){
        PatientItemDto patientItemDtoById = patientItemManager.getPatientItemDtoById(id);
        String idCard = patientItemDtoById.getIdCard();
        if (StringUtils.isNotEmpty(idCard)){
            if (StringUtils.equals("00",idCard.substring(0,2))){
                patientItemDtoById.setIdCard(null);
            }
        }
        return ResponseUtil.setSuccessResult(patientItemDtoById);
    }
}
