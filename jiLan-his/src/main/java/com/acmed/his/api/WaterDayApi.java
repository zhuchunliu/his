package com.acmed.his.api;

import com.acmed.his.constants.StatusCode;
import com.acmed.his.model.WaterDay;
import com.acmed.his.model.dto.ShouzhiCountDto;
import com.acmed.his.model.dto.WaterDayAndMonthCountDto;
import com.acmed.his.model.dto.WaterDetailDto;
import com.acmed.his.pojo.vo.WaterDetailVo;
import com.acmed.his.service.WaterDayManager;
import com.acmed.his.support.AccessInfo;
import com.acmed.his.support.AccessToken;
import com.acmed.his.util.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * WaterDayApi
 * 报表相关
 * @author jimson
 * @date 2018/1/8
 */
@Api(tags = "报表相关")
@RestController
@RequestMapping("/waterDay")
public class WaterDayApi {
    @Autowired
    private WaterDayManager waterDayManager;

    @ApiOperation(value = "获取区间内报表列表  分页")
    @GetMapping("getListBetweenTimes")
    public ResponseResult<PageResult<WaterDay>> getBetweenTimes(@ApiParam("页数 必填")@RequestParam("pageNum")Integer pageNum,
                                                          @ApiParam("每页记录数 必填")@RequestParam("pageSize")Integer pageSize,@ApiParam("区间开始时间  2017-01-02这种字符串格式   开始时间必填") @RequestParam("startTime") String startTime,
                                                          @ApiParam("区间结束时间 2017-01-02这种字符串格式") @RequestParam(value = "endTime",required = false) String endTime,@AccessToken AccessInfo info){
        if (StringUtils.isEmpty(startTime) && StringUtils.isNotEmpty(endTime)){
            startTime = endTime;
        }
        if (StringUtils.isNotEmpty(startTime) && StringUtils.isEmpty(endTime)){
            endTime = startTime;
        }
        Integer orgCode = info.getUser().getOrgCode();
        return ResponseUtil.setSuccessResult(waterDayManager.getListBetweenTimesByPage(startTime, endTime,orgCode,pageNum,pageSize));
    }

    @ApiOperation(value = "获取区间内报表数据累加")
    @GetMapping("getListBetweenTimesSum")
    public ResponseResult<WaterDay> getListBetweenTimesIncludeSum(@ApiParam("区间开始时间  2017-01-02这种字符串格式   开始时间必填") @RequestParam("startTime") String startTime,
                                                                @ApiParam("区间结束时间 2017-01-02这种字符串格式") @RequestParam(value = "endTime",required = false) String endTime,@AccessToken AccessInfo info){
        if (StringUtils.isEmpty(startTime) && StringUtils.isNotEmpty(endTime)){
            startTime = endTime;
        }
        if (StringUtils.isNotEmpty(startTime) && StringUtils.isEmpty(endTime)){
            endTime = startTime;
        }
        Integer orgCode = info.getUser().getOrgCode();
        WaterDay listBetweenTimes = waterDayManager.getListBetweenTimesSum(startTime, endTime, orgCode);
        return ResponseUtil.setSuccessResult(listBetweenTimes);
    }



    @ApiOperation(value = "收支明细")
    @GetMapping("detailList")
    public ResponseResult<PageResult<WaterDetailVo>> waterDetailList(@ApiParam("页数 必填")@RequestParam("pageNum")Integer pageNum,
                                     @ApiParam("每页记录数 必填")@RequestParam("pageSize")Integer pageSize,
                                                                     @ApiParam("区间开始时间  2018-01-02这种字符串格式   必填") @RequestParam("startTime") String startTime,
                                                                     @ApiParam("区间结束时间 2018-01-20这种字符串格式 必填") @RequestParam("endTime") String endTime,
                                     @AccessToken AccessInfo info){
        PageResult<WaterDetailDto> result = waterDayManager.waterDetailList(info.getUser().getOrgCode(), pageNum, pageSize,startTime,endTime);
        List<WaterDetailDto> data = result.getData();
        PageResult<WaterDetailVo> re = new PageResult<>();
        ArrayList<WaterDetailVo> waterDetailVos = new ArrayList<>();
        re.setPageSize(pageSize);
        re.setPageNum(pageNum);
        re.setTotal(result.getTotal());

        if (data.size() != 0){
            for (WaterDetailDto source : data){
                WaterDetailVo waterDetailVo = new WaterDetailVo();
                BeanUtils.copyProperties(source,waterDetailVo);
                waterDetailVo.setBalanceStr(NumberFormtUtil.toString2decimal(source.getFee()));
                if (source.getIsIn() == 1){
                    waterDetailVo.setIsInStr("收入");
                }else {
                    waterDetailVo.setIsInStr("退款");
                }
                // 支付类型
                String feeType = source.getFeeType();
                if (StringUtils.equals("1",feeType)){
                    waterDetailVo.setFeeTypeStr("微信");
                }else if (StringUtils.equals("2",feeType)){
                    waterDetailVo.setFeeTypeStr("支付宝");
                }else {
                    waterDetailVo.setFeeTypeStr("现金");
                }
                //来源 1挂号  2药品  3检查  4附加费
                String source1 = source.getSource();
                if (StringUtils.equals("1",source1)){
                    waterDetailVo.setSourceStr("挂号");
                }else if (StringUtils.equals("2",source1)){
                    waterDetailVo.setSourceStr("药品");
                }else if (StringUtils.equals("3",source1)){
                    waterDetailVo.setSourceStr("检测");
                }else {
                    waterDetailVo.setSourceStr("附加费");
                }
                waterDetailVos.add(waterDetailVo);
            }
        }
        re.setData(waterDetailVos);
        return ResponseUtil.setSuccessResult(re);
    }

    @ApiOperation(value = "年月报表，年月选填一个")
    @GetMapping("yearMonthBaobiao")
    public ResponseResult<List<WaterDay>> getYearMonthBaobiao(@AccessToken AccessInfo info,
                                                              @ApiParam("月份 2018-01   ") @RequestParam(value = "month",required = false) String month,
                                                              @ApiParam("年份 2018   ") @RequestParam(value = "year",required = false) String year
                                                              ){
        if ((StringUtils.isEmpty(year) && StringUtils.isEmpty(month)) ){
            return ResponseUtil.setErrorMeg(StatusCode.ERROR_PARAM,"参数错误");
        }
        if (StringUtils.isNotEmpty(year) && StringUtils.isNotEmpty(month)){
            return ResponseUtil.setErrorMeg(StatusCode.ERROR_PARAM,"参数错误");
        }
        return ResponseUtil.setSuccessResult(waterDayManager.getMonthYearBaobiao(year,month,info.getUser().getOrgCode()));
    }


    @ApiOperation(value = "获取昨日和昨日所在月收益")
    @GetMapping("getWaterDayAndMonthCount")
    public ResponseResult<WaterDayAndMonthCountDto> getWaterDayAndMonthCount(@AccessToken AccessInfo info){
        LocalDate today = LocalDate.now();
        LocalDate localDate = today.plusDays(1);
        return ResponseUtil.setSuccessResult(waterDayManager.getWaterDayAndMonthCount(localDate.toString(),info.getUser().getOrgCode()));
    }


    @ApiOperation(value = "诊所收支统计汇总  pc端诊所收支统计")
    @GetMapping("zhensuoShouzhiCount")
    public ResponseResult<ShouzhiCountDto> getShouzhiCountDto(
            @ApiParam("开始时间 2017-01-01   ") @RequestParam(value = "startTime")String startTime,
            @ApiParam("结束时间 2018-01-20   ") @RequestParam(value = "endTime")String endTime,
            @AccessToken AccessInfo info){
        startTime = DateTimeUtil.parsetLocalDateStart(startTime).toString();
        endTime = DateTimeUtil.parsetLocalDateEnd(endTime).toString();
        return ResponseUtil.setSuccessResult(waterDayManager.getShouzhiCountDto(startTime,endTime,info.getUser().getOrgCode()));
    }

    @ApiOperation(value = "机构第一条报表数据")
    @GetMapping("firstWaterDay")
    public ResponseResult<WaterDay> firstWaterDay(@AccessToken AccessInfo info){
        return ResponseUtil.setSuccessResult(waterDayManager.firstWaterDay(info.getUser().getOrgCode()));
    }
}
