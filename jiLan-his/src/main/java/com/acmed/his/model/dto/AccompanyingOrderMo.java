package com.acmed.his.model.dto;

import com.acmed.his.model.AccompanyingOrder;
import lombok.Data;

/**
 * AccompanyingOrderMo
 *
 * @author jimson
 * @date 2018/3/5
 */
@Data
public class AccompanyingOrderMo extends AccompanyingOrder {
    private String orderBy;
}
