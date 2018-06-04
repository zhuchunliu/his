package com.acmed.his.pojo.zy;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * Created by Darren on 2018-05-24
 **/
@Data
public class ZYOrderSubmitPayMo {

    @ApiModelProperty("地址id")
    private Integer addressId;

    @ApiModelProperty("详情")
    private List<ZYOrderSubmitPayDetailMo> detailMoList;

    @Data
    public class ZYOrderSubmitPayDetailMo {

        @ApiModelProperty("订单id")
        private String orderId;

        @ApiModelProperty("快递主键")
        private String expressId;

        @ApiModelProperty("快递费用")
        private Double expressFee;

        @ApiModelProperty("快递名称")
        private String expressName;

        @ApiModelProperty("详情主键")
        private List<String> itemIdList;
    }
}
