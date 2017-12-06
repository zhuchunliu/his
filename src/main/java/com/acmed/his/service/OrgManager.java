package com.acmed.his.service;

import com.acmed.his.dao.AreaMapper;
import com.acmed.his.dao.OrgMapper;
import com.acmed.his.model.Area;
import com.acmed.his.model.Org;
import com.acmed.his.pojo.mo.OrgMo;
import com.acmed.his.pojo.vo.UserInfo;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * Created by Darren on 2017-11-22
 **/
@Service
public class OrgManager {

    @Autowired
    private OrgMapper orgMapper;

    @Autowired
    private AreaMapper areaMapper;

    /**
     * 获取机构列表
     * @return
     */
    public List<Org> getOrgList(Integer cityId){
        return orgMapper.getOrgListByCity(cityId);
    }

    /**
     * 新增、编辑机构
     * @param mo
     */
    public void saveOrg(OrgMo mo, UserInfo userInfo){

        if(null == mo.getOrgCode()){
            Org org = new Org();
            BeanUtils.copyProperties(mo,org);
            this.getOrgArea(org);
            org.setRemoved("0");
            org.setCreateAt(LocalDateTime.now().toString());
            org.setCreateBy(userInfo.getId().toString());
            orgMapper.insert(org);
        }else{
            Org org = orgMapper.selectByPrimaryKey(mo.getOrgCode());
            BeanUtils.copyProperties(mo,org);
            this.getOrgArea(org);
            org.setModifyAt(LocalDateTime.now().toString());
            org.setModifyBy(userInfo.getId().toString());
            orgMapper.updateByPrimaryKey(org);
        }
    }

    private void getOrgArea(Org org){
        if(StringUtils.isEmpty(org.getAddress())){
            return;
        }
        List<Area> list = areaMapper.getOrgAddress(org.getAddress());
        if(list.size()==0) return;
        Area area = list.get(0);
        if(0 == area.getPid()){
            org.setProvince(area.getId().toString());
            list.forEach(obj->{
                if(obj.getPid().equals(area.getId())){
                    org.setCity(obj.getId().toString());
                    return;
                }
            });
            list.forEach(obj->{
                if(obj.getPid().equals(Integer.parseInt(org.getCity()))){
                    org.setCountry(obj.getId().toString());
                    return;
                }
            });
        }else{
            org.setProvince(area.getPid().toString());
            org.setCity(area.getId().toString());
            list.forEach(obj->{
                if(obj.getPid().equals(area.getId())){
                    org.setCountry(obj.getId().toString());
                    return;
                }
            });
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

    /**
     * 根据经纬度获取最近的医院信息
     * @param lng 经度
     * @param lat 纬度
     * @param range 直线距离
     *
     * @return
     */
    public List<Org> getOrgList(Double lng, Double lat,Double range) {
        Double offset = range * 1.5 / 100; //防止误差，先夸大范围
        List<Org> list = orgMapper.getOrgList(lng-offset,lng+offset,lat-offset,lat+offset);
        return list;
    }
}
