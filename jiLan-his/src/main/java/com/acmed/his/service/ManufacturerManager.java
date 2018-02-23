package com.acmed.his.service;

import com.acmed.his.dao.ManufacturerMapper;
import com.acmed.his.model.Manufacturer;
import com.acmed.his.util.PageBase;
import com.acmed.his.util.PageResult;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

/**
 * Created by Darren on 2018-01-17
 **/
@Service
public class ManufacturerManager {

    @Autowired
    private ManufacturerMapper manufacturerMapper;

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
        PageHelper.startPage(pageNum,pageSize);
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
        PageHelper.startPage(pageNum,pageSize);
        List<Manufacturer> manufacturers = manufacturerMapper.selectAll();
        PageInfo<Manufacturer> manufacturerPageInfo = new PageInfo<>(manufacturers);
        pageResult.setPageNum(pageNum);
        pageResult.setPageSize(pageSize);
        pageResult.setData(manufacturers);
        pageResult.setTotal(manufacturerPageInfo.getTotal());
        return pageResult;
    }
}
