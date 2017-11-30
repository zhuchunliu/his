package com.acmed.his.api;

import com.acmed.his.model.Drug;
import com.acmed.his.service.DrugManager;
import com.acmed.his.util.ResponseResult;
import com.acmed.his.util.ResponseUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * DrugApi
 *
 * @author jimson
 * @date 2017/11/23
 */
@Api(tags = "药品管理")
@RequestMapping("drug")
@RestController
public class DrugApi {
    @Autowired
    private DrugManager drugManager;

    @ApiOperation(value = "添加")
    @PostMapping("save")
    public ResponseResult saveDrug(@RequestBody Drug drug){
        drugManager.saveDrug(drug);
        return ResponseUtil.setSuccessResult();
    }

    @ApiOperation(value = "条件查询")
    @GetMapping("bydrug")
    public ResponseResult<List<Drug>> selectDrugsByDrug(Drug drug){
        List<Drug> drugsByDrug = drugManager.getDrugsByDrug(drug);
        return ResponseUtil.setSuccessResult(drugsByDrug);
    }
}
