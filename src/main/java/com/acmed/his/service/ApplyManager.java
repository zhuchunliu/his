package com.acmed.his.service;

import com.acmed.his.dao.ApplyMapper;
import com.acmed.his.dao.PatientMapper;
import com.acmed.his.model.Apply;
import com.acmed.his.model.Patient;
import com.acmed.his.model.PayStatements;
import com.acmed.his.pojo.vo.ApplyDoctorVo;
import com.acmed.his.pojo.vo.UserInfo;
import com.acmed.his.util.IdCardUtil;
import com.acmed.his.util.date.DateUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * ApplyManager
 * 挂号
 * @author jimson
 * @date 2017/11/20
 */
@Service
@Transactional
public class ApplyManager {
    @Autowired
    private ApplyMapper applyMapper;

    @Autowired
    private PatientMapper patientMapper;

    @Autowired
    private CommonManager commonManager;

    @Autowired
    private PayManager payManager;

    /**
     * 添加挂号
     * @param apply 属性
     * @return 0失败  1 成功
     */
    public int addApply(Apply apply){
        apply.setId(null);
        String now = LocalDateTime.now().toString();
        apply.setCreateAt(now);
        apply.setModifyAt(now);
        apply.setFee(1.00);
        // 0表示未支付
        apply.setIsPaid("0");
        // 未就诊
        apply.setStatus("0");
        // 医疗机构编码
        Integer orgCode = apply.getOrgCode();
        // 根据医疗机构id 和 时间查询数字 +1 就是现在的就诊号
        // TODO 过期时间
        String formatVal = commonManager.getFormatVal(orgCode + "applyCode", "000000000");
        apply.setClinicNo(formatVal);
        // 插入
        return applyMapper.insert(apply);
    }

    /**
     * 自己挂号
     * @param apply 挂号
     * @param patient 患者信息
     * @return 0失败  1 成功
     */
    public int addApplyBySlf(Apply apply,Patient patient){
        String now = LocalDateTime.now().toString();
        String idCard = patient.getIdCard();
        apply.setId(null);
        Integer orgCode = apply.getOrgCode();
        String formatVal = commonManager.getFormatVal(orgCode + "applyCode", "000000000");
        apply.setClinicNo(formatVal);
        apply.setPatientId(patient.getId());
        apply.setPatientName(patient.getUserName());
        apply.setGender(patient.getGender());
        apply.setPinYin(patient.getInputCode());
        apply.setFee(1.00);
        apply.setStatus("0");
        apply.setAge(IdCardUtil.idCardToAge(idCard));
        apply.setCreateAt(now);
        return applyMapper.insert(apply);

    }

    /**
     * 根据患者id查找他的挂号列表
     * @param patientId 患者id
     * @return List<Apply> 挂号列表
     */
    public List<Apply> getApplyByPatientId(Integer patientId){
        Example example = new Example(Apply.class);
        example.setOrderByClause("createAt desc");
        example.createCriteria().andEqualTo("patientId",patientId);
        return applyMapper.selectByExample(example);
    }

    /**
     * 根据挂号id 查询详情
     * @param id 挂号id 主键
     * @return Apply
     */
    public Apply getApplyById(Integer id){
        return applyMapper.selectByPrimaryKey(id);
    }

    /**
     * 查询某医疗机构 某段时间的挂号列表
     * @param orgCode 机构id
     * @return List<Apply>
     */
    public List<Apply> getApplyByOrgCode(Integer orgCode){
        Example example = new Example(Apply.class);
        example.createCriteria().andEqualTo("orgCode",orgCode);
        return applyMapper.selectByExample(example);
    }

    /**
     * 根据科室id 获取挂号列表
     * @param deptId 科室id
     * @return 当前科室的挂号列表
     */
    public List<Apply> getApplyByDeptIdAndStatus(Integer deptId,String status){
        String now = DateUtil.DateToString(new Date(),"yyyy-MM-dd 00:00:00");
        Example example = new Example(Apply.class);
        if (status == null){
            example.createCriteria().andEqualTo("dept",deptId).andEqualTo("isPaid",1).andBetween("createAt", DateUtil.StringToDate(now, "yyyy-MM-dd 00:00:00"),DateUtil.StringToDate(now,  "yyyy-MM-dd 23:59:59"));
        }else {
            example.createCriteria().andEqualTo("dept",deptId).andEqualTo("isPaid",1).andBetween("createAt", DateUtil.StringToDate(now, "yyyy-MM-dd 00:00:00"),DateUtil.StringToDate(now, "yyyy-MM-dd 23:59:59")).andEqualTo("status",status);
        }

        return applyMapper.selectByExample(example);
    }

    /**
     * 根据主键修改
     * @param apply 参数
     * @return 0失败 参数
     */
    public int updateApply(Apply apply){
        apply.setModifyAt(LocalDateTime.now().toString());
        return applyMapper.updateByPrimaryKeySelective(apply);
    }

    /**
     * 根据科室id 时间 状态查询
     * @param deptId 科室
     * @param date 时间
     * @param status 状态
     * @return List<ApplyDoctorVo>
     */
    public List<ApplyDoctorVo> getApplyDoctorVoList(Integer deptId,String date,String status){
        List<ApplyDoctorVo> resultList = new ArrayList<>();
        if (StringUtils.isEmpty(date)){
            date = DateUtil.DateToString(new Date(),"yyyy-MM-dd 00:00:00");
        }
        Example example = new Example(Apply.class);
        if (status == null){
            example.createCriteria().andEqualTo("dept",deptId).andBetween("createAt", DateUtil.StringToDate(date, "yyyy-MM-dd 00:00:00"),DateUtil.StringToDate(date, "yyyy-MM-dd 23:59:59"));
        }else {
            example.createCriteria().andEqualTo("dept",deptId).andBetween("createAt", DateUtil.StringToDate(date, "yyyy-MM-dd 00:00:00"),DateUtil.StringToDate(date, "yyyy-MM-dd 23:59:59")).andEqualTo("status",status);
        }
        List<Apply> applies = applyMapper.selectByExample(example);
        if (applies.size()==0){
            return resultList;
        }
        List<Integer> patientIds = new ArrayList<>();
        for (Apply a : applies){
            patientIds.add(a.getPatientId());
        }
        Example patientExample = new Example(Patient.class);
        patientExample.createCriteria().andIn("id",patientIds);
        List<Patient> patients = patientMapper.selectByExample(patientExample);
        for (Apply a : applies){
            Integer patientId = a.getPatientId();
            ApplyDoctorVo applyDoctorVo = new ApplyDoctorVo();
            BeanUtils.copyProperties(a,applyDoctorVo);
            for (Patient p : patients){
                Integer id = p.getId();
                if (patientId.equals(id)){
                    applyDoctorVo.setIdCard(p.getIdCard());
                    applyDoctorVo.setMobile(p.getMobile());
                    resultList.add(applyDoctorVo);
                }
            }
        }
        return resultList;
    }

    @Transactional
    public void pay(int applyId,  Double fee, String feeType,UserInfo userInfo){
        Apply apply = applyMapper.selectByPrimaryKey(applyId);
        apply.setIsPaid("1");
        apply.setFeeType(feeType);
        applyMapper.updateByPrimaryKey(apply);

        // TODO 付款后期完善
        PayStatements payStatements = new PayStatements();
        payStatements.setFee(BigDecimal.valueOf(fee));
        payStatements.setFeeType(feeType);
        payStatements.setCreateAt(LocalDateTime.now().toString());
        payStatements.setCreateBy(userInfo.getId().toString());
        payStatements.setApplyId(apply.getId());
        payStatements.setPatientId(apply.getPatientId());
        payManager.addPayStatements(payStatements);

    }

    public List<Apply> getApply(Integer orgCode, String name, String date, String status) {
        return applyMapper.getApply(orgCode,name,date,status);
    }

    /**
     * 根据姓名或者拼音模糊查询
     * @param param 姓名或者拼音
     * @param status 状态  不传就是全部
     * @param dept 科室
     * @return List<ApplyDoctorVo>
     */
    public List<ApplyDoctorVo> getByPinyinOrName(String param,String status,Integer dept,Date date){
        List<ApplyDoctorVo> resultList = new ArrayList<>();
        if (date==null){
            date = new Date();
        }
        List<Apply> applies = applyMapper.mohu(dept,date,status,param);
        if (applies.size()==0){
            return resultList;
        }
        List<Integer> patientIds = new ArrayList<>();
        for (Apply a : applies){
            patientIds.add(a.getPatientId());
        }
        Example patientExample = new Example(Patient.class);
        patientExample.createCriteria().andIn("id",patientIds);
        List<Patient> patients = patientMapper.selectByExample(patientExample);
        for (Apply a : applies){
            Integer patientId = a.getPatientId();
            ApplyDoctorVo applyDoctorVo = new ApplyDoctorVo();
            BeanUtils.copyProperties(a,applyDoctorVo);
            for (Patient p : patients){
                Integer id = p.getId();
                if (patientId.equals(id)){
                    applyDoctorVo.setIdCard(p.getIdCard());
                    applyDoctorVo.setMobile(p.getMobile());
                    resultList.add(applyDoctorVo);
                }
            }
        }
        return resultList;
    }
}
