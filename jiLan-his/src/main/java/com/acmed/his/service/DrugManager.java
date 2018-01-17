package com.acmed.his.service;

import com.acmed.his.dao.DrugDictMapper;
import com.acmed.his.dao.DrugMapper;
import com.acmed.his.dao.ManufacturerMapper;
import com.acmed.his.dao.SupplyMapper;
import com.acmed.his.model.Drug;
import com.acmed.his.model.DrugDict;
import com.acmed.his.model.Manufacturer;
import com.acmed.his.model.Supply;
import com.acmed.his.model.dto.DrugDto;
import com.acmed.his.model.dto.DrugStockDto;
import com.acmed.his.pojo.mo.DrugMo;
import com.acmed.his.pojo.vo.UserInfo;
import com.acmed.his.util.PageBase;
import com.acmed.his.util.PageResult;
import com.acmed.his.util.PinYinUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static java.awt.SystemColor.info;

/**
 * DrugManager
 * 药品
 * @author jimson
 * @date 2017/11/21
 */
@Service
public class DrugManager {
    @Autowired
    private DrugMapper drugMapper;

    @Autowired
    private DrugDictMapper drugDictMapper;

    @Autowired
    private CommonManager commonManager;

    /**
     * 添加药品
     * @param mo 药
     * @return 0失败 1成功
     */
    public int saveDrug(DrugMo mo,UserInfo userInfo){
        String date = LocalDateTime.now().toString();
        if (mo.getId()!=null){
            Drug drug = drugMapper.selectByPrimaryKey(mo.getId());
            BeanUtils.copyProperties(mo,drug);
            drug.setModifyAt(date);
            drug.setModifyBy(userInfo.getId().toString());
            drug.setPinYin(Optional.ofNullable(drug.getName()).map(PinYinUtil::getPinYinHeadChar).orElse(null));
            drug.setGoodsPinYin(Optional.ofNullable(drug.getGoodsName()).map(PinYinUtil::getPinYinHeadChar).orElse(null));
            return drugMapper.updateByPrimaryKeySelective(drug);
        }else {
            Drug drug = new Drug();
            BeanUtils.copyProperties(mo,drug);
            drug.setOrgCode(userInfo.getOrgCode());
            String key = this.getCategory(drug.getCategory())+new java.text.DecimalFormat("000000").format(userInfo.getOrgCode());
            drug.setDrugCode(key+String.format("%06d",Integer.parseInt(commonManager.getNextVal(key))));
            drug.setPinYin(Optional.ofNullable(drug.getName()).map(PinYinUtil::getPinYinHeadChar).orElse(null));
            drug.setGoodsPinYin(Optional.ofNullable(drug.getGoodsName()).map(PinYinUtil::getPinYinHeadChar).orElse(null));
            drug.setCreateAt(date);
            drug.setCreateBy(date);
            drug.setPinYin(PinYinUtil.getPinYinHeadChar(drug.getName()));
            return drugMapper.insert(drug);
        }

    }

    /**
     * 根据id查询药品详情
     * @param id 药品id
     * @return 药品详情
     */
    public Drug getDrugById(Integer id){
        return drugMapper.selectByPrimaryKey(id);
    }



    /**
     * 模糊搜索药品库信息
     * @param pageNum
     * @param pageSize
     * @param name
     * @param category
     * @return
     */
    public List<DrugDto> getDrugList(Integer orgCode, String name, String category, Integer pageNum, Integer pageSize ) {

        PageHelper.startPage(pageNum,pageSize);
        return drugMapper.getDrugList(orgCode,name,category);

    }

    /**
     * 模糊搜索药品库信息
     * @param name
     * @param category
     * @return
     */
    public Integer getDrugTotal(Integer orgCode,String name, String category ) {

        return drugMapper.getDrugTotal(orgCode,name,category);

    }

    /**
     * 删除药品信息
     * @param id
     * @param info
     */
    public void delDrug(Integer id, UserInfo info) {
        Drug drug = drugMapper.selectByPrimaryKey(id);
        drug.setModifyAt(LocalDateTime.now().toString());
        drug.setModifyBy(info.getId().toString());
        drug.setRemoved("1");
        drugMapper.updateByPrimaryKey(drug);
    }

    /**
     * 获取药品字典表
     * @param orgCode
     * @param name
     * @param category
     * @param pageNum
     * @param pageSize
     * @return
     */
    public List<DrugDict> getDrugDictList(Integer orgCode, String name, String category, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum,pageSize);
        return drugDictMapper.getDrugDictList(orgCode,name,category);

    }

    public int getDrugDictTotal(Integer orgCode, String name, String category) {
        return drugDictMapper.getDrugDictTotal(orgCode,name,category);
    }

    /**
     * 批量添加药品信息
     * @param codes
     */
    @Transactional
    public void saveDrugByDict(String[] codes,UserInfo info) {
        for(String code :codes){
            DrugDict dict = drugDictMapper.selectByPrimaryKey(code);
            Drug drug = new Drug();

            drug.setOrgCode(info.getOrgCode());
            String key = this.getCategory(dict.getCategory())+new java.text.DecimalFormat("000000").format(info.getOrgCode());
            drug.setDrugCode(key+String.format("%06d",Integer.parseInt(commonManager.getNextVal(key))));
            drug.setName(dict.getSpecName());
            drug.setPinYin(dict.getPinYin());
            drug.setCategory(dict.getCategory());
            drug.setRemoved("0");
            drug.setCreateAt(LocalDateTime.now().toString());
            drug.setCreateBy(info.getId().toString());
            drugMapper.insert(drug);
        }

    }

    /**
     * 库存列表
     * @param name
     * @param info
     */
    public List<DrugStockDto> getStockList(Integer pageNum , Integer pageSize, String name, UserInfo info) {
        PageHelper.startPage(pageNum,pageSize);
        return drugMapper.getStockList(name,info.getOrgCode());
    }

    /**
     * 库存数据
     * @param name
     * @param user
     * @return
     */
    public Integer getStockTotal(String name, UserInfo user) {
        return drugMapper.getStockTotal(name,user.getOrgCode());
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


    private String getCategory(String category){
        switch (category){
            case "0": // 西药
                return "X";
            case "1": // 中成药
                return "ZC";
            case "2": // 中药
                return "Z";
            case "3": // 血液制品
                return "B";
            case "4": // 诊断试剂
                return "D";
         }
         return "O";
    }


}
