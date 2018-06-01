package com.acmed.his.model.fzw;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import tk.mybatis.mapper.annotation.NameStyle;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * FZWServicePackage
 *
 * @author jimson
 * @date 2018/5/18
 */
@Data
@Table(name = "tb_fzw_service_package")
@NameStyle
public class FZWServicePackage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @ApiModelProperty("服务包名字")
    private String name;
    @ApiModelProperty("服务包内容")
    private String content;
    @ApiModelProperty("服务包价格   单位分")
    private Integer price;
}
