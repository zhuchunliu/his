package com.acmed.his.api;

import com.acmed.his.model.Drug;
import com.acmed.his.service.DrugManager;
import com.acmed.his.support.AccessInfo;
import com.acmed.his.support.AccessToken;
import com.acmed.his.util.ResponseResult;
import com.acmed.his.util.ResponseUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
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
    public ResponseResult saveDrug(@RequestBody Drug drug,@AccessToken AccessInfo info){
        drug.setCreateBy(info.getUserId().toString());
        drugManager.saveDrug(drug);
        return ResponseUtil.setSuccessResult();
    }

    @ApiOperation(value = "条件查询")
    @GetMapping("bydrug")
    public ResponseResult<List<Drug>> selectDrugsByDrug(Drug drug){
        List<Drug> drugsByDrug = drugManager.getDrugsByDrug(drug);
        return ResponseUtil.setSuccessResult(drugsByDrug);
    }

    @ApiOperation(value = "拼音模糊查询")
    @GetMapping("pinyin")
    public ResponseResult<List<Drug>> selectDrugsByDrug(@ApiParam("拼音模糊查询")String pinYin){
        List<Drug> drugsByDrug = drugManager.getDrugsByPinYinLike(pinYin);
        return ResponseUtil.setSuccessResult(drugsByDrug);
    }
}
