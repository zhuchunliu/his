package com.acmed.his.service;

import com.acmed.his.dao.ZhangYaoMapper;
import com.acmed.his.dao.ZyOrderItemMapper;
import com.acmed.his.dao.ZyOrderMapper;
import com.acmed.his.model.ZyOrder;
import com.acmed.his.model.ZyOrderItem;
import com.acmed.his.model.dto.ZyOrderItemReceiveDto;
import com.acmed.his.pojo.vo.UserInfo;
import com.acmed.his.pojo.zy.ZYReceiveMo;
import com.acmed.his.pojo.zy.ZYReceiveQueryMo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Created by Darren on 2018-06-04
 **/
@Service
public class ZhangYaoReceiveManager {

    @Autowired
    private ZyOrderMapper zyOrderMapper;

    @Autowired
    private ZyOrderItemMapper zyOrderItemMapper;

    @Autowired
    private ZhangYaoMapper zhangYaoMapper;

    /**
     * 确认发药
     * @param list
     * @param user
     */
    @Transactional
    public void recepit(List<ZYReceiveMo> list, UserInfo user) {
        ZyOrder zyOrder = null;
        for(ZYReceiveMo mo : list) {
            ZyOrderItem zyOrderItem = zyOrderItemMapper.selectByPrimaryKey(mo.getItemId());
            zyOrderItem.setReceiveNum(mo.getReceiveNum());
            if(zyOrderItem.getNum() == zyOrderItem.getReceiveNum()){

            }
            zyOrderItemMapper.updateByPrimaryKey(zyOrderItem);
            if(null == zyOrder) {
                zyOrder = zyOrderMapper.selectByPrimaryKey(zyOrderItem.getOrderId());
                zyOrder.setIsRecepit(1);
                zyOrder.setModifyAt(LocalDateTime.now().toString());
                zyOrder.setModifyBy(user.getId().toString());
                zyOrderMapper.updateByPrimaryKey(zyOrder);
            }
        }
    }

    /**
     * 获取收款列表
     * @param user
     * @param mo
     * @return
     */
    public List<ZyOrderItemReceiveDto> getReceiveOrder(UserInfo user, ZYReceiveQueryMo mo) {
        return zhangYaoMapper.getReceiveOrder(user.getOrgCode(),mo);
    }
}
