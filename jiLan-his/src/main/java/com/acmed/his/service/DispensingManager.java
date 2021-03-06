package com.acmed.his.service;

import com.acmed.his.constants.StatusCode;
import com.acmed.his.dao.*;
import com.acmed.his.exceptions.BaseException;
import com.acmed.his.model.*;
import com.acmed.his.model.dto.DispensingDto;
import com.acmed.his.pojo.mo.DispensingRefundApplyMo;
import com.acmed.his.pojo.mo.DispensingRefundMo;
import com.acmed.his.pojo.vo.UserInfo;
import com.acmed.his.util.PageResult;
import com.acmed.his.util.ResponseResult;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Created by Darren on 2017-12-05
 **/
@Service
public class DispensingManager {

    @Autowired
    private ApplyMapper applyMapper;

    @Autowired
    private PayManager payManager;

    @Autowired
    private PrescriptionItemMapper preItemMapper;

    @Autowired
    private InspectMapper inspectMapper;

    @Autowired
    private ChargeMapper chargeMapper;

    @Autowired
    private PrescriptionFeeMapper feeMapper;

    @Autowired
    private PrescriptionMapper preMapper;

    @Autowired
    private DrugStockMapper drugStockMapper;

    @Autowired
    private DrugMapper drugMapper;

    @Autowired
    private PrescriptionItemStockMapper itemStockMapper;


    public PageResult<DispensingDto> getDispensingList(Integer pageNum , Integer pageSize, Integer orgCode, String name, String status,
                                                                 String diagnoseStartDate, String diagnoseEndDate) {
        PageResult result = new PageResult();
        Page page = PageHelper.startPage(pageNum,pageSize);
        result.setData(preMapper.getDispensingList(orgCode,name,status,diagnoseStartDate,diagnoseEndDate));
        result.setTotal(page.getTotal());
        return result;
    }

    public Integer getDispensingTotal(Integer orgCode, String name, String status,
                                      String diagnoseStartDate,String diagnoseEndDate) {
        return preMapper.getDispensingTotal(orgCode,name,status,diagnoseStartDate,diagnoseEndDate);
    }

    /**
     * 支付
     * @param applyId
     * @param feeType
     * @param userInfo
     */
    @Transactional
    public void pay(String applyId, String feeType,UserInfo userInfo){

        Apply apply = applyMapper.selectByPrimaryKey(applyId);

        Example example = new Example(Prescription.class);
        example.createCriteria().andEqualTo("applyId",applyId);
        Prescription prescription= preMapper.selectByExample(example).get(0);
        prescription.setIsPaid("1");
        prescription.setFeeType(feeType);
        preMapper.updateByPrimaryKey(prescription);

        example = new Example(PrescriptionItem.class);
        example.createCriteria().andEqualTo("applyId",applyId).andEqualTo("payStatus","0");
        Double drugFee = 0.0d;
        List<PrescriptionItem> itemList = preItemMapper.selectByExample(example);
        for(PrescriptionItem item : itemList){
            drugFee += item.getFee();
            item.setPayStatus(1);
            preItemMapper.updateByPrimaryKey(item);
        }

        if(drugFee > 0){
            PayStatements payStatements = new PayStatements();
            payStatements.setFeeType(feeType);
            payStatements.setOrgCode(prescription.getOrgCode());
            payStatements.setSource("2");
            payStatements.setPrescriptionId(itemList.get(0).getPrescriptionId());
            payStatements.setApplyId(applyId.toString());
            payStatements.setPatientId(apply.getPatientId());
            payStatements.setFee(new BigDecimal(drugFee));
            payStatements.setCreateAt(LocalDateTime.now().toString());
            payStatements.setCreateBy(userInfo.getId().toString());
            payManager.addPayStatements(payStatements);
        }

        Double inspectFee = 0.0d;
        example = new Example(Inspect.class);
        example.createCriteria().andEqualTo("applyId",applyId).andEqualTo("payStatus","0");
        List<Inspect> inspectList = inspectMapper.selectByExample(example);
        for(Inspect inspect : inspectList){
            inspectFee += inspect.getFee();
            inspect.setPayStatus(1);
            inspectMapper.updateByPrimaryKey(inspect);
        }

        if(inspectFee > 0){
            PayStatements payStatements = new PayStatements();
            payStatements.setFeeType(feeType);
            payStatements.setOrgCode(prescription.getOrgCode());
            payStatements.setSource("3");
            payStatements.setPrescriptionId(inspectList.get(0).getPrescriptionId());
            payStatements.setApplyId(applyId.toString());
            payStatements.setPatientId(apply.getPatientId());
            payStatements.setFee(new BigDecimal(inspectFee));
            payStatements.setCreateAt(LocalDateTime.now().toString());
            payStatements.setCreateBy(userInfo.getId().toString());
            payManager.addPayStatements(payStatements);
        }


        Double chargeFee = 0.0d;
        example = new Example(Inspect.class);
        example.createCriteria().andEqualTo("applyId",applyId).andEqualTo("payStatus","0");
        List<Charge> chargeList = chargeMapper.selectByExample(example);
        for(Charge charge : chargeList){
            chargeFee += charge.getFee();
            charge.setPayStatus(1);
            chargeMapper.updateByPrimaryKey(charge);
        }

        if(chargeFee > 0){
            PayStatements payStatements = new PayStatements();
            payStatements.setFeeType(feeType);
            payStatements.setOrgCode(prescription.getOrgCode());
            payStatements.setSource("4");
            payStatements.setPrescriptionId(chargeList.get(0).getPrescriptionId());
            payStatements.setApplyId(applyId.toString());
            payStatements.setPatientId(apply.getPatientId());
            payStatements.setFee(new BigDecimal(chargeFee));
            payStatements.setCreateAt(LocalDateTime.now().toString());
            payStatements.setCreateBy(userInfo.getId().toString());
            payManager.addPayStatements(payStatements);
        }


        //修改支付
        feeMapper.payApplyFee(applyId,userInfo);

    }


    /**
     * 确认发药
     * @param applyId
     * @param userInfo
     */
    @Transactional
    public void dispensing(String applyId, UserInfo userInfo) {
        Example example = new Example(Prescription.class);
        example.createCriteria().andEqualTo("applyId",applyId);
        Prescription prescription = preMapper.selectByExample(example).get(0);
        prescription.setIsDispensing("1");
        prescription.setModifyBy(userInfo.getId().toString());
        prescription.setModifyAt(LocalDateTime.now().toString());
        preMapper.updateByPrimaryKey(prescription);

        //扣除库存
//        example = new Example(PrescriptionItem.class);
//        example.createCriteria().andEqualTo("applyId",applyId);
//        List<PrescriptionItem> list = preItemMapper.selectByExample(example);
//        list.forEach(obj->{
//            Drug drug = drugMapper.selectByPrimaryKey(obj.getDrugId());
//            drug.setNum(drug.getNum()-obj.getNum());
//            drugMapper.updateByPrimaryKey(drug);
//        });
    }

    /**
     * 获取付费列表
     *
     * @param applyId
     */
    public List<PrescriptionFee> getPreFeeList(String applyId) {
        return feeMapper.getByApplyId(applyId);
    }


    @Transactional
    public void refund(DispensingRefundApplyMo mo , UserInfo userInfo) {
        DispensingRefundMo refundMo = new DispensingRefundMo();
        BeanUtils.copyProperties(mo,refundMo);
        List<DispensingRefundMo.RefundMo> refundMoList = Lists.newArrayList();

        Example example = new Example(PrescriptionItem.class);
        example.createCriteria().andEqualTo("applyId",mo.getApplyId()).andEqualTo("payStatus",1);
        List<PrescriptionItem> itemList = preItemMapper.selectByExample(example);

        itemList.forEach(obj->refundMoList.add(new DispensingRefundMo.RefundMo(obj.getId(),1)));

        example = new Example(Inspect.class);
        example.createCriteria().andEqualTo("applyId",mo.getApplyId()).andEqualTo("payStatus",1);
        List<Inspect> inspectList = inspectMapper.selectByExample(example);
        inspectList.forEach(obj->refundMoList.add(new DispensingRefundMo.RefundMo(obj.getId(),2)));

        example = new Example(Charge.class);
        example.createCriteria().andEqualTo("applyId",mo.getApplyId()).andEqualTo("payStatus",1);
        List<Charge> chargeList = chargeMapper.selectByExample(example);
        chargeList.forEach(obj->refundMoList.add(new DispensingRefundMo.RefundMo(obj.getId(),3)));

        refundMo.setMoList(refundMoList);
        this.refund(refundMo,userInfo);
    }

    @Transactional
    public void refund(DispensingRefundMo mo , UserInfo userInfo) {
        Apply apply = applyMapper.selectByPrimaryKey(mo.getApplyId());

        Map<String,Double> groupFeeMap = Maps.newHashMap();
        for(DispensingRefundMo.RefundMo refundMo : mo.getMoList()){

            switch (refundMo.getType()){
                case 1:
                    PrescriptionItem item = preItemMapper.selectByPrimaryKey(refundMo.getId());
                    if(item.getPayStatus() == 2){
                        break;
                    }
                    item.setPayStatus(2);
                    preItemMapper.updateByPrimaryKey(item);

                    PayRefuse itemPayRefuse = new PayRefuse();
                    itemPayRefuse.setOrgCode(userInfo.getOrgCode());
                    itemPayRefuse.setFeeType(mo.getFeeType());
                    itemPayRefuse.setSource("2");
                    itemPayRefuse.setPrescriptionId(item.getPrescriptionId());
                    itemPayRefuse.setApplyId(mo.getApplyId());
                    itemPayRefuse.setPatientId(apply.getPatientId());
                    itemPayRefuse.setItemId(item.getId());
                    itemPayRefuse.setFee(new BigDecimal(item.getFee()));
                    itemPayRefuse.setReason(mo.getReason());
                    itemPayRefuse.setState(mo.getState());
                    itemPayRefuse.setCreateAt(LocalDateTime.now().toString());
                    itemPayRefuse.setCreateBy(userInfo.getId().toString());
                    payManager.addPayRefuse(itemPayRefuse);

                    if(!groupFeeMap.containsKey(item.getGroupNum())){
                        groupFeeMap.put(item.getGroupNum(),item.getFee());
                    }else{
                        groupFeeMap.put(item.getGroupNum(),groupFeeMap.get(item.getGroupNum())+item.getFee());
                    }
                    break;
                case 2:
                    Inspect inspect = inspectMapper.selectByPrimaryKey(refundMo.getId());
                    if(inspect.getPayStatus()== 2){
                        break;
                    }
                    inspect.setPayStatus(2);
                    inspectMapper.updateByPrimaryKey(inspect);

                    PayRefuse inspectPayRefuse = new PayRefuse();
                    inspectPayRefuse.setOrgCode(userInfo.getOrgCode());
                    inspectPayRefuse.setFeeType(mo.getFeeType());
                    inspectPayRefuse.setSource("3");
                    inspectPayRefuse.setPrescriptionId(inspect.getPrescriptionId());
                    inspectPayRefuse.setApplyId(mo.getApplyId());
                    inspectPayRefuse.setPatientId(apply.getPatientId());
                    inspectPayRefuse.setItemId(inspect.getId());
                    inspectPayRefuse.setFee(new BigDecimal(inspect.getFee()));
                    inspectPayRefuse.setReason(mo.getReason());
                    inspectPayRefuse.setState(mo.getState());
                    inspectPayRefuse.setCreateAt(LocalDateTime.now().toString());
                    inspectPayRefuse.setCreateBy(userInfo.getId().toString());
                    payManager.addPayRefuse(inspectPayRefuse);

                    if(!groupFeeMap.containsKey(inspect.getGroupNum())){
                        groupFeeMap.put(inspect.getGroupNum(),inspect.getFee());
                    }else{
                        groupFeeMap.put(inspect.getGroupNum(),groupFeeMap.get(inspect.getGroupNum())+inspect.getFee());
                    }
                    break;
                case 3:
                    Charge charge = chargeMapper.selectByPrimaryKey(refundMo.getId());
                    if(charge.getPayStatus() == 2){
                        break;
                    }
                    charge.setPayStatus(2);
                    chargeMapper.updateByPrimaryKey(charge);

                    PayRefuse chargePayRefuse = new PayRefuse();
                    chargePayRefuse.setOrgCode(userInfo.getOrgCode());
                    chargePayRefuse.setFeeType(mo.getFeeType());
                    chargePayRefuse.setSource("4");
                    chargePayRefuse.setPrescriptionId(charge.getPrescriptionId());
                    chargePayRefuse.setApplyId(mo.getApplyId());
                    chargePayRefuse.setPatientId(apply.getPatientId());
                    chargePayRefuse.setItemId(charge.getId());
                    chargePayRefuse.setFee(new BigDecimal(charge.getFee()));
                    chargePayRefuse.setReason(mo.getReason());
                    chargePayRefuse.setState(mo.getState());
                    chargePayRefuse.setCreateAt(LocalDateTime.now().toString());
                    chargePayRefuse.setCreateBy(userInfo.getId().toString());
                    payManager.addPayRefuse(chargePayRefuse);

                    if(!groupFeeMap.containsKey(charge.getGroupNum())){
                        groupFeeMap.put(charge.getGroupNum(),charge.getFee());
                    }else{
                        groupFeeMap.put(charge.getGroupNum(),groupFeeMap.get(charge.getGroupNum())+charge.getFee());
                    }
                    break;

            }


        }

        //维护收款明细
        List<PrescriptionFee> feeList = feeMapper.getByApplyId(mo.getApplyId());
        Boolean flag = true;//全部退款
        for(PrescriptionFee fee : feeList){
            if(!groupFeeMap.containsKey(fee.getGroupNum())){
                continue;
            }
            fee.setRefunded(Optional.ofNullable(fee.getRefunded()).orElse(0d)+groupFeeMap.get(fee.getGroupNum()));
            fee.setModifyAt(LocalDateTime.now().toString());
            fee.setModifyBy(userInfo.getId().toString());
            feeMapper.updateByPrimaryKey(fee);

            if(!new DecimalFormat("#.00").format(fee.getRefunded().doubleValue()).equals(
                    new DecimalFormat("#.00").format(fee.getReceivables().doubleValue()))){
                flag = false;
            }
        }

        Prescription prescription = preMapper.getPreByApply(mo.getApplyId()).get(0);
        prescription.setIsPaid("3");//设置退款状态
        preMapper.updateByPrimaryKey(prescription);
    }


    /**
     * 发药
     * @param applyId
     * @param user
     */
    public void medicine(String applyId, UserInfo user) {
        Prescription prescription = preMapper.getPreByApply(applyId).get(0);
        prescription.setIsDispensing("1");
        prescription.setModifyAt(LocalDateTime.now().toString());
        prescription.setModifyBy(user.getId().toString());
        preMapper.updateByPrimaryKey(prescription);
    }

    /**
     * 锁定库存
     *
     * @param applyId
     * @param info
     */
    @Transactional
    public void lockStock(String applyId,UserInfo info) {
        Example stockExample = new Example(PrescriptionItemStock.class);
        stockExample.createCriteria().andEqualTo("applyId", applyId);
        int count = itemStockMapper.selectCountByExample(stockExample);
        if (0 != count) {//已经锁定了库存，则不再占用库存
            return;
        }
        Apply apply = applyMapper.selectByPrimaryKey(applyId);
        apply.setStatus("1");
        applyMapper.updateByPrimaryKey(apply);

        Prescription prescription = preMapper.getPreByApply(applyId).get(0);
        prescription.setIsDispensing("1");
        prescription.setModifyAt(LocalDateTime.now().toString());
        prescription.setModifyBy(info.getId().toString());
        preMapper.updateByPrimaryKey(prescription);

        synchronized ("org_" + info.getOrgCode()) {
            Example example = new Example(PrescriptionItem.class);
            example.createCriteria().andEqualTo("applyId", applyId).andEqualTo("payStatus", 1);
            List<PrescriptionItem> itemList = preItemMapper.selectByExample(example);

            for (PrescriptionItem item : itemList) {
                if(StringUtils.isNotEmpty(item.getZyStoreId())){//跳过掌药
                    continue;
                }
                List<PrescriptionItemStock> itemStockList = Lists.newArrayList();
                Drug drug = drugMapper.selectByPrimaryKey(item.getDrugId());
                //判断库存是否满足：大单位,小单位，剂量单位
                if ((1 == item.getUnitType()) && drug.getNum() < item.getNum()) {
                    throw new BaseException(StatusCode.FAIL, Optional.ofNullable(drug.getGoodsName()).orElse(drug.getName()) + "库存不足");
                } else if (2 == item.getUnitType() && 1 == drug.getMinPriceUnitType() && (drug.getNum() * drug.getConversion() + drug.getMinNum()) < item.getNum()) {
                    throw new BaseException(StatusCode.FAIL, Optional.ofNullable(drug.getGoodsName()).orElse(drug.getName()) + "库存不足");
                } else if (2 == item.getUnitType() && 2 == drug.getMinPriceUnitType() &&
                        (drug.getNum() * drug.getConversion() * drug.getDose() + drug.getMinNum() * drug.getDose() + drug.getDose()) < item.getNum()) {
                    throw new BaseException(StatusCode.FAIL, Optional.ofNullable(drug.getGoodsName()).orElse(drug.getName()) + "库存不足");
                } else {
                    if (1 == item.getUnitType()) {
                        drug.setNum(drug.getNum() - item.getNum());
                        drugMapper.updateByPrimaryKey(drug);//扣除药品库存

                        List<DrugStock> drugStockList = drugStockMapper.getByDrugId(drug.getId());
                        Integer num = item.getNum();
                        for (DrugStock drugStock : drugStockList) {

                            if (num == 0) {
                                break;
                            }
                            int occupyNum = drugStock.getNum() < num ? drugStock.getNum() : num;
                            drugStock.setNum(drugStock.getNum() - occupyNum);
                            drugStockMapper.updateByPrimaryKey(drugStock);

                            PrescriptionItemStock itemStock = new PrescriptionItemStock();
                            itemStock.setPrescriptionId(item.getPrescriptionId());
                            itemStock.setItemId(item.getId());
                            itemStock.setDrugId(item.getDrugId());
                            itemStock.setApplyId(item.getApplyId());
                            itemStock.setBatchNumber(drugStock.getBatchNumber());
                            itemStock.setExpiryDate(drugStock.getExpiryDate());
                            itemStock.setNum(occupyNum);
                            itemStockList.add(itemStock);

                            num = num - occupyNum;
                        }
                    } else if (1 == drug.getMinPriceUnitType()) {//小单位扣库存
                        int total = drug.getNum() * drug.getConversion() + drug.getMinNum() - item.getNum();
                        drug.setNum(total / drug.getConversion());
                        drug.setMinNum(total % drug.getConversion());
                        drugMapper.updateByPrimaryKey(drug);//扣除药品库存

                        List<DrugStock> drugStockList = drugStockMapper.getByDrugId(drug.getId());
                        Integer num = item.getNum();
                        for (DrugStock drugStock : drugStockList) {

                            if (num == 0) {
                                break;
                            }
                            int totalNum = drugStock.getNum() * drug.getConversion() + drugStock.getMinNum();
                            int occupyNum = totalNum < num ? totalNum : num;
                            drugStock.setNum((totalNum - occupyNum) / drug.getConversion());
                            drugStock.setMinNum((totalNum - occupyNum - drug.getNum()*drug.getConversion()) % drug.getConversion());
                            drugStockMapper.updateByPrimaryKey(drugStock);

                            PrescriptionItemStock itemStock = new PrescriptionItemStock();
                            itemStock.setPrescriptionId(item.getPrescriptionId());
                            itemStock.setItemId(item.getId());
                            itemStock.setDrugId(item.getDrugId());
                            itemStock.setApplyId(item.getApplyId());
                            itemStock.setBatchNumber(drugStock.getBatchNumber());
                            itemStock.setExpiryDate(drugStock.getExpiryDate());
                            itemStock.setNum(occupyNum / drug.getConversion());
                            itemStock.setMinNum(occupyNum % drug.getConversion());
                            itemStockList.add(itemStock);

                            num = num - occupyNum;
                        }
                    } else {//剂量单位扣库存
                        Double total = drug.getNum() * drug.getConversion() * drug.getDose() + drug.getMinNum() * drug.getDose() + drug.getDoseNum() - item.getNum();
                        drug.setNum((int) Math.floor(total / (drug.getConversion() * drug.getDose())));
                        drug.setMinNum((int) Math.floor((total - drug.getNum() * drug.getConversion() * drug.getDose()) / drug.getDose()));
                        drug.setDoseNum(total - drug.getNum() * drug.getConversion() * drug.getDose() - drug.getMinNum() * drug.getDose());
                        drugMapper.updateByPrimaryKey(drug);//扣除药品库存

                        List<DrugStock> drugStockList = drugStockMapper.getByDrugId(drug.getId());
                        Double num = (double) item.getNum();
                        for (DrugStock drugStock : drugStockList) {

                            if (num == 0) {
                                break;
                            }
                            Double totalNum = drugStock.getNum() * drug.getConversion() * drug.getDose() + drugStock.getMinNum() * drug.getDose() + drugStock.getDoseNum();
                            Double occupyNum = totalNum < num ? totalNum : num;
                            drugStock.setNum((int) Math.floor((totalNum - occupyNum) / (drug.getConversion() * drug.getDose())));
                            drugStock.setMinNum((int) Math.floor((totalNum - occupyNum - drug.getNum() * drug.getConversion() * drug.getDose()) / drug.getDose()));
                            drugStock.setDoseNum(totalNum - occupyNum - drug.getNum() * drug.getConversion() * drug.getDose() - drug.getMinNum() * drug.getDose());
                            drugStockMapper.updateByPrimaryKey(drugStock);

                            PrescriptionItemStock itemStock = new PrescriptionItemStock();
                            itemStock.setPrescriptionId(item.getPrescriptionId());
                            itemStock.setItemId(item.getId());
                            itemStock.setDrugId(item.getDrugId());
                            itemStock.setApplyId(item.getApplyId());
                            itemStock.setBatchNumber(drugStock.getBatchNumber());
                            itemStock.setExpiryDate(drugStock.getExpiryDate());
                            itemStock.setNum((int) Math.floor(occupyNum / (drug.getConversion() * drug.getDose())));
                            itemStock.setMinNum((int) Math.floor((occupyNum - itemStock.getNum() * drug.getConversion() * drug.getDose()) / drug.getDose()));
                            itemStock.setDoseNum(occupyNum - itemStock.getNum() * drug.getConversion() * drug.getDose() - itemStock.getMinNum() * drug.getDose());
                            itemStockList.add(itemStock);

                            num = num - occupyNum;
                        }
                    }
                }
                itemStockMapper.insertList(itemStockList);
            }
        }
    }

}
