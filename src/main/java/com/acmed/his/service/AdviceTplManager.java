package com.acmed.his.service;

import com.acmed.his.dao.AdviceTplMapper;
import com.acmed.his.model.AdviceTpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

/**
 * Created by Darren on 2017-11-20
 **/
@Service
public class AdviceTplManager {
    @Autowired
    private AdviceTplMapper adviceTplMapper;

    public void add(){
        AdviceTpl adviceTpl = new AdviceTpl();
        adviceTpl.setAdvice("advice");
        adviceTpl.setIsValid("0");
        adviceTpl.setOrgCode(10001);
        adviceTplMapper.insert(adviceTpl);
    }

    public List<AdviceTpl> info(){
        Example example = new Example(AdviceTpl.class);
        example.createCriteria().andEqualTo("orgCode","10001");
        example.orderBy("id");
        return adviceTplMapper.selectByExample(example);
    }

}
