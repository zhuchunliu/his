package com.acmed.his.api;

import com.acmed.his.model.DicItem;
import com.acmed.his.model.DicType;
import com.acmed.his.service.BaseInfoManager;
import com.acmed.his.util.ResponseResult;
import com.acmed.his.util.ResponseUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * BaseInfoApi
 *
 * @author jimson
 * @date 2017/11/21
 */
@Api("基础信息")
@RestController
public class BaseInfoApi {
    @Autowired
    private BaseInfoManager baseInfoManager;

    @ApiOperation(value = "根据等级查询行政区划列表")
    @GetMapping("area/level")
    public ResponseResult getAreasByLevel(@ApiParam("等级") @RequestParam("level") Integer level){
        return ResponseUtil.setSuccessResult(baseInfoManager.getAreasByLevel(level));
    }

    @ApiOperation(value = "根据首字母查询行政区划列表")
    @GetMapping("area/irst")
    public ResponseResult getAreasByFirst(@ApiParam("首字母大写") @RequestParam("first") String first){
        return ResponseUtil.setSuccessResult(baseInfoManager.getAreasByFirst(first));
    }

    @ApiOperation(value = "根据首字母查询行政区划列表")
    @GetMapping("area/pid")
    public ResponseResult getAreasByid(@ApiParam("上级id") @RequestParam("pid") Integer pid){
        return ResponseUtil.setSuccessResult(baseInfoManager.getAreasByPid(pid));
    }

    @ApiOperation(value = "根据id查询行政区划")
    @GetMapping("area/id")
    public ResponseResult getAreasByFirst(@ApiParam("id") @RequestParam("id") Integer id){
        return ResponseUtil.setSuccessResult(baseInfoManager.getAreaById(id));
    }

    @ApiOperation(value = "根据拼音查询行政区划")
    @GetMapping("area/pinYin")
    public ResponseResult getAreasByFirstByPinYin(@ApiParam("pinYin") @RequestParam("pinYin") String pinYin){
        return ResponseUtil.setSuccessResult(baseInfoManager.getAreasByPinYin(pinYin));
    }

    @ApiOperation(value = "添加字典类型")
    @PostMapping("dicType/save")
    public ResponseResult addDicType(@ApiParam("dicTypeCode == null  新增  不然修改") @RequestBody DicType dicType){
        return ResponseUtil.setSuccessResult(baseInfoManager.saveDicType(dicType));
    }

    @ApiOperation(value = "所有字典类型")
    @GetMapping("/dicType/list")
    public ResponseResult getDicTypeList(){
        return ResponseUtil.setSuccessResult(baseInfoManager.getAllDicTypes());
    }

    @ApiOperation(value = "添加字典")
    @PostMapping("/dicItem/add")
    public ResponseResult addDicItem(@RequestBody DicItem dicItem){
        return ResponseUtil.setSuccessResult(baseInfoManager.addDicItem(dicItem));
    }
    @ApiOperation(value = "查询所有字典")
    @GetMapping("/dicItem/list")
    public ResponseResult allDicItem(){
        return ResponseUtil.setSuccessResult(baseInfoManager.getAllDicItems());
    }

    @ApiOperation(value = "根据字典类型查找字典列表")
    @GetMapping("/dicItem/dicTypeCode")
    public ResponseResult getDicItemsByDicTypeCode(@ApiParam("dicTypeCode == null  新增  不然修改") String dicTypeCode){
        return ResponseUtil.setSuccessResult(baseInfoManager.getDicItemsByDicTypeCode(dicTypeCode));
    }
}
