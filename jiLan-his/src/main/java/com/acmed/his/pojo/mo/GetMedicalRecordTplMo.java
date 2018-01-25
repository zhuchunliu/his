package com.acmed.his.pojo.mo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * GetMedicalRecordTplMo
 *
 * @author jimson
 * @date 2018/1/19
 */
@Data
public class GetMedicalRecordTplMo {
    @ApiModelProperty("模板名 或者拼音 王五/WW")
    private String tplName;

    @ApiModelProperty("病例模板类型 字典表 MedicalRecordTpl")
    private String category;

    @ApiModelProperty("医疗机构编码 不传表示自己 0 表示全部 其他指定")
    private Integer orgCode;

    @ApiModelProperty("科室id 不传表示自己 0 表示全部 其他指定")
    private Integer dept;

    @ApiModelProperty("是否是自己的 0 不是自己  1是自己  不传表示全部")
    private Integer isSelf;

    @ApiModelProperty("0 未删除  1 已删除 不传给全部")
    private String removed;
}
