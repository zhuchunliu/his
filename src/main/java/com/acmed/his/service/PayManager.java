package com.acmed.his.service;

import com.acmed.his.dao.PayRefuseMapper;
import com.acmed.his.dao.PayStatementsMapper;
import com.acmed.his.model.PayRefuse;
import com.acmed.his.model.PayStatements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Date;

/**
 * PayManager
 *
 * @author jimson
 * @date 2017/11/24
 */
@Service
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
     * 添加退款流水
     * @param payRefuse 参数
     * @return 0失败  1成功
     */
    public int addPayRefuse(PayRefuse payRefuse){
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
}
