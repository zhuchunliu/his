package com.acmed.his.service;

import com.acmed.his.dao.AccompanyingOrderMapper;
import com.acmed.his.model.AccompanyingOrder;
import com.acmed.his.model.dto.AccompanyingOrderCountDto;
import com.acmed.his.model.dto.AccompanyingOrderMo;
import com.acmed.his.util.WaterCodeUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * AccompanyingOrderManager
 * 就医北上广订单
 * @author jimson
 * @date 2017/12/22
 */
@Service
@Transactional
public class AccompanyingOrderManager {
    @Autowired
    private AccompanyingOrderMapper accompanyingOrderMapper;

    /**
     * 创建就医北上广订单
     * @param accompanyingOrder 订单
     * @return 不成功 返回null
     */
    public AccompanyingOrder addAccompanyingOrder(AccompanyingOrder accompanyingOrder){
        accompanyingOrder.setCreateAt(LocalDateTime.now().toString());
        accompanyingOrder.setPayStatus(0);
        accompanyingOrder.setOrderCode(WaterCodeUtil.getWaterCode());

        int insert = accompanyingOrderMapper.insert(accompanyingOrder);
        if (insert == 0){
            return null;
        }else {
            return accompanyingOrder;
        }
    }

    /**
     * 修改订单
     * @param accompanyingOrder
     * @return
     */
    public int update(AccompanyingOrder accompanyingOrder){
        return accompanyingOrderMapper.updateByPrimaryKeySelective(accompanyingOrder);
    }

    /**
     * 条件查询
     * @param accompanyingOrder
     * @param orderBy
     * @return List<AccompanyingOrder>
     */
    public List<AccompanyingOrder> selectByAccompanyingOrder(AccompanyingOrder accompanyingOrder,String orderBy){
        AccompanyingOrderMo bean = new AccompanyingOrderMo();
        BeanUtils.copyProperties(accompanyingOrder,bean);
        bean.setOrderBy(orderBy);
        return accompanyingOrderMapper.selectByAccompanyingOrder(bean);
    }

    /**
     * 跟他在状态查询
     * @param status  状态   如果为空 就查询全部
     * @return List<AccompanyingOrderCountDto>
     */
    public List<AccompanyingOrderCountDto>  getOrderCountGroupByOrgCode(Integer status){
        return accompanyingOrderMapper.selectCountNumGroupByOrgCode(status);
    }

    public AccompanyingOrder getByOrderCode(String orderCode){
        return accompanyingOrderMapper.selectByPrimaryKey(orderCode);
    }

    /**
     * 根据邀请码获取列表
     * @param invitationCode 邀请码
     * @return
     */
    public List<AccompanyingOrder> getByInvitationCode(String invitationCode){
        List<Integer> list = new ArrayList<>();
        list.add(1);
        list.add(2);
        list.add(3);
        Example example = new Example(AccompanyingOrder.class);
        example.createCriteria().andIn("status",list).andEqualTo("invitationCode",invitationCode);
        return accompanyingOrderMapper.selectByExample(example);
    }


}
