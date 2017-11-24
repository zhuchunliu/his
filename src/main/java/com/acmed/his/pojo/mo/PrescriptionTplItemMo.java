package com.acmed.his.pojo.mo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * Created by Darren on 2017-11-24
 **/
@Data
public class PrescriptionTplItemMo {
    @ApiModelProperty("详情id")
    private Integer id;

    @ApiModelProperty("处方模板id")
    private Integer tplId;

    @ApiModelProperty("用药id")
    private Integer drugId;

    @ApiModelProperty("药品类型")
    private String drugCategory;

    @ApiModelProperty("单次剂量")
    private Integer dose;

    @ApiModelProperty("途径 用法")
    private String way;

    @ApiModelProperty("频率,用药频率")
    private Float frequency;

    @ApiModelProperty("天数")
    private Integer num;

    @ApiModelProperty("疗程")
    private Integer course;

    @ApiModelProperty("备注")
    private String memo;
}
