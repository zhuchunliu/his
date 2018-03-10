package com.acmed.his.service;

import com.acmed.his.constants.StatusCode;
import com.acmed.his.dao.DeptMapper;
import com.acmed.his.exceptions.BaseException;
import com.acmed.his.model.Dept;
import com.acmed.his.pojo.mo.DeptMo;
import com.acmed.his.pojo.vo.UserInfo;
import org.apache.commons.lang3.StringUtils;
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
public class DeptManager {

    @Autowired
    private DeptMapper deptMapper;

    @Autowired
    private UserManager userManager;

    /**
     * 根据机构获取科室列表
     * @return
     */
    public List<Dept> getDeptList(Integer orgCode){
        Example example = new Example(Dept.class);
        example.createCriteria().andEqualTo("orgCode",orgCode).andEqualTo("removed","0");
        return deptMapper.selectByExample(example);
    }

    /**
     * 条件查询
     * @param dept 条件
     * @return
     */
    public List<Dept> getDeptList(Dept dept){
        return deptMapper.select(dept);
    }


    /**
     * 新增、编辑科室
     * @param mo
     */
    public void saveDept(DeptMo mo,UserInfo userInfo){

        Example example = new Example(Dept.class);
        example.createCriteria().andEqualTo("orgCode",userInfo.getOrgCode()).andEqualTo("dept",mo.getDept()).
                andEqualTo("removed","0");
        List<Dept> list = deptMapper.selectByExample(example);
        if(null != list && 0 != list.size() &&  (null == mo.getId() || list.get(0).getId() != mo.getId())){
            throw new BaseException(StatusCode.FAIL,"科室名称不能重复!");
        }
        if(null == mo.getId()){
            Dept dept = new Dept();
            BeanUtils.copyProperties(mo,dept);
            dept.setOrgCode(userInfo.getOrgCode());
            dept.setRemoved("0");
            dept.setCreateBy(userInfo.getId().toString());
            dept.setCreateAt(LocalDateTime.now().toString());
            deptMapper.insert(dept);
        }else{
            Dept dept = deptMapper.selectByPrimaryKey(mo.getId());
            BeanUtils.copyProperties(mo,dept);
            dept.setModifyBy(userInfo.getId().toString());
            dept.setModifyAt(LocalDateTime.now().toString());
            deptMapper.updateByPrimaryKey(dept);
            if (StringUtils.isNotEmpty(mo.getDept())){
                userManager.updateUserDept(mo.getId(),mo.getDept());
            }
        }
    }

    /**
     * 获取科室详情
     * @param id
     */
    public Dept getDeptDetail(Integer id){
        return deptMapper.selectByPrimaryKey(id);
    }

    /**
     * 删除科室
     * @param id
     */
    public void delDept(Integer id,UserInfo userInfo){
        Dept dept = deptMapper.selectByPrimaryKey(id);
        dept.setRemoved("1");
        dept.setModifyAt(LocalDateTime.now().toString());
        dept.setModifyBy(userInfo.getId().toString());
        deptMapper.updateByPrimaryKey(dept);
    }
}
