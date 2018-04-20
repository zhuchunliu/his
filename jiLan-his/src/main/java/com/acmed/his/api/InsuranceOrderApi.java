package com.acmed.his.api;

import com.acmed.his.model.InsuranceOrder;
import com.acmed.his.service.InsuranceOrderManager;
import com.acmed.his.support.AccessInfo;
import com.acmed.his.support.AccessToken;
import com.acmed.his.util.PageResult;
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

/**
 * InsuranceOrderApi
 *
 * @author jimson
 * @date 2018/4/20
 */
@RestController
@Api(tags = "保单")
@RequestMapping("/insuranceOrder")
public class InsuranceOrderApi {
    @Autowired
    private InsuranceOrderManager insuranceOrderManager;

    @GetMapping("selfInsuranceOrderListByPage")
    @ApiOperation(value = "拉去自己保单列表")
    public ResponseResult selfInsuranceOrderListByPage(@AccessToken AccessInfo info,
                                             @ApiParam("页码") @RequestParam(value = "pageNum")Integer pageNum,
                                             @ApiParam("每页记录数") @RequestParam(value = "pageSize")Integer pageSize,
                                             @ApiParam("已支付1  未支付0") @RequestParam(value = "isPaid")Integer isPaid,
                                             @ApiParam("开始时间 2018-04-02") @RequestParam(value = "startTime")String startTime,
                                             @ApiParam("结束时间 2018-04-08") @RequestParam(value = "endTime")String endTime){
        Integer userId = info.getUserId();
        PageResult<InsuranceOrder> pageResult = insuranceOrderManager.insuranceOrderList(pageNum, pageSize, userId,isPaid,startTime,endTime);
        return ResponseUtil.setSuccessResult(pageResult);
    }
}
