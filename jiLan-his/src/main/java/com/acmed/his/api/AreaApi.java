package com.acmed.his.api;

import com.acmed.his.model.Area;
import com.acmed.his.service.BaseInfoManager;
import com.acmed.his.util.ResponseResult;
import com.acmed.his.util.ResponseUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * AreaApi
 *
 * @author jimson
 * @date 2018/1/25
 */
@RestController
@Api(tags = "地区")
@RequestMapping("/area")
public class AreaApi {

    @Autowired
    private BaseInfoManager baseInfoManager;

    @ApiOperation(value = "根据等级查询行政区划列表")
    @GetMapping("/level")
    public ResponseResult<List<Area>> getAreasByLevel(@ApiParam("等级 1省  2 市  3区") @RequestParam("level") Integer level){
        List<Area> areasByLevel = baseInfoManager.getAreasByLevel(level);
        return ResponseUtil.setSuccessResult(areasByLevel);
    }

    @ApiOperation(value = "根据首字母查询行政区划列表")
    @GetMapping("/first")
    public ResponseResult<List<Area>> getAreasByFirst(@ApiParam("首字母大写") @RequestParam("first") String first){
        List<Area> areasByFirst = baseInfoManager.getAreasByFirst(first);
        return ResponseUtil.setSuccessResult(areasByFirst);
    }

    @ApiOperation(value = "根据首字母查询行政区划列表")
    @GetMapping("/pid")
    public ResponseResult<List<Area>> getAreasByid(@ApiParam("上级id") @RequestParam("pid") Integer pid){
        List<Area> areasByPid = baseInfoManager.getAreasByPid(pid);
        return ResponseUtil.setSuccessResult(areasByPid);
    }

    @ApiOperation(value = "根据id查询行政区划")
    @GetMapping("/id")
    public ResponseResult<Area> getAreasByFirst(@ApiParam("id") @RequestParam("id") Integer id){
        Area areaById = baseInfoManager.getAreaById(id);
        return ResponseUtil.setSuccessResult(areaById);
    }

    @ApiOperation(value = "根据拼音查询行政区划")
    @GetMapping("/pinYin")
    public ResponseResult<List<Area>> getAreasByFirstByPinYin(@ApiParam("pinYin") @RequestParam("pinYin") String pinYin){
        List<Area> areasByPinYin = baseInfoManager.getAreasByPinYin(pinYin);
        return ResponseUtil.setSuccessResult(areasByPinYin);
    }


    @ApiOperation(value = "北上广城市列表")
    @GetMapping("/bsg")
    public ResponseResult<List<Area>> bsg(){
        return ResponseUtil.setSuccessResult(baseInfoManager.bsgcitys());
    }
}
