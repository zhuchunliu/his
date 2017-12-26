package com.acmed.his.service;

import com.acmed.his.dao.AccompanyingOrderConfirmationMapper;
import com.acmed.his.model.AccompanyingOrderConfirmation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

/**
 * AccompanyingOrderConfirmationManager
 *
 * @author jimson
 * @date 2017/12/22
 */
@Service
@Transactional
public class AccompanyingOrderConfirmationManager {
    @Autowired
    private AccompanyingOrderConfirmationMapper accompanyingOrderConfirmationMapper;

    /**
     * 保存
     * @param param
     * @return
     */
    public int save(AccompanyingOrderConfirmation param){
        String orderCode = param.getOrderCode();
        AccompanyingOrderConfirmation accompanyingOrderConfirmation = accompanyingOrderConfirmationMapper.selectByPrimaryKey(orderCode);
        if (accompanyingOrderConfirmation==null){
            return accompanyingOrderConfirmationMapper.insert(param);
        }else {
            return accompanyingOrderConfirmationMapper.updateByPrimaryKeySelective(param);
        }
    }

    /**
     * 批量查找
     * @param orderCodes
     * @return
     */
    public List<AccompanyingOrderConfirmation> getByOrderCodes(List<String> orderCodes){
        Example example = new Example(AccompanyingOrderConfirmation.class);
        example.createCriteria().andIn("orderCode",orderCodes);
        return accompanyingOrderConfirmationMapper.selectByExample(example);
    }

    public AccompanyingOrderConfirmation getByOrderCode(String orderCode){
        return accompanyingOrderConfirmationMapper.selectByPrimaryKey(orderCode);
    }
}
