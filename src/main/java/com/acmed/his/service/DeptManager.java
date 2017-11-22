package com.acmed.his.service;

import com.acmed.his.dao.DeptMapper;
import com.acmed.his.model.Dept;
import com.acmed.his.pojo.mo.DeptMo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.Date;
import java.util.List;

/**
 * Created by Darren on 2017-11-22
 **/
@Service
public class DeptManager {

    @Autowired
    private DeptMapper deptMapper;

    /**
     * 根据机构获取科室列表
     * @return
     */
    public List<Dept> getDeptList(Integer orgCode){
        Example example = new Example(Dept.class);
        example.createCriteria().andEqualTo("orgCode",orgCode);
        return deptMapper.selectByExample(example);
    }

    /**
     * 新增、编辑科室
     * @param mo
     */
    public void saveDept(DeptMo mo){
        Dept dept = new Dept();
        BeanUtils.copyProperties(mo,dept);
        if(null == dept.getId()){
            dept.setCreateAt(new Date());
            deptMapper.insert(dept);
        }else{
            dept.setModifyAt(new Date());
            deptMapper.updateByPrimaryKey(dept);
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
    public void delDept(Integer id){

        deptMapper.deleteByPrimaryKey(id);
    }
}
