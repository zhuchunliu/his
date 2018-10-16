package com.acmed.his.service;

import com.acmed.his.HisApplication;
import com.acmed.his.dao.AccompanyingOrderMapper;
import com.acmed.his.model.AccompanyingOrder;
import com.acmed.his.pojo.wxmb.WxTplMsg;
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

    @Autowired
    private WxMsgManager wxMsgManager;

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

    @Test
    public void t(){
        WxTplMsg wxTplMsg = new WxTplMsg();
        wxTplMsg.setTouser("oTAaixJPBOtwrvbqbeZU4ZtXmSOo");
        wxTplMsg.setTemplate_id("ZRO6hHafw6k4FG8rysVLVZVA6ZT6IheuzFXEEj1DVpU");
        WxTplMsg.DataBean dataBean = new WxTplMsg.DataBean();
        WxTplMsg.DataBean.FirstBean firstBean = new WxTplMsg.DataBean.FirstBean();
        firstBean.setColor("#173177");
        firstBean.setValue("北上广订单提醒");
        WxTplMsg.DataBean.Keyword1Bean keyword1 = new WxTplMsg.DataBean.Keyword1Bean();
        keyword1.setColor("#173177");
        keyword1.setValue("真实姓名");
        WxTplMsg.DataBean.Keyword2Bean keyword2 = new WxTplMsg.DataBean.Keyword2Bean();
        keyword2.setColor("#173177");
        keyword2.setValue("机构名");
        WxTplMsg.DataBean.Keyword3Bean keyword3 = new WxTplMsg.DataBean.Keyword3Bean();
        keyword3.setColor("#173177");
        keyword3.setValue("科室");
        WxTplMsg.DataBean.Keyword4Bean keyword4 = new WxTplMsg.DataBean.Keyword4Bean();
        keyword4.setColor("#173177");
        keyword4.setValue("开始时间");
        WxTplMsg.DataBean.Keyword5Bean keyword5 = new WxTplMsg.DataBean.Keyword5Bean();
        keyword5.setColor("#173177");
        keyword5.setValue("备注");
        dataBean.setFirst(firstBean);
        dataBean.setKeyword1(keyword1);
        dataBean.setKeyword2(keyword2);
        dataBean.setKeyword3(keyword3);
        dataBean.setKeyword4(keyword4);
        dataBean.setKeyword5(keyword5);
        wxMsgManager.sendtplmsg(wxTplMsg);
    }
}