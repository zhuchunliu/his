package com.acmed.his.service;

import com.acmed.his.dao.AreaMapper;
import com.acmed.his.dao.DicItemMapper;
import com.acmed.his.dao.DicTypeMapper;
import com.acmed.his.model.Area;
import com.acmed.his.model.DicItem;
import com.acmed.his.model.DicType;
import com.acmed.his.pojo.vo.DicDetailVo;
import com.acmed.his.pojo.vo.UserInfo;
import com.acmed.his.util.UUIDUtil;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.*;

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

    @Autowired
    private CommonManager commonManager;

    public List<Area> bsgcitys(){
        return areaMapper.bsgcitys();
    }


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
    @CacheEvict(value = "dicItem",allEntries = true)
    public String addDicItem(DicItem dicItem, UserInfo userInfo){
        DicItem dicItemParam = new DicItem();
        dicItemParam.setDicItemName(dicItem.getDicItemName());
        dicItemParam.setDicTypeCode(dicItem.getDicTypeCode());

        List<DicItem> select = dicItemMapper.select(dicItemParam);
        if (select.size() == 0){
            dicItem.setRemoved("0");
            dicItem.setOrgCode(userInfo.getOrgCode());
            dicItem.setDicItemCode(commonManager.getNextVal(dicItem.getDicTypeCode()));
            dicItemMapper.insertSelective(dicItem);
            return dicItem.getDicItemCode();
        }else {
            // 如果字典存在 就改成未删除
            DicItem dicItem1 = select.get(0);
            dicItem1.setOrgCode(userInfo.getOrgCode());
            dicItem1.setRemoved("0");
            dicItemMapper.updateByPrimaryKeySelective(dicItem1);
            return dicItem1.getDicItemCode();
        }
    }
    @CacheEvict(value = "dicItem",allEntries = true)
    public int updateDicItem(DicItem dicItem){
        DicItem dicItemParam = new DicItem();
        dicItemParam.setDicItemName(dicItem.getDicItemName());
        dicItemParam.setDicTypeCode(dicItem.getDicTypeCode());
        List<DicItem> select = dicItemMapper.select(dicItemParam);
        if (select.size() == 0){
            dicItem.setDicItemCode(null);
            dicItem.setDicTypeCode(null);
            return dicItemMapper.updateByPrimaryKeySelective(dicItem);
        }else {
            DicItem dicItem1 = select.get(0);
            dicItem1.setDicItemName(null);
            dicItem1.setDicItemCode(null);
            dicItem1.setDicTypeCode(null);
            if (StringUtils.equals("1",dicItem1.getRemoved())){
                dicItem.setRemoved("0");
            }
            if (StringUtils.isEmpty(dicItem.getStartTime()) && StringUtils.isEmpty(dicItem.getEndTime()) && StringUtils.isEmpty(dicItem.getRemoved())){
                return 0;
            }
            if (StringUtils.isNotEmpty(dicItem.getStartTime())){
                dicItem1.setStartTime(dicItem.getStartTime());
            }
            if (StringUtils.isNotEmpty(dicItem.getEndTime())){
                dicItem1.setEndTime(dicItem.getEndTime());
            }
            if (StringUtils.isNotEmpty(dicItem.getRemoved())){
                dicItem1.setRemoved(dicItem.getRemoved());
            }
            return dicItemMapper.updateByPrimaryKeySelective(dicItem);
        }
    }

    /**
     * 查询所有字典
     * @return List<Map<String,String>>
     */
    public List<DicDetailVo> getAllDicItems(){
        List<DicDetailVo> list = new ArrayList<>();
        DicItem param = new DicItem();
        param.setRemoved("0");
        List<DicItem> dicItems = dicItemMapper.select(param);
        if (dicItems.size()==0){
            return list;
        }
        List<DicType> dicTypes = dicTypeMapper.selectAll();
        for (DicItem d:dicItems){
            String dicTypeCode = d.getDicTypeCode();
            String dicItemCode = d.getDicItemCode();
            String dicItemName = d.getDicItemName();
            DicDetailVo vo = new DicDetailVo();
            vo.setDicTypeCode(dicTypeCode);
            vo.setDicItemCode(dicItemCode);
            vo.setDicItemName(dicItemName);
            vo.setStartTime(d.getStartTime());
            vo.setEndTime(d.getEndTime());
            for (DicType dt : dicTypes){
                String dicTypeCode1 = dt.getDicTypeCode();
                String dicTypeName = dt.getDicTypeName();
                String productCode = dt.getProductCode();
                if (StringUtils.equals(dicTypeCode1,dicTypeCode)){
                    vo.setDicTypeName(dicTypeName);
                    vo.setProductCode(productCode);
                    list.add(vo);
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
        example.setOrderByClause("id ASC");
        return dicItemMapper.selectByExample(example);
    }

    public List<DicItem> getDicItems(String dicTypeCode){
        Example example = new Example(DicItem.class);
        example.createCriteria().andEqualTo("dicTypeCode",dicTypeCode).andEqualTo("removed","0").andIsNotNull("dicItemName");
        example.setOrderByClause("id ASC");
        return dicItemMapper.selectByExample(example);
    }

    public List<DicItem> getDicItemsByDicTypeCode(String dicTypeCode, UserInfo user) {
        return dicItemMapper.getDicItemsByDicTypeCode(dicTypeCode,user.getOrgCode());
    }


    public DicItem getByDicTypeCodeAndDicItemCode(String dicTypeCode,String dicItemCode){
        DicItem param = new DicItem();
        param.setDicTypeCode(dicTypeCode);
        param.setDicItemCode(dicItemCode);
        return dicItemMapper.selectOne(param);
    }


    @Cacheable(value = "dicItem",key = "'dicItem_cache_'+#dicTypeCode+#dicItemCode")
    public DicItem getDicItem(String dicTypeCode,String dicItemCode){
        Example example = new Example(DicItem.class);
        example.createCriteria().andEqualTo("dicTypeCode",dicTypeCode).andEqualTo("dicItemCode",dicItemCode).andEqualTo("removed","0");
        return Optional.ofNullable(dicItemMapper.selectByExample(example)).filter(obj->obj.size()!=0).map(obj->obj.get(0)).orElse(new DicItem());
    }


}
