package com.acmed.his.service;

import com.acmed.his.constants.StatusCode;
import com.acmed.his.dao.ApplyMapper;
import com.acmed.his.model.*;
import com.acmed.his.model.dto.ApplyDoctorDto;
import com.acmed.his.model.dto.ChuZhenFuZhenCountDto;
import com.acmed.his.model.dto.DispensingDto;
import com.acmed.his.pojo.mo.ApplyMo;
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
import java.util.List;
import java.util.Map;

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
    private CommonManager commonManager;

    @Autowired
    private PayManager payManager;

    @Autowired
    private UserManager userManager;

    @Autowired
    private WxManager wxManager;

    private static Logger logger = Logger.getLogger(ApplyManager.class);

    /**
     * 添加挂号
     * @param mo 属性
     * @param patientId 患者主键
     * @return 0失败  1 成功
     */
    @Transactional
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
    }


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
    public PageResult<ApplyDoctorDto> getByPinyinOrNameOrClinicnoTiaojianByPage(Integer orgCode, Integer dept, String startTime, String endTime, String status, String param, String isPaid, Integer pageNum, Integer pageSize){
        if (StringUtils.isNotEmpty(startTime)){
            startTime = DateTimeUtil.parsetLocalDateStart(startTime).toString();
        }
        if (StringUtils.isNotEmpty(endTime)){
            endTime = DateTimeUtil.parsetLocalDateEnd(endTime).toString();
        }
        PageHelper.startPage(pageNum,pageSize);
        List<ApplyDoctorDto> byPinyinOrNameOrClinicnoTiaojian = applyMapper.getByPinyinOrNameOrClinicnoTiaojian( orgCode,  dept,  startTime,  endTime,  status,  param,  isPaid);
        PageInfo<ApplyDoctorDto> applyDoctorDtoPageInfo = new PageInfo<>(byPinyinOrNameOrClinicnoTiaojian);
        PageResult<ApplyDoctorDto> applyDoctorDtoPageResult = new PageResult<>();
        applyDoctorDtoPageResult.setPageSize(pageSize);
        applyDoctorDtoPageResult.setPageNum(pageNum);
        applyDoctorDtoPageResult.setTotal(applyDoctorDtoPageInfo.getTotal());
        applyDoctorDtoPageResult.setData(byPinyinOrNameOrClinicnoTiaojian);
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


}
