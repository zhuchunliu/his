package com.acmed.his.service;

import com.acmed.his.dao.DrugMapper;
import com.acmed.his.model.Drug;
import com.acmed.his.pojo.mo.DrugStockPrice;
import com.acmed.his.pojo.vo.UserInfo;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Created by Darren on 2018-03-02
 **/
@Service
public class DrugStockManager {

    @Autowired
    private DrugMapper drugMapper;

    /**
     * 库存列表
     * @param name
     * @param info
     */
    public List<Drug> getStockList(Integer pageNum , Integer pageSize, String name, UserInfo info) {
        PageHelper.startPage(pageNum,pageSize);
        return drugMapper.getStockList(name,info.getOrgCode());
    }

    /**
     * 库存数据
     * @param name
     * @param user
     * @return
     */
    public Integer getStockTotal(String name, UserInfo user) {
        return drugMapper.getStockTotal(name,user.getOrgCode());
    }

    /**
     * 调整价格
     * @param user
     */
    public void modifyPrice(DrugStockPrice drugStockPrice, UserInfo user) {
        Drug drug = drugMapper.selectByPrimaryKey(drugStockPrice.getId());
        drug.setRetailPrice(drugStockPrice.getRetailPrice());
        drug.setMinRetailPrice(drugStockPrice.getMinRetailPrice());
        drug.setMinPriceUnitType(drugStockPrice.getMinPriceUnitType());
        drug.setModifyAt(LocalDateTime.now().toString());
        drug.setModifyBy(user.getId().toString());
        drugMapper.updateByPrimaryKey(drug);
    }
}
