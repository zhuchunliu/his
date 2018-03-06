package com.acmed.his.service;

import com.acmed.his.dao.AreaMapper;
import com.acmed.his.dao.OrgMapper;
import com.acmed.his.dao.RoleVsPermissionMapper;
import com.acmed.his.dao.UserVsRoleMapper;
import com.acmed.his.model.*;
import com.acmed.his.model.dto.OrgDto;
import com.acmed.his.pojo.mo.OrgMo;
import com.acmed.his.pojo.mo.RoleMo;
import com.acmed.his.pojo.mo.UserMo;
import com.acmed.his.pojo.vo.OrgVo;
import com.acmed.his.pojo.vo.UserInfo;
import com.acmed.his.util.PageBase;
import com.acmed.his.util.PageResult;
import com.acmed.his.util.PinYinUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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

    @Autowired
    private UserManager userManager;

    @Autowired
    private RoleManager roleManager;

    @Autowired
    private PermissionManager permissionManager;

    @Autowired
    private UserVsRoleMapper userVsRoleMapper;

    @Autowired
    private RoleVsPermissionMapper  roleVsPermissionMapper;

    /**
     * 获取机构列表
     * @return
     */
    public List<OrgVo> getOrgList(Integer cityId, String orgName){
        return orgMapper.getOrgListByCity(cityId,orgName);
    }

    /**
     * 获取机构列表
     * @return
     */
    public List<Org> getList(Org org){
        return orgMapper.select(org);
    }

    /**
     * 新增、编辑机构
     * @param mo
     */
    @Transactional
    public void saveOrg(OrgMo mo, UserInfo userInfo){
        if(null == mo.getOrgCode()){
            Org org = new Org();
            BeanUtils.copyProperties(mo,org);
            this.getOrgArea(org);
            org.setRemoved("0");
            org.setCreateAt(LocalDateTime.now().toString());
            org.setCreateBy(userInfo.getId().toString());
            orgMapper.insert(org);
            // 添加用户
            UserMo userMo = new UserMo();
            userMo.setMobile(mo.getMobile());
            userMo.setLoginName(PinYinUtil.getPinYinHeadChar(mo.getOrgName()));
            userMo.setOrgCode(org.getOrgCode());
            User save = userManager.save(userMo, userInfo);
            // 给用户设置管理员权限
            Role roleMo = new Role();
            roleMo.setRoleName("机构管理员");
            roleMo.setRoleCode("orgAdmin"+org.getOrgCode());
            roleMo.setOrgCode(org.getOrgCode());
            roleMo.setOperatorUserId(userInfo.getId().toString());
            Role add = roleManager.add(roleMo);

            UserVsRole userVsRole = new UserVsRole();
            userVsRole.setUid(save.getId());
            userVsRole.setRid(add.getId());
            userVsRoleMapper.insert(userVsRole);
            roleVsPermissionMapper.init(add.getId());
        }else{
            Org org = orgMapper.selectByPrimaryKey(mo.getOrgCode());
            BeanUtils.copyProperties(mo,org);
            this.getOrgArea(org);
            org.setModifyAt(LocalDateTime.now().toString());
            org.setModifyBy(userInfo.getId().toString());
            orgMapper.updateByPrimaryKey(org);
        }
    }

    /**
     * 根据机构地址信息，解析出省市县信息
     * @param org
     */
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
     * @param orgName 机构名称
     * @return
     */
    public List<Org> getOrgList(Double lng, Double lat,Double range,String orgName) {
        Double offset = range * 1.5 / 100; //防止误差，先夸大范围
        List<Org> list = orgMapper.getOrgList(lng-offset,lng+offset,lat-offset,lat+offset,orgName);
        return list;
    }

    public List<Org> getOrgsByIdList(List<Integer> orgCodeList){
        Example example = new Example(Org.class);
        example.createCriteria().andIn("orgCode",orgCodeList);
        return orgMapper.selectByExample(example);
    }

    public PageResult<OrgDto> getOrgDtoByPage(PageBase<String> pageBase) {
        PageResult<OrgDto> result = new PageResult<>();
        result.setPageNum(pageBase.getPageNum());
        result.setPageSize(pageBase.getPageSize());
        PageHelper.startPage(pageBase.getPageNum(),pageBase.getPageSize());
        List<OrgDto> orgDtoList = orgMapper.getOrgDtoList(pageBase.getParam());
        PageInfo<OrgDto> supplyPageInfo = new PageInfo<>(orgDtoList);
        result.setTotal(supplyPageInfo.getTotal());
        result.setData(orgDtoList);
        return result;
    }
}
