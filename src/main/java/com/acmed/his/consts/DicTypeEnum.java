package com.acmed.his.consts;

/**
 * Created by Darren on 2017-11-30
 **/
public enum DicTypeEnum {

    ADVICE("adviceTpl","医嘱模板类型"),
    DIAGNOSIS("diagnosisTpl","诊断模板类型"),
    PRE_DRUG("preDrugTpl","药品处方模板类型"),
    PRE_INSPECT("preInspectTpl","检查处方模板类型");

    DicTypeEnum(String code,String name){
        this.code = code;
        this.name = name;
    }

    private String code;
    private String name;

    public String getCode() {
        return code;
    }
}
