package com.acmed.his.service;

import com.acmed.his.dao.AccompanyingPriceMapper;
import com.acmed.his.model.AccompanyingPrice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * AccompanyingPriceManager
 *
 * @author jimson
 * @date 2017/12/22
 */
@Service
@Transactional
public class AccompanyingPriceManager {
    @Autowired
    private AccompanyingPriceMapper accompanyingPriceMapper;

    /**
     * 根据医院code 获取价格
     * @param orgCode
     * @return
     */
    public AccompanyingPrice getByOrgCode(Integer orgCode){
        return accompanyingPriceMapper.selectByPrimaryKey(orgCode);
    }

    /**
     * 添加或保存
     * @param param
     * @return
     */
    public int save(AccompanyingPrice param){
        AccompanyingPrice accompanyingPrice = accompanyingPriceMapper.selectByPrimaryKey(param.getOrgCode());
        if (accompanyingPrice == null){
            return accompanyingPriceMapper.insert(param);
        }else {
            return accompanyingPriceMapper.updateByPrimaryKeySelective(param);
        }
    }
}
