package com.acmed.his.pojo.mo;

import com.acmed.his.model.Inspect;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * Created by Darren on 2017-11-30
 **/
@Data
public class  PreMo {

    @ApiModelProperty("处方主键")
    private String id;

    @ApiModelProperty("挂号主键 null:新开就诊；非null:挂号就诊")
    private String applyId;

    @ApiModelProperty("是否完成 0:未完成，1：完成")
    private Integer isFinish;

    @ApiModelProperty("处方编号")
    private String prescriptionNo;

    @ApiModelProperty("挂号单号")
    private String clinicNo;

    @ApiModelProperty("处方集合")
    private List<PrescriptMo> PreList;

    @ApiModelProperty("患者信息")
    private PatientMo patient;

    @ApiModelProperty("病例")
    private MedicalRecordMo record;

    @ApiModelProperty("注射单")
    private List<List<InjectMo>> injectList;



    @Data
    public static class PrescriptMo {
        @ApiModelProperty("药品集合")
        private List<PreMo.ItemMo> itemList;

        @ApiModelProperty("附加费")
        private List<PreMo.ChargeMo> chargeList;

        @ApiModelProperty("检查集合")
        private List<PreMo.InspectMo> inspectList;

        @ApiModelProperty("要求")
        private String requirement;

        @ApiModelProperty("备注")
        private String remark;

    }

    @Data
    public static class ItemMo{

        @ApiModelProperty("处方药品主键")
        private String itemId;

        @ApiModelProperty("药品id")
        private Integer drugId;

        @ApiModelProperty("途径")
        private String way;

        @ApiModelProperty("数量")
        private Integer num;

        @ApiModelProperty("单位类型 1：一级计价单位，2：二级计价单位")
        private Integer unitType;

        @ApiModelProperty("频率 字典表：DrugFrequency")
        private Integer frequency;

        @ApiModelProperty("单次剂量")
        private Double singleDose;

        @ApiModelProperty("备注")
        private String memo;

        @ApiModelProperty("药品名称 -掌药")
        private String drugName;

        @ApiModelProperty("药品单位名称 -掌药")
        private String unitName;

        @ApiModelProperty("药店id - 掌药")
        private String storeId;

        @ApiModelProperty("药店名称 - 掌药")
        private String storeName;

        @ApiModelProperty("药品类型 - 掌药")
        private String spec;

        @ApiModelProperty("药品厂家 - 掌药")
        private String manufacturerName;

        @ApiModelProperty("药品价格 - 掌药")
        private Double retailPrice;
    }

    @Data
    public static class ChargeMo{

        @ApiModelProperty("处方附加主键")
        private String chargeId;

        @ApiModelProperty("费用类型")
        private String category;
    }

    @Data
    public static class InspectMo{

        @ApiModelProperty("处方检查主键")
        private String inspectId;

        @ApiModelProperty("检查目的")
        private String aim;

        @ApiModelProperty("检查部位")
        private String part;

        @ApiModelProperty("检查类型")
        private String category;

        @ApiModelProperty("病情摘要")
        private String summary;

        @ApiModelProperty("检查诊断")
        private String diagnosis;

        @ApiModelProperty("备注")
        private String memo;

    }

    @Data
    public static class PatientMo{

        @ApiModelProperty("患者详情主键")
        private String patientItemId;

        @ApiModelProperty("用户姓名")
        private String realName;

        @ApiModelProperty("性别 0:男;1:女")
        private String gender;

        @ApiModelProperty("身份证")
        private String idCard;

        @ApiModelProperty("出生日期")
        private String dateOfBirth;

        @ApiModelProperty("社保卡")
        private String socialCard;

        @ApiModelProperty("号码")
        private String mobile;

        @ApiModelProperty("地址")
        private String address;

        @ApiModelProperty("过敏史")
        private String anaphylaxis;

    }


    @Data
    public static class MedicalRecordMo{
        @ApiModelProperty("主诉")
        private String chiefComplaint;

        @ApiModelProperty("诊断信息")
        private String diagnosis;

        @ApiModelProperty("医嘱")
        private String advice;

        @ApiModelProperty("1 初诊、0 复诊")
        private String isFirst;

        @ApiModelProperty("发病日期")
        private String onSetDate;

        @ApiModelProperty("是否传染病 0：否；1：是")
        private String isContagious;

        @ApiModelProperty("备注")
        private String remark;

    }


    @Data
    public static class InjectMo{
        @ApiModelProperty("药品id")
        private Integer drugId;

        @ApiModelProperty("单次剂量")
        private Double singleDose;

        @ApiModelProperty("备注")
        private String memo;

        @ApiModelProperty("频率 字典表：DrugFrequency")
        private Integer frequency;
    }

}
