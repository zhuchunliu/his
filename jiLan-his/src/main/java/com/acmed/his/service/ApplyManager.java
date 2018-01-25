package com.acmed.his.service;

import com.acmed.his.constants.StatusCode;
import com.acmed.his.dao.ApplyMapper;
import com.acmed.his.model.*;
import com.acmed.his.model.dto.ApplyDoctorDto;
import com.acmed.his.model.dto.ChuZhenFuZhenCountDto;
import com.acmed.his.pojo.mo.ApplyMo;
import com.acmed.his.pojo.vo.ApplyDoctorVo;
import com.acmed.his.pojo.vo.UserInfo;
import com.acmed.his.util.*;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

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
    private PatientManager patientManager;
    @Autowired
    private PatientCardManager patientCardManager;
    @Autowired
    private PatientItemManager patientItemManager;

    @Autowired
    private PayManager payManager;

    @Autowired
    private CommonManager commonManager;

    @Autowired
    private UserManager userManager;

    @Autowired
    private WxManager wxManager;

    private static Logger logger = Logger.getLogger(ApplyManager.class);

/*    *//**
     * 添加挂号
     * @param mo 属性
     * @param patientId 患者主键
     * @return 0失败  1 成功
     *//*
    public int addApply(ApplyMo mo, String patientId){
        Integer doctorId = mo.getDoctorId();

        User userDetail = userManager.getUserDetail(doctorId);
        if (userDetail == null){
            return 0;
        }
        Integer dept = userDetail.getDept();
        if (dept == null){
            return 0;
        }
        Apply apply = new Apply();
        BeanUtils.copyProperties(mo,apply);
        Apply p1 = new Apply();
        apply.setPatientId(patientId);
        apply.setOrgCode(userDetail.getOrgCode());
        List<Apply> select = applyMapper.select(p1);
        if (select.size() == 0){
            // 初诊
            apply.setIsFirst(1);
        }else {
            // 复诊
            apply.setIsFirst(0);
        }
        apply.setPatientId(patientId);
        apply.setCreateBy(patientId);
        apply.setCreateAt(LocalDateTime.now().toString());
        apply.setStatus("0");
        apply.setIsPaid("0");
        apply.setPatientName(mo.getPatientName());
        apply.setPinYin(PinYinUtil.getPinYinHeadChar(mo.getPatientName()));
        apply.setFee(userDetail.getApplyfee());
        apply.setOrgName(userDetail.getOrgName());
        apply.setDeptName(userDetail.getDeptName());
        apply.setDept(userDetail.getDept());
        apply.setId(UUIDUtil.generate());
        // 医疗机构编码
        Integer orgCode = apply.getOrgCode();
        // 根据医疗机构id 和 时间查询数字 +1 就是现在的就诊号
        // TODO 过期时间
        String formatVal = commonManager.getFormatVal(orgCode + "applyCode", "000000000");
        apply.setClinicNo(formatVal);
        // 插入
        applyMapper.insert(apply);

        Patient patient = patientManager.getPatientById(patientId);
        if(null != patient && StringUtils.isEmpty(patient.getModifyAt())){//第一次挂号的时候，将挂号页面数据带入个人信息表中

            patient.setUserName(apply.getPatientName());
            patient.setGender(apply.getGender());
            patient.setMobile(mo.getMobile());
            patient.setIdCard(mo.getIdcard());
            patient.setSocialCard(mo.getSocialCard());
            patient.setModifyAt(LocalDateTime.now().toString());
            patient.setModifyBy(patientId.toString());
            patient.setInputCode(PinYinUtil.getPinYinHeadChar(patient.getUserName()));
            patientManager.update(patient);
        }

        return 1;
    }*/


    /**
     * 根据患者id查找他的挂号列表
     * @param patientId 患者id
     * @return List<Apply> 挂号列表
     */
    public List<Apply> getApplyByPatientId(String patientId){
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
    public Apply getApplyById(String id){
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
     * 根据主键修改
     * @param apply 参数
     * @return 0失败 参数
     */
    public int updateApply(Apply apply){
        apply.setModifyAt(LocalDateTime.now().toString());
        return applyMapper.updateByPrimaryKeySelective(apply);
    }

    /**
     * 收款
     * @param applyId
     * @param fee
     * @param feeType
     * @param userInfo
     * @return
     */
    public ResponseResult pay(String applyId, Double fee, String feeType, UserInfo userInfo){
        Apply apply = applyMapper.selectByPrimaryKey(applyId);
        if (apply!=null){
            if ("0".equals(apply.getIsPaid())){
                apply.setIsPaid("1");
                apply.setFeeType(feeType);
                apply.setClinicNo(commonManager.getFormatVal(userInfo.getOrgCode() + "applyCode", "000000000"));
                apply.setModifyBy(userInfo.getId().toString());
                apply.setModifyAt(LocalDateTime.now().toString());
                applyMapper.updateByPrimaryKey(apply);
                PayStatements payStatements = new PayStatements();
                payStatements.setFee(BigDecimal.valueOf(fee));
                payStatements.setFeeType(feeType);
                payStatements.setSource("1");
                payStatements.setCreateAt(LocalDateTime.now().toString());
                payStatements.setCreateBy(userInfo.getId().toString());
                payStatements.setOrgCode(userInfo.getOrgCode());
                payStatements.setApplyId(apply.getId());
                payStatements.setPatientId(apply.getPatientId());
                payStatements.setPatientId(apply.getPatientId());
                int i = payManager.addPayStatements(payStatements);
                if (i==1){
                    return ResponseUtil.setSuccessResult();
                }
                logger.error("收款失败--applyId:"+applyId);
                return ResponseUtil.setErrorMeg(StatusCode.ERROR_COLLECTION,"收款失败请联系管理员");
            }else {
                return ResponseUtil.setErrorMeg(StatusCode.ERROR_IS_PAY,"挂号费已收取，请不要重复收费");
            }
        }else {
            logger.error("收款端传入非正常数据 操作用户为"+userInfo.toString());
            return ResponseUtil.setErrorMeg(StatusCode.ERROR_ORDER,"挂号单号不存在");
        }
    }


    /**
     * 获取指定机构的就诊量
     * @param orgCode
     * @return
     */
    public Integer getApplyNum(Integer orgCode) {
        return applyMapper.getApplyNum(orgCode);
    }

    /**
     * 获取某机构的初诊数和复诊数
     * @param orgCode 机构编码
     * @return ChuZhenFuZhenCountDto
     */
    public ChuZhenFuZhenCountDto chuZhenAndFuZhenTongJi(Integer orgCode){
        return applyMapper.chuZhenAndFuZhenTongJi(orgCode);
    }

    /**
     * 条件模糊查询
     * @param orgCode
     * @param dept
     * @param startTime 2018-01-01
     * @param endTime 2018-01-01
     * @param status
     * @param param
     * @param isPaid
     * @param pageNum
     * @param pageSize
     * @return
     */
    public PageResult<ApplyDoctorVo> getByPinyinOrNameOrClinicnoTiaojianByPage(Integer orgCode, Integer dept, String startTime, String endTime, String status, String param, String isPaid, Integer pageNum, Integer pageSize){
        if (StringUtils.isNotEmpty(startTime)){
            startTime = DateTimeUtil.parsetLocalDateStart(startTime).toString();
        }
        if (StringUtils.isNotEmpty(endTime)){
            endTime = DateTimeUtil.parsetLocalDateEnd(endTime).toString();
        }
        PageHelper.startPage(pageNum,pageSize);
        List<ApplyDoctorDto> byPinyinOrNameOrClinicnoTiaojian = applyMapper.getByPinyinOrNameOrClinicnoTiaojian( orgCode,  dept,  startTime,  endTime,  status,  param,  isPaid);
        PageInfo<ApplyDoctorDto> applyDoctorDtoPageInfo = new PageInfo<>(byPinyinOrNameOrClinicnoTiaojian);
        PageResult<ApplyDoctorVo> applyDoctorDtoPageResult = new PageResult<>();
        applyDoctorDtoPageResult.setPageSize(pageSize);
        applyDoctorDtoPageResult.setPageNum(pageNum);
        applyDoctorDtoPageResult.setTotal(applyDoctorDtoPageInfo.getTotal());
        List<ApplyDoctorVo> list = new ArrayList<>();
        if (byPinyinOrNameOrClinicnoTiaojian.size()!=0){
            for (ApplyDoctorDto applyDoctorDto :byPinyinOrNameOrClinicnoTiaojian){
                ApplyDoctorVo applyDoctorVo = new ApplyDoctorVo();
                BeanUtils.copyProperties(applyDoctorDto,applyDoctorVo);
                list.add(applyDoctorVo);
            }
        }
        applyDoctorDtoPageResult.setData(list);
        return applyDoctorDtoPageResult;
    }

    public ResponseResult refund(String applyId, Double fee, String feeType, UserInfo userInfo){
        Apply apply = applyMapper.selectByPrimaryKey(applyId);
        String patientId = apply.getPatientId();
        if (apply!=null){
            if(StringUtils.equals("1",apply.getIsPaid())){
                Integer orgCode = userInfo.getOrgCode();
                Integer userId = userInfo.getId();
                PayRefuse payRefuse = new PayRefuse();
                payRefuse.setOrgCode(orgCode);
                payRefuse.setApplyId(applyId);
                payRefuse.setSource("1");
                List<PayRefuse> byPayRefuse = payManager.getByPayRefuse(payRefuse);
                if (byPayRefuse.size() ==0){
                    // 还没退款
                    if (StringUtils.equals("1",feeType)){
                        // 微信退款
                        PayStatements payStatements = new PayStatements();
                        payStatements.setSource("1");
                        payStatements.setApplyId(applyId);
                        List<PayStatements> byPayStatements = payManager.getByPayStatements(payStatements);
                        if (byPayStatements.size()!=0){
                            PayStatements payStatements1 = byPayStatements.get(0);
                            if (payStatements1.getFeeType().equals("1")){
                                String refunffee = new BigDecimal(apply.getFee()).multiply(new BigDecimal(100)).toString();
                                Map<String, String> refund = null;
                                try {
                                    refund = wxManager.refund(payStatements1.getId(), refunffee, "退款", refunffee);
                                } catch (Exception e) {
                                    logger.error("微信退款异常"+e.toString());
                                    e.printStackTrace();
                                    return ResponseUtil.setErrorMeg(StatusCode.ERROR_REFUND_ERR,"退款失败");
                                }
                                String returnCode = refund.get("return_code");
                                if (StringUtils.equals("SUCCESS",returnCode)){
                                    // 退款成功
                                    //商户退款单号
                                    String outRefundNo = refund.get("out_refund_no");
                                    // 微信退款单号
                                    String refundId = refund.get("refund_id");
                                    // 之前付款时候的单号 也就是支付表id
                                    String out_trade_no = refund.get("out_trade_no");

                                    // 之前付款时候的微信单号
                                    String transaction_id = refund.get("transaction_id");

                                    payRefuse.setPayId(transaction_id);
                                    payRefuse.setRefuseId(refundId);
                                    payRefuse.setId(outRefundNo);


                                    payRefuse.setFeeType(feeType);
                                    payRefuse.setFee(new BigDecimal(fee));
                                    payRefuse.setCreateBy(userId.toString());
                                    payRefuse.setPatientId(patientId);
                                    payManager.addPayRefuse(payRefuse);
                                    return ResponseUtil.setSuccessResult();
                                }else {
                                    return ResponseUtil.setErrorMeg(StatusCode.ERROR_REFUND_ERR,"退款失败");
                                }
                            }
                        }

                    }
                    payRefuse.setFeeType(feeType);
                    payRefuse.setFee(new BigDecimal(fee));
                    payRefuse.setCreateBy(userId.toString());
                    payRefuse.setPatientId(patientId);
                    payManager.addPayRefuse(payRefuse);
                    return ResponseUtil.setSuccessResult();
                }else {
                    // 已经退款
                    return ResponseUtil.setErrorMeg(StatusCode.ERROR_IS_REFUND,"请不要重复退款");
                }
            }else if(StringUtils.equals("0",apply.getIsPaid())){
                return ResponseUtil.setErrorMeg(StatusCode.FAIL,"还未支付");
            }else {
                return ResponseUtil.setErrorMeg(StatusCode.FAIL,"程序出错，请联系管理员！");
            }
        }else {
            logger.error("收款端传入非正常数据 操作用户为"+userInfo.toString());
            return ResponseUtil.setErrorMeg(StatusCode.FAIL,"挂号单号不存在");
        }
    }


    public ResponseResult<String> addApply(ApplyMo mo,String patientId,UserInfo userInfo){
        String patientCardId = mo.getPatientCardId();
        if (StringUtils.isNotEmpty(patientCardId)){
            PatientCard patientCard = patientCardManager.patientCardDetail(patientCardId);
            if (patientCard!=null){
                mo.setSocialCard(patientCard.getSocialCard());
                mo.setIdcard(patientCard.getIdCard());
                mo.setPatientName(patientCard.getPatientName());
                mo.setMobile(patientCard.getMobile());
                mo.setGender(patientCard.getGender());
            }
        }else {
            if (StringUtils.isNotEmpty(patientId)){
                PatientCard patientCard = new PatientCard();
                patientCard.setIdCard(mo.getIdcard());
                patientCard.setCreateBy(patientId);
                List<PatientCard> patientCardList = patientCardManager.getPatientCardList(patientCard);
                if (patientCardList.size() == 0){
                    patientCard.setGender(mo.getGender());
                    patientCard.setSocialCard(mo.getSocialCard());
                    patientCard.setMobile(mo.getMobile());
                    patientCard.setRelation(mo.getRelation());
                    patientCard.setPatientName(mo.getPatientName());
                    patientCardManager.add(patientCard);
                }
            }
        }
        Integer doctorId = mo.getDoctorId();
        Integer dept = null;
        String deptName = null;
        Integer orgCode = null;
        String orgName = null;
        String doctorName = null;
        // 挂号对象
        String createBy = null;
        Double fee = null;
        if (StringUtils.isNotEmpty(patientId) && userInfo == null){
            // 患者挂号
            if (doctorId == null){
                return ResponseUtil.setErrorMeg(StatusCode.ERROR_PARAM,"参数错误");
            }
            User userDetail = userManager.getUserDetail(doctorId);
            dept = userDetail.getDept();
            deptName = userDetail.getDeptName();
            orgCode = userDetail.getOrgCode();
            orgName = userDetail.getOrgName();
            fee = userDetail.getApplyfee();
            doctorName = userDetail.getUserName();
            createBy = patientId;
        }else if (StringUtils.isEmpty(patientId) && userInfo != null){
            //医生挂号
            if (doctorId == null){
                dept = userInfo.getDept();
                deptName = userInfo.getDeptName();
                orgCode = userInfo.getOrgCode();
                orgName = userInfo.getOrgName();
                doctorId = userInfo.getId();
                doctorName = userInfo.getUserName();
                fee = userInfo.getApplyfee();
            }else {
                User userDetail = userManager.getUserDetail(doctorId);
                dept = userDetail.getDept();
                deptName = userDetail.getDeptName();
                orgCode = userDetail.getOrgCode();
                orgName = userDetail.getOrgName();
                doctorName = userDetail.getUserName();
                fee = userDetail.getApplyfee();
            }
            createBy = userInfo.getId().toString();
        }else {
            return ResponseUtil.setErrorMeg(StatusCode.ERROR_PARAM,"参数错误");
        }
        Apply apply = new Apply();
        String applyId = UUIDUtil.generate();
        apply.setId(applyId);
        apply.setDoctorName(doctorName);
        apply.setDoctorId(doctorId);
        apply.setAppointmentTime(mo.getAppointmentTime());
        Patient param = new Patient();
        param.setIdCard(mo.getIdcard());
        List<Patient> byPatient = patientManager.getByPatient(param);
        apply.setOrgCode(orgCode);
        apply.setDept(dept);
        apply.setDeptName(deptName);
        apply.setOrgName(orgName);
        apply.setCreateBy(createBy);
        if (byPatient.size() == 0){
            // 不存在主表信息
            // 创建患者主表
            BeanUtils.copyProperties(mo,param);
            param.setRealName(mo.getPatientName());
            param.setCreateBy(createBy);
            String generatePatientId = UUIDUtil.generate();
            param.setId(generatePatientId);
            int addPatient = patientManager.add(param);
            if (addPatient==0){
                return ResponseUtil.setErrorMeg(StatusCode.FAIL,"请联系管理员");
            }
            // 创建子表
            String generatePatientItemId = UUIDUtil.generate();
            PatientItem patientItem = new PatientItem();
            patientItem.setId(generatePatientItemId);
            patientItem.setAnaphylaxis(mo.getAnaphylaxis());
            patientItem.setAddress(mo.getAddress());
            patientItem.setPatientId(generatePatientId);
            patientItem.setPatientName(mo.getPatientName());
            patientItem.setGender(mo.getGender());
            patientItem.setCreateBy(createBy);
            patientItem.setOrgCode(orgCode);
            patientItem.setIdCard(mo.getIdcard());
            patientItem.setMobile(mo.getMobile());
            patientItem.setSocialCard(mo.getSocialCard());
            int i = patientItemManager.addPatinetItem(patientItem);
            if (i==0){
                return ResponseUtil.setErrorMeg(StatusCode.FAIL,"请联系管理员");
            }
            BeanUtils.copyProperties(mo,apply);
            apply.setIsFirst(1);
            apply.setPatientId(generatePatientId);
            apply.setPatientName(mo.getPatientName());
            apply.setFee(fee);
            apply.setPatientItemId(generatePatientItemId);
            int i1 = addApply(apply);
            return ResponseUtil.setSuccessResult(applyId);
        }else {
            // 存在主表信息
            Patient patient = byPatient.get(0);

            PatientItem patientItem = new PatientItem();
            patientItem.setOrgCode(orgCode);
            patientItem.setPatientId(patient.getId());
            List<PatientItem> patientItems = patientItemManager.patientItems(patientItem);
            if (patientItems.size() != 0){
                //存在子表
                PatientItem patientItem1 = patientItems.get(0);
                if (patientItem1.getBlackFlag()==1){
                    // 被拉黑
                    return ResponseUtil.setErrorMeg(StatusCode.FAIL,"您在该医院存在不良记录，具体操作请联系客服");
                }
                apply.setPatientId(patient.getId());
                apply.setPatientName(patientItem1.getPatientName());
                apply.setGender(patientItem1.getGender());
                apply.setPinYin(patientItem1.getInputCode());
                apply.setFee(fee);
                apply.setAge(patientItem1.getAge());
                apply.setIsFirst(0);
                apply.setPatientItemId(patientItem1.getId());
                int i1 = addApply(apply);
                return ResponseUtil.setSuccessResult(applyId);
            }else {
                // 不存在子表
                // 创建子表
                String generatePatientItemId = UUIDUtil.generate();
                PatientItem patientItem1 = new PatientItem();
                patientItem1.setId(generatePatientItemId);
                patientItem1.setPatientId(patient.getId());
                patientItem1.setPatientName(mo.getPatientName());
                patientItem1.setGender(mo.getGender());
                patientItem1.setCreateBy(createBy);
                patientItem1.setOrgCode(orgCode);
                patientItem1.setIdCard(mo.getIdcard());
                patientItem1.setMobile(mo.getMobile());
                patientItem1.setSocialCard(mo.getSocialCard());
                patientItem1.setAnaphylaxis(mo.getAnaphylaxis());
                patientItem1.setAddress(mo.getAddress());
                int i = patientItemManager.addPatinetItem(patientItem1);
                apply.setPatientId(patient.getId());
                apply.setPatientName(patientItem1.getPatientName());
                apply.setGender(patientItem1.getGender());
                apply.setPinYin(patientItem1.getInputCode());
                apply.setFee(fee);
                apply.setAge(IdCardUtil.idCardToAge(mo.getIdcard()));
                apply.setIsFirst(0);
                apply.setPatientItemId(generatePatientItemId);
                int i1 = addApply(apply);
                return ResponseUtil.setSuccessResult(applyId);
            }
        }
    }


    public List<Apply> getApplys(Apply apply){
        return applyMapper.select(apply);
    }

    public PageResult<Apply> getApplysByPage(PageBase<Apply> pageBase){
        Integer pageNum = pageBase.getPageNum();
        Integer pageSize = pageBase.getPageSize();
        PageResult<Apply> result = new PageResult<>();
        PageHelper.startPage(pageNum,pageSize);
        List<Apply> select = applyMapper.select(pageBase.getParam());
        PageInfo<Apply> applyPageInfo = new PageInfo<>(select);
        result.setTotal(applyPageInfo.getTotal());
        result.setData(select);
        result.setPageNum(pageNum);
        result.setPageSize(pageSize);

        return result;
    }


    private int addApply(Apply apply){
        apply.setId(Optional.ofNullable(apply.getId()).orElse(UUIDUtil.generate()));
        apply.setCreateAt(LocalDateTime.now().toString());
        // 挂号单号在付款完成后生成
        //String formatVal = commonManager.getFormatVal(apply.getOrgCode() + "applyCode", "000000000");
        apply.setClinicNo(null);
        apply.setIsPaid("0");
        apply.setStatus("0");
        apply.setModifyBy(null);
        apply.setModifyAt(null);
        if (StringUtils.isNotEmpty(apply.getPatientName())){
            apply.setPinYin(PinYinUtil.getPinYinHeadChar(apply.getPatientName()));
        }
        return applyMapper.insert(apply);
    }
}
