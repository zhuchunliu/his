package com.acmed.his.api;

import com.acmed.his.model.Drug;
import com.acmed.his.service.DrugManager;
import com.acmed.his.util.ResponseResult;
import com.acmed.his.util.ResponseUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * DrugApi
 *
 * @author jimson
 * @date 2017/11/23
 */
@Api("药品管理")
@RestController
public class DrugApi {
    @Autowired
    private DrugManager drugManager;

    @ApiOperation(value = "添加")
    @PostMapping("/drug/save")
    public ResponseResult saveDrug(@RequestBody Drug drug){
        drugManager.saveDrug(drug);
        return ResponseUtil.setSuccessResult();
    }

    @ApiOperation(value = "条件查询")
    @GetMapping("/drugs/drug")
    public ResponseResult selectDrugsByDrug(Drug drug){
        return ResponseUtil.setSuccessResult(drugManager.getDrugsByDrug(drug));
    }
}
