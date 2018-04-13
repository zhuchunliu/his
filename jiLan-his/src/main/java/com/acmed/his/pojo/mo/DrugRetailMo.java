package com.acmed.his.pojo.mo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * Created by Darren on 2018-04-13
 **/
@Data
public class DrugRetailMo {

    @ApiModelProperty("药品零售主键 null：新增;not null:编辑")
    private String id;

    @ApiModelProperty("患者信息")
    private PatientRetailMo patient;

    @ApiModelProperty
    private List<DrugRetailItemMo> drugList;

    @Data
    public static class DrugRetailItemMo{
        @ApiModelProperty("药品id")
        private Integer drugId;

        @ApiModelProperty("数量")
        private Integer num;

        @ApiModelProperty("备注")
        private String remark;

    }

    @Data
    public static class PatientRetailMo{

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
}
