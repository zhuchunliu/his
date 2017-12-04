package com.acmed.his.service;

import com.acmed.his.dao.DrugMapper;
import com.acmed.his.dao.ManufacturerMapper;
import com.acmed.his.dao.SupplyMapper;
import com.acmed.his.model.Drug;
import com.acmed.his.model.Manufacturer;
import com.acmed.his.model.Supply;
import com.acmed.his.util.PinYinUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

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
    private ManufacturerMapper manufacturerMapper;

    @Autowired
    private SupplyMapper supplyMapper;

    /**
     * 添加药品
     * @param drug 药
     * @return 0失败 1成功
     */
    public int saveDrug(Drug drug){
        String date = LocalDateTime.now().toString();
        if (drug.getId()!=null){
            drug.setModifyAt(date);
            return drugMapper.updateByPrimaryKeySelective(drug);
        }else {
            drug.setCreateAt(date);
            drug.setPinYin(PinYinUtil.getPinYinHeadChar(drug.getName()));
            return drugMapper.insert(drug);
        }

    }

    /**
     * 条件查询药品
     * @param drug 条件
     * @return List<Drug>
     */
    public List<Drug> getDrugsByDrug(Drug drug){
        return drugMapper.select(drug);
    }

    /**
     * 拼音模糊查询药品详情列表
     * @param pinYin 拼音
     * @return 列表
     */
    public List<Drug> getDrugsByPinYinLike(String pinYin){
        Example example = new Example(Drug.class);
        example.createCriteria().andLike("pinYin","%"+pinYin+"%");
        return drugMapper.selectByExample(example);
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
     * 存在id 则更新药厂   不存在则添加
     * @param manufacturer 药品生产商参数
     * @return 0失败 1成功
     */
    public int saveManufacturer(Manufacturer manufacturer){
        Integer id = manufacturer.getId();
        if (id == null){
            return manufacturerMapper.insert(manufacturer);
        }else {
            Manufacturer manufacturer1 = manufacturerMapper.selectByPrimaryKey(id);
            if (manufacturer1 == null){
                //id 不存在
                return 0;
            }else {
                manufacturerMapper.updateByPrimaryKeySelective(manufacturer);
                return 1;
            }
        }
    }

    /**
     * 根据id 查询药品生产商
     * @param id 厂商id
     * @return Manufacturer
     */
    public Manufacturer getManufacturerById(Integer id){
        return manufacturerMapper.selectByPrimaryKey(id);
    }

    /**
     * 根据药品生产商名字模糊查询
     * @param name 生产商名字
     * @return List<Manufacturer>
     */
    public List<Manufacturer> getManufacturerLikeName(String name){
        Example example = new Example(Manufacturer.class);
        example.createCriteria().andLike("name","%"+name+"%");
        return manufacturerMapper.selectByExample(example);
    }

    /**
     * 查询所有药厂
     * @return List<Manufacturer>
     */
    public List<Manufacturer> getAllManufacturers(){
        return manufacturerMapper.selectAll();
    }

    /**
     * 存在id就更新  不存在就添加
     * @param supply 供应商参数
     * @return 0失败  1成功
     */
    public int saveSupply(Supply supply){
        Integer id = supply.getId();
        if (id==null){
            return supplyMapper.insert(supply);
        }else {
            return supplyMapper.updateByPrimaryKeySelective(supply);
        }
    }

    /**
     * 根据主键查询供应商详情
     * @param id 主键id
     * @return Supply
     */
    public Supply getSupplyById(Integer id){
        return supplyMapper.selectByPrimaryKey(id);
    }

    /**
     * 查询所有供应商list
     * @return List<Supply>
     */
    public List<Supply> getAllSupply(){
        return supplyMapper.selectAll();
    }

}
