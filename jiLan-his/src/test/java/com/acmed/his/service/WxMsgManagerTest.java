package com.acmed.his.service;

import com.acmed.his.HisApplication;
import com.acmed.his.pojo.wxmb.WxTplMsg;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.*;

/**
 * WxMsgManagerTest
 *
 * @author jimson
 * @date 2018/5/25
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = HisApplication.class)
public class WxMsgManagerTest {

    @Autowired
    private WxMsgManager wxMsgManager;
    @Test
    public void task5pm(){
        wxMsgManager.task5pm();
    }

    @Test
    public void sendtplmsg() {
        WxTplMsg wxTplMsg = new WxTplMsg();
        wxTplMsg.setTouser("oTAaixA9X74whvto1wK4H-zn9wAY");
        wxTplMsg.setTemplate_id("MjH5LSyxWWx-kyN3PnV6kWqt-s8TFvk3DjYsiwwnC4Y");

        WxTplMsg.DataBean dataBean = new WxTplMsg.DataBean();
        WxTplMsg.DataBean.FirstBean firstBean = new WxTplMsg.DataBean.FirstBean();
        firstBean.setColor("#173177");
        firstBean.setValue("您好，您的诊所今天已经采购云药房的药品，为保证药品及时送达，请尽快完成支付。详情如下");


        WxTplMsg.DataBean.Keyword1Bean keyword1 = new WxTplMsg.DataBean.Keyword1Bean();
        keyword1.setColor("#173177");
        keyword1.setValue("姓名");


        WxTplMsg.DataBean.Keyword2Bean keyword2 = new WxTplMsg.DataBean.Keyword2Bean();
        keyword2.setColor("#173177");
        keyword2.setValue("药品费用");

        WxTplMsg.DataBean.Keyword3Bean keyword3 = new WxTplMsg.DataBean.Keyword3Bean();
        keyword3.setColor("#173177");
        keyword3.setValue("2018-05-25");

/*        WxTplMsg.DataBean.RemarkBean remarkBean = new WxTplMsg.DataBean.RemarkBean();
        remarkBean.setColor("#173177");
        remarkBean.setValue();*/

        dataBean.setFirst(firstBean);
        dataBean.setKeyword1(keyword1);
        dataBean.setKeyword2(keyword2);
        dataBean.setKeyword3(keyword3);
        //dataBean.setRemark(remarkBean);

        wxTplMsg.setData(dataBean);
        wxMsgManager.sendtplmsg(wxTplMsg);
    }
}