package com.acmed.his.pojo.zy;

import lombok.Data;

import java.util.List;

/**
 * Created by Darren on 2018-05-21
 **/
@Data
public class ZYDrugDetailObj {

    private String drugId;

    private String number;

    private String drugName;

    private String form;

    private String companyName;

    private String picPath;

    private String OTC;

    private String component;

    private String indication;

    private String goodsPrice;

    private String url;

    private List<String> drugPicList;

    @Data
    private static class ZYDrugStoreDetailObj{
        private String storeId;

        private String city;

        private String storeName;

        private String storePic;

        private String lat;

        private String lng;

        private String storeTel;

        private String onSale;

        private String ydNotice;

        private String ydNoticePart;
    }

}
