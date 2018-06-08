package com.acmed.his.service;

import com.acmed.his.dao.ZyOrderFeedbackMapper;
import com.acmed.his.dao.ZyOrderItemMapper;
import com.acmed.his.dao.ZyOrderMapper;
import com.acmed.his.model.ZyOrder;
import com.acmed.his.pojo.zy.ZYExpressCallbackMo;
import com.acmed.his.pojo.zy.ZYPayCallbackMo;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.ImmutableMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.util.List;
import java.util.Map;

/**
 * Created by Darren on 2018-06-07
 **/
@Service
public class ZhangYaoCallbackManager {
    @Autowired
    private ZyOrderMapper zyOrderMapper;

    @Autowired
    private SimpMessagingTemplate template;

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
    @Transactional
    public void updateZyOrderPayStatus(ZYPayCallbackMo mo) {
        //设置反馈信息，为支付成功
        if(mo.getPayStatus() == 1){
            zyOrderMapper.updateZyOrderFeedBack(mo.getOrderIds(),"20");
        }

        //设置订单关联的掌药订单主键   目的：防止支付之后，又有人重新提交过订单，导致zyorderid变更
        zyOrderMapper.updateZyOrderId(mo.getOrderIds());

        zyOrderMapper.updateZyOrderPayStatus(mo.getOrderIds(),mo.getPayStatus() == 1?2:3,mo.getRemark());

        if(mo.getPayStatus() == 1) { //如果支付成功之后，发现有订单被删除的情况，将删除标志设置为0
            zyOrderMapper.updateZyOrderItem(mo.getOrderIds());
        }




    }

    /**
     * 推送消息
     * @param mo
     */
    public void pushMsg(ZYPayCallbackMo mo) {
        List<Map<String,Object>> list = zyOrderMapper.getPushUserId(mo.getOrderIds());
        if(null != list && 0 != list.size()){
            for(Map<String,Object> map:list){
                String userId = map.get("userId").toString();
                JSONObject.toJSONString(ImmutableMap.of("flag",true));
                template.convertAndSendToUser(userId,"/information",
                        JSONObject.toJSONString(ImmutableMap.of("flag",1==mo.getPayStatus()?true:false)) );
            }
        }
    }


}
