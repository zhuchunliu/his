package com.acmed.his.service;

import com.acmed.his.dao.SupplyMapper;
import com.acmed.his.model.Supply;
import com.acmed.his.util.PageBase;
import com.acmed.his.util.PageResult;
import com.acmed.his.util.PinYinUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by Darren on 2018-01-17
 **/
@Service
public class SupplyManager {

    @Autowired
    private SupplyMapper supplyMapper;

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
     * 分页条件查询
     * @return PageResult<Supply>
     */
    public PageResult<Supply> getSupplyByPage(PageBase<String> pageBase){
        PageResult<Supply> pageResult = new PageResult<>();
        Integer pageNum = pageBase.getPageNum();
        Integer pageSize = pageBase.getPageSize();
        PageHelper.startPage(pageNum,pageSize);
        List<Supply> supplies = supplyMapper.selectMohu(pageBase.getParam());
        PageInfo<Supply> supplyPageInfo = new PageInfo<>(supplies);
        pageResult.setPageNum(pageNum);
        pageResult.setPageSize(pageSize);
        pageResult.setData(supplies);
        pageResult.setTotal(supplyPageInfo.getTotal());
        return pageResult;
    }

    /**
     * 条件查询
     * @param param
     * @return
     */
    public List<Supply> getSupply(String param){
        return supplyMapper.selectMohu(param);
    }
}
