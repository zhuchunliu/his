package com.acmed.his.consts;

/**
 * Created by Darren on 2017-11-30
 **/
public enum DicTypeEnum {

    USER_CATEGORY("UserCategory","用户类型"),
    ORG_CATEGORY("OrgCategory","机构类型"),
    DRUG_CLASSIFICATION("DrugClassification","药品分类"),
    FEE_ITEM("FeeItem","诊疗项目类型"),
    CHARGE_CATEGORY("ChargeCategory","附加费用类型"),
    INSPECT_CATEGORY("InspectCategory","检查单类型"),
    APPLY_CATEGORY("ApplyCategory","挂号费类型"),
    PERMISSION("Permission","权限类型"),
    ADVICE_TPL("AdviceTpl","医嘱模板类型"),
    DIAGNOSIS_TPL("DiagnosisTpl","诊断模板类型"),
    MEDICAL_RECORD_TPL("MedicalRecordTpl","病例模板类型"),
    PRESCRIPTION("Prescription","处方类型"),
    PRE_DRUG_TPL("PreDrugTpl","药品处方模板类型"),
    PRE_INSPECT_TPL("PreInspectTpl","检查处方模板类型"),
    PACK_UNIT("PackUnit","包装单位"),
    PAYMENT_METHOD("PaymentMethod","付费方式"),
    ROUTE_OF_DRUG("RouteOfDrug","用药类型"),
    ORG_LEVEL("OrgLevel","机构等级"),
    SCHEDULE("Schedule","排班类型"),
    DUTY("Duty","职称等级"),
    DIAGNOSIS_LEVEL("DiagnosisLevel","门诊类型"),
    REFUND_REASON("RefundReason","退款理由"),
    DRUG_FREQUENCY("DrugFrequency","用药频率"),
    DRUG_FORM("DrugForm","剂型"),
    UNIT("Unit","单位"),
    USEAGE("Useage","药品用法"),
    PRESCRIPTION_TYPE("Prescription","处方类型");
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
