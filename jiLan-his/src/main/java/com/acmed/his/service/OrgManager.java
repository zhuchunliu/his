package com.acmed.his.service;

import com.acmed.his.constants.StatusCode;
import com.acmed.his.dao.AreaMapper;
import com.acmed.his.dao.OrgMapper;
import com.acmed.his.dao.RoleVsPermissionMapper;
import com.acmed.his.dao.UserVsRoleMapper;
import com.acmed.his.exceptions.BaseException;
import com.acmed.his.model.*;
import com.acmed.his.model.dto.OrgDto;
import com.acmed.his.pojo.mo.BsgOrgMo;
import com.acmed.his.pojo.mo.OrgMo;
import com.acmed.his.pojo.mo.UserMo;
import com.acmed.his.pojo.vo.OrgVo;
import com.acmed.his.pojo.vo.UserInfo;
import com.acmed.his.util.PageBase;
import com.acmed.his.util.PageResult;
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
    private UserVsRoleMapper userVsRoleMapper;

    @Autowired
    private RoleVsPermissionMapper  roleVsPermissionMapper;

    /**
     * 获取机构列表
     * @return
     */
    public List<OrgVo> getOrgList(Integer cityId, String orgName,String level,String isRecommend){
        return orgMapper.getOrgListByCity(cityId,orgName,level,isRecommend);
    }

    /**
     * 获取机构列表
     * @return
     */
    public List<Org> getList(Org org){
        Example example = new Example(Org.class);
        Example.Criteria criteria = example.createCriteria();
        String city = org.getCity();
        if (StringUtils.isNotEmpty(city)){
            criteria.andEqualTo("city",city);
        }
        String orgName = org.getOrgName();
        if (StringUtils.isNotEmpty(orgName)){
            criteria.andLike("orgName","%"+orgName+"%");
        }
        String level = org.getLevel();
        if (StringUtils.isNotEmpty(level)){
            criteria.andEqualTo("level",level);
        }
        return orgMapper.selectByExample(example);
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
            //默认不是就医北上广医院
            org.setIsRecommend("0");
            orgMapper.insert(org);
            // 添加用户
            UserMo userMo = new UserMo();
            userMo.setMobile(mo.getMobile());
            userMo.setLoginName(mo.getLoginName());
            userMo.setOrgCode(org.getOrgCode());
            userMo.setUserName(mo.getLinkMan());
            User save = userManager.save(userMo, userInfo);
            // 给用户设置管理员权限
            Role roleMo = new Role();
            roleMo.setRoleName("机构管理员");
            roleMo.setRoleCode("orgAdmin"+org.getOrgCode());
            roleMo.setOrgCode(org.getOrgCode());
            roleMo.setOperatorUserId(userInfo.getId().toString());
            Role add = roleManager.add(roleMo);
            org.setManager(save.getId().toString());
            orgMapper.updateByPrimaryKeySelective(org);
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
            orgMapper.updateByPrimaryKeySelective(org);
            if (StringUtils.isNotEmpty(mo.getOrgName())){
                userManager.updateUserOrg(mo.getOrgCode(),mo.getOrgName());
            }
        }
    }


    @Transactional
    public void saveBsgOrg(BsgOrgMo bsgOrgMo,UserInfo userInfo){
        Integer orgCode = bsgOrgMo.getOrgCode();
        if (orgCode==null){
            // 新增
            Org org = new Org();
            org.setOrgName(bsgOrgMo.getOrgName());
            List<Org> select = orgMapper.select(org);
            if(select.size()!=0){
                throw new BaseException(StatusCode.FAIL,"机构已存在");
            }
            BeanUtils.copyProperties(bsgOrgMo,org);
            //this.getOrgArea(org);
            org.setRemoved("0");
            org.setIsRecommend("1");
            org.setCreateAt(LocalDateTime.now().toString());
            org.setCreateBy(userInfo.getId().toString());
            orgMapper.insert(org);
        }else {
            Org org = new Org();
            BeanUtils.copyProperties(bsgOrgMo,org);
            //this.getOrgArea(org);
            org.setModifyAt(LocalDateTime.now().toString());
            org.setModifyBy(userInfo.getId().toString());
            orgMapper.updateByPrimaryKeySelective(org);
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
    public List<Org> getOrgList(Double lng, Double lat,Double range,String orgName,String isRecommend) {
        Double offset = range * 1.5 / 100; //防止误差，先夸大范围
        List<Org> list = orgMapper.getOrgList(lng-offset,lng+offset,lat-offset,lat+offset,orgName,isRecommend);
        return list;
    }

    public List<Org> getOrgsByIdList(List<Integer> orgCodeList){
        Example example = new Example(Org.class);
        example.createCriteria().andIn("orgCode",orgCodeList);
        return orgMapper.selectByExample(example);
    }

    public PageResult<OrgDto> getOrgDtoByPage(PageBase<String> pageBase,String isRecommend) {
        PageResult<OrgDto> result = new PageResult<>();
        result.setPageNum(pageBase.getPageNum());
        result.setPageSize(pageBase.getPageSize());
        PageHelper.startPage(pageBase.getPageNum(),pageBase.getPageSize());
        List<OrgDto> orgDtoList = orgMapper.getOrgDtoList(pageBase.getParam(),null,isRecommend);
        PageInfo<OrgDto> supplyPageInfo = new PageInfo<>(orgDtoList);
        result.setTotal(supplyPageInfo.getTotal());
        result.setData(orgDtoList);
        return result;
    }

    public void switchOrg(Integer id, UserInfo user) {
        Org org = orgMapper.selectByPrimaryKey(id);
        org.setStatus(org.getStatus().equals("1")?"0":"1");
        org.setModifyAt(LocalDateTime.now().toString());
        org.setModifyBy(user.getId().toString());
        orgMapper.updateByPrimaryKey(org);
    }

    public PageResult<OrgDto> getOrgDtoByPage(Integer pageNum,Integer pageSize,String orgName,String cityId,String isRecommend) {
        PageResult<OrgDto> result = new PageResult<>();
        result.setPageNum(pageNum);
        result.setPageSize(pageSize);
        PageHelper.startPage(pageNum,pageSize);
        List<OrgDto> orgDtoList = orgMapper.getOrgDtoList(orgName,cityId,isRecommend);
        PageInfo<OrgDto> supplyPageInfo = new PageInfo<>(orgDtoList);
        result.setTotal(supplyPageInfo.getTotal());
        result.setData(orgDtoList);
        return result;
    }
}
