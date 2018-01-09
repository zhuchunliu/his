package com.acmed.his.api;

import com.acmed.his.model.Drug;
import com.acmed.his.model.DrugDict;
import com.acmed.his.pojo.mo.DrugMo;
import com.acmed.his.pojo.vo.DrugDictVo;
import com.acmed.his.pojo.vo.DrugVo;
import com.acmed.his.service.DrugManager;
import com.acmed.his.support.AccessInfo;
import com.acmed.his.support.AccessToken;
import com.acmed.his.util.PageBase;
import com.acmed.his.util.PageResult;
import com.acmed.his.util.ResponseResult;
import com.acmed.his.util.ResponseUtil;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

/**
 * DrugApi
 *
 * @author jimson
 * @date 2017/11/23
 */
@Api(tags = "药品管理")
@RequestMapping("/drug")
@RestController
public class DrugApi {
    @Autowired
    private DrugManager drugManager;

    @ApiOperation(value = "药品信息列表")
    @PostMapping("/list")
    public ResponseResult<PageResult<DrugVo>> getDrugList(@RequestBody(required = false) PageBase<DrugMo> pageBase,
                                                          @AccessToken AccessInfo info){
        List<Drug> list = drugManager.getDrugList(info.getUser().getOrgCode(),
                Optional.ofNullable(pageBase.getParam()).map(DrugMo::getName).orElse(null),
                Optional.ofNullable(pageBase.getParam()).map(DrugMo::getCategory).orElse(null),
                pageBase.getPageNum(), pageBase.getPageSize());
        int total = drugManager.getDrugTotal(info.getUser().getOrgCode(),
                Optional.ofNullable(pageBase.getParam()).map(DrugMo::getName).orElse(null),
                Optional.ofNullable(pageBase.getParam()).map(DrugMo::getCategory).orElse(null));

        List<DrugVo> voList = Lists.newArrayList();
        list.forEach(obj->{
            DrugVo vo = new DrugVo();
            BeanUtils.copyProperties(obj,vo);
            voList.add(vo);
        });

        PageResult pageResult = new PageResult();
        BeanUtils.copyProperties(pageBase,pageResult);
        pageResult.setTotal((long)total);
        pageResult.setData(voList);
        return ResponseUtil.setSuccessResult(pageResult);

    }

    @ApiOperation(value = "获取药品字典表")
    @PostMapping("/dict")
    public ResponseResult<PageResult<DrugDictVo>> getDrugDictList(@RequestBody(required = false) PageBase<DrugMo> pageBase,
                                                                  @AccessToken AccessInfo info){
        List<DrugDict> list = drugManager.getDrugDictList(info.getUser().getOrgCode(),
                Optional.ofNullable(pageBase.getParam()).map(DrugMo::getName).orElse(null),
                Optional.ofNullable(pageBase.getParam()).map(DrugMo::getCategory).orElse(null),
                pageBase.getPageNum(), pageBase.getPageSize());
        int total = drugManager.getDrugDictTotal(info.getUser().getOrgCode(),
                Optional.ofNullable(pageBase.getParam()).map(DrugMo::getName).orElse(null),
                Optional.ofNullable(pageBase.getParam()).map(DrugMo::getCategory).orElse(null));

        List<DrugDictVo> voList = Lists.newArrayList();
        list.forEach(obj->{
            DrugDictVo vo = new DrugDictVo();
            BeanUtils.copyProperties(obj,vo);
            voList.add(vo);
        });

        PageResult pageResult = new PageResult();
        BeanUtils.copyProperties(pageBase,pageResult);
        pageResult.setTotal((long)total);
        pageResult.setData(voList);
        return ResponseUtil.setSuccessResult(pageResult);

    }



    @ApiOperation(value = "删除")
    @DeleteMapping("/del")
    public ResponseResult delDrug(@ApiParam("药品主键")@RequestParam(value = "id",required = true) Integer id,
                                  @AccessToken AccessInfo info){
        drugManager.delDrug(id,info.getUser());
        return ResponseUtil.setSuccessResult();
    }

    @ApiOperation(value = "添加药品信息")
    @PostMapping("/save")
    public ResponseResult saveDrug(@ApiParam("{\"codes\":\"XXX,XXX\"}  code为药品编码集合，逗号间隔") @RequestBody String param,
                                   @AccessToken AccessInfo info){

        if(StringUtils.isEmpty(param) || null == JSONObject.parseObject(param).get("codes")){
            return ResponseUtil.setParamEmptyError("codes");
        }
        drugManager.saveDrugByDict(JSONObject.parseObject(param).get("codes").toString().split(","),info.getUser());
        return ResponseUtil.setSuccessResult();
    }


    @ApiOperation(value = "更新药品信息")
    @PostMapping("/update")
    public ResponseResult saveDrug(@RequestBody Drug drug,@AccessToken AccessInfo info){
        drug.setCreateBy(info.getUserId().toString());
        drugManager.saveDrug(drug);
        return ResponseUtil.setSuccessResult();
    }


    @ApiOperation(value = "拼音模糊查询")
    @GetMapping("/pinyin")
    public ResponseResult<List<Drug>> selectDrugsByDrug(@ApiParam("拼音模糊查询") @RequestParam(value = "pinYin") String pinYin){
        List<Drug> drugsByDrug = drugManager.getDrugsByPinYinLike(pinYin);
        return ResponseUtil.setSuccessResult(drugsByDrug);
    }

    @ApiOperation(value = "根据id查询药品详情")
    @GetMapping("/id")
    public ResponseResult<Drug> selectDrugsById(@ApiParam("药品id") @RequestParam("id") Integer id){
        return ResponseUtil.setSuccessResult(drugManager.getDrugById(id));
    }
}
