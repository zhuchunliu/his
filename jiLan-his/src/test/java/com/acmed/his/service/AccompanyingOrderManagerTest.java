package com.acmed.his.service;

import com.acmed.his.HisApplication;
import com.acmed.his.dao.AccompanyingOrderMapper;
import com.acmed.his.model.AccompanyingOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.*;

/**
 * AccompanyingOrderManagerTest
 *
 * @author jimson
 * @date 2017/12/28
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = HisApplication.class)
public class AccompanyingOrderManagerTest {
    @Autowired
    private AccompanyingOrderMapper accompanyingOrderMapper;

    @Autowired
    private AccompanyingOrderManager accompanyingOrderManager;

    @Test
    public void addAccompanyingOrder() throws Exception {
    }

    @Test
    public void update() throws Exception {
        AccompanyingOrder accompanyingOrder = new AccompanyingOrder();
        accompanyingOrder.setOrderCode("2017122700001000990015033764");
        accompanyingOrder.setStatus(15);
/*        accompanyingOrder.setPayStatus(1);
        accompanyingOrder.setStatus(2);
        accompanyingOrder.setPayType(1);*/
        int update = accompanyingOrderManager.update(accompanyingOrder);
        System.err.print(update);
    }

    @Test
    public void selectByAccompanyingOrder() throws Exception {
    }

    @Test
    public void getOrderCountGroupByOrgCode() throws Exception {
    }

    @Test
    public void getByOrderCode() throws Exception {
    }

    @Test
    public void getByInvitationCode() throws Exception {
    }

}