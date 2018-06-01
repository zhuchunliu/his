package com.acmed.his.service;

import com.acmed.his.dao.InsuranceOrderMapper;
import com.acmed.his.model.InsuranceOrder;
import com.acmed.his.util.PageResult;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * InsuranceOrderManager
 * 保险
 * @author jimson
 * @date 2018/4/19
 */
@Service
public class InsuranceOrderManager {

    @Autowired
    private InsuranceOrderMapper insuranceOrderMapper;

    @Autowired
    private Environment environment;

    /**
     * 每日跑批
     */
    //@Scheduled(cron = "0 0 2 * * ?")
    public void task(){
        Integer property = Integer.valueOf(environment.getProperty("insurance.price"));
        LocalDateTime now = LocalDateTime.now();
        String start = now.minusDays(1).format(DateTimeFormatter.ofPattern("yyyy-MM-dd 00:00:00"));
        String end = now.minusDays(1).format(DateTimeFormatter.ofPattern("yyyy-MM-dd 23:59:59"));
        InsuranceOrder insuranceOrder = new InsuranceOrder();
        insuranceOrder.setAppointmentTime(start);
        List<InsuranceOrder> select = insuranceOrderMapper.select(insuranceOrder);
        if (select.size()==0){
            insuranceOrderMapper.insertInsuranceOrderList(now.toString(),start,start,end,property);
            insuranceOrderMapper.updateId(start);
        }
    }


    public InsuranceOrder getById(String id){
        return insuranceOrderMapper.selectByPrimaryKey(id);
    }

    @Transactional
    public int update(InsuranceOrder insuranceOrder){
        insuranceOrder.setCreateAt(null);
        insuranceOrder.setModifyAt(LocalDateTime.now().toString());
        return insuranceOrderMapper.updateByPrimaryKeySelective(insuranceOrder);
    }


    public PageResult<InsuranceOrder> insuranceOrderList(Integer pageNum,Integer pageSize,Integer userId,Integer isPaid,String startTime,String endTime){
        Example example = new Example(InsuranceOrder.class);
        if (isPaid==null){
            example.createCriteria().andEqualTo("userId",userId).andBetween("appointmentTime",startTime,endTime);
        }else if (isPaid==1){
            example.createCriteria().andEqualTo("userId",userId).andEqualTo("fee",0).orIsNotNull("payId").andBetween("appointmentTime",startTime,endTime);
        }else {
            example.createCriteria().andEqualTo("userId",userId).orIsNotNull("payId").andBetween("appointmentTime",startTime,endTime);
        }
        example.setOrderByClause("createAt desc");
        PageHelper.startPage(pageNum,pageSize);
        List<InsuranceOrder> insuranceOrders = insuranceOrderMapper.selectByExample(example);
        PageResult<InsuranceOrder> pageResult = new PageResult<>();
        pageResult.setPageNum(pageNum);
        pageResult.setPageSize(pageSize);
        PageInfo<InsuranceOrder> pageInfo = new PageInfo<>(insuranceOrders);
        pageResult.setData(insuranceOrders);
        pageResult.setTotal(pageInfo.getTotal());
        return pageResult;
    }

}
