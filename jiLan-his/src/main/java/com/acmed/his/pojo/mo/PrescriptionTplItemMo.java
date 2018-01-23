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
    public class ItemMo {

        @ApiModelProperty("药品编码")
        private String drugCode;

        @ApiModelProperty("数量")
        private Integer num;

        @ApiModelProperty("单次剂量")
        private Integer dose;

        @ApiModelProperty("频率,用药频率")
        private Float frequency;

        @ApiModelProperty("备注")
        private String memo;
    }

}
