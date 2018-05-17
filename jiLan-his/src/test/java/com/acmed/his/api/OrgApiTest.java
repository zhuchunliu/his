package com.acmed.his.api;

import com.acmed.his.HisApplication;
import com.acmed.his.pojo.vo.OrgVo;
import com.acmed.his.util.ResponseResult;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

import static org.junit.Assert.*;

/**
 * OrgApiTest
 *
 * @author jimson
 * @date 2018/5/17
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = HisApplication.class)
public class OrgApiTest {

    @Autowired
    private OrgApi orgApi;
    @Test
    public void getBSGList() {
        ResponseResult<List<OrgVo>> bsgList = orgApi.getBSGList(null, "协和", null);
        System.err.println(bsgList);
    }
}