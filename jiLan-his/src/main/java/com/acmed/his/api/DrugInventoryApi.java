package com.acmed.his.api;

import com.acmed.his.dao.DrugStockMapper;
import com.acmed.his.model.DrugStock;
import com.acmed.his.model.dto.DrugInventoryDto;
import com.acmed.his.pojo.mo.DrugInventoryMo;
import com.acmed.his.pojo.mo.DrugInventoryQueryMo;
import com.acmed.his.pojo.vo.DrugInventoryVo;
import com.acmed.his.pojo.vo.DrugStockInventoryVo;
import com.acmed.his.service.DrugInventoryManager;
import com.acmed.his.service.PermissionManager;
import com.acmed.his.support.AccessInfo;
import com.acmed.his.support.AccessToken;
import com.acmed.his.util.PageBase;
import com.acmed.his.util.PageResult;
import com.acmed.his.util.ResponseResult;
import com.acmed.his.util.ResponseUtil;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import tk.mybatis.mapper.entity.Example;

import java.util.List;
import java.util.Optional;

/**
 * Created by Darren on 2018-03-13
 **/
@Api(tags = "药品盘库")
@RequestMapping("/inventory")
@RestController
public class DrugInventoryApi {

    @Autowired
    private DrugInventoryManager drugInventoryManager;

    @Autowired
    private PermissionManager permissionManager;



    @ApiOperation(value = "保存盘库")
    @PostMapping("/save")
    public ResponseResult save(@RequestBody DrugInventoryMo mo,
                               @AccessToken AccessInfo info){

        drugInventoryManager.save(mo,info.getUser());
        return ResponseUtil.setSuccessResult();
    }

    @ApiOperation(value = "查看审核权利")
    @GetMapping("/permission")
    public ResponseResult getPermission(@AccessToken AccessInfo info){

        return ResponseUtil.setSuccessResult(ImmutableMap.of("hasPermission",
                permissionManager.hasMenu(info.getUser().getId().toString(),"pdsh")));
    }

    @ApiOperation(value = "入库审核列表")
    @PostMapping("/audit/list")
    public ResponseResult<PageResult<DrugInventoryDto>> auditList(@RequestBody(required = false) PageBase<DrugInventoryQueryMo> pageBase,
                                                                  @AccessToken AccessInfo info){
        List<DrugInventoryDto> list = drugInventoryManager.getAuditList(Optional.ofNullable(pageBase.getParam()).orElse(new DrugInventoryQueryMo()), info.getUser(),
                pageBase.getPageNum(), pageBase.getPageSize());
        Integer total = drugInventoryManager.getAuditTotal(Optional.ofNullable(pageBase.getParam()).orElse(new DrugInventoryQueryMo()), info.getUser());
        PageResult pageResult = new PageResult();
        BeanUtils.copyProperties(pageBase,pageResult);
        pageResult.setTotal((long)total);
        pageResult.setData(list);
        return ResponseUtil.setSuccessResult(pageResult);
    }

    @ApiOperation(value = "入库详情")
    @GetMapping("/detail")
    public ResponseResult<List<DrugInventoryVo>> getDrugInventoryDetail(@ApiParam("盘库主键") @RequestParam(value = "id") String id){
        return ResponseUtil.setSuccessResult(drugInventoryManager.getDrugInventoryDetail(id));
    }

    @ApiOperation(value = "审核入库")
    @PostMapping("/audit")
    public ResponseResult audit(@ApiParam("{\"id\":\"\"}") @RequestBody String param,
                                @AccessToken AccessInfo info){
        drugInventoryManager.audit(JSONObject.parseObject(param).get("id").toString(),info.getUser());
        return ResponseUtil.setSuccessResult();
    }

    @ApiOperation(value = "驳回")
    @PostMapping("/reject")
    public ResponseResult reject(@ApiParam("{\"id\":\"\"}") @RequestBody String param,
                                 @AccessToken AccessInfo info){
        drugInventoryManager.reject(JSONObject.parseObject(param).get("id").toString(),info.getUser());
        return ResponseUtil.setSuccessResult();
    }

    @ApiOperation(value = "提交审核")
    @PostMapping("/submit")
    public ResponseResult submit(@ApiParam("{\"id\":\"\"}") @RequestBody String param,
                                 @AccessToken AccessInfo info){
        drugInventoryManager.submit(JSONObject.parseObject(param).get("id").toString(),info.getUser());
        return ResponseUtil.setSuccessResult();
    }

    @ApiOperation(value = "删除入库信息")
    @DeleteMapping("/del")
    public ResponseResult deleteInfo(@ApiParam("盘库主键") @RequestParam(value = "id") String id,
                                                  @AccessToken AccessInfo info){
        drugInventoryManager.deleteInfo(id,info.getUser());
        return ResponseUtil.setSuccessResult();
    }


    @ApiOperation(value = "根据药品id查询具体的库存信息")
    @GetMapping("/drug/stock/list")
    public ResponseResult<List<DrugStockInventoryVo>> getDrugStockDetail(@ApiParam("盘库主键集合，逗号间隔") @RequestParam(value = "drugIds") String drugIds){

        return ResponseUtil.setSuccessResult(drugInventoryManager.getDrugStockDetail(drugIds));
    }


}
