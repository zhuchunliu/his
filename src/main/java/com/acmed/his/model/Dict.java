package com.acmed.his.model;

import lombok.Data;
import tk.mybatis.mapper.annotation.NameStyle;

import javax.persistence.Table;

/**
 * 测试
 * Created by Issac on 2017/11/18 0018.
 */
@Data
@Table(name = "t_p_dict")
@NameStyle
public class Dict {

    private String itemName;

    private String itemCode;
}
