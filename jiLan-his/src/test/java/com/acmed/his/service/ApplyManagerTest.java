package com.acmed.his.service;

import com.acmed.his.HisApplication;
import com.acmed.his.model.Apply;
import com.acmed.his.model.User;
import com.acmed.his.model.dto.ChuZhenFuZhenCountDto;
import com.acmed.his.pojo.mo.ApplyMo;
import com.acmed.his.pojo.vo.UserInfo;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * ApplyManagerTest
 *
 * @author jimson
 * @date 2017/11/23
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = HisApplication.class)
public class ApplyManagerTest {

    @Autowired
    private ApplyManager applyManager;
    @Test
    public void addApply() throws Exception {
        Apply apply = new Apply();
        apply.setOrgCode(1);
        apply.setOrgName("和谐医院");
        apply.setPatientId("1");
        apply.setPatientName("王二");
        apply.setGender("1");
        apply.setPinYin("WE");
        apply.setDept(1);
        apply.setStatus("0");
        apply.setIsPaid("1");
        apply.setDeptName("内科");
//        applyManager.addApply(apply);
    }

    @Test
    public void getApplyByPatientId() throws Exception {
        applyManager.getApplyByPatientId("1");
    }

    @Test
    public void getApplyById() throws Exception {
        applyManager.getApplyById("2");
    }

    @Test
    public void getApplyByOrgCode() throws Exception {
        applyManager.getApplyByOrgCode(1);
    }



    @Test
    public void updateApply() throws Exception {
        Apply apply = new Apply();
        apply.setId("3");
        apply.setOrgName("和谐医院232332");
        applyManager.updateApply(apply);
    }




    @Test
    public void chuZhenAndFuZhenTongJi() throws Exception {
        ChuZhenFuZhenCountDto chuZhenFuZhenCountDto = applyManager.chuZhenAndFuZhenTongJi(1);
        System.err.println(chuZhenFuZhenCountDto);
    }
    @Test
    public void refund(){
        String applyId = "21";
        Double fee = 2.00;
        String feeType = "0";
        UserInfo userInfo = new UserInfo();
        userInfo.setOrgCode(3);
        userInfo.setId(4);
        userInfo.setDept(6);
        System.err.println(applyManager.refund(applyId,fee,feeType,userInfo));
    }

    @Test
    public void addByPatient(){
        for (int i = 1;i<2;i++){
            ApplyMo mo = new ApplyMo();
            mo.setPatientName("周三");
            mo.setGender("1");
            mo.setMobile("13288778877");
            mo.setIdcard("320586199910316655");
            mo.setSocialCard("fdd4445");
            mo.setDoctorId(2);
            mo.setAppointmentTime("2018-03-02");
            mo.setRelation("家人");
            mo.setPatientCardId("376500dd108c4563ac091147488abbbfao5a");
            /*UserInfo userInfo = new UserInfo();
            userInfo.setId(7);
            userInfo.setDept(13);
            userInfo.setDeptName("泌尿外科");
            userInfo.setOrgCode(i);
            userInfo.setOrgName("北京大学第一医院");
            userInfo.setApplyfee(1.0);*/
            System.err.println(applyManager.addApply(mo,"fe11af7e242249c087469c1dc8158b76DIfq",null).toString());
        }
    }
    @Test
    public  void aa(){
        User user = new User();
        user.setUserName("wanger");

        User user1 = new User();
        user1.setAge(1);
        BeanUtils.copyProperties(user,user1);
        System.err.println(user1);



    }
}