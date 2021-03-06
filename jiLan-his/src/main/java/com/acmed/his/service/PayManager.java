package com.acmed.his.service;

import com.acmed.his.dao.PayRefuseMapper;
import com.acmed.his.dao.PayStatementsMapper;
import com.acmed.his.model.PayRefuse;
import com.acmed.his.model.PayStatements;
import com.acmed.his.util.UUIDUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

/**
 * PayManager
 *
 * @author jimson
 * @date 2017/11/24
 */
@Service
@Transactional
public class PayManager {
    @Autowired
    private PayStatementsMapper payStatementsMapper;

    @Autowired
    private PayRefuseMapper payRefuseMapper;

    /**
     * 添加支付流水
     * @param payStatements 参数
     * @return 0失败  1成功
     */
    public int addPayStatements(PayStatements payStatements){
        payStatements.setId(UUIDUtil.generate());
        payStatements.setCreateAt(LocalDateTime.now().toString());
        return payStatementsMapper.insert(payStatements);
    }

    /**
     * 更新
     * @param payStatements 参数
     * @return 0失败  1成功
     */
    public int updatePayStatements(PayStatements payStatements){
        payStatements.setModifyAt(LocalDateTime.now().toString());
        return payStatementsMapper.updateByPrimaryKeySelective(payStatements);
    }

    /**
     * 查询
     * @param payStatements
     * @return
     */
    public List<PayStatements> getByPayStatements(PayStatements payStatements){
        return payStatementsMapper.select(payStatements);
    }

    /**
     * 添加退款流水 传入id的时候不随机生成
     * @param payRefuse 参数
     * @return 0失败  1成功
     */
    public int addPayRefuse(PayRefuse payRefuse){
        if (StringUtils.isEmpty(payRefuse.getId())){
            payRefuse.setId(UUIDUtil.generate());
        }
        payRefuse.setCreateAt(LocalDateTime.now().toString());
        return payRefuseMapper.insert(payRefuse);
    }

    /**
     * 更新退款
     * @param payRefuse 参数
     * @return 0失败  1成功
     */
    public int updatePayRefuse(PayRefuse payRefuse){
        payRefuse.setModifyAt(LocalDateTime.now().toString());
        return payRefuseMapper.updateByPrimaryKeySelective(payRefuse);
    }

    /**
     * 查询
     * @param payRefuse
     * @return
     */
    public List<PayRefuse> getByPayRefuse(PayRefuse payRefuse){
        return payRefuseMapper.select(payRefuse);
    }
}
