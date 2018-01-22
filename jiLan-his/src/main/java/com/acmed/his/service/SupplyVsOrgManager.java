package com.acmed.his.service;

import com.acmed.his.dao.SupplyVsOrgMapper;
import com.acmed.his.model.Org;
import com.acmed.his.model.Supply;
import com.acmed.his.model.SupplyVsOrg;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * SupplyVsOrgManager
 * 渠道机构管理
 * @author jimson
 * @date 2018/1/22
 */
@Service
@Transactional
public class SupplyVsOrgManager {
    @Autowired
    private SupplyVsOrgMapper supplyVsOrgMapper;


    public void addSupplys(String createBy,List<Integer> supplyIds,Integer orgCode){
        String time = LocalDateTime.now().toString();
        for (Integer supplyId :supplyIds){
            SupplyVsOrg param = new SupplyVsOrg();
            param.setOrgCode(orgCode);
            param.setSupplyId(supplyId);
            List<SupplyVsOrg> select = supplyVsOrgMapper.select(param);
            if (select.size()==0){
                // 之前不存在数据，直接插入
                param.setRemoved("0");
                param.setCreateAt(time);
                param.setCreateBy(createBy);
                supplyVsOrgMapper.insert(param);
            }else if (select.size()==1){
                // 存在一条数据
                SupplyVsOrg supplyVsOrg1 = select.get(0);
                if (StringUtils.equals(supplyVsOrg1.getRemoved(),"1")){
                    // 已经删除
                    supplyVsOrg1.setRemoved("0");
                    supplyVsOrg1.setModifyAt(time);
                    supplyVsOrg1.setModifyBy(createBy);
                    supplyVsOrgMapper.updateByPrimaryKeySelective(supplyVsOrg1);
                }
            }
        }
    }


    public int update(SupplyVsOrg supplyVsOrg){
        if(supplyVsOrg.getId() != null){
            supplyVsOrg.setModifyAt(LocalDateTime.now().toString());
            return supplyVsOrgMapper.updateByPrimaryKeySelective(supplyVsOrg);
        }else {
            return 0;
        }
    }

    /**
     * 根据机构id 查找渠道列表
     * @return
     */
    public List<Supply> getSupplyByOrgCode(Integer orgCode,String removed){
        return supplyVsOrgMapper.getSupplysByOrgCode(orgCode,removed);
    }

    /**
     * 根据渠道id 查找机构列表
     * @param supplyId
     * @param removed
     * @return
     */
    public List<Org> getOrgsBySupplyId(Integer supplyId,String removed){
        return supplyVsOrgMapper.getOrgsBySupplyId(supplyId,removed);
    }

    public SupplyVsOrg getById(Integer id){
        return supplyVsOrgMapper.selectByPrimaryKey(id);
    }
}
