package com.acmed.his.pojo.mo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * Created by Darren on 2017-11-30
 **/
@Data
public class PreMo {

    @ApiModelProperty("处方主键")
    private String id;

    @ApiModelProperty("挂号主键 null:新开就诊；非null:挂号就诊")
    private String applyId;

    @ApiModelProperty("处方集合")
    private List<PrescriptMo> PreList;

    @ApiModelProperty("患者信息")
    private PatientMo patient;

    @ApiModelProperty("病例")
    private MedicalRecordMo record;

    @Data
    public class PrescriptMo {
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
        @ApiModelProperty("药品id")
        private Integer drugId;

        @ApiModelProperty("途径")
        private String way;

        @ApiModelProperty("数量")
        private Integer num;

        @ApiModelProperty("频率")
        private Integer frequency;

        @ApiModelProperty("单次剂量")
        private Integer dose;

        @ApiModelProperty("备注")
        private String memo;
    }

    @Data
    public static class ChargeMo{
        @ApiModelProperty("费用类型")
        private String category;
    }

    @Data
    public static class InspectMo{

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
    public class PatientMo{
        @ApiModelProperty("用户主键 非必填 null:系统自动添加用户；not null：系统编辑用户")
        private String patientId;

        @ApiModelProperty("用户姓名")
        private String userName;

        @ApiModelProperty("性别 0:男;1:女")
        private String gender;

        @ApiModelProperty("出生日期")
        private String dateOfBirth;

        @ApiModelProperty("号码")
        private String mobile;

        @ApiModelProperty("地址")
        private String address;

        @ApiModelProperty("过敏史")
        private String anaphylaxis;

    }


    @Data
    public class MedicalRecordMo{
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

}
