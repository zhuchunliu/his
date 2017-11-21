package com.acmed.his.service;

import com.acmed.his.HisApplication;
import com.acmed.his.model.User;
import com.acmed.his.pojo.RequestToken;
import com.acmed.his.util.ResponseResult;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Created by Darren on 2017-11-21
 **/
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = HisApplication.class)
public class LoginManagerTest {

    @Autowired
    private LoginManager loginManager;

    @Test
    public void userlogin(){
        try {
            ResponseResult result = loginManager.userlogin("zhangsan","==wMwIjM1ETM1ITMxUTMhZTN0MjMxs0a1F2V");
            System.err.print(result.getMsg()+" "+result.getResult());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void getUserByToken(){
        try {
            Object object = loginManager.getUserByToken("j==gN4kTcwE2S3N3RUd1QDhWcPRjMjVDN1YDN0AjNyETM1EjMS9EVD9ER");
            System.err.print(object);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void getTokenByOpenid() {
        try {
            RequestToken requestToken = loginManager.getTokenByOpenid("789");
            System.err.print(requestToken);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
