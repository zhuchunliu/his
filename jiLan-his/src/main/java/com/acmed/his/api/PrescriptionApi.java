package com.acmed.his.api;

import com.acmed.his.constants.StatusCode;
import com.acmed.his.consts.DicTypeEnum;
import com.acmed.his.pojo.mo.PreMo;
import com.acmed.his.pojo.vo.PreDrugVo;
import com.acmed.his.pojo.vo.PreTitleVo;
import com.acmed.his.pojo.vo.PreVo;
import com.acmed.his.service.FeeItemManager;
import com.acmed.his.service.PrescriptionManager;
import com.acmed.his.support.AccessInfo;
import com.acmed.his.support.AccessToken;
import com.acmed.his.util.ResponseResult;
import com.acmed.his.util.ResponseUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Created by Darren on 2017-11-22
 **/
@RestController
@RequestMapping("/pre")
@Api(tags = "新开就诊")
public class PrescriptionApi {

    @Autowired
    private PrescriptionManager preManager;

    @ApiOperation(value = "保存处方")
    @PostMapping
    public ResponseResult savePre(@ApiParam("id等于null:新增; id不等于null：编辑") @RequestBody PreMo mo,
                                  @AccessToken AccessInfo info){

        if(StringUtils.isEmpty(mo.getApplyId())) {//挂号单为null验证用户信息

            if (null == mo.getPatient() || StringUtils.isEmpty(mo.getPatient().getRealName())) {
                return ResponseUtil.setParamEmptyError("患者姓名不能为空!");
            }

            if (StringUtils.isNotEmpty(mo.getPatient().getIdCard()) && mo.getPatient().getIdCard().length() == 8){
                // 表示传的是生日
                String orgCode = "0000000"+info.getUser().getOrgCode();
                long l = System.currentTimeMillis();
                String s = l + "";
                mo.getPatient().setIdCard(orgCode.substring(orgCode.length()-6)+mo.getPatient().getIdCard()+s.substring(s.length()-4));
            }
        }

        boolean flag = preManager.savePre(mo,info.getUser());
        return flag?ResponseUtil.setSuccessResult():ResponseUtil.setErrorMeg(StatusCode.FAIL,"新增处方失败");
    }

    @ApiOperation(value = "获取处方")
    @GetMapping
    public ResponseResult<PreVo> getPre(@ApiParam("挂号单主键") @RequestParam("applyId") String applyId){
        return ResponseUtil.setSuccessResult(preManager.getPre(applyId));
    }


    @ApiOperation(value = "获取药品处方")
    @GetMapping("/drug")
    public ResponseResult<PreDrugVo> getPreDrug(@ApiParam("挂号单主键") @RequestParam("applyId") String applyId){
        return ResponseUtil.setSuccessResult(preManager.getPreDrug(applyId));
    }




    @ApiOperation(value = "根据挂号id获取处方列表",hidden = true)
    @PostMapping("/list")
    public ResponseResult<PreTitleVo> getPreByApply(@ApiParam("挂号主键") @RequestParam("applyId") String applyId){
        List<PreTitleVo> list = new ArrayList<>();
        preManager.getPreByApply(applyId).forEach(obj->list.add(new PreTitleVo(obj)));
        return ResponseUtil.setSuccessResult(list);
    }





}
