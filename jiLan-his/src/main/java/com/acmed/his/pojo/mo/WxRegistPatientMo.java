package com.acmed.his.pojo.mo;

import com.acmed.his.model.Patient;
import lombok.Data;

/**
 * WxRegistPatientMo
 * 微信注册
 * @author jimson
 * @date 2017/11/22
 */
@Data
public class WxRegistPatientMo extends Patient{
    private String code;
}
