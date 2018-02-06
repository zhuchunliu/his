package com.acmed.his.model.dto;

import com.acmed.his.model.MedicalRecordTpl;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * MedicalRecordTplDto
 *
 * @author jimson
 * @date 2018/1/25
 */
@Data
public class MedicalRecordTplDto extends MedicalRecordTpl {
    @ApiModelProperty("病例模板类型 名")
    private String categoryName;

    @ApiModelProperty("创建人")
    private String createUserName;
}
