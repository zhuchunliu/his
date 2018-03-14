package com.acmed.his.service;

import com.acmed.his.constants.StatusCode;
import com.acmed.his.consts.DicTypeEnum;
import com.acmed.his.dao.*;
import com.acmed.his.exceptions.BaseException;
import com.acmed.his.model.*;
import com.acmed.his.model.dto.DrugInventoryDto;
import com.acmed.his.pojo.mo.DrugInventoryMo;
import com.acmed.his.pojo.mo.DrugInventoryQueryMo;
import com.acmed.his.pojo.vo.DrugInventoryVo;
import com.acmed.his.pojo.vo.PurchaseVo;
import com.acmed.his.pojo.vo.UserInfo;
import com.acmed.his.util.UUIDUtil;
import com.github.pagehelper.PageHelper;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import tk.mybatis.mapper.entity.Example;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Created by Darren on 2018-03-13
 **/
@Service
public class DrugInventoryManager {

    @Autowired
    private DrugInventoryMapper inventoryMapper;

    @Autowired
    private DrugInventoryItemMapper inventoryItemMapper;

    @Autowired
    private DrugMapper drugMapper;

    @Autowired
    private DrugStockMapper drugStockMapper;

    @Autowired
    private PermissionManager permissionManager;

    @Autowired
    private BaseInfoManager baseInfoManager;

    @Autowired
    private ManufacturerMapper manufacturerMapper;


    public void save(DrugInventoryMo mo, UserInfo info) {
        DrugInventory drugInventory = Optional.ofNullable(mo.getId()).
                map(id->inventoryMapper.selectByPrimaryKey(id)).
                orElse(new DrugInventory());

        BeanUtils.copyProperties(mo,drugInventory);
        drugInventory.setOrgCode(info.getOrgCode());
        drugInventory.setRemoved("0");
        if(2 == mo.getStatus()){//直接入库
            drugInventory.setAuditUserId(info.getId());
            drugInventory.setAuditDate(LocalDateTime.now().toString());
        }
        if(StringUtils.isEmpty(mo.getId())) {
            drugInventory.setId(UUIDUtil.generate());
            drugInventory.setUserId(info.getId());
            drugInventory.setCreateAt(LocalDateTime.now().toString());
            drugInventory.setCreateBy(info.getId().toString());
            inventoryMapper.insert(drugInventory);
        }else {
            drugInventory.setModifyAt(LocalDateTime.now().toString());
            drugInventory.setModifyBy(info.getId().toString());
            inventoryMapper.updateByPrimaryKey(drugInventory);
        }

        Example example = new Example(DrugInventoryItem.class);
        example.createCriteria().andEqualTo("inventoryId",mo.getId());
        inventoryItemMapper.deleteByExample(example);


        mo.getDetailList().forEach(obj->{

            DrugInventoryItem item = new DrugInventoryItem();
            BeanUtils.copyProperties(obj,item);
            item.setId(UUIDUtil.generate());
            item.setInventoryId(drugInventory.getId());
            inventoryItemMapper.insert(item);
        });

        if(2 == drugInventory.getStatus()){//直接审核通过
            this.updateStock(drugInventory.getId(),info);
        }
    }

    /**
     * 审核通过
     * @param inventoryId
     */
    @Transactional
    public void audit(String inventoryId,UserInfo info){
        synchronized (inventoryId) {
            DrugInventory drugInventory = inventoryMapper.selectByPrimaryKey(inventoryId);
            if (drugInventory.getStatus() == 2) {
                throw new BaseException(StatusCode.FAIL, "禁止重复审核");
            }
            drugInventory.setStatus(2);
            drugInventory.setModifyAt(LocalDateTime.now().toString());
            drugInventory.setModifyBy(info.getId().toString());
            inventoryMapper.updateByPrimaryKey(drugInventory);

            this.updateStock(inventoryId, info);
        }
    }

    private void updateStock(String inventoryId, UserInfo info) {
        Example example = new Example(DrugInventoryItem.class);
        example.createCriteria().andEqualTo("inventoryId",inventoryId);
        List<DrugInventoryItem> list = inventoryItemMapper.selectByExample(example);
        Map<Integer,List<DrugStock>> map = Maps.newHashMap();
        list.forEach(item->{

            //更新库存信息
            DrugStock stock = drugStockMapper.selectByPrimaryKey(item.getStockId());
            stock.setNum(item.getNum());
            stock.setMinNum(item.getMinNum());
            stock.setDoseNum(item.getDoseNum());
            stock.setModifyAt(LocalDateTime.now().toString());
            stock.setModifyBy(info.getId().toString());
            drugStockMapper.updateByPrimaryKey(stock);

            if(map.containsKey(stock.getDrugId())){
                map.get(stock.getDrugId()).add(stock);
            }else{
                map.put(stock.getDrugId(),Lists.newArrayList(stock));
            }
        });

        //更新药品库信息
        for(Integer drugId : map.keySet()){
            Drug drug = drugMapper.selectByPrimaryKey(drugId);
            int num = 0;
            int minum =0;
            Double doseNum = 0d;
            for(DrugStock stock : map.get(drugId)){
                num += stock.getNum();
                minum += stock.getMinNum();
                doseNum += stock.getDoseNum();
            }
            minum += doseNum/drug.getDose();
            doseNum = doseNum % drug.getDose();
            num += minum / drug.getConversion();
            minum = minum % drug.getConversion();
            drug.setNum(num);
            drug.setMinNum(minum);
            drug.setDoseNum(doseNum);
            drug.setModifyBy(info.getId().toString());
            drug.setModifyAt(LocalDateTime.now().toString());
            drugMapper.updateByPrimaryKey(drug);
        }

    }

    @Transactional
    public void reject(String inventoryId,UserInfo info){
        DrugInventory drugInventory = inventoryMapper.selectByPrimaryKey(inventoryId);
        drugInventory.setStatus(3);
        drugInventory.setModifyAt(LocalDateTime.now().toString());
        drugInventory.setModifyBy(info.getId().toString());
        inventoryMapper.updateByPrimaryKey(drugInventory);
    }

    @Transactional
    public void submit(String inventoryId,UserInfo info){
        DrugInventory drugInventory = inventoryMapper.selectByPrimaryKey(inventoryId);
        drugInventory.setStatus(1);
        drugInventory.setModifyAt(LocalDateTime.now().toString());
        drugInventory.setModifyBy(info.getId().toString());
        inventoryMapper.updateByPrimaryKey(drugInventory);
    }

    /**
     * 删除入库信息
     * @param inventoryId
     * @param info
     * @return
     */
    public void deleteInfo(String inventoryId, UserInfo info) {
        DrugInventory drugInventory = inventoryMapper.selectByPrimaryKey(inventoryId);
        drugInventory.setRemoved("1");
        drugInventory.setModifyBy(info.getId().toString());
        drugInventory.setModifyAt(LocalDateTime.now().toString());
        inventoryMapper.updateByPrimaryKey(drugInventory);
    }


    /**
     * 获取审核列表
     * @param user
     * @return
     */
    public List<DrugInventoryDto> getAuditList(DrugInventoryQueryMo mo, UserInfo user, Integer pageNum, Integer pageSize) {
        Boolean hasPermission = permissionManager.hasMenu(user.getId().toString(),"rksh");
        PageHelper.startPage(pageNum,pageSize);
        return inventoryMapper.getAuditList(user,mo,hasPermission);
    }

    public Integer getAuditTotal(DrugInventoryQueryMo mo, UserInfo user) {
        Boolean hasPermission = permissionManager.hasMenu(user.getId().toString(),"rksh");
        return inventoryMapper.getAuditTotal(user,mo,hasPermission);
    }

    public List<DrugInventoryVo> getDrugInventoryDetail(String id) {

        Example example = new Example(DrugInventoryItem.class);
        example.createCriteria().andEqualTo("inventoryId",id);
        List<DrugInventoryItem> itemList = inventoryItemMapper.selectByExample(example);

        Map<Integer,List<DrugInventoryVo.DrugInventoryItemVo>> map = Maps.newHashMap();

        itemList.forEach(item->{
            DrugInventoryVo.DrugInventoryItemVo itemVo = new DrugInventoryVo.DrugInventoryItemVo();
            BeanUtils.copyProperties(item,itemVo);

            if(map.containsKey(item.getDrugId())){
                map.get(item.getDrugId()).add(itemVo);
            }else{
                map.put(item.getDrugId(),Lists.newArrayList(itemVo));
            }
        });


        List<DicItem> dicItemList = baseInfoManager.getDicItemsByDicTypeCode(DicTypeEnum.UNIT.getCode());
        Map<String,String> dicItemName = Maps.newHashMap();
        dicItemList.forEach(obj->{
            dicItemName.put(obj.getDicItemCode(),obj.getDicItemName());
        });

        List<DrugInventoryVo> list = Lists.newArrayList();
        for(Integer drugId : map.keySet()) {
            Drug drug = drugMapper.selectByPrimaryKey(drugId);
            DrugInventoryVo vo = new DrugInventoryVo();
            BeanUtils.copyProperties(drug,vo);
            vo.setUnitName(null == drug.getUnit()?"":baseInfoManager.getDicItem(DicTypeEnum.UNIT.getCode(),drug.getUnit().toString()).getDicItemName());
            vo.setMinUnitName(null==drug.getMinUnit()?"":baseInfoManager.getDicItem(DicTypeEnum.UNIT.getCode(),drug.getMinUnit().toString()).getDicItemName());
            vo.setDoseUnitName(null==drug.getDoseUnit()?"":baseInfoManager.getDicItem(DicTypeEnum.UNIT.getCode(),drug.getDoseUnit().toString()).getDicItemName());
            vo.setManufacturerName(Optional.ofNullable(drug.getManufacturer()).map(obj->manufacturerMapper.selectByPrimaryKey(obj)).
                    map(obj->obj.getName()).orElse(""));

            vo.setDetailList(map.get(drug.getId()));
            list.add(vo);
        }
        return list;
    }
}
