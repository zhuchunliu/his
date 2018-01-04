package com.acmed.his.drug;

import com.acmed.his.util.PinYinUtil;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import okio.Okio;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.io.IOException;

/**
 * Created by Darren on 2018-01-02
 **/
public class DrugTest {


    public static void main(String[] args) throws IOException {

        File file = new File("jiLan-his/src/test/java/com/acmed/his/drug/drug.sql");
        if(!file.exists()){
            file.createNewFile();
        }

        String sql = "http://120.26.165.247:8081/api/DrugInfo/PageXTYPXX?cflx=1&pageIndex=%s&pageSize=%s&sig=d8a11801c1710175cddd5c2a4b8f4da32e641b2a0c725a4e391d4baa8bfadda43f5dcc9fedbea742ea6ec7a69c81bff06c7b0d84be51c2d135b59a36cb2278ec8b6dbd7d09d6c8c68db184ba95226005124e6edef1cbf5aef2fadd155e40502010ed511682707128bffd923593265b0045ff0192b5d1f71d50d40718244ea01e8efe88e68ec5e575de9cac2f7d53551d54407aba9380ae258797046d01d400ab6fc71481f7a19f068e07a4f23d2a574ef75672fc3774060a92c4e1f84c8ee617bd9dfae940c6c53564e5e4138308c661bffd923593265b00e62788d8c38a1c2c4b709963294652e40ed1b220f1b2527012530956e838af259e12979008fffa611837bc4fffeb4c6dd7952b094c1c84b86845c2edd553d12241a4ac80e4488a491fc69aee6e5daa21b25c168ba0b13ed8a531d422e93c9b1e12530956e838af25289bca729a924fad&token=5b287b8d55790b717fefd6d45390c616&tokenUserID=5416&tokenZhid=2017121500000003&yplbbh=641&ypmc=&zhid=2017121500000003";
        String drugsql = "insert into t_b_drugdict(code,specname,pinyin,category,unit,spec,packunit) values ('%s','%s','%s','0','%s','%s','%s')";

        RestTemplate restTemplate = new RestTemplate();
        int pageIndex = 1,pageSize = 1000;
        boolean flag = true;
        while(flag) {
            System.err.println("当前页: "+pageIndex);
            JSONObject json = restTemplate.getForObject(String.format(sql, pageIndex, pageSize), JSONObject.class).getJSONObject("Data");
            JSONArray array = json.getJSONArray("Entity");
            if(array.size()== pageSize){
                pageIndex++;
            }else{
                flag = false;
            }
            for (int index = 0; index < array.size(); index++) {
                JSONObject child = array.getJSONObject(index);
                String insert = String.format(drugsql, child.getString("YPBH"),
                        child.getString("YPMC"), PinYinUtil.getPinYinHeadChar(child.getString("YPMC")),
                        child.getString("HSL"), child.getString("YPXDWMC"),
                        child.getString("YPDDWMC")) + ";\r\n";
                Okio.buffer(Okio.appendingSink(file)).writeUtf8(insert).close();
            }
        }

    }




}
