package com.acmed.his.service;

import com.acmed.his.UserApplication;
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
@SpringBootTest(classes = UserApplication.class)
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
    public void getTokenByOpenid() {
        try {
            RequestToken requestToken = loginManager.getTokenByOpenid("oTAaixA9X74whvto1wK4H-zn9wAY");
            System.err.print(requestToken);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
