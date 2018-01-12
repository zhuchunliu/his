package com.acmed.his.api;

import com.acmed.his.dao.ChargeMapper;
import com.acmed.his.dao.PrescriptionItemMapper;
import com.acmed.his.dao.PrescriptionMapper;
import com.acmed.his.model.Charge;
import com.acmed.his.model.Inspect;
import com.acmed.his.model.Prescription;
import com.acmed.his.model.PrescriptionItem;
import com.acmed.his.model.dto.DispensingDto;
import com.acmed.his.pojo.vo.DispensingPreVo;
import com.acmed.his.pojo.vo.DispensingVo;
import com.acmed.his.service.ApplyManager;
import com.acmed.his.service.BaseInfoManager;
import com.acmed.his.service.DispensingManager;
import com.acmed.his.service.PrescriptionManager;
import com.acmed.his.support.AccessInfo;
import com.acmed.his.support.AccessToken;
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
    private ApplyManager applyManager;

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
    @GetMapping("/list")
    public ResponseResult<DispensingVo> getDispensingList(
            @ApiParam("患者姓名或者编号") @RequestParam(value = "name",required = false)String name,
            @ApiParam("1:未收费、2:未发药、3：已退款、4：已完成") @RequestParam(value = "status",required = false)String status,
            @AccessToken AccessInfo info){
        List<DispensingVo> list = new ArrayList<>();
        List<DispensingDto> applyList = applyManager.getDispensingList(info.getUser().getOrgCode(),name,status);
        applyList.forEach(obj->{
            DispensingVo mo = new DispensingVo();
            BeanUtils.copyProperties(obj,mo);
            if("0".equals(obj.getIsPaid())) mo.setStatus("1");
            if("1".equals(obj.getIsPaid()) && "0".equals(obj.getIsDispensing())) mo.setStatus("2");
            if("2".equals(obj.getIsPaid())) mo.setStatus("3");
            if("1".equals(obj.getIsDispensing())) mo.setStatus("4");

            list.add(mo);
        });
        return ResponseUtil.setSuccessResult(list);
    }

    @ApiOperation(value = "根据挂号id获取处方列表",hidden = true)
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

    @ApiOperation(value = "退款列表")
    @GetMapping("/refund/list")
    public ResponseResult getRefundList(@ApiParam("挂号主键") @RequestParam("applyId") String id){
        List<Map<String,Object>> list = dispensingManager.getRefundList(id);
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
