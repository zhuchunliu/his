package com.acmed.his.pojo.zy;

import com.acmed.his.pojo.zy.dto.ZYLogisticsObj;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * Created by Darren on 2018-06-04
 **/
@Data
public class ZYLogisticsVo {
    @ApiModelProperty("手机号")
    private String tel;

    @ApiModelProperty("快递名称")
    private String expressName;

    @ApiModelProperty("快递编号")
    private String expressNo;

    @ApiModelProperty("快递客服电话")
    private String expressMobile;

    @ApiModelProperty("省份名称")
    private String provinceName;

    @ApiModelProperty("省份名称")
    private String cityName;

    @ApiModelProperty("县名称")
    private String countyName;

    @ApiModelProperty("详细地址")
    private String address;

    @ApiModelProperty("收件人")
    private String recipient;

    @ApiModelProperty("收件人电话")
    private String phone;

    private List<ZYLogisticsObj.ZYLogisticsDetailObj> detailObjList;

    @Data
    public static class ZYLogisticsDetailObj{

        @ApiModelProperty("时间")
        private String time;

        @ApiModelProperty("物流信息")
        private String context;
    }
}
