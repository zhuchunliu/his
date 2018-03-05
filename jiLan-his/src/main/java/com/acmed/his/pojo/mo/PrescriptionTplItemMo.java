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

    private List<ItemMo> list;

    @Data
    public static class ItemMo {

        @ApiModelProperty("药品编码")
        private String drugCode;

        @ApiModelProperty("数量")
        private Integer num;

        @ApiModelProperty("单位类型 1：一级计价单位，2：二级计价单位")
        private Integer unitType;

        @ApiModelProperty("单次剂量")
        private Integer dose;

        @ApiModelProperty("频率,用药频率")
        private String frequency;

        @ApiModelProperty("备注")
        private String memo;
    }

}
