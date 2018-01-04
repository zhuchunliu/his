package com.acmed.his.service;

import com.acmed.his.dao.DrugDictMapper;
import com.acmed.his.dao.DrugMapper;
import com.acmed.his.dao.PurchaseItemMapper;
import com.acmed.his.dao.PurchaseMapper;
import com.acmed.his.model.*;
import com.acmed.his.model.dto.DrugStockDto;
import com.acmed.his.model.dto.PurchaseDto;
import com.acmed.his.pojo.vo.UserInfo;
import com.acmed.his.util.UUIDUtil;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Created by Darren on 2018-01-03
 **/
@Service
public class PurchaseManager {

    @Autowired
    private PurchaseMapper purchaseMapper;

    @Autowired
    private PurchaseItemMapper purchaseItemMapper;

    @Autowired
    private DrugMapper drugMapper;

    @Autowired
    private DrugDictMapper drugDictMapper;

    /**
     * 采购入库
     * @param mo
     * @param info
     */
    @Transactional
    public void save(PurchaseMo mo, UserInfo info) {

        Purchase purchase = Optional.ofNullable(mo.getId()).
                map(id->purchaseMapper.selectByPrimaryKey(id)).
                orElse(new Purchase());

        BeanUtils.copyProperties(mo,purchase);
        purchase.setOrgCode(info.getOrgCode());
        purchase.setRemoved("0");
        if(1 == mo.getStatus()){
            purchase.setAuditUserId(info.getId());
            purchase.setAuditDate(LocalDateTime.now().toString());
        }
        if(StringUtils.isEmpty(mo.getId())) {
            purchase.setId(UUIDUtil.generate());
            purchase.setUserId(info.getId());
            purchase.setCreateAt(LocalDateTime.now().toString());
            purchase.setCreateBy(info.getId().toString());
            purchaseMapper.insert(purchase);
        }else {
            purchase.setModifyAt(LocalDateTime.now().toString());
            purchase.setModifyBy(info.getId().toString());
            purchaseMapper.updateByPrimaryKey(purchase);
        }

        List<String> itemIds = Lists.newArrayList();
        mo.getDetailList().forEach(obj->{
            PurchaseItem item = Optional.ofNullable(obj.getId()).
                    map(id->purchaseItemMapper.selectByPrimaryKey(id)).
                    orElse(new PurchaseItem());
            BeanUtils.copyProperties(obj,item);
            if(StringUtils.isEmpty(item.getId())) {
                item.setId(UUIDUtil.generate());
                item.setCreateAt(LocalDateTime.now().toString());
                item.setCreateBy(info.getId().toString());
                item.setPurchaseId(purchase.getId());
                purchaseItemMapper.insert(item);
            }else {
                item.setModifyAt(LocalDateTime.now().toString());
                item.setModifyBy(info.getId().toString());
                purchaseItemMapper.updateByPrimaryKey(item);
            }
            itemIds.add(item.getId());
        });

        if(0 != itemIds.size()) {
            purchaseItemMapper.delItemNotInIds(itemIds, purchase.getId());
        }

        if(1 == purchase.getStatus()){//直接审核通过
            this.updateStock(purchase.getId(),info);
        }
    }


    /**
     * 审核通过，添加库存信息
     * @param purchaseId
     */
    @Transactional
    public void     audit(String purchaseId,UserInfo info){
        Purchase purchase = purchaseMapper.selectByPrimaryKey(purchaseId);
        purchase.setStatus(1);
        purchase.setModifyAt(LocalDateTime.now().toString());
        purchase.setModifyBy(info.getId().toString());
        purchaseMapper.updateByPrimaryKey(purchase);

        this.updateStock(purchaseId,info);
    }

    private void updateStock(String purchaseId,UserInfo info){


        Example example = new Example(PurchaseItem.class);
        example.createCriteria().andEqualTo("purchaseId",purchaseId);
        List<PurchaseItem> list = purchaseItemMapper.selectByExample(example);
        list.forEach(item->{

            DrugDict drugDict = drugDictMapper.selectByPrimaryKey(item.getDrugCode());

            Example drugExample = new Example(Drug.class);
            drugExample.createCriteria().andEqualTo("drugCode",item.getDrugCode()).
                    andEqualTo("orgCode",info.getOrgCode()).
                    andEqualTo("removed","0");
            List<Drug> drugList = drugMapper.selectByExample(drugExample);
            if(null != drugList && 0 != drugList.size()){
                Drug drug = drugList.get(0);
                if(0 != drug.getNum()){
                    Double num = drug.getNum();
                    drug.setNum(num + item.getNum());
                    drug.setBid((num * drug.getBid() + item.getNum() * item.getBid())/drug.getNum());
                    drug.setRetailPrice((num * drug.getRetailPrice() + item.getNum() * item.getRetailPrice())/drug.getNum());
                }else{
                    drug.setNum(Double.parseDouble(item.getNum().toString()));
                    drug.setBid(item.getBid());
                    drug.setRetailPrice(item.getRetailPrice());
                }
                drug.setModifyBy(info.getId().toString());
                drug.setModifyAt(LocalDateTime.now().toString());
                drugMapper.updateByPrimaryKey(drug);
            }else{
                Drug drug = new Drug();
                BeanUtils.copyProperties(drugDict,drug);
                drug.setOrgCode(info.getOrgCode());
                drug.setName(drugDict.getSpecName());
                drug.setDrugCode(drugDict.getCode());
                drug.setCreateAt(LocalDateTime.now().toString());
                drug.setCreateBy(info.getId().toString());
                drug.setNum(Double.parseDouble(item.getNum().toString()));
                drug.setBid(item.getBid());
                drug.setRetailPrice(item.getRetailPrice());
                drug.setCreateBy(info.getId().toString());
                drug.setCreateAt(LocalDateTime.now().toString());
                drug.setRemoved("0");
                drugMapper.insert(drug);

            }

        });

    }

    /**
     * 获取审核列表
     * @param purchaseNo
     * @param status
     * @param supplierId
     * @param startTime
     * @param endTime
     * @param user
     * @return
     */
    public List<PurchaseDto> getAuditList(String purchaseNo, Integer status, Integer supplierId, String startTime, String endTime, UserInfo user) {
        return purchaseMapper.getAuditList(user.getOrgCode(),purchaseNo,status,supplierId,startTime,endTime);
    }

    /**
     * 删除入库信息
     * @param id
     * @param info
     * @return
     */
    public void deleteInfo(String id, UserInfo info) {
        Purchase purchase = purchaseMapper.selectByPrimaryKey(id);
        purchase.setRemoved("1");
        purchase.setModifyBy(info.getId().toString());
        purchase.setModifyAt(LocalDateTime.now().toString());
        purchaseMapper.updateByPrimaryKey(purchase);
    }


    /**
     * 库存列表
     * @param name
     * @param info
     */
    public List<DrugStockDto> getStockList(String name, UserInfo info) {
        return drugMapper.getStockList(name,info.getOrgCode());
    }

    /**
     * 调整价格
     * @param id
     * @param price
     * @param num
     * @param user
     */
    public void modifyPrice(Integer id, Double price, Double num, UserInfo user) {
        Drug drug = drugMapper.selectByPrimaryKey(id);
        drug.setNum(num);
        drug.setRetailPrice(price);
        drug.setModifyAt(LocalDateTime.now().toString());
        drug.setModifyBy(user.getId().toString());
        drugMapper.updateByPrimaryKey(drug);
    }
}
