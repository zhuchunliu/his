package com.acmed.his.api;

import com.acmed.his.HisApplication;
import com.acmed.his.pojo.vo.OrgVo;
import com.acmed.his.util.ResponseResult;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.client.RestTemplate;

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


    @Test
    public void getBSGList1() {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        MediaType type = MediaType.parseMediaType("application/json; charset=UTF-8");
        headers.setContentType(type);
        headers.add("Content-Type", "application/json");
        headers.add("Authorization", "d25c4647aa76adc0549c515720754936");

        HttpEntity<String> formEntity = new HttpEntity<String>("{\n" +
                "\t\"yuyue\":\"预约\",\n" +
                "\t\"name\":\"姓名\",\n" +
                "\t\"date\":\"生日\",\n" +
                "\t\"sex\":\"性别\",\n" +
                "\t\"email\":\"123@qq.com\",\n" +
                "\t\"user-id\":\"最近体检时间\",\n" +
                "\t\"tel\":\"13288778877\",\n" +
                "\t\"s_province\":\"省\",\n" +
                "\t\"s_city\":\"市\",\n" +
                "\t\"s_county\":\"区\",\n" +
                "\t\"detail-add\":\"详细地址\",\n" +
                "\t\"company\":\"主检单位\",\n" +
                "\t\"sel1\":\"预约项目\",\n" +
                "\t\"sel2\":\"医院\",\n" +
                "\t\"z_content\":\"咨询内容\"\n" +
                "}", headers);
        String result = restTemplate.postForObject("http://www.4000663363.com/index.php/consultation/personala", formEntity, String.class);
        System.err.println(result);
    }
}