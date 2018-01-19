package com.acmed.his.api;

import com.acmed.his.dao.ChargeMapper;
import com.acmed.his.dao.PrescriptionItemMapper;
import com.acmed.his.dao.PrescriptionMapper;
import com.acmed.his.model.Charge;
import com.acmed.his.model.Inspect;
import com.acmed.his.model.Prescription;
import com.acmed.his.model.PrescriptionItem;
import com.acmed.his.model.dto.DispensingDto;
import com.acmed.his.pojo.mo.DispensQueryMo;
import com.acmed.his.pojo.vo.DispensingPreVo;
import com.acmed.his.pojo.vo.DispensingVo;
import com.acmed.his.service.ApplyManager;
import com.acmed.his.service.BaseInfoManager;
import com.acmed.his.service.DispensingManager;
import com.acmed.his.service.PrescriptionManager;
import com.acmed.his.support.AccessInfo;
import com.acmed.his.support.AccessToken;
import com.acmed.his.util.PageBase;
import com.acmed.his.util.PageResult;
import com.acmed.his.util.ResponseResult;
import com.acmed.his.util.ResponseUtil;
import com.alibaba.fastjson.JSONObject;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import tk.mybatis.mapper.entity.Example;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * 发药接口
 *
 * Created by Darren on 2017-12-05
 **/
@RestController
@Api(tags = "发药管理")
@RequestMapping("/dispens")
public class DispensingApi {

    @Autowired
    private ChargeMapper chargeMapper;

    @Autowired
    private PrescriptionMapper preMapper;

    @Autowired
    private PrescriptionItemMapper preItemMapper;

    @Autowired
    private BaseInfoManager baseInfoManager;

    @Autowired
    private DispensingManager dispensingManager;

    @Autowired
    private PrescriptionManager preManager;

    @ApiOperation(value = "获取发药收费列表")
    @PostMapping("/list")
    public ResponseResult<PageResult<DispensingVo>> getDispensingList(
            @RequestBody(required = false) PageBase<DispensQueryMo> mo,
            @AccessToken AccessInfo info){
        List<DispensingVo> list = new ArrayList<>();
        List<DispensingDto> applyList = dispensingManager.getDispensingList(mo.getPageNum(),mo.getPageSize(),
                info.getUser().getOrgCode(), Optional.ofNullable(mo.getParam()).map(obj->obj.getName()).orElse(null),
                Optional.ofNullable(mo.getParam()).map(obj->obj.getStatus()).orElse(null));
        applyList.forEach(obj->{
            DispensingVo vo = new DispensingVo();
            BeanUtils.copyProperties(obj,vo);
            if("0".equals(obj.getIsPaid())) vo.setStatus("0");
            if("1".equals(obj.getIsPaid()) && "0".equals(obj.getIsDispensing())) vo.setStatus("2");
            if("2".equals(obj.getIsPaid())) vo.setStatus("3");
            if("3".equals(obj.getIsPaid())) vo.setStatus("4");
            if("1".equals(obj.getIsDispensing())) vo.setStatus("5");

            list.add(vo);
        });
        int total = dispensingManager.getDispensingTotal(info.getUser().getOrgCode(),
                Optional.ofNullable(mo.getParam()).map(obj->obj.getName()).orElse(null),
                Optional.ofNullable(mo.getParam()).map(obj->obj.getStatus()).orElse(null));
        return ResponseUtil.setSuccessResult(new PageResult(list,(long)total));
    }

    @ApiOperation(value = "根据挂号id获取处方列表")
    @GetMapping("/pre")
    public ResponseResult<DispensingPreVo> getDispensingDetail(@ApiParam("挂号主键") @RequestParam("applyId") String applyId){
        List<DispensingPreVo> list = new ArrayList<>();
        preManager.getPreByApply(applyId).forEach(obj->{

            Prescription prescription = preMapper.selectByPrimaryKey(obj.getId());
            if(prescription.getCategory().equals("1")){//只处理药品处方
                Example example = new Example(Charge.class);
                example.createCriteria().andEqualTo("prescriptionId",obj.getId());
                List<Charge> chargeList = chargeMapper.selectByExample(example);

                example = new Example(Inspect.class);
                example.createCriteria().andEqualTo("prescriptionId",obj.getId());


                example = new Example(PrescriptionItem.class);
                example.createCriteria().andEqualTo("prescriptionId",obj.getId());
                List<PrescriptionItem> preItemList = preItemMapper.selectByExample(example);
                list.add(new DispensingPreVo(prescription,chargeList,preItemList,baseInfoManager));
            }


        });
        return ResponseUtil.setSuccessResult(list);
    }

    @ApiOperation(value = "确认发药")
    @PostMapping
    public ResponseResult dispensing(@ApiParam("{\"applyId\":},applyId：挂号单id") @RequestBody String param,
                                        @AccessToken AccessInfo info){
        if(StringUtils.isEmpty(param) || null == JSONObject.parseObject(param).get("applyId")){
            return ResponseUtil.setParamEmptyError("applyId");
        }
        dispensingManager.dispensing(JSONObject.parseObject(param).getString("applyId"),info.getUser());
        return ResponseUtil.setSuccessResult();
    }



    @ApiOperation(value = "付费")
    @PostMapping("/pay")
    public ResponseResult pay(@ApiParam("{\"applyId\":\"\",\"fee\":\"\",\"feeType\":\"\"},applyId：处方主键、fee：实际付费金额、feeType：付费类型") @RequestBody String param,
                                     @AccessToken AccessInfo info){
        if(StringUtils.isEmpty(param) || null == JSONObject.parseObject(param).get("applyId")){
            return ResponseUtil.setParamEmptyError("applyId");
        }
        dispensingManager.pay(JSONObject.parseObject(param).getString("id"),
                JSONObject.parseObject(param).getDouble("fee"),
                JSONObject.parseObject(param).getString("feeType"),info.getUser());
        return ResponseUtil.setSuccessResult();
    }

    @ApiOperation(value = "费用列表")
    @GetMapping("/fee/list")
    public ResponseResult getRefundList(@ApiParam("挂号主键") @RequestParam("applyId") String id,
                                        @ApiParam("费用类型 null:所有类型合并, 0:未支付,1:已支付,2:已退款") @RequestParam(value = "type",required = false) Integer type){
        List<Map<String,Object>> list = dispensingManager.getRefundList(id,type);
        return ResponseUtil.setSuccessResult(list);
    }


    @ApiOperation(value = "退款")
    @PostMapping("/refund")
    public ResponseResult refund(@ApiParam("{\"applyId\":\"\",\"groupNum\":\"\"},applyId:挂号单id groupNum：序号,多个值，逗号间隔") @RequestBody String param,
                                 @AccessToken AccessInfo info){
        if(StringUtils.isEmpty(param) || null == JSONObject.parseObject(param).get("applyId")){
            return ResponseUtil.setParamEmptyError("applyId");
        }
        if(StringUtils.isEmpty(param) || null == JSONObject.parseObject(param).get("groupNum")){
            return ResponseUtil.setParamEmptyError("groupNum");
        }
        dispensingManager.refund(JSONObject.parseObject(param).getString("applyId"),
                JSONObject.parseObject(param).getString("groupNum"));
        return ResponseUtil.setSuccessResult();
    }

}
