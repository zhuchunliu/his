package com.acmed.his.pojo.vo;

import com.acmed.his.model.dto.PreTitleDto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * Created by Darren on 2017-11-22
 **/
@Data
public class PreTitleVo {

    @ApiModelProperty("主键")
    private Integer id;
    @ApiModelProperty("编号")
    private String number;

    public PreTitleVo(PreTitleDto dto){
        this.id = dto.getId();
        this.number = dto.getPrescriptionNo();
    }
}
