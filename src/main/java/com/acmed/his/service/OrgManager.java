package com.acmed.his.service;

import com.acmed.his.dao.OrgMapper;
import com.acmed.his.model.Org;
import com.acmed.his.pojo.mo.OrgMo;
import com.acmed.his.pojo.vo.UserInfo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

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
        return orgMapper.selectAll();
    }

    /**
     * 新增、编辑机构
     * @param mo
     */
    public void saveOrg(OrgMo mo, UserInfo userInfo){
        Org org = new Org();
        BeanUtils.copyProperties(mo,org);
        org.setRemoved("0");
        if(null == org.getOrgCode()){
            org.setCreateAt(new Date());
            org.setCreateBy(Optional.ofNullable(userInfo).map((obj)->obj.getId().toString()).orElse(null));
            orgMapper.insert(org);
        }else{
            org.setModifyAt(new Date());
            org.setModifyBy(Optional.ofNullable(userInfo).map((obj)->obj.getId().toString()).orElse(null));
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
        org.setModifyAt(new Date());
        org.setRemoved("1");
        org.setModifyBy(Optional.ofNullable(userInfo).map((obj)->obj.getId().toString()).orElse(null));
        orgMapper.updateByPrimaryKey(org);
    }
}
