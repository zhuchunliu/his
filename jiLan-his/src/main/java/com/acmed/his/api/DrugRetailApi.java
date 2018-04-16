package com.acmed.his.api;

import com.acmed.his.model.dto.DrugRetailDto;
import com.acmed.his.pojo.mo.DrugRetailMo;
import com.acmed.his.pojo.mo.DrugRetailQueryMo;
import com.acmed.his.pojo.vo.DrugListVo;
import com.acmed.his.pojo.vo.DrugRetailVo;
import com.acmed.his.service.DrugRetailManager;
import com.acmed.his.support.AccessInfo;
import com.acmed.his.support.AccessToken;
import com.acmed.his.util.*;
import com.alibaba.fastjson.JSONObject;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

/**
 * 药品零售
 * Created by Darren on 2018-04-13
 **/
@Api(tags = "药品零售")
@RequestMapping("/retail")
@RestController
public class DrugRetailApi {

    @Autowired
    private DrugRetailManager drugRetailManager;

    @ApiOperation(value = "零售列表")
    @PostMapping("/list")
    public ResponseResult<PageResult<DrugRetailDto>> getRetailList(@RequestBody(required = false) PageBase<DrugRetailQueryMo> pageBase,
                                                                   @AccessToken AccessInfo info) {
        DrugRetailQueryMo mo = Optional.ofNullable(pageBase.getParam()).orElse(new DrugRetailQueryMo());
        mo.setStartTime(Optional.ofNullable(mo.getStartTime()).map(DateTimeUtil::getBeginDate).orElse(mo.getStartTime()));
        mo.setEndTime(Optional.ofNullable(mo.getEndTime()).map(DateTimeUtil::getEndDate).orElse(mo.getEndTime()));
        pageBase.setParam(mo);
        PageResult result =drugRetailManager.getRetailList(pageBase.getPageNum(),pageBase.getPageSize(),mo,info.getUser());
        return ResponseUtil.setSuccessResult(result);
    }

    @ApiOperation(value = "添加零售信息")
    @PostMapping("/save")
    public ResponseResult<DrugRetailMo> saveRetail(@RequestBody(required = false) DrugRetailMo mo,
                                                                 @AccessToken AccessInfo info) {
        drugRetailManager.saveRetail(mo,info.getUser());
        return ResponseUtil.setSuccessResult();
    }

    @ApiOperation(value = "零售详情")
    @GetMapping("/detail")
    public ResponseResult<PageResult<DrugRetailVo>> getRetailDetail(@ApiParam("零售主键")@RequestParam("id") String id) {

        return ResponseUtil.setSuccessResult(drugRetailManager.getRetailDetail(id));
    }

    @ApiOperation(value = "删除药品零售信息")
    @DeleteMapping("/del")
    public ResponseResult<PageResult<DrugListVo>> delRetail(@ApiParam("零售主键")@RequestParam("id") String id,
                                                                  @AccessToken AccessInfo info) {
        drugRetailManager.delRetail(id,info.getUser());
        return ResponseUtil.setSuccessResult();
    }

    @ApiOperation(value = "付费")
    @PostMapping("/pay")
    public ResponseResult pay(@ApiParam("{\"id\":\"\",\"feeType\":\"\"},id：零售主键、feeType：付费类型") @RequestBody String param,
                              @AccessToken AccessInfo info){
        if(StringUtils.isEmpty(param) || null == JSONObject.parseObject(param).get("id")){
            return ResponseUtil.setParamEmptyError("id");
        }
        drugRetailManager.pay(JSONObject.parseObject(param).getString("id"),
                JSONObject.parseObject(param).getString("feeType"),info.getUser());
        return ResponseUtil.setSuccessResult();
    }
}
