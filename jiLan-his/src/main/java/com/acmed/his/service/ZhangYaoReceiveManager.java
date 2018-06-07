package com.acmed.his.service;

import com.acmed.his.dao.PrescriptionMapper;
import com.acmed.his.dao.ZhangYaoMapper;
import com.acmed.his.dao.ZyOrderItemMapper;
import com.acmed.his.dao.ZyOrderMapper;
import com.acmed.his.model.Prescription;
import com.acmed.his.model.ZyOrder;
import com.acmed.his.model.ZyOrderItem;
import com.acmed.his.model.dto.ZyDispenseOrderDetailDto;
import com.acmed.his.model.dto.ZyDispenseOrderDto;
import com.acmed.his.model.dto.ZyOrderItemReceiveDto;
import com.acmed.his.pojo.vo.UserInfo;
import com.acmed.his.pojo.zy.ZYDispenseMo;
import com.acmed.his.pojo.zy.ZYReceiveMo;
import com.acmed.his.pojo.zy.ZYReceiveQueryMo;
import com.acmed.his.util.PageBase;
import com.acmed.his.util.PageResult;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

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

    @Autowired
    private PrescriptionMapper preMapper;

    /**
     * 确认收药
     * @param list
     * @param user
     */
    @Transactional
    public void recepit(List<ZYReceiveMo> list, UserInfo user) {
        ZyOrder zyOrder = null;
        Boolean flag = true;
        List<String> preIdList = Lists.newArrayList();
        for(ZYReceiveMo mo : list) {
            ZyOrderItem orderItem = zyOrderItemMapper.selectByPrimaryKey(mo.getItemId());
            if(null == zyOrder) {
                zyOrder = zyOrderMapper.selectByPrimaryKey(orderItem.getOrderId());
            }
            orderItem.setReceiveNum(mo.getReceiveNum());
            if(orderItem.getReceiveNum() < orderItem.getNum()){
                flag = false;
            }else{
                preIdList.add(orderItem.getPreItemId());
            }
            zyOrderItemMapper.updateByPrimaryKey(orderItem);

        }
        zyOrder.setRecepitStatus(flag?1:2);
        zyOrder.setModifyAt(LocalDateTime.now().toString());
        zyOrder.setModifyBy(user.getId().toString());
        zyOrderMapper.updateByPrimaryKey(zyOrder);

        zhangYaoMapper.updatePreItemStatusByIds(preIdList,4);//设置成已收货状态

    }

    /**
     * 获取收款列表
     * @param user
     * @param pageBase
     * @return
     */
    public PageResult<ZyOrderItemReceiveDto> getReceiveOrder(PageBase<ZYReceiveQueryMo> pageBase,UserInfo user) {
        Page page = PageHelper.startPage(pageBase.getPageNum(),pageBase.getPageSize());
        List<ZyOrderItemReceiveDto> list = zhangYaoMapper.getReceiveOrder(user.getOrgCode(),
                Optional.ofNullable(pageBase.getParam()).orElse(new ZYReceiveQueryMo()));
        PageResult result = new PageResult();
        result.setData(list);
        result.setTotal(page.getTotal());
        return result;
    }



    /**
     * 获取发药列表
     * @param pageBase
     * @param user
     */
    public PageResult<ZyDispenseOrderDto> getDispenseOrder(PageBase<ZYDispenseMo> pageBase, UserInfo user) {
        Page page = PageHelper.startPage(pageBase.getPageNum(),pageBase.getPageSize());
        List<ZyDispenseOrderDto> list = zhangYaoMapper.getDispenseOrder(user.getOrgCode(),
                Optional.ofNullable(pageBase.getParam()).orElse(new ZYDispenseMo()));
        PageResult<ZyDispenseOrderDto> result = new PageResult();
        result.setData(list);
        result.setTotal(page.getTotal());
        return result;
    }

    /**
     * 发货
     *
     * @param applyId 订单主键
     */
    public void dispense(String applyId) {
        Prescription prescription = preMapper.selectByPrimaryKey(applyId);
        prescription.setIsZyDispensing(1);
        preMapper.updateByPrimaryKey(prescription);
    }

    /**
     * 获取发货详情
     *
     * @param applyId
     * @return
     */
    public List<ZyDispenseOrderDetailDto> getDispenseDetail(String applyId) {
        return zhangYaoMapper.getDispenseDetail(applyId);
    }
}
