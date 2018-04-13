package com.acmed.his.service;

import com.acmed.his.constants.StatusCode;
import com.acmed.his.dao.*;
import com.acmed.his.exceptions.BaseException;
import com.acmed.his.model.*;
import com.acmed.his.pojo.mo.DrugRetailMo;
import com.acmed.his.pojo.mo.DrugRetailQueryMo;
import com.acmed.his.pojo.mo.PreMo;
import com.acmed.his.pojo.vo.DrugRetailVo;
import com.acmed.his.pojo.vo.UserInfo;
import com.acmed.his.util.DateTimeUtil;
import com.acmed.his.util.PageResult;
import com.acmed.his.util.UUIDUtil;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Created by Darren on 2018-04-13
 **/
@Service
public class DrugRetailManager {

    @Autowired
    private DrugRetailMapper drugRetailMapper;

    @Autowired
    private DrugRetailItemMapper drugRetailItemMapper;

    @Autowired
    private DrugRetailItemStockMapper drugRetailItemStockMapper;

    @Autowired
    private PrescriptionManager prescriptionManager;

    @Autowired
    private DrugMapper drugMapper;

    @Autowired
    private DrugStockMapper drugStockMapper;

    @Autowired
    private ManufacturerMapper manufacturerMapper;

    @Autowired
    private PayManager payManager;

    /**
     * 零售列表
     *
     * @param pageNum
     * @param pageSize
     * @param mo
     * @return
     */
    public PageResult getRetailList(Integer pageNum, Integer pageSize, DrugRetailQueryMo mo, UserInfo userInfo) {
        Page page = PageHelper.startPage(pageNum,pageSize);
        PageResult result = new PageResult();
        result.setData(drugRetailMapper.getRetailList(mo,userInfo.getOrgCode()));
        result.setTotal(page.getTotal());
        return result;
    }

    /**
     * 添加零售信息
     * @param mo
     * @param userInfo
     */
    @Transactional
    public void saveRetail(DrugRetailMo mo, UserInfo userInfo) {
        //step1: 处理患者信息
        PreMo.PatientMo patientMo = new PreMo.PatientMo();
        BeanUtils.copyProperties(mo.getPatient(),patientMo);
        PatientItem patientItem = prescriptionManager.handlePatient(patientMo,userInfo);//处理患者信息

        // step2: 处理零售详情
        if(StringUtils.isNotEmpty(mo.getId())) {//编辑时候，先清除老的零售详情信息
            Example example = new Example(DrugRetailItem.class);
            example.createCriteria().andEqualTo("retailId", mo.getId());
            drugRetailItemMapper.deleteByExample(example);
        }

        String retailId = StringUtils.isEmpty(mo.getId())?UUIDUtil.generate():mo.getId();//定义零售主键
        double fee = 0d;
        for(int index = 0; index < mo.getDrugList().size(); index++) {//添加零售详情
            DrugRetailItem item = new DrugRetailItem();
            BeanUtils.copyProperties(mo.getDrugList().get(index),item);
            item.setId(UUIDUtil.generate());
            item.setRetailId(retailId);

            Drug drug = drugMapper.selectByPrimaryKey(item.getDrugId());
            item.setBid(drug.getBid());
            item.setRetailPrice(drug.getRetailPrice());
            item.setFee(Optional.ofNullable(item.getRetailPrice()).orElse(0d)*Optional.ofNullable(item.getNum()).orElse(0));
            drugRetailItemMapper.insert(item);

            fee += item.getFee();
        }

        // step3: 处理零售信息

        DrugRetail drugRetail = Optional.ofNullable(mo.getId()).map(drugRetailMapper::selectByPrimaryKey).orElse(new DrugRetail());
        drugRetail.setPatientId(patientItem.getPatientId());
        drugRetail.setPatientItemId(patientItem.getId());
        drugRetail.setAge(DateTimeUtil.getAge(patientItem.getDateOfBirth()));
        drugRetail.setFee(fee);
        if(StringUtils.isNotEmpty(drugRetail.getId())){//更新零售信息
            drugRetail.setModifyAt(LocalDate.now().toString());
            drugRetail.setModifyBy(userInfo.getId().toString());
            drugRetailMapper.updateByPrimaryKey(drugRetail);
        }else{
            drugRetail.setId(retailId);
            drugRetail.setOrgCode(userInfo.getOrgCode());
            drugRetail.setCreateAt(LocalDate.now().toString());
            drugRetail.setCreateBy(userInfo.getId().toString());
            drugRetail.setRemoved("0");
            drugRetail.setPayStatus(0);
            drugRetailMapper.insert(drugRetail);
        }



    }

    /**
     * 零售
     * @param id
     * @param feeType
     * @param user
     */
    @Transactional
    public void pay(String id, String feeType, UserInfo user) {
        //step1: 付费
        DrugRetail retail = drugRetailMapper.selectByPrimaryKey(id);
        if(retail.getPayStatus() ==1){
            throw new BaseException(StatusCode.FAIL,"已付费订单禁止重复收费!");
        }

        retail.setPayStatus(1);
        retail.setFeeType(feeType);
        retail.setModifyBy(user.getId().toString());
        retail.setModifyAt(LocalDate.now().toString());
        drugRetailMapper.updateByPrimaryKey(retail);

        // step2：扣除库存
        synchronized ("org_" + user.getOrgCode()) {
            Example example = new Example(DrugRetailItem.class);
            example.createCriteria().andEqualTo("retailId", id);
            List<DrugRetailItem> itemList = drugRetailItemMapper.selectByExample(example);

            for (DrugRetailItem item : itemList) {
                List<PrescriptionItemStock> itemStockList = Lists.newArrayList();
                Drug drug = drugMapper.selectByPrimaryKey(item.getDrugId());
                //判断库存是否满足：大单位,小单位，剂量单位
                if (drug.getNum() < item.getNum()) {
                    throw new BaseException(StatusCode.FAIL, Optional.ofNullable(drug.getGoodsName()).orElse(drug.getName()) + "库存不足");
                } else {
                    drug.setNum(drug.getNum() - item.getNum());
                    drugMapper.updateByPrimaryKey(drug);//扣除药品库存

                    List<DrugStock> drugStockList = drugStockMapper.getByDrugId(drug.getId());
                    Integer num = item.getNum();
                    for (DrugStock drugStock : drugStockList) {

                        if (num == 0) {
                            break;
                        }
                        int occupyNum = drugStock.getNum() < num ? drugStock.getNum() : num;
                        drugStock.setNum(drugStock.getNum() - occupyNum);
                        drugStockMapper.updateByPrimaryKey(drugStock);

                        DrugRetailItemStock itemStock = new DrugRetailItemStock();
                        itemStock.setId(UUIDUtil.generate());
                        itemStock.setRetailId(item.getRetailId());
                        itemStock.setDrugId(item.getDrugId());
                        itemStock.setBatchNumber(drugStock.getBatchNumber());
                        itemStock.setExpiryDate(drugStock.getExpiryDate());
                        itemStock.setNum(occupyNum);
                        drugRetailItemStockMapper.insert(itemStock);

                        num = num - occupyNum;
                    }
                }
            }
        }

        //step3：记录日志
        PayStatements payStatements = new PayStatements();
        payStatements.setFeeType(feeType);
        payStatements.setRetailId(id);
        payStatements.setOrgCode(user.getOrgCode());
        payStatements.setSource("5");
        payStatements.setPatientId(retail.getPatientId());
        payStatements.setFee(new BigDecimal(retail.getFee()));
        payStatements.setCreateAt(LocalDateTime.now().toString());
        payStatements.setCreateBy(user.getId().toString());
        payManager.addPayStatements(payStatements);
    }



    /**
     * 删除零售信息
     * @param id
     * @param user
     */
    public void delRetail(String id, UserInfo user) {
        DrugRetail retail = drugRetailMapper.selectByPrimaryKey(id);
        retail.setRemoved("1");
        retail.setModifyBy(user.getId().toString());
        retail.setModifyAt(LocalDate.now().toString());
        drugRetailMapper.updateByPrimaryKey(retail);
    }

    /**
     * 获取零售详情
     *
     * @param id
     * @return
     */
    public Object getRetailDetail(String id) {
        DrugRetail retail = drugRetailMapper.selectByPrimaryKey(id);
        DrugRetailVo vo = new DrugRetailVo();
        BeanUtils.copyProperties(retail,vo);

        Example example = new Example(DrugRetailItem.class);
        example.createCriteria().andEqualTo("retailId",id);

        List<DrugRetailVo.DrugRetailItemVo> list = Lists.newArrayList();
        drugRetailItemMapper.selectByExample(example).forEach(obj->{
            DrugRetailVo.DrugRetailItemVo itemVo = new DrugRetailVo.DrugRetailItemVo();
            BeanUtils.copyProperties(obj,itemVo);

            Drug drug = drugMapper.selectByPrimaryKey(obj.getDrugId());
            if(null != drug){
                itemVo.setGoodsName(drug.getGoodsName());
                itemVo.setName(drug.getName());
                itemVo.setSpec(drug.getSpec());
                itemVo.setManufacturerName(Optional.ofNullable(drug.getManufacturer()).map(manufacturerMapper::selectByPrimaryKey).
                        map(Manufacturer::getName).orElse(""));
            }
            list.add(itemVo);

        });
        vo.setItemList(list);
        return vo;
    }
}
