package com.acmed.his.pojo.zy.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * Created by Darren on 2018-05-22
 **/
@Data
public class ZYLogisticsObj {

    @ApiModelProperty("手机号")
    private String tel;

    @ApiModelProperty("快递名称")
    private String expressName;

    @ApiModelProperty("快递编号")
    private String expressNo;

    @ApiModelProperty("快递客服电话")
    private String expressMobile;

    private List<ZYLogisticsDetailObj> detailObjList;

    @Data
    public static class ZYLogisticsDetailObj{

        @ApiModelProperty("时间")
        private String time;

        @ApiModelProperty("物流信息")
        private String context;
    }
}
