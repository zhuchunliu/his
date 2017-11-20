package com.acmed.his.util;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2017-08-26.
 */
public class CreateParamMap {
    /**
     * 组装参数map
     * @param orderid
     * @param pid
     * @param id
     * @return
     */
    public static Map<String,String> createMap(String orderid,String caroutid, String pid, String id){
        Map<String,String> map = new HashMap<String,String>();
        map.put("orderid",orderid);
        map.put("caroutid",caroutid);
        map.put("pid",pid);
        map.put("id",id);
        return map;
    }
}
