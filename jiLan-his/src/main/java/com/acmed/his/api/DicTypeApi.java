package com.acmed.his.api;

import com.acmed.his.model.DicType;
import com.acmed.his.service.BaseInfoManager;
import com.acmed.his.util.ResponseResult;
import com.acmed.his.util.ResponseUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

/**
 * DicTypeApi
 *
 * @author jimson
 * @date 2018/1/25
 */
@Api(tags = "字典分类管理")
@RequestMapping("dicType")
public class DicTypeApi {

    @Autowired
    private BaseInfoManager baseInfoManager;

    @ApiOperation(value = "添加字典类型",hidden = true)
    @PostMapping("/save")
    public ResponseResult addDicType(@ApiParam("dicTypeCode == null  新增  不然修改") @RequestBody DicType dicType){
        baseInfoManager.saveDicType(dicType);
        return ResponseUtil.setSuccessResult();
    }

    @ApiOperation(value = "所有字典类型")
    @GetMapping("/list")
    public ResponseResult<List<DicType>> getDicTypeList(){
        List<DicType> allDicTypes = baseInfoManager.getAllDicTypes();
        return ResponseUtil.setSuccessResult(allDicTypes);
    }
}
