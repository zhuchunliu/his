package com.acmed.his.dao;

import com.acmed.his.model.Dept;
import com.acmed.his.pojo.vo.DeptVo;
import com.acmed.his.util.TkMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * DeptMapper
 *
 * @author jimson
 * @date 2017/11/20
 */
public interface DeptMapper extends TkMapper<Dept> {
    List<DeptVo> getDeptVoList(@Param("orgCode") Integer orgCode, @Param("superiorityFlag") Integer superiorityFlag);
}
