package com.acmed.his.service;

import com.acmed.his.dao.HisApplicationFormMapper;
import com.acmed.his.model.HisApplicationForm;
import com.acmed.his.util.PageResult;
import com.acmed.his.util.UUIDUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.time.LocalDateTime;
import java.util.List;

/**
 * HisApplicationFormService
 *
 * @author jimson
 * @date 2018/6/4
 */
@Service
public class HisApplicationFormService {
    @Autowired
    private HisApplicationFormMapper hisApplicationFormMapper;

    /**
     * 根据状态查找
     * @param status
     * @return
     */
    public PageResult<HisApplicationForm> getByStatus(Integer status,Integer pageNum,Integer pageSize){
        Example example = new Example(HisApplicationForm.class);
        if (status!=null){
            example.createCriteria().andEqualTo("status",status);
        }
        example.setOrderByClause("createAt desc");
        PageHelper.startPage(pageNum,pageSize);
        List<HisApplicationForm> hisApplicationForms = hisApplicationFormMapper.selectByExample(example);
        PageInfo<HisApplicationForm> pageInfo = new PageInfo<>(hisApplicationForms);
        long total = pageInfo.getTotal();
        PageResult<HisApplicationForm> hisApplicationFormPageResult = new PageResult<>();
        hisApplicationFormPageResult.setTotal(total);
        hisApplicationFormPageResult.setPageNum(pageNum);
        hisApplicationFormPageResult.setPageSize(pageSize);
        hisApplicationFormPageResult.setData(hisApplicationForms);
        return hisApplicationFormPageResult;
    }


    public void createHisApplicationForm(HisApplicationForm hisApplicationForm){
        hisApplicationForm.setId(UUIDUtil.generate32());
        hisApplicationForm.setCreateAt(LocalDateTime.now().toString());
        hisApplicationForm.setCreateBy(null);
        hisApplicationForm.setModifyAt(null);
        hisApplicationForm.setModifyBy(null);
        hisApplicationForm.setStatus(1);
        hisApplicationFormMapper.insert(hisApplicationForm);
    }

    public void updateStatus(String id,Integer status,String modifyBy){
        HisApplicationForm hisApplicationForm = new HisApplicationForm();
        hisApplicationForm.setId(id);
        hisApplicationForm.setModifyAt(LocalDateTime.now().toString());
        hisApplicationForm.setModifyBy(modifyBy);
        hisApplicationForm.setStatus(status);
        hisApplicationFormMapper.updateByPrimaryKeySelective(hisApplicationForm);
    }
}
