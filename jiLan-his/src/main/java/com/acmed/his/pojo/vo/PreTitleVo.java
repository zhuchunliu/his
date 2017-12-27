package com.acmed.his.pojo.vo;

import com.acmed.his.model.Prescription;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * Created by Darren on 2017-11-22
 **/
@Data
public class PreTitleVo {

    @ApiModelProperty("主键")
    private String id;
    @ApiModelProperty("编号")
    private String number;
    @ApiModelProperty("处方类型 1:药品处方，2：检查处方")
    private String category;

    public PreTitleVo(Prescription prescription){
        this.id = prescription.getId();
        this.number = prescription.getPrescriptionNo();
        this.category = prescription.getCategory();
    }
}
