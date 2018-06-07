package com.acmed.his.service;

import com.acmed.his.dao.ZyOrderMapper;
import com.acmed.his.model.ZyOrder;
import com.acmed.his.pojo.zy.ZYExpressCallbackMo;
import com.acmed.his.pojo.zy.ZYPayCallbackMo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

/**
 * Created by Darren on 2018-06-07
 **/
@Service
public class ZhangYaoCallbackManager {
    @Autowired
    private ZyOrderMapper zyOrderMapper;

    /**
     * 发货回调
     * @param mo
     */
    public void express(ZYExpressCallbackMo mo) {
        Example example = new Example(ZyOrder.class);
        example.createCriteria().andEqualTo("zyOrderId",mo.getOrderId());
        List<ZyOrder> list = zyOrderMapper.selectByExample(example);
        if(null != list){
            ZyOrder zyOrder = list.get(0);
            zyOrder.setRecepitStatus(0);
            zyOrder.setExpressNo(mo.getExpressNo());
            zyOrderMapper.updateByPrimaryKey(zyOrder);
        }
    }

    /**
     * 支付回调
     * @param mo
     */
    public void updateZyOrderPayStatus(ZYPayCallbackMo mo) {
        zyOrderMapper.updateZyOrderPayStatus(mo.getOrderIds(),mo.getPayStatus() == 1?2:3,mo.getRemark());

    }
}
