package com.acmed.his.service;

import com.acmed.his.dao.AreaMapper;
import com.acmed.his.dao.DicItemMapper;
import com.acmed.his.dao.DicTypeMapper;
import com.acmed.his.model.Area;
import com.acmed.his.model.DicItem;
import com.acmed.his.model.DicType;
import com.acmed.his.util.UUIDUtil;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * BaseInfoManager
 * 基础信息
 * @author jimson
 * @date 2017/11/21
 */
@Service
public class BaseInfoManager {
    @Autowired
    private AreaMapper areaMapper;

    @Autowired
    private DicItemMapper dicItemMapper;

    @Autowired
    private DicTypeMapper dicTypeMapper;

    /**
     * 根据层级查询列表
     * @param level 1省  2 市  3区
     * @return List<Area>
     */
    public List<Area> getAreasByLevel(Integer level){
        Area area = new Area();
        area.setLevel(level);
        return areaMapper.select(area);
    }

    /**
     * 根据首字母查询列表
     * @param first 首字母大写
     * @return List<Area>
     */
    public List<Area> getAreasByFirst(String first){
        Area area = new Area();
        area.setFirst(first);
        return areaMapper.select(area);
    }

    /**
     * 根据pid查询列表
     * @param pid 0查询的是全部的省和直辖市
     * @return List<Area>
     */
    public List<Area> getAreasByPid(Integer pid){
        Area area = new Area();
        area.setPid(pid);
        return areaMapper.select(area);
    }

    /**
     * 根据id查询列表
     * @param id 主键
     * @return Area
     */
    public Area getAreaById(Integer id){
        Area area = new Area();
        area.setId(id);
        return areaMapper.selectOne(area);
    }

    /**
     * 根据拼音模糊查询
     * @param pinYin 小写
     * @return List<Area>
     */
    public List<Area> getAreasByPinYin(String pinYin){
        Example example = new Example(Area.class);
        example.createCriteria().andLike("pinyin",pinYin);
        return areaMapper.selectByExample(example);
    }

    // --------字典相关

    /**
     * 查询所有字典类型list
     * @return List<DicType>
     */
    public List<DicType> getAllDicTypes(){
        return dicTypeMapper.selectAll();
    }



    /**
     * 插入
     * @param dicType 参数
     * @return 0 失败  1 成功
     */
    public int saveDicType(DicType dicType){
        String dicTypeCode = dicType.getDicTypeCode();
        if (StringUtils.isEmpty(dicTypeCode)){
            dicType.setDicTypeCode(UUIDUtil.generate());
            return dicTypeMapper.insert(dicType);
        }else {
            return dicTypeMapper.updateByPrimaryKeySelective(dicType);
        }

    }

    /**
     * 添加字典项
     * @param dicItem 1成功
     * @return 0 失败
     */
    public int addDicItem(DicItem dicItem){
        dicItem.setDicItemCode(UUIDUtil.generate());
        String dicTypeCode = dicItem.getDicTypeCode();
        Example example = new Example(DicItem.class);
        example.setOrderByClause("sn desc");
        example.createCriteria().andEqualTo("dicTypeCode",dicTypeCode);
        List<DicItem> dicItems = dicItemMapper.selectByExample(example);
        if (dicItems.size() == 0){
            DicType dicType = dicTypeMapper.selectByPrimaryKey(dicTypeCode);
            if (dicType==null){
                // 字典类型不存在
                return 0;
            }
            dicItem.setSn(1);
        }else {
            dicItem.setSn(dicItems.get(0).getSn()+1);
        }
        return dicItemMapper.insert(dicItem);
    }

    /**
     * 查询所有字典
     * @return List<Map<String,String>>
     */
    public List<Map<String,String>> getAllDicItems(){
        List<Map<String,String>> list = new ArrayList<>();
        List<DicItem> dicItems = dicItemMapper.selectAll();
        if (dicItems.size()==0){
            return list;
        }
        List<DicType> dicTypes = dicTypeMapper.selectAll();
        for (DicItem d:dicItems){
            String dicTypeCode = d.getDicTypeCode();
            String dicItemCode = d.getDicItemCode();
            String dicItemName = d.getDicItemName();
            Map<String,String> map = new HashMap<>();
            map.put("dicTypeCode",dicTypeCode);
            map.put("dicItemCode",dicItemCode);
            map.put("dicItemName",dicItemName);
            for (DicType dt : dicTypes){
                String dicTypeCode1 = dt.getDicTypeCode();
                String dicTypeName = dt.getDicTypeName();
                String productCode = dt.getProductCode();
                if (StringUtils.equals(dicTypeCode1,dicTypeCode)){
                    map.put("dicTypeName",dicTypeName);
                    map.put("productCode",productCode);
                    list.add(map);
                }
            }
        }
        return list;
    }

    /**
     * 根据类型查找
     * @param dicTypeCode 字典类型
     * @return List<DicItem>
     */
    public List<DicItem> getDicItemsByDicTypeCode(String dicTypeCode){
        Example example = new Example(DicItem.class);
        example.createCriteria().andEqualTo("dicTypeCode",dicTypeCode);
        example.setOrderByClause("sn desc");
        return dicItemMapper.selectByExample(example);
    }
}
