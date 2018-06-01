package com.acmed.his.consts;

import java.util.Optional;

/**
 * 掌药对接地址
 * Created by Darren on 2018-04-10
 **/
public class ZhangYaoConstant {

    private final static String DRUG_LIST_URL = "r=jizhi/search/index";//药品查询接口

    private final static String DRUG_DETAIL_URL = "r=jizhi/goods/detail";//药品详情接口

    private final static String STORE_URL ="r=jizhi/store/detail";//药店详情

    private final static String ORDER_QUERY_URL = "r=jizhi/order/query-order";//订单查询接口

    private final static String CITY_URL ="r=jizhi/search/area";//地址列表

    private final static String EXPRESS_URL ="r=jizhi/search/express";//获取快递列表

    private final static String LOGISTICS_URL ="r=jizhi/search/logistics";//物流地址

    private final static String ORDER_URL = "?r=jizhi/order/place";//下单地址
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
    public static String buildDrugListUrl(String keyword,Integer start,Integer rows,Integer first,String lat,String lng,String storeId){
        StringBuilder builder = new StringBuilder();
        builder.append("?").append(DRUG_LIST_URL).
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
     * 获取药品详情
     *
     * @param storeId 药店id
     * @param drugId 药品id
     * @return
     */
    public static String buildDrugDetailUrl(String storeId,String drugId){
        StringBuilder builder = new StringBuilder();
        builder.append("?").append(DRUG_DETAIL_URL).
                append(Optional.ofNullable(storeId).map(obj->"&storeId="+storeId).orElse("")).
                append(Optional.ofNullable(drugId).map(obj->"&drugId="+drugId).orElse(""));
        return builder.toString();
    }

    /**
     * 获取药店详情
     *
     * @param storeId 药店id
     * @return
     */
    public static String buildStoreDetailUrl(String storeId){
        StringBuilder builder = new StringBuilder();
        builder.append("?").append(STORE_URL).
                append(Optional.ofNullable(storeId).map(obj->"&storeId="+storeId).orElse(""));
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

    /**
     * 获取省市县信息
     *
     * @param areaId 省市县id
     * @return
     */
    public static String buildCityUrl(String areaId){
        StringBuilder builder = new StringBuilder();
        builder.append("?").append(CITY_URL).
                append(Optional.ofNullable(areaId).map(obj->"&areaId="+areaId).orElse(""));
        return builder.toString();
    }

    /**
     * 获取快递信息
     *
     * @param storeId 药店id
     * @param provinceId 省份id
     * @return
     */
    public static String buildExpressUrl(String storeId, String provinceId){
        StringBuilder builder = new StringBuilder();
        builder.append("?").append(EXPRESS_URL).
                append(Optional.ofNullable(storeId).map(obj->"&storeId="+storeId).orElse("")).
                append(Optional.ofNullable(provinceId).map(obj->"&provinceId="+provinceId).orElse(""));
        return builder.toString();
    }

    /**
     * 根据订单号获取物流信息
     * @param orderSn
     * @return
     */
    public static String buildLogisticsUrl(String orderSn){
        StringBuilder builder = new StringBuilder();
        builder.append("?").append(LOGISTICS_URL).
                append(Optional.ofNullable(orderSn).map(obj->"&orderSn="+orderSn).orElse(""));
        return builder.toString();
    }

    /**
     * 下单地址
     *
     * @return
     */
    public static String buildOrderUrl(){
        StringBuilder builder = new StringBuilder();
        builder.append("?").append(ORDER_URL);
        return builder.toString();
    }

}
