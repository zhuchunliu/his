package com.acmed.his.support;

import com.acmed.his.model.Patient;
import com.acmed.his.model.User;
import com.acmed.his.pojo.vo.PatientInfoVo;
import com.acmed.his.pojo.vo.UserInfo;
import lombok.Data;
import org.springframework.beans.BeanUtils;

/**
 * Created by Darren on 2017-11-23
 **/
@Data
public class AccessInfo {
    private Integer source;//登录途径 1：患者微信;2:老板微信;3:医生Pad
    private Integer patientId;//当前token对应的患者主键
    private Integer userId;//当前token对应的用户主键
    private PatientInfoVo patient;//当前token对应的患者信息
    private UserInfo user;//当前token对应的用户信息

    public AccessInfo(){

    }

    public AccessInfo(Patient patient, User user, Integer source) {
        this.source = source;
        if(null != patient){
            this.patientId = patient.getId();
            this.patient = new PatientInfoVo();
            BeanUtils.copyProperties(patient,this.patient);
        }

        if(null != user){
            this.userId = user.getId();
            this.user = new UserInfo();
            BeanUtils.copyProperties(user,this.user);

        }
    }
}
