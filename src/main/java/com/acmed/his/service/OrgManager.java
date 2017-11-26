package com.acmed.his.service;

import com.acmed.his.dao.OrgMapper;
import com.acmed.his.model.Org;
import com.acmed.his.pojo.mo.OrgMo;
import com.acmed.his.pojo.vo.UserInfo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Created by Darren on 2017-11-22
 **/
@Service
public class OrgManager {

    @Autowired
    private OrgMapper orgMapper;

    /**
     * 获取机构列表
     * @return
     */
    public List<Org> getOrgList(){
        Example example = new Example(Org.class);
        example.createCriteria().andEqualTo("removed", "0");
        return orgMapper.selectByExample(example);
    }

    /**
     * 新增、编辑机构
     * @param mo
     */
    public void saveOrg(OrgMo mo, UserInfo userInfo){

        if(null == mo.getOrgCode()){
            Org org = new Org();
            BeanUtils.copyProperties(mo,org);
            org.setRemoved("0");
            org.setCreateAt(LocalDateTime.now().toString());
            org.setCreateBy(userInfo.getId().toString());
            orgMapper.insert(org);
        }else{
            Org org = orgMapper.selectByPrimaryKey(mo.getOrgCode());
            BeanUtils.copyProperties(mo,org);
            org.setModifyAt(LocalDateTime.now().toString());
            org.setModifyBy(userInfo.getId().toString());
            orgMapper.updateByPrimaryKey(org);
        }
    }

    /**
     * 获取机构详情
     * @param orgCode
     */
    public Org getOrgDetail(Integer orgCode){
        return orgMapper.selectByPrimaryKey(orgCode);
    }

    /**
     * 删除机构
     * @param orgCode
     */
    public void delOrg(Integer orgCode,UserInfo userInfo){
        Org org = orgMapper.selectByPrimaryKey(orgCode);
        org.setModifyAt(LocalDateTime.now().toString());
        org.setRemoved("1");
        org.setModifyBy(userInfo.getId().toString());
        orgMapper.updateByPrimaryKey(org);
    }
}
