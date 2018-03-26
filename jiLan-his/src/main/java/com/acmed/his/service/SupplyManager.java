package com.acmed.his.service;

import com.acmed.his.constants.StatusCode;
import com.acmed.his.dao.SupplyMapper;
import com.acmed.his.exceptions.BaseException;
import com.acmed.his.model.Supply;
import com.acmed.his.pojo.vo.UserInfo;
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
    public int saveSupply(Supply supply, UserInfo userInfo){
        Integer id = supply.getId();
        supply.setPinYin(PinYinUtil.getPinYinHeadChar(supply.getSupplyerName()));
        supply.setOrgCode(userInfo.getOrgCode());
        if (id==null){
            Supply param = new Supply();
            param.setSupplyerName(supply.getSupplyerName());
            List<Supply> select = supplyMapper.select(param);
            if (select.size()!=0){
                throw new BaseException(StatusCode.FAIL,supply.getSupplyerName()+"已经存在，请勿重复添加");
            }
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
    public PageResult<Supply> getSupplyByPage(PageBase<String> pageBase,UserInfo userInfo){
        PageResult<Supply> pageResult = new PageResult<>();
        Integer pageNum = pageBase.getPageNum();
        Integer pageSize = pageBase.getPageSize();
        PageHelper.startPage(pageNum,pageSize);
        List<Supply> supplies = supplyMapper.selectMohu(pageBase.getParam(),userInfo.getOrgCode());
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
    public List<Supply> getSupply(String param,UserInfo userInfo){
        return supplyMapper.selectMohu(param,userInfo.getOrgCode());
    }
}
