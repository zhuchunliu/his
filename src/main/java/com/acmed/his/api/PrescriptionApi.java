package com.acmed.his.api;

import com.acmed.his.pojo.mo.PreInspectMo;
import com.acmed.his.pojo.mo.PreMedicineMo;
import com.acmed.his.pojo.vo.PreInspectVo;
import com.acmed.his.pojo.vo.PreMedicineVo;
import com.acmed.his.pojo.vo.PreTitleVo;
import com.acmed.his.service.PrescriptionManager;
import com.acmed.his.util.ResponseResult;
import com.acmed.his.util.ResponseUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Darren on 2017-11-22
 **/
@RestController
@RequestMapping("/pre")
@Api("处方")
public class PrescriptionApi {

    @Autowired
    private PrescriptionManager preManager;

    @ApiOperation(value = "保存药品处方")
    @PostMapping("/medicine")
    public ResponseResult savePreMedicine(@ApiParam("id等于null:新增; id不等于null：编辑") @RequestBody PreMedicineMo preMedicineMo){
        preManager.savePreMedicine(preMedicineMo);
        return ResponseUtil.setSuccessResult();
    }

    @ApiOperation(value = "新增检查处方")
    @PostMapping("/inspect")
    public ResponseResult savePreInspect(@ApiParam("id等于null:新增; id不等于null：编辑") @RequestBody PreInspectMo preInspectMo){
        preManager.savePreInspect(preInspectMo);
        return ResponseUtil.setSuccessResult();
    }

    @ApiOperation(value = "根据挂号id获取处方列表")
    @PostMapping("/list")
    public ResponseResult<PreTitleVo> getPreByApply(@ApiParam("挂号主键") @RequestParam("applyId") Integer applyId){
        List<PreTitleVo> list = new ArrayList<>();
        preManager.getPreByApply(applyId).forEach((obj)->list.add(new PreTitleVo(obj)));
        return ResponseUtil.setSuccessResult(list);
    }

    @ApiOperation(value = "保存药品处方")
    @GetMapping("/medicine")
    public ResponseResult<PreMedicineVo> getPreMedicine(@ApiParam("id 主键") @RequestParam("id") Integer id){
        return ResponseUtil.setSuccessResult(preManager.getPreMedicine(id));
    }

    @ApiOperation(value = "新增检查处方")
    @GetMapping("/inspect")
    public ResponseResult<PreInspectVo> getPreInspect(@ApiParam("id 主键") @RequestParam("id") Integer id){
        return ResponseUtil.setSuccessResult(preManager.getPreInspect(id));
    }


}
