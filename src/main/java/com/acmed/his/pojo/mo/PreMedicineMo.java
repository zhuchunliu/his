package com.acmed.his.pojo.mo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * 药品处方
 * Created by Darren on 2017-11-22
 **/
@Data
public class PreMedicineMo {

    @ApiModelProperty
    private Integer id;

    @ApiModelProperty("挂号单号")
    private Integer applyId;

    @ApiModelProperty("药品集合")
    private List<Item> itemList;

    @ApiModelProperty("附加费")
    private List<Charge> chargeList;

    @Data
    public class Item{
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
    }

    @Data
    public class Charge{
        @ApiModelProperty("费用类型")
        private String category;
    }

}
