package com.acmed.his.service;

import com.acmed.his.HisApplication;
import com.acmed.his.pojo.mo.DrugZYQueryMo;
import com.acmed.his.pojo.zy.ZYOrderDetailVo;
import com.acmed.his.util.PageBase;
import com.acmed.his.util.PageResult;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Created by Darren on 2018-04-10
 **/
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = HisApplication.class)
public class ZhangYaoManagerTest {

    @Autowired
    private ZhangYaoManager zhangYaoManager;

    @Test
    public void getDrugList(){
        DrugZYQueryMo mo = new DrugZYQueryMo();
        mo.setName("999");
        mo.setLat("31.270177");
        mo.setLng("120.738683");

        PageBase pageBase = new PageBase<DrugZYQueryMo>();
        pageBase.setParam(mo);
        pageBase.setPageNum(1);
        pageBase.setPageSize(2);
        PageResult pageResult = zhangYaoManager.getDrugList(pageBase);
        System.err.println(pageResult);
    }

    @Test
    public void getOrderDetail(){

//        ZYOrderDetailVo vo = zhangYaoManager.getOrderDetail("b0f86b08b9fa4024b3c07b15893d4d54BCQm");
//        System.err.println(vo);
    }
}
