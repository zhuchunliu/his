package com.acmed.his.service;

import com.acmed.his.dao.FeeItemMapper;
import com.acmed.his.model.FeeItem;
import com.acmed.his.model.Org;
import com.acmed.his.pojo.mo.FeeItemMo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by Darren on 2017-11-22
 **/
@Service
public class FeeItemManager {
    @Autowired
    private FeeItemMapper feeItemMapper;

    /**
     * 获取收费列表
     * @return
     */
    public List<FeeItem> getFeeItemList(){
        return feeItemMapper.selectAll();
    }

    /**
     * 新增、编辑收费项目
     * @param mo
     */
    public void saveFeeItem(FeeItemMo mo){
        FeeItem item = new FeeItem();
        BeanUtils.copyProperties(mo,item);
        item.setIsValid("1");
        if(null == item.getId()){
            feeItemMapper.insert(item);
        }else{
            feeItemMapper.updateByPrimaryKey(item);
        }
    }

    /**
     * 获取收费详情
     * @param id
     */
    public FeeItem getFeeItemDetail(Integer id){
        return feeItemMapper.selectByPrimaryKey(id);
    }

    /**
     * 删除收费详情
     * @param id
     */
    public void delFeeItem(Integer id){
        FeeItem item = feeItemMapper.selectByPrimaryKey(id);
        item.setIsValid("0");
        feeItemMapper.updateByPrimaryKey(item);
    }
}
