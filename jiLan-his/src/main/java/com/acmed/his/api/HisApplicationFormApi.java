package com.acmed.his.api;

import com.acmed.his.model.HisApplicationForm;
import com.acmed.his.pojo.mo.AddHisApplicationFormMo;
import com.acmed.his.pojo.mo.UpdateHisApplicationFormStatusMo;
import com.acmed.his.service.HisApplicationFormService;
import com.acmed.his.support.AccessInfo;
import com.acmed.his.support.AccessToken;
import com.acmed.his.support.WithoutToken;
import com.acmed.his.util.PageResult;
import com.acmed.his.util.ResponseResult;
import com.acmed.his.util.ResponseUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * HisApplicationFormApi
 *
 * @author jimson
 * @date 2018/6/4
 */
@Api(tags = "his使用申请留言板")
@RestController
@RequestMapping("HisApplicationForm")
public class HisApplicationFormApi {
    @Autowired
    private HisApplicationFormService hisApplicationFormService;

    @ApiOperation(value = "创建留言")
    @WithoutToken
    @PostMapping("add")
    public ResponseResult addHisApplicationForm(@RequestBody AddHisApplicationFormMo addHisApplicationFormMo){
        HisApplicationForm hisApplicationForm = new HisApplicationForm();
        BeanUtils.copyProperties(addHisApplicationFormMo,hisApplicationForm);
        hisApplicationFormService.createHisApplicationForm(hisApplicationForm);
        return ResponseUtil.setSuccessResult();
    }

    @ApiOperation(value = "修改状态")
    @PostMapping("updateStatus")
    public ResponseResult updateStatus(@RequestBody UpdateHisApplicationFormStatusMo updateHisApplicationFormStatusMo,@AccessToken AccessInfo info){
        hisApplicationFormService.updateStatus(updateHisApplicationFormStatusMo.getId(),updateHisApplicationFormStatusMo.getStatus(),info.getUserId().toString());
        return ResponseUtil.setSuccessResult();
    }

    @ApiOperation(value = "列表")
    @GetMapping("list")
    public ResponseResult<PageResult<HisApplicationForm>> list(@ApiParam("状态 不传表示全部") @RequestParam(value = "status",required = false)Integer status,@ApiParam("页码") @RequestParam(value = "pageNum")Integer pageNum,@ApiParam("每页记录数") @RequestParam(value = "pageSize")Integer pageSize){
        PageResult<HisApplicationForm> byStatus = hisApplicationFormService.getByStatus(status, pageNum, pageSize);
        return ResponseUtil.setSuccessResult(byStatus);
    }
}
