package com.acmed.his.service;

import com.acmed.his.dao.*;
import com.acmed.his.model.*;
import com.acmed.his.pojo.vo.UserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

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
    private PrescriptionMapper preMapper;

    @Autowired
    private DrugMapper drugMapper;


    /**
     * 支付
     * @param applyId
     * @param fee
     * @param feeType
     * @param userInfo
     */
    @Transactional
    public void pay(String applyId,  Double fee, String feeType,UserInfo userInfo){
        Apply apply = applyMapper.selectByPrimaryKey(applyId);
        apply.setIsPaid("1");
        apply.setFeeType(feeType);
        applyMapper.updateByPrimaryKey(apply);

        Example example = new Example(PrescriptionItem.class);
        example.createCriteria().andEqualTo("applyId",applyId).andEqualTo("payStatus","1");
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
            payStatements.setOrgCode(apply.getOrgCode());
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
        example.createCriteria().andEqualTo("applyId",applyId).andEqualTo("payStatus","1");
        List<Inspect> inspectList = inspectMapper.selectByExample(example);
        for(Inspect inspect : inspectList){
            inspectFee += inspect.getFee();
            inspect.setPayStatus(1);
            inspectMapper.updateByPrimaryKey(inspect);
        }

        if(inspectFee > 0){
            PayStatements payStatements = new PayStatements();
            payStatements.setFeeType(feeType);
            payStatements.setOrgCode(apply.getOrgCode());
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
        example.createCriteria().andEqualTo("applyId",applyId).andEqualTo("payStatus","1");
        List<Charge> chargeList = chargeMapper.selectByExample(example);
        for(Charge charge : chargeList){
            chargeFee += charge.getFee();
            charge.setPayStatus(1);
            chargeMapper.updateByPrimaryKey(charge);
        }

        if(chargeFee > 0){
            PayStatements payStatements = new PayStatements();
            payStatements.setFeeType(feeType);
            payStatements.setOrgCode(apply.getOrgCode());
            payStatements.setSource("4");
            payStatements.setPrescriptionId(chargeList.get(0).getPrescriptionId());
            payStatements.setApplyId(applyId.toString());
            payStatements.setPatientId(apply.getPatientId());
            payStatements.setFee(new BigDecimal(chargeFee));
            payStatements.setCreateAt(LocalDateTime.now().toString());
            payStatements.setCreateBy(userInfo.getId().toString());
            payManager.addPayStatements(payStatements);
        }

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
        example = new Example(PrescriptionItem.class);
        example.createCriteria().andEqualTo("applyId",applyId);
        List<PrescriptionItem> list = preItemMapper.selectByExample(example);
        list.forEach(obj->{
            Drug drug = drugMapper.selectByPrimaryKey(obj.getDrugId());
            drug.setNum(drug.getNum()-obj.getNum());
            drugMapper.updateByPrimaryKey(drug);
        });
    }

    /**
     * 获取退款列表
     *
     * @param applyId
     */
    public List<Map<String,Object>> getRefundList(String applyId,Integer type) {
        return preItemMapper.getRefundList(applyId,type);
    }

    @Transactional
    public void refund(String applyId, String groupNum) {
        preItemMapper.refund(applyId,groupNum.split(","));
        inspectMapper.refund(applyId,groupNum.split(","));
        chargeMapper.refund(applyId,groupNum.split(","));
    }
}
