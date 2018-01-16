package com.acmed.his.service;

import com.acmed.his.dao.CommonMapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by Darren on 2017-11-20
 **/
@Service
public class CommonManager {

    @Autowired
    private CommonMapper commonMapper;


    /**
     * 取得下一个序列，如果没有创建序列
     * @param name
     * @return
     */
    public String getNextVal(String name){
        return commonMapper.getNextVal(name);
    }

    /**
     * 获取格式化后的数字内容
     * @param name 格式化字段
     * @param pattern 格式样式
     * @return 格式化后的值
     */
    public String getFormatVal(String name, String pattern) {
        java.text.DecimalFormat df = new java.text.DecimalFormat(pattern);
        String val=this.getNextVal(name);
        return StringUtils.isNotBlank(val) ? df.format((new Integer(val)).intValue()) : null;
    }


    /**
     * 生成病人编号
     * @return
     */
    public String buildPatientNo() {
        String code=null;
        String pattern="000000000";
        java.text.DecimalFormat df = new java.text.DecimalFormat(pattern);
        String val = getNextVal("PatientNo");
        if(val!=null&&!val.isEmpty())
        {
            code=df.format((new Integer(val)).intValue());
        }

        return code;
    }

    public static void main(String[] args) {
        java.text.DecimalFormat df = new java.text.DecimalFormat("00000");

        System.err.println(df.format(10));
    }
}
