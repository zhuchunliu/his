package com.acmed.his.pojo.mo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by Darren on 2018-01-23
 **/
@Data
public class PrescriptionTplItemMo {

    @ApiModelProperty("处方模板id")
    private Integer tplId;

    private List<TplItemMo> list;

    @Data
    public static class TplItemMo {

        @ApiModelProperty("药品id")
        private String drugId;

        @ApiModelProperty("数量")
        private Integer num;

        @ApiModelProperty("单位类型 1：一级计价单位，2：二级计价单位")
        private Integer unitType;

        @ApiModelProperty("单次剂量")
        private Double singleDose;

        @ApiModelProperty("频率,用药频率")
        private Integer frequency;

        @ApiModelProperty("备注")
        private String memo;
    }

}
