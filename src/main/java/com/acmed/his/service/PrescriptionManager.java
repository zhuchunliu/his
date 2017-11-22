package com.acmed.his.service;

import com.acmed.his.dao.*;
import com.acmed.his.model.*;
import com.acmed.his.model.dto.PreTitleDto;
import com.acmed.his.pojo.mo.PreInspectMo;
import com.acmed.his.pojo.mo.PreMedicineMo;
import com.acmed.his.pojo.vo.PreInspectVo;
import com.acmed.his.pojo.vo.PreMedicineVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Created by Darren on 2017-11-22
 **/
@Service
public class PrescriptionManager {

    @Autowired
    private PrescriptionMapper preMapper;

    @Autowired
    private PrescriptionItemMapper preItemMapper;

    @Autowired
    private InspectMapper inspectMapper;

    @Autowired
    private ChargeMapper chargeMapper;

    @Autowired
    private ApplyMapper applyMapper;

    @Autowired
    private DrugMapper drugMapper;

    @Autowired
    private FeeItemMapper feeItemMapper;

    @Autowired
    private CommonManager commonManager;

    /**
     * 开药品处方
     * @param mo
     */
    @Transactional
    public void savePreMedicine(PreMedicineMo mo) {
        Apply apply = applyMapper.selectByPrimaryKey(mo.getApplyId());
        Prescription prescription = null;
        if(null == mo.getId()){
            prescription = new Prescription();
            prescription.setDeptName(apply.getDeptName());
            prescription.setDept(apply.getDept().toString());
            prescription.setPatientId(apply.getPatientId());
            prescription.setOrgCode(apply.getOrgCode());
            prescription.setApplyId(mo.getApplyId());
            prescription.setCategory("1");
            prescription.setPrescriptionNo(apply.getId()+commonManager.getNextVal("Org"+apply.getId()));
            prescription.setCreateAt(LocalDateTime.now().toString());
            preMapper.insert(prescription);

            prescription = preMapper.getByNo(prescription.getPrescriptionNo());
        }else{
            prescription = preMapper.selectByPrimaryKey(mo.getId());
            prescription.setModifyAt(LocalDateTime.now().toString());
            preItemMapper.delByPreId(prescription.getId());
            chargeMapper.delByPreId(prescription.getId());
        }

        if(null != mo.getItemList()) {
            for (PreMedicineMo.Item info : mo.getItemList()) {
                Drug drug = drugMapper.selectByPrimaryKey(info.getDrugId());
                PrescriptionItem item = new PrescriptionItem();
                BeanUtils.copyProperties(drug, item,"id");
                BeanUtils.copyProperties(info,item,"id");
                item.setDrugName(drug.getName());
                item.setPrescriptionId(prescription.getId());
                item.setApplyId(mo.getApplyId());
                item.setDrugId(info.getDrugId());
                preItemMapper.insert(item);
            }
        }

        if(null != mo.getChargeList()) {
            for (PreMedicineMo.Charge info : mo.getChargeList()) {
                Charge charge = new Charge();
                BeanUtils.copyProperties(info, charge);
                charge.setApplyId(mo.getApplyId());
                charge.setPrescriptionId(prescription.getId());
                chargeMapper.insert(charge);
            }
        }

        // TODO : 费用暂时没做

    }



    @Transactional
    public void savePreInspect(PreInspectMo mo) {
        Apply apply = applyMapper.selectByPrimaryKey(mo.getApplyId());
        Prescription prescription = null;
        if(null == mo.getId()){
            prescription = new Prescription();
            prescription.setDeptName(apply.getDeptName());
            prescription.setDept(apply.getDept().toString());
            prescription.setPatientId(apply.getPatientId());
            prescription.setOrgCode(apply.getOrgCode());
            prescription.setApplyId(mo.getApplyId());
            prescription.setCategory("2");
            prescription.setPrescriptionNo(apply.getId()+commonManager.getNextVal("Org"+apply.getId()));
            prescription.setCreateAt(LocalDateTime.now().toString());
            preMapper.insert(prescription);

            prescription = preMapper.getByNo(prescription.getPrescriptionNo());
        }else{
            prescription = preMapper.selectByPrimaryKey(mo.getId());
            prescription.setModifyAt(LocalDateTime.now().toString());
            inspectMapper.delByPreId(prescription.getId());
            chargeMapper.delByPreId(prescription.getId());
        }

        if(null != mo.getInspectList()) {
            for (PreInspectMo.Inspect info : mo.getInspectList()) {
                Inspect inspect = new Inspect();
                BeanUtils.copyProperties(info, inspect);
                inspect.setPrescriptionId(prescription.getId());
                inspect.setApplyId(mo.getApplyId());
                inspectMapper.insert(inspect);
            }
        }

        if(null != mo.getChargeList()) {
            for (PreInspectMo.Charge info : mo.getChargeList()) {
                Charge charge = new Charge();
                BeanUtils.copyProperties(info, charge);
                charge.setApplyId(mo.getApplyId());
                charge.setPrescriptionId(prescription.getId());
                chargeMapper.insert(charge);
            }
        }

        // TODO : 费用暂时没做
    }

    public List<PreTitleDto> getPreByApply(Integer applyId) {
        return preMapper.getPreByApply(applyId);
    }

    /**
     * 获取用药处方
     * @param id
     * @return
     */
    public PreMedicineVo getPreMedicine(Integer id) {

        Example example = new Example(Charge.class);
        example.createCriteria().andEqualTo("prescriptionId",id);
        List<Charge> chargeList = chargeMapper.selectByExample(example);

        example = new Example(PrescriptionItem.class);
        example.createCriteria().andEqualTo("prescriptionId",id);
        List<PrescriptionItem> preItemList = preItemMapper.selectByExample(example);

        Prescription prescription = preMapper.selectByPrimaryKey(id);
        return new PreMedicineVo(prescription,preItemList,chargeList);
    }

    /**
     * 获取检查处方
     * @param id
     * @return
     */
    public PreInspectVo getPreInspect(Integer id) {
        Example example = new Example(Charge.class);
        example.createCriteria().andEqualTo("prescriptionId",id);
        List<Charge> chargeList = chargeMapper.selectByExample(example);

        example = new Example(Inspect.class);
        example.createCriteria().andEqualTo("prescriptionId",id);
        List<Inspect> preItemList = inspectMapper.selectByExample(example);

        Prescription prescription = preMapper.selectByPrimaryKey(id);
        return new PreInspectVo(prescription,preItemList,chargeList);
    }
}
