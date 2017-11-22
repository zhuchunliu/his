package com.acmed.his.service;

import com.acmed.his.HisApplication;
import com.acmed.his.model.Permission;
import com.acmed.his.pojo.mo.PermissionMo;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Date;
import java.util.List;

/**
 * Created by Darren on 2017-11-22
 **/
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = HisApplication.class)
public class PermissionManagerTest {

    @Autowired
    private PermissionManager permissionManager;

    @Test
    public void getPermissionList(){
        permissionManager.getPermissionList().forEach((obj)->System.err.println(obj));
    }


    @Test
    public void save(){
        PermissionMo mo = new PermissionMo();
        mo.setCategory("1");
        mo.setPerCode("AddUser");
        mo.setPerDesc("添加用户");
        permissionManager.save(mo);

        mo.setCategory("1");
        mo.setPerCode("DelUser");
        mo.setPerDesc("删除用户");
        permissionManager.save(mo);
    }

    @Test
    public void update(){
        PermissionMo mo = new PermissionMo();
        mo.setId(1);
        mo.setCategory("1");
        mo.setPerCode("AddUser");
        mo.setPerDesc("添加用户-update");
        permissionManager.save(mo);

    }

    @Test
    public void getPermissionDetail(){
        System.err.println(permissionManager.getPermissionDetail(1));
    }

    @Test
    public void delPermission(){
        permissionManager.delPermission(2);
    }
}
