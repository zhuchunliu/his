package com.acmed.his.api;

import com.acmed.his.HisApplication;
import com.acmed.his.model.DicItem;
import com.acmed.his.pojo.vo.UserInfo;
import com.acmed.his.support.AccessInfo;
import com.acmed.his.util.ResponseResult;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

import static org.junit.Assert.*;

/**
 * DicItemApiTest
 *
 * @author jimson
 * @date 2018/5/15
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = HisApplication.class)
public class DicItemApiTest {

    @Autowired
    private DicItemApi dicItemApi;
    @Test
    public void getDicItemsByDicTypeCode() {
        AccessInfo accessInfo = new AccessInfo();
        UserInfo user = accessInfo.getUser();
        ResponseResult<List<DicItem>> orgLevel = dicItemApi.getDicItemsByDicTypeCode("OrgLevel", accessInfo);
    }
}