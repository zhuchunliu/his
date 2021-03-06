package com.acmed.his.service;

import com.acmed.his.HisApplication;
import com.acmed.his.dao.DicItemMapper;
import com.acmed.his.model.DicItem;
import com.acmed.his.model.UserVsRole;
import com.acmed.his.pojo.mo.UserMo;
import com.acmed.his.pojo.mo.UserVsRoleMo;
import com.acmed.his.pojo.vo.UserInfo;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Created by Darren on 2017-11-22
 **/
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = HisApplication.class)
public class UserManagerTest {

    @Autowired
    private UserManager userManager;

    @Autowired
    private BaseInfoManager baseInfoManager;

    @Test
    public void getUserList(){
//        userManager.getUserList(new UserInfo(),null).forEach((obj)->System.err.println(obj));
    }

    @Test
    public void save(){
        UserMo userMo = new UserMo();
        userMo.setUserName("wangwu");
        userManager.save(userMo,new UserInfo());

        userMo.setUserName("zhaoliu");
        userManager.save(userMo,new UserInfo());
    }

    @Test
    public void getUserDetail(){
        System.err.println(userManager.getUserDetail(1));
        System.err.println(userManager.getUserDetail(2));
        System.err.println(baseInfoManager.getDicItem("UserCategory","1"));
        System.err.println(baseInfoManager.getDicItem("UserCategory","2"));
        System.err.println(baseInfoManager.getDicItem("Schedule","1"));
        System.err.println(baseInfoManager.getDicItem("Schedule","2"));
    }

    @Test
    public void delUser(){
        userManager.delUser(4,new UserInfo());
    }

    @Test
    public void getRoleByUser() {
//        userManager.getRoleByUser(1).forEach((obj)->System.err.println(obj));
    }

    @Test
    public void addUserRole() {
        UserVsRoleMo mo = new UserVsRoleMo();
        mo.setUid(1);
        mo.setRids("1");
//        userManager.addUserRole(mo);

        mo.setRids("3");
//        userManager.addUserRole(mo);
    }

    @Test
    public void delUserRole() {
//        userManager.delUserRole(1,3);
    }

    @Test
    public void changeOpenId() {
        userManager.changeOpenId("openid123",17,"17");
    }
}
