package com.acmed.his.pojo.vo;

import lombok.Data;

/**
 * SuppliersOrderVo
 *
 * @author jimson
 * @date 2017/12/25
 */
@Data
public class SuppliersOrderVo extends AccompanyingOrderPatientVo{
    /**
     * 描述
     */
    private String caseDetail;
    /**
     * 描述图片','拼接
     */
    private String casePictures;
    /**
     * 服务费
     */
    private String servicePrice;
    /**
     * 陪诊费
     */
    private String accompanyingPrice;
}
