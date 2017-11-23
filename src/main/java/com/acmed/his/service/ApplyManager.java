package com.acmed.his.service;

import com.acmed.his.dao.ApplyMapper;
import com.acmed.his.model.Apply;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

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

    /**
     * 添加挂号
     * @param apply 属性
     * @return 0失败  1 成功
     */
    public int addApply(Apply apply){
        apply.setId(null);
        Date now = new Date();
        System.err.println(DateFormatUtils.format(now, "yyyy-MM-dd hh:mm:ss"));
        apply.setCreateAt(now);
        apply.setModifyAt(now);
        // 0表示未支付
        apply.setIsPaid("0");
        // 医疗机构编码
        Integer orgCode = apply.getOrgCode();
        // 根据医疗机构id 和 时间查询数字 +1 就是现在的就诊号
        // TODO 设置挂号单号 过期时间
        // 插入
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
        example.setOrderByClause("createAt desc");
        example.createCriteria().andEqualTo("orgCode",orgCode);
        return applyMapper.selectByExample(example);
    }

    /**
     * 根据科室id 获取挂号列表
     * @param deptId 科室id
     * @return 当前科室的挂号列表
     */
    public List<Apply> getApplyByDeptIdAndStatus(Integer deptId,String status){
        Date now = new Date();
        Example example = new Example(Apply.class);
        example.setOrderByClause("createAt desc");
        if (status == null){
            example.createCriteria().andEqualTo("dept",deptId).andEqualTo("isPaid",1).andBetween("createAt", DateFormatUtils.format(now, "yyyy-MM-dd 00:00:00"),DateFormatUtils.format(now, "yyyy-MM-dd 23:59:59"));
        }else {
            example.createCriteria().andEqualTo("dept",deptId).andEqualTo("isPaid",1).andBetween("createAt", DateFormatUtils.format(now, "yyyy-MM-dd 00:00:00"),DateFormatUtils.format(now, "yyyy-MM-dd 23:59:59")).andEqualTo("status",status);
        }

        return applyMapper.selectByExample(example);
    }

    /**
     * 根据主键修改
     * @param apply 参数
     * @return 0失败 参数
     */
    public int updateApply(Apply apply){
        apply.setModifyAt(new Date());
        return applyMapper.updateByPrimaryKeySelective(apply);
    }
}
