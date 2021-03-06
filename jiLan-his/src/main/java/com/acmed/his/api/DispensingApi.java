package com.acmed.his.api;

import com.acmed.his.constants.StatusCode;
import com.acmed.his.dao.*;
import com.acmed.his.model.*;
import com.acmed.his.model.dto.DispensingDto;
import com.acmed.his.pojo.mo.DispensQueryMo;
import com.acmed.his.pojo.mo.DispensingRefundApplyMo;
import com.acmed.his.pojo.mo.DispensingRefundMo;
import com.acmed.his.pojo.vo.*;
import com.acmed.his.service.BaseInfoManager;
import com.acmed.his.service.DispensingManager;
import com.acmed.his.service.MedicalRecordManager;
import com.acmed.his.support.AccessInfo;
import com.acmed.his.support.AccessToken;
import com.acmed.his.util.*;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import tk.mybatis.mapper.entity.Example;

import java.time.LocalDate;
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
    private InspectMapper inspectMapper;

    @Autowired
    private BaseInfoManager baseInfoManager;

    @Autowired
    private DispensingManager dispensingManager;

    @Autowired
    private PrescriptionItemStockMapper preItemStockMapper;

    @Autowired
    private PayStatementsMapper payStatementsMapper;

    @Autowired
    private PayRefuseMapper payRefuseMapper;

    @Autowired
    private DrugMapper drugMapper;

    @Autowired
    private ManufacturerMapper manufacturerMapper;

    @Autowired
    private InjectMapper injectMapper;

    @Autowired
    private MedicalRecordManager medicalRecordManager;

    @Autowired
    private ApplyMapper applyMapper;


    @ApiOperation(value = "收支概况")
    @GetMapping("/fee/survey")
    public ResponseResult<DispensingFeeSurveyVo> getFeeSurvey(@Param("日期 yyyy-MM-dd格式 默认当天")@RequestParam(required = false) String startDate,
                                                              @Param("日期 yyyy-MM-dd格式 默认当天")@RequestParam(required = false) String endDate,
                                                              @AccessToken AccessInfo info){
        if(null == startDate && null == endDate){
            startDate = DateTimeUtil.getBeginDate(LocalDate.now().toString());
            endDate = DateTimeUtil.getEndDate(LocalDate.now().toString());
        }else{
            startDate = Optional.ofNullable(startDate).map(DateTimeUtil::getBeginDate).orElse(null);
            endDate = Optional.ofNullable(endDate).map(DateTimeUtil::getEndDate).orElse(null);
        }
        List<Map<String,Object>> payList = payStatementsMapper.getFeeSurvey(startDate,endDate,info.getUser().getOrgCode());
        List<Map<String,Object>> refundList = payRefuseMapper.getFeeSurvey(startDate,endDate,info.getUser().getOrgCode());
        Double fee = preMapper.getFee(startDate,endDate,info.getUser().getOrgCode());

        return ResponseUtil.setSuccessResult(new DispensingFeeSurveyVo(fee,payList,refundList));
    }

    @ApiOperation(value = "收费发药列表")
    @PostMapping("/list")
    public ResponseResult<PageResult<DispensingVo>> getDispensingList(
            @RequestBody(required = false) PageBase<DispensQueryMo> mo,
            @AccessToken AccessInfo info){

        if(null == mo.getParam() || (StringUtils.isEmpty(mo.getParam().getDiagnoseStartDate())) &&
                (StringUtils.isEmpty(mo.getParam().getDiagnoseEndDate()))){
            if(null == mo.getParam()){
                mo.setParam(new DispensQueryMo());
            }
            mo.getParam().setDiagnoseStartDate(DateTimeUtil.getBeginDate(LocalDate.now().toString()));
            mo.getParam().setDiagnoseEndDate(DateTimeUtil.getEndDate(LocalDate.now().toString()));
        }else{
            mo.getParam().setDiagnoseStartDate(Optional.ofNullable(mo.getParam().getDiagnoseStartDate()).
                    map(DateTimeUtil::getBeginDate).orElse(null));
            mo.getParam().setDiagnoseEndDate(Optional.ofNullable(mo.getParam().getDiagnoseEndDate()).
                    map(DateTimeUtil::getEndDate).orElse(null));
        }
        List<DispensingVo> list = new ArrayList<>();
        PageResult<DispensingDto> result = dispensingManager.getDispensingList(mo.getPageNum(),mo.getPageSize(),
                info.getUser().getOrgCode(), Optional.ofNullable(mo.getParam()).map(obj->obj.getName()).orElse(null),
                Optional.ofNullable(mo.getParam().getStatus()).orElse(null),
                mo.getParam().getDiagnoseStartDate(),mo.getParam().getDiagnoseEndDate());
        result.getData().forEach(obj->{
            DispensingVo vo = new DispensingVo();
            BeanUtils.copyProperties(obj,vo);
            if("0".equals(obj.getIsPaid())) vo.setStatus("1");
            if("1".equals(obj.getIsPaid()) && "0".equals(obj.getIsDispensing())) vo.setStatus("2");
            if("2".equals(obj.getIsPaid()) || "3".equals(obj.getIsPaid())) vo.setStatus("4");
            if("1".equals(obj.getIsDispensing()) ||
                    ("1".equals(obj.getIsPaid()) && "2".equals(obj.getIsDispensing()))) vo.setStatus("5");

            list.add(vo);
        });

        return ResponseUtil.setSuccessResult(new PageResult(list,result.getTotal()));
    }

    @ApiOperation(value = "付费费用列表，支付页面显示")
    @GetMapping("/pay/fee")
    public ResponseResult<PrescriptionFeeVo> getPreFeeList(@ApiParam("挂号主键") @RequestParam("applyId") String applyId){
        List<PrescriptionFee> sourceList = dispensingManager.getPreFeeList(applyId);
        List<PrescriptionFeeVo> list = Lists.newArrayList();
        sourceList.forEach(obj->{
            PrescriptionFeeVo vo = new PrescriptionFeeVo();
            BeanUtils.copyProperties(obj,vo);
            list.add(vo);
        });
        return ResponseUtil.setSuccessResult(list);
    }


    @ApiOperation(value = "付费")
    @PostMapping("/pay")
    public ResponseResult pay(@ApiParam("{\"applyId\":\"\",\"feeType\":\"\"},applyId：处方主键、feeType：付费类型") @RequestBody String param,
                              @AccessToken AccessInfo info){
        if(StringUtils.isEmpty(param) || null == JSONObject.parseObject(param).get("applyId")){
            return ResponseUtil.setParamEmptyError("applyId");
        }
        dispensingManager.pay(JSONObject.parseObject(param).getString("applyId"),
                JSONObject.parseObject(param).getString("feeType"),info.getUser());
        return ResponseUtil.setSuccessResult();
    }




    @ApiOperation(value = "退款")
    @PostMapping("/refund")
    public ResponseResult refund(@RequestBody DispensingRefundApplyMo mo,
                                 @AccessToken AccessInfo info){


        Prescription prescription = preMapper.getPreByApply(mo.getApplyId()).get(0);
        if("0".equals(prescription.getIsPaid())){
            return ResponseUtil.setErrorMeg(StatusCode.FAIL,"尚未付款，无法退款！");
        }
        if("1".equals(prescription.getIsDispensing())){
            return ResponseUtil.setErrorMeg(StatusCode.FAIL,"已经发药，无法退款！");
        }
        dispensingManager.refund(mo,info.getUser());
        return ResponseUtil.setSuccessResult();
    }

    @ApiOperation(value = "退费列表")
    @GetMapping("/refund/list")
    public ResponseResult<DispensingRefundVo> getDispensingDetail(@ApiParam("挂号主键") @RequestParam("applyId") String applyId){

        Example example = new Example(Prescription.class);
        example.createCriteria().andEqualTo("applyId",applyId);
        Prescription prescription = preMapper.selectByExample(example).get(0);

        example = new Example(PrescriptionItem.class);
        example.createCriteria().andEqualTo("applyId",applyId);
        List<PrescriptionItem> itemList = preItemMapper.selectByExample(example);

        example = new Example(Inspect.class);
        example.createCriteria().andEqualTo("applyId",applyId);
        example.orderBy("id").asc();
        List<Inspect> inspectList = inspectMapper.selectByExample(example);


        example = new Example(Charge.class);
        example.createCriteria().andEqualTo("applyId",applyId);
        example.orderBy("id").asc();
        List<Charge> chargeList = chargeMapper.selectByExample(example);


        return ResponseUtil.setSuccessResult(new DispensingRefundVo(prescription,itemList,inspectList,chargeList,baseInfoManager,drugMapper));
    }


    @ApiOperation(value = "发药列表")
    @GetMapping("/medicine/list")
    public ResponseResult<DispensingMedicineVo> getMedicineDetail(@ApiParam("挂号主键") @RequestParam("applyId") String applyId,
                                                                  @AccessToken AccessInfo info){

        dispensingManager.lockStock(applyId,info.getUser());//锁定库存

        Prescription prescription = preMapper.getPreByApply(applyId).get(0);

        Example example = new Example(PrescriptionItem.class);
        example.createCriteria().andEqualTo("applyId",applyId).andEqualTo("payStatus","1")
        .andIsNull("zyStoreId");//过滤掌药药品信息
        example.orderBy("sn").asc();
        List<PrescriptionItem> itemList = preItemMapper.selectByExample(example);

        Map<String,List<PrescriptionItemStock>> map = Maps.newHashMap();
        itemList.forEach(obj->{
            Example stockExample = new Example(PrescriptionItemStock.class);
            stockExample.createCriteria().andEqualTo("itemId",obj.getId());
            map.put(obj.getId(),preItemStockMapper.selectByExample(stockExample));
        });

        MedicalRecord medicalRecord = medicalRecordManager.getMedicalRecordByApplyId(prescription.getApplyId());

        return ResponseUtil.setSuccessResult(new DispensingMedicineVo(applyMapper.selectByPrimaryKey(applyId),prescription,medicalRecord,itemList,map,baseInfoManager,drugMapper,manufacturerMapper));
    }


    @ApiOperation(value = "收费发药详情")
    @GetMapping("/detail")
    public ResponseResult<DispensingDetailVo> getDetail(@ApiParam("挂号主键") @RequestParam("applyId") String applyId,
                                                                  @AccessToken AccessInfo info){

        dispensingManager.lockStock(applyId,info.getUser());//锁定库存

        Prescription prescription = preMapper.getPreByApply(applyId).get(0);

        Example example = new Example(PrescriptionItem.class);
        example.createCriteria().andEqualTo("applyId",applyId);
        example.orderBy("sn").asc();
        List<PrescriptionItem> itemList = preItemMapper.selectByExample(example);

        example = new Example(Inspect.class);
        example.createCriteria().andEqualTo("applyId",applyId);
        example.orderBy("sn").asc();
        List<Inspect> inspectList = inspectMapper.selectByExample(example);

        example = new Example(Charge.class);
        example.createCriteria().andEqualTo("applyId",applyId);
        example.orderBy("sn").asc();
        List<Charge> chargeList = chargeMapper.selectByExample(example);

        Map<String,List<PrescriptionItemStock>> map = Maps.newHashMap();
        itemList.forEach(obj->{
            Example stockExample = new Example(PrescriptionItemStock.class);
            stockExample.createCriteria().andEqualTo("itemId",obj.getId());
            map.put(obj.getId(),preItemStockMapper.selectByExample(stockExample));
        });


        example = new Example(Inject.class);
        example.createCriteria().andEqualTo("applyId",prescription.getApplyId());
        example.orderBy("groupNum").orderBy("id");
        List<Inject> injectList = injectMapper.selectByExample(example);
        MedicalRecord medicalRecord = medicalRecordManager.getMedicalRecordByApplyId(prescription.getApplyId());
        return ResponseUtil.setSuccessResult(new DispensingDetailVo(applyMapper.selectByPrimaryKey(applyId),prescription,medicalRecord,itemList,inspectList,chargeList,
                map,injectList,baseInfoManager,drugMapper,manufacturerMapper));
    }


    @ApiOperation(value = "退款",hidden = true)
//    @PostMapping("/refund")
    public ResponseResult refund(@RequestBody DispensingRefundMo mo,
                                 @AccessToken AccessInfo info){
        Prescription prescription = preMapper.getPreByApply(mo.getApplyId()).get(0);
        if("0".equals(prescription.getIsPaid())){
            return ResponseUtil.setErrorMeg(StatusCode.FAIL,"尚未付款，无法退款！");
        }
        if("1".equals(prescription.getIsDispensing())){
            return ResponseUtil.setErrorMeg(StatusCode.FAIL,"已经发药，无法退款！");
        }
        dispensingManager.refund(mo,info.getUser());
        return ResponseUtil.setSuccessResult();
    }

    @ApiOperation(value = "发药",hidden = true)
//    @PostMapping("/medicine")
    public ResponseResult medicine(@ApiParam("{\"applyId\":\"\"},applyId：处方主键") @RequestBody String param,
                                   @AccessToken AccessInfo info){

        if(StringUtils.isEmpty(param) || null == JSONObject.parseObject(param).get("applyId")){
            return ResponseUtil.setParamEmptyError("applyId");
        }
        dispensingManager.medicine(JSONObject.parseObject(param).getString("applyId"),info.getUser());
        return ResponseUtil.setSuccessResult();
    }

    @ApiOperation(value = "确认发药",hidden = true)
    @PostMapping
    public ResponseResult dispensing(@ApiParam("{\"applyId\":},applyId：挂号单id") @RequestBody String param,
                                     @AccessToken AccessInfo info){
        if(StringUtils.isEmpty(param) || null == JSONObject.parseObject(param).get("applyId")){
            return ResponseUtil.setParamEmptyError("applyId");
        }
        dispensingManager.dispensing(JSONObject.parseObject(param).getString("applyId"),info.getUser());
        return ResponseUtil.setSuccessResult();
    }
}
