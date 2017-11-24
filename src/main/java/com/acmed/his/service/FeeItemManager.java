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
import tk.mybatis.mapper.entity.Example;

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
    public List<FeeItem> getFeeItemList(Integer orgCode){
        Example example = new Example(FeeItem.class);
        example.createCriteria().andEqualTo("orgCode",orgCode).andEqualTo("isValid","1");
        return feeItemMapper.selectByExample(example);
    }

    /**
     * 新增、编辑收费项目
     * @param mo
     */
    public void saveFeeItem(FeeItemMo mo, UserInfo userInfo){
        if(null == mo.getId()){
            FeeItem item = new FeeItem();
            BeanUtils.copyProperties(mo,item);
            item.setIsValid("1");
            item.setOrgCode(userInfo.getOrgCode());
            item.setCreateAt(LocalDateTime.now().toString());
            item.setCreateBy(userInfo.getId().toString());
            feeItemMapper.insert(item);
        }else{
            FeeItem item = feeItemMapper.selectByPrimaryKey(mo.getId());
            BeanUtils.copyProperties(mo,item);
            item.setModifyAt(LocalDateTime.now().toString());
            item.setModifyBy(userInfo.getId().toString());
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
    public void delFeeItem(Integer id,UserInfo userInfo){
        FeeItem item = feeItemMapper.selectByPrimaryKey(id);
        item.setIsValid("0");
        item.setModifyAt(LocalDateTime.now().toString());
        item.setModifyBy(userInfo.getId().toString());
        feeItemMapper.updateByPrimaryKey(item);
    }
}
