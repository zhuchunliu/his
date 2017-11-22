package com.acmed.his.service;

import com.acmed.his.HisApplication;
import com.acmed.his.model.RoleVsPermission;
import com.acmed.his.pojo.mo.RoleMo;
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
        roleManager.save(role);

        role.setRoleCode("doctor");
        roleManager.save(role);
    }

    @Test
    public void update(){
        RoleMo role = new RoleMo();
        role.setId(1);
        role.setRoleCode("admin-update");
        roleManager.save(role);

    }

    @Test
    public void getRoleDetail(){
        System.err.println(roleManager.getRoleDetail(1));
    }

    @Test
    public void delRole(){
        roleManager.delRole(2);
    }



    @Test
    public void getPermissionByRole() {
        roleManager.getPermissionByRole(1).forEach((obj)->System.err.println(obj));
    }


    @Test
    public void addRolePermission() {
        RoleVsPermission roleVsPermission = new RoleVsPermission();
        roleVsPermission.setRid(1);
        roleVsPermission.setPid(1);
        roleManager.addRolePermission(roleVsPermission);


        roleVsPermission.setRid(1);
        roleVsPermission.setPid(2);
        roleManager.addRolePermission(roleVsPermission);
    }

    @Test
    public void delRolePermission() {
        roleManager.delRolePermission(1,2);
    }
}
