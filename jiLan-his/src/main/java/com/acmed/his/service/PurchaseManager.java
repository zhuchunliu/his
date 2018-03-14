package com.acmed.his.service;

import com.acmed.his.constants.StatusCode;
import com.acmed.his.dao.DrugMapper;
import com.acmed.his.dao.DrugStockMapper;
import com.acmed.his.dao.PurchaseItemMapper;
import com.acmed.his.dao.PurchaseMapper;
import com.acmed.his.exceptions.BaseException;
import com.acmed.his.model.Drug;
import com.acmed.his.model.DrugStock;
import com.acmed.his.model.Purchase;
import com.acmed.his.model.PurchaseItem;
import com.acmed.his.model.dto.PurchaseDto;
import com.acmed.his.model.dto.PurchaseStockDto;
import com.acmed.his.pojo.mo.PurchaseMo;
import com.acmed.his.pojo.mo.PurchaseQueryMo;
import com.acmed.his.pojo.vo.UserInfo;
import com.acmed.his.util.UUIDUtil;
import com.github.pagehelper.PageHelper;
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
    private DrugStockMapper drugStockMapper;

    @Autowired
    private PermissionManager permissionManager;

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
        if(2 == mo.getStatus()){//直接入库
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

        if(2 == purchase.getStatus()){//直接审核通过
            this.updateStock(purchase.getId(),info);
        }
    }


    /**
     * 审核通过，添加库存信息
     * @param purchaseId
     */
    @Transactional
    public void audit(String purchaseId,UserInfo info){
        synchronized (purchaseId) {
            Purchase purchase = purchaseMapper.selectByPrimaryKey(purchaseId);
            if (purchase.getStatus() == 2) {
                throw new BaseException(StatusCode.FAIL, "禁止重复审核");
            }
            purchase.setStatus(2);
            purchase.setModifyAt(LocalDateTime.now().toString());
            purchase.setModifyBy(info.getId().toString());
            purchaseMapper.updateByPrimaryKey(purchase);

            this.updateStock(purchaseId, info);
        }
    }

    private void updateStock(String purchaseId,UserInfo info){

        Purchase purchase = purchaseMapper.selectByPrimaryKey(purchaseId);

        Example example = new Example(PurchaseItem.class);
        example.createCriteria().andEqualTo("purchaseId",purchaseId);
        List<PurchaseItem> list = purchaseItemMapper.selectByExample(example);
        list.forEach(item->{

            Example drugExample = new Example(Drug.class);
            drugExample.createCriteria().andEqualTo("id",item.getDrugId()).
                    andEqualTo("orgCode",info.getOrgCode()).
                    andEqualTo("removed","0");

            //更新药品库信息
            List<Drug> drugList = drugMapper.selectByExample(drugExample);
            Drug drug = drugList.get(0);
            if(null != drug.getNum()){
                Integer num = drug.getNum();
                drug.setNum(num + item.getNum());
                drug.setBid(item.getBid());
//                drug.setRetailPrice((num * drug.getRetailPrice() + item.getNum() * item.getRetailPrice())/drug.getNum());
                drug.setRetailPrice(item.getRetailPrice());
            }else{
                drug.setNum(item.getNum());
                drug.setBid(item.getBid());
                drug.setRetailPrice(item.getRetailPrice());
            }
            drug.setModifyBy(info.getId().toString());
            drug.setModifyAt(LocalDateTime.now().toString());
            drugMapper.updateByPrimaryKey(drug);


            //更新库存信息
            Example stockExample = new Example(DrugStock.class);
            stockExample.createCriteria().andEqualTo("drugId",item.getDrugId()).
                    andEqualTo("batchNumber",item.getBatchNumber()).
                    andEqualTo("expiryDate",item.getExpiryDate());

            DrugStock stock = Optional.ofNullable(drugStockMapper.selectByExample(stockExample)).filter(obj->0!=obj.size()).
                    map(obj->obj.get(0)).orElse(null);
            if(null == stock){
                stock = new DrugStock();
                stock.setOrgCode(drug.getOrgCode());
                stock.setDrugId(drug.getId());
                stock.setExpiryDate(item.getExpiryDate());
                stock.setBatchNumber(item.getBatchNumber());
                stock.setSupply(purchase.getSupplierId());
                stock.setNum(item.getNum());
                stock.setMinNum(0);
                stock.setDoseNum(0d);
                stock.setRemoved("0");
                stock.setCreateAt(LocalDateTime.now().toString());
                stock.setCreateBy(info.getId().toString());
                drugStockMapper.insert(stock);
            }else{
                stock.setNum(stock.getNum()+item.getNum());
                stock.setModifyAt(LocalDateTime.now().toString());
                stock.setModifyBy(info.getId().toString());
                drugStockMapper.updateByPrimaryKey(stock);
            }
        });

    }

    @Transactional
    public void reject(String purchaseId,UserInfo info){
        Purchase purchase = purchaseMapper.selectByPrimaryKey(purchaseId);
        purchase.setStatus(3);
        purchase.setModifyAt(LocalDateTime.now().toString());
        purchase.setModifyBy(info.getId().toString());
        purchaseMapper.updateByPrimaryKey(purchase);
    }

    @Transactional
    public void submit(String purchaseId,UserInfo info){
        Purchase purchase = purchaseMapper.selectByPrimaryKey(purchaseId);
        purchase.setStatus(1);
        purchase.setModifyAt(LocalDateTime.now().toString());
        purchase.setModifyBy(info.getId().toString());
        purchaseMapper.updateByPrimaryKey(purchase);
    }

    /**
     * 获取审核列表
     * @param user
     * @return
     */
    public List<PurchaseDto> getAuditList(PurchaseQueryMo mo, UserInfo user,Integer pageNum, Integer pageSize) {
        Boolean hasPermission = permissionManager.hasMenu(user.getId().toString(),"rksh");
        PageHelper.startPage(pageNum,pageSize);
        return purchaseMapper.getAuditList(user,mo,hasPermission);
    }

    public Integer getAuditTotal(PurchaseQueryMo mo, UserInfo user) {
        Boolean hasPermission = permissionManager.hasMenu(user.getId().toString(),"rksh");
        return purchaseMapper.getAuditTotal(user,mo,hasPermission);
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

    public Double getCurrentDayFee(Integer orgCode) {
        return purchaseMapper.getCurrentDayFee(orgCode);
    }

    public Double getSurveyFee(Integer orgCode,String startTime, String endTime ) {
        return purchaseMapper.getSurveyFee(orgCode,startTime,endTime);
    }

    public List<PurchaseStockDto> getBatchList(Integer pageNum, Integer pageSize, String name, UserInfo user) {
        PageHelper.startPage(pageNum,pageSize);
        return purchaseItemMapper.getBatchList(name,user.getOrgCode());
    }

    public Integer getBatchTotal(String name, UserInfo user) {
        return purchaseItemMapper.getBatchTotal(name,user.getOrgCode());
    }

}
