package com.acmed.his.service;

import com.acmed.his.HisApplication;
import com.acmed.his.model.RoleVsPermission;
import com.acmed.his.pojo.mo.RoleMo;
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
public class RoleManagerTest {

    @Autowired
    private RoleManager roleManager;

    @Test
    public void getRoleList(){
        roleManager.getRoleList().forEach((obj)->System.err.println(obj));
    }

    @Test
    public void save(){
        RoleMo role = new RoleMo();
        role.setRoleCode("admin");
        roleManager.save(role,new UserInfo());

        role.setRoleCode("doctor");
        roleManager.save(role,new UserInfo());
    }

    @Test
    public void update(){
        RoleMo role = new RoleMo();
        role.setId(1);
        role.setRoleCode("admin-update");
        roleManager.save(role,new UserInfo());

    }

    @Test
    public void getRoleDetail(){
        System.err.println(roleManager.getRoleDetail(1));
    }

    @Test
    public void delRole(){
        roleManager.delRole(2,new UserInfo());
    }



    @Test
    public void getPermissionByRole() {
        roleManager.getPermissionByRole(1).forEach((obj)->System.err.println(obj));
    }




    @Test
    public void delRolePermission() {
        roleManager.delRolePermission(1,2);
    }

}
