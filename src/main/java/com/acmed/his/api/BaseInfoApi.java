package com.acmed.his.api;

import com.acmed.his.model.Area;
import com.acmed.his.model.DicItem;
import com.acmed.his.model.DicType;
import com.acmed.his.pojo.vo.DicDetailVo;
import com.acmed.his.service.BaseInfoManager;
import com.acmed.his.util.ResponseResult;
import com.acmed.his.util.ResponseUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * BaseInfoApi
 *
 * @author jimson
 * @date 2017/11/21
 */
@Api(tags = "基础信息")
@RestController
public class BaseInfoApi {
    @Autowired
    private BaseInfoManager baseInfoManager;

    @ApiOperation(value = "根据等级查询行政区划列表")
    @GetMapping("area/level")
    public ResponseResult<List<Area>> getAreasByLevel(@ApiParam("等级") @RequestParam("level") Integer level){
        List<Area> areasByLevel = baseInfoManager.getAreasByLevel(level);
        return ResponseUtil.setSuccessResult(areasByLevel);
    }

    @ApiOperation(value = "根据首字母查询行政区划列表")
    @GetMapping("area/first")
    public ResponseResult<List<Area>> getAreasByFirst(@ApiParam("首字母大写") @RequestParam("first") String first){
        List<Area> areasByFirst = baseInfoManager.getAreasByFirst(first);
        return ResponseUtil.setSuccessResult(areasByFirst);
    }

    @ApiOperation(value = "根据首字母查询行政区划列表")
    @GetMapping("area/pid")
    public ResponseResult<List<Area>> getAreasByid(@ApiParam("上级id") @RequestParam("pid") Integer pid){
        List<Area> areasByPid = baseInfoManager.getAreasByPid(pid);
        return ResponseUtil.setSuccessResult(areasByPid);
    }

    @ApiOperation(value = "根据id查询行政区划")
    @GetMapping("area/id")
    public ResponseResult<Area> getAreasByFirst(@ApiParam("id") @RequestParam("id") Integer id){
        Area areaById = baseInfoManager.getAreaById(id);
        return ResponseUtil.setSuccessResult(areaById);
    }

    @ApiOperation(value = "根据拼音查询行政区划")
    @GetMapping("area/pinYin")
    public ResponseResult<List<Area>> getAreasByFirstByPinYin(@ApiParam("pinYin") @RequestParam("pinYin") String pinYin){
        List<Area> areasByPinYin = baseInfoManager.getAreasByPinYin(pinYin);
        return ResponseUtil.setSuccessResult(areasByPinYin);
    }

    @ApiOperation(value = "添加字典类型")
    @PostMapping("dicType/save")
    public ResponseResult addDicType(@ApiParam("dicTypeCode == null  新增  不然修改") @RequestBody DicType dicType){
        baseInfoManager.saveDicType(dicType);
        return ResponseUtil.setSuccessResult();
    }

    @ApiOperation(value = "所有字典类型")
    @GetMapping("/dicType/list")
    public ResponseResult<List<DicType>> getDicTypeList(){
        List<DicType> allDicTypes = baseInfoManager.getAllDicTypes();
        return ResponseUtil.setSuccessResult(allDicTypes);
    }

    @ApiOperation(value = "添加字典")
    @PostMapping("/dicItem/add")
    public ResponseResult addDicItem(@RequestBody DicItem dicItem){
        baseInfoManager.addDicItem(dicItem);
        return ResponseUtil.setSuccessResult();
    }
    @ApiOperation(value = "查询所有字典")
    @GetMapping("/dicItem/list")
    public ResponseResult<List<DicDetailVo>> allDicItem(){
        List<DicDetailVo> allDicItems = baseInfoManager.getAllDicItems();
        return ResponseUtil.setSuccessResult(allDicItems);
    }

    @ApiOperation(value = "根据字典类型查找字典列表")
    @GetMapping("/dicItem/dicTypeCode")
    public ResponseResult<List<DicItem>> getDicItemsByDicTypeCode(@ApiParam("dicTypeCode == null  新增  不然修改")@RequestParam String dicTypeCode){
        List<DicItem> dicItemsByDicTypeCode = baseInfoManager.getDicItemsByDicTypeCode(dicTypeCode);
        return ResponseUtil.setSuccessResult(dicItemsByDicTypeCode);
    }
}
