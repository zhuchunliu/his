package com.acmed.his.service;

import com.acmed.his.dao.FeeItemMapper;
import com.acmed.his.model.FeeItem;
import com.acmed.his.model.Org;
import com.acmed.his.model.User;
import com.acmed.his.pojo.mo.FeeItemMo;
import com.acmed.his.pojo.vo.UserInfo;
import com.acmed.his.support.AccessInfo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
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
    public void saveFeeItem(FeeItemMo mo, UserInfo user){
        FeeItem item = new FeeItem();
        BeanUtils.copyProperties(mo,item);
        if(null == item.getId()){
            item.setIsValid("1");
            item.setOrgCode(user.getOrgCode());
            item.setCreateAt(LocalDateTime.now().toString());
            item.setCreateBy(user.toString());
            feeItemMapper.insert(item);
        }else{
            item.setModifyAt(LocalDateTime.now().toString());
            item.setModifyAt(user.toString());
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
    public void delFeeItem(Integer id,UserInfo user){
        FeeItem item = feeItemMapper.selectByPrimaryKey(id);
        item.setIsValid("0");
        item.setModifyAt(LocalDateTime.now().toString());
        item.setModifyBy(user.getId().toString());
        feeItemMapper.updateByPrimaryKey(item);
    }
}
