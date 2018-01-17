package com.acmed.his.service;

import com.acmed.his.constants.StatusCode;
import com.acmed.his.dao.PatientItemMapper;
import com.acmed.his.dao.PatientMapper;
import com.acmed.his.model.Patient;
import com.acmed.his.model.PatientItem;
import com.acmed.his.model.dto.OrgPatientNumDto;
import com.acmed.his.model.dto.PatientCountDto;
import com.acmed.his.pojo.mo.WxRegistPatientMo;
import com.acmed.his.pojo.vo.PatientInfoVo;
import com.acmed.his.util.*;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.soecode.wxtools.exception.WxErrorException;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * PatientManager
 *
 * @author jimson
 * @date 2017/11/20
 */
@Service
@Transactional
public class PatientManager {

    @Autowired
    private PatientMapper patientMapper;

    @Autowired
    private WxManager wxManager;

    @Autowired
    private PatientItemMapper patientItemMapper;


    /**
     * 添加子表
     * @param patientItem
     * @return
     */
    public int addPatinetItem(PatientItem patientItem){
        patientItem.setId(UUIDUtil.generate());
        patientItem.setBlackFlag(0);
        patientItem.setCreateAt(LocalDate.now().toString());
        return patientItemMapper.insert(patientItem);
    }

    /**
     * 第三方添加患者信息
     * @param patient 参数
     * @return 0 失败  1 成功
     */
    public int add(Patient patient){
        // 查询身份证是否已经注册过
        if(!StringUtils.isEmpty(patient.getIdCard())) {
            Patient param = new Patient();
            param.setIdCard(patient.getIdCard());
            List<Patient> select = patientMapper.select(param);
            if (select.size() != 0) {
                // 表示已经注册过
                return 0;
            }
        }
        String now = LocalDateTime.now().toString();
        patient.setInputCode(PinYinUtil.getPinYinHeadChar(patient.getUserName()));
        patient.setId(null);
        patient.setOpenid(null);
        patient.setUnionid(null);
        patient.setCreateAt(now);
        patient.setModifyAt(now);
        patient.setId(Optional.ofNullable(patient.getId()).orElse(UUIDUtil.generate()));//新开就诊时，用户id由开诊接口创建
        return patientMapper.insert(patient);
    }

    public int update(Patient patient){
        return patientMapper.updateByPrimaryKeySelective(patient);
    }

    /**
     * 根据id查询病患信息
     * @param id 患者id
     * @return Patient
     */
    public Patient getPatientById(String id){
        return patientMapper.selectByPrimaryKey(id);
    }

    /**
     * 根据openid 查询患者详情
     * @param openid 微信openid
     * @return Patient
     */
    public Patient getPatientByOpenid(String openid){
        Patient param = new Patient();
        param.setOpenid(openid);
        return patientMapper.selectOne(param);
    }

    /**
     * 根据身份证号查询患者详情
     * @param idCard 身份证号码
     * @return Patient
     */
    public Patient getPatientByIdCard(String idCard){
        Patient param = new Patient();
        param.setIdCard(idCard);
        return patientMapper.selectOne(param);
    }

    /**
     * 根据患者姓名查询患者list（可能存在同名同姓）
     * @param userName 患者姓名
     * @return List<Patient>
     */
    public List<Patient> getPatientByUserName(String userName){
        Patient param = new Patient();
        param.setUserName(userName);
        return patientMapper.select(param);
    }

    /**
     * 用户注册绑定身份信息
     * @param wxRegistPatientMo 注册参数
     * @return ResponseResult
     */
    public ResponseResult<PatientInfoVo> wxRegistPatient(WxRegistPatientMo wxRegistPatientMo){
        //获取openid
        String openid = null;
        try {
            openid = wxManager.getOPenidByCode(wxRegistPatientMo.getCode());
        } catch (WxErrorException e) {
            e.printStackTrace();
            return ResponseUtil.setErrorMeg(StatusCode.ERROR_GETOPENIDECORD,"获取openid异常");
        }
        String idCard = wxRegistPatientMo.getIdCard();
        String now = LocalDateTime.now().toString();
        PatientInfoVo patientInfoVo = new PatientInfoVo();
        Example example = new Example(Patient.class);
        example.createCriteria().orEqualTo("openId",openid).orEqualTo("idCard",idCard);
        List<Patient> select = patientMapper.selectByExample(example);
        int size = select.size();
        if ( size == 0){
            // 之前没有任何信息记录
            Patient patient = new Patient();
            BeanUtils.copyProperties(wxRegistPatientMo,patient);
            patient.setId(null);
            patient.setOpenid(openid);
            patient.setInputCode(patient.getUserName());
            patient.setUnionid(null);
            patient.setCreateAt(now);
            patient.setModifyAt(now);
            patientMapper.insert(patient);
            BeanUtils.copyProperties(patient,patientInfoVo);
        }else if (size==1){
            // 有过一条记录
            Patient patient = select.get(0);
            String openid1 = patient.getOpenid();
            if (openid1 == null){
                // 信息已录入 没有绑定微信号
                patient.setOpenid(openid);
                patient.setModifyAt(now);
                patientMapper.updateByPrimaryKeySelective(patient);
            }
            BeanUtils.copyProperties(patient,patientInfoVo);
        }else {
            // 已经绑定过
            for (Patient p : select){
                if (StringUtils.equals(p.getOpenid(),openid)){
                    BeanUtils.copyProperties(p,patientInfoVo);
                }
            }
        }
        // TODO 跟登录返回一致
        return ResponseUtil.setSuccessResult(patientInfoVo);
    }

    /**
     * 根据患者姓名拼音查询患者信息列表
     * @param pinYin 拼音
     * @return 患者列表
     */
    public List<Patient> getPatientLikePinYin(String pinYin){
        Example example = new Example(Patient.class);
        example.createCriteria().andLike("inputCode","%"+pinYin+"%");
        return patientMapper.selectByExample(example);
    }

    /**
     * 查询机构患者统计
     * @param orgCode 机构编号
     * @return List<PatientCountDto>
     */
    public List<PatientCountDto> getPatientCount(Integer orgCode){
        return patientMapper.getPatientCount(orgCode);
    }

    /**
     * 机构当日和总的患者数
     * @param orgCode 机构码
     * @param date 日期
     * @return OrgPatientNumDto
     */
    public OrgPatientNumDto getDayNumAnTotalNum(Integer orgCode,  String date){
        return patientMapper.getDayNumAnTotalNum(orgCode,date);
    }

    /**
     * 查询患者库
     * @param orgCode 机构编码
     * @param pageNum 页码
     * @param pageSize 每页条数
     * @param blackFlag 拉黑标记  0 未拉黑 1 已拉黑
     * @return PageResult<PatientItem>
     */
    public PageResult<PatientItem> getPatientBlacklistByPage(Integer orgCode, Integer pageNum, Integer pageSize,Integer blackFlag){
        PatientItem patientItem = new PatientItem();
        patientItem.setOrgCode(orgCode);
        patientItem.setBlackFlag(1);
        PageHelper.startPage(pageNum,pageSize);
        List<PatientItem> select = patientItemMapper.select(patientItem);
        PageInfo<PatientItem> patientPageInfo = new PageInfo<>(select);
        PageResult<PatientItem> pageResult = new PageResult<>();
        pageResult.setData(select);
        pageResult.setTotal(patientPageInfo.getTotal());
        pageResult.setPageNum(pageNum);
        pageResult.setPageSize(pageSize);
        return pageResult;
    }

    /**
     * 患者库黑名单操作
     * @param orgCode 机构code
     * @param userId 用户id
     * @param id 患者库id
     * @param blackFlag 删除标记
     * @return 0失败 1成功
     */
    public int updatePatientItemBlackFlag(Integer orgCode,Integer userId,String id,Integer blackFlag){
        PatientItem patientItem = new PatientItem();
        patientItem.setId(id);
        patientItem.setOrgCode(orgCode);
        PatientItem patientItem1 = patientItemMapper.selectOne(patientItem);
        if (patientItem1!=null){
            patientItem.setBlackFlag(blackFlag);
            patientItem.setModifyAt(LocalDate.now().toString());
            patientItem.setModifyBy(userId.toString());
            return patientItemMapper.updateByPrimaryKeySelective(patientItem);
        }else {
            return 0;
        }

    }

}
