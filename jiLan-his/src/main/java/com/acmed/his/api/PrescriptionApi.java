package com.acmed.his.api;

import com.acmed.his.constants.StatusCode;
import com.acmed.his.pojo.mo.PreMo;
import com.acmed.his.pojo.vo.PreTitleVo;
import com.acmed.his.pojo.vo.PreVo;
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
        if(null == mo.getPatient() || StringUtils.isEmpty(mo.getPatient().getIdCard())){
            return ResponseUtil.setParamEmptyError("患者身份证号不能为空!");
        }
        boolean flag = preManager.savePre(mo,info.getUser());
        return flag?ResponseUtil.setSuccessResult():ResponseUtil.setErrorMeg(StatusCode.FAIL,"新增处方失败");
    }

    @ApiOperation(value = "获取处方")
    @GetMapping
    public ResponseResult<PreVo> getPre(@ApiParam("id 主键") @RequestParam("id") String id){
        return ResponseUtil.setSuccessResult(preManager.getPre(id));
    }




    @ApiOperation(value = "根据挂号id获取处方列表")
    @PostMapping("/list")
    public ResponseResult<PreTitleVo> getPreByApply(@ApiParam("挂号主键") @RequestParam("applyId") String applyId){
        List<PreTitleVo> list = new ArrayList<>();
        preManager.getPreByApply(applyId).forEach(obj->list.add(new PreTitleVo(obj)));
        return ResponseUtil.setSuccessResult(list);
    }





}
