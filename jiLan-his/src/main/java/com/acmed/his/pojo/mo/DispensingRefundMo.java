package com.acmed.his.pojo.mo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * Created by Darren on 2018-01-22
 **/
@Data
public class DispensingRefundMo {
    @ApiModelProperty("挂号id")
    private String applyId;

    @ApiModelProperty("付费类型")
    private String feeType;

    @ApiModelProperty("退款明细")
    private List<RefundMo> moList;

    @Data
    public class RefundMo{
        @ApiModelProperty("主键")
        private String id;

        @ApiModelProperty("类型：1:药，2：检查，3：附加")
        private Integer type;
    }
}
