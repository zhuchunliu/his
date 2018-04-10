package com.acmed.his.consts;

import java.util.Optional;

/**
 * 掌药对接地址
 * Created by Darren on 2018-04-10
 **/
public class ZhangYaoConstant {

    private final static String DRUG_URL = "r=jizhi/search/index";//药品查询接口

    private final static String ORDER_QUERY_URL = "r=jizhi/order/query-order";//订单查询接口

    /**
     * 获取药品信息
     * @param keyword 搜索关键词
     * @param start 起始行
     * @param rows 需要的行数
     * @param first 排序类型 1优先,3距离优先
     * @param lat 经度
     * @param lng 纬度
     * @param storeId 店铺id
     * @return
     */
    public static String buildDrugUrl(String keyword,Integer start,Integer rows,Integer first,Double lat,Double lng,Double storeId){
        StringBuilder builder = new StringBuilder();
        builder.append("?").append(DRUG_URL).
                append(Optional.ofNullable(keyword).map(obj->"&keyword="+obj).orElse("")).
                append(Optional.ofNullable(start).map(obj->"&start="+obj).orElse("")).
                append(Optional.ofNullable(rows).map(obj->"&rows="+obj).orElse("")).
                append(Optional.ofNullable(first).map(obj->"&first="+obj).orElse("")).
                append(Optional.ofNullable(lat).map(obj->"&lat="+obj).orElse("")).
                append(Optional.ofNullable(lng).map(obj->"&lng="+obj).orElse("")).
                append(Optional.ofNullable(storeId).map(obj->"&storeId="+obj).orElse(""));
        return builder.toString();
    }

    /**
     * 支付信息查询
     * @param orderSn 订单号
     * @return
     */
    public static String buildOrderQueryUrl(String orderSn){
        StringBuilder builder = new StringBuilder();
        builder.append("?").append(ORDER_QUERY_URL).
                append(Optional.ofNullable(orderSn).map(obj->"&orderSn="+obj).orElse(""));
        return builder.toString();
    }
}
