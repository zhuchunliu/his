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
import com.acmed.his.pojo.vo.UserInfo;
import com.acmed.his.util.PageBase;
import com.acmed.his.util.PageResult;
import com.acmed.his.util.PinYinUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.time.LocalDateTime;
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
    private DrugDictMapper drugDictMapper;

    @Autowired
    private ManufacturerMapper manufacturerMapper;

    @Autowired
    private SupplyMapper supplyMapper;

    @Autowired
    private CommonManager commonManager;

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
     * 根据药品生产商名字模糊查询 分页
     * @param pageBase 生产商名字
     * @return List<Manufacturer>
     */
    public PageResult<Manufacturer> getManufacturerLikeNameByPage(PageBase<String> pageBase){
        Example example = new Example(Manufacturer.class);
        example.createCriteria().andLike("name","%"+pageBase.getParam()+"%");
        PageResult<Manufacturer> pageResult = new PageResult<>();
        Integer pageNum = pageBase.getPageNum();
        Integer pageSize = pageBase.getPageSize();
        PageHelper.startPage(pageSize,pageNum);
        List<Manufacturer> manufacturers = manufacturerMapper.selectByExample(example);
        PageInfo<Manufacturer> manufacturerPageInfo = new PageInfo<>(manufacturers);
        pageResult.setPageNum(pageNum);
        pageResult.setPageSize(pageSize);
        pageResult.setData(manufacturers);
        pageResult.setTotal(manufacturerPageInfo.getTotal());
        return pageResult;
    }

    /**
     * 查询所有药厂
     * @return List<Manufacturer>
     */
    public List<Manufacturer> getAllManufacturers(){
        return manufacturerMapper.selectAll();
    }

    /**
     * 分页查询药厂列表
     * @param pageBase
     * @return
     */
    public PageResult<Manufacturer> getAllManufacturersByPage(PageBase pageBase){
        PageResult<Manufacturer> pageResult = new PageResult<>();
        Integer pageNum = pageBase.getPageNum();
        Integer pageSize = pageBase.getPageSize();
        PageHelper.startPage(pageSize,pageNum);
        List<Manufacturer> manufacturers = manufacturerMapper.selectAll();
        PageInfo<Manufacturer> manufacturerPageInfo = new PageInfo<>(manufacturers);
        pageResult.setPageNum(pageNum);
        pageResult.setPageSize(pageSize);
        pageResult.setData(manufacturers);
        pageResult.setTotal(manufacturerPageInfo.getTotal());
        return pageResult;
    }

    /**
     * 存在id就更新  不存在就添加
     * @param supply 供应商参数
     * @return 0失败  1成功
     */
    public int saveSupply(Supply supply){
        Integer id = supply.getId();
        supply.setPinYin(PinYinUtil.getPinYinHeadChar(supply.getSupplyerName()));
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
    /**
     * 查询所有供应商list
     * @return List<Supply>
     */
    public PageResult<Supply> getAllSupplyByPage(PageBase pageBase){
        PageResult<Supply> pageResult = new PageResult<>();
        Integer pageNum = pageBase.getPageNum();
        Integer pageSize = pageBase.getPageSize();
        PageHelper.startPage(pageSize,pageNum);
        List<Supply> supplies = supplyMapper.selectAll();
        PageInfo<Supply> supplyPageInfo = new PageInfo<>(supplies);
        pageResult.setPageNum(pageNum);
        pageResult.setPageSize(pageSize);
        pageResult.setData(supplies);
        pageResult.setTotal(supplyPageInfo.getTotal());
        return pageResult;
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
            drug.setDrugCode(commonManager.getFormatVal(key,key+"000000"));
            drug.setName(dict.getSpecName());
            drug.setPinYin(dict.getPinYin());
            drug.setSpec(dict.getSpec());
            drug.setCategory(dict.getCategory());
            drug.setUnit(dict.getUnit());
            drug.setPackUnit(dict.getPackUnit());
            drug.setRemoved("0");
            drug.setCreateAt(LocalDateTime.now().toString());
            drug.setCreateBy(info.getId().toString());
            drugMapper.insert(drug);
        }

//        drugMapper.saveDrugByDict(codes,info);
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
