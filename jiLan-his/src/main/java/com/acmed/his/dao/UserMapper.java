package com.acmed.his.dao;

import com.acmed.his.model.User;
import com.acmed.his.model.dto.UserDto;
import com.acmed.his.util.TkMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * UserMapper
 *
 * @author jimson
 * @date 2017/11/20
 */
public interface UserMapper extends TkMapper<User> {

    User getUserByOpenid(@Param("openid") String openid);

    List<UserDto> getUserList(@Param("deptId") Integer deptId, @Param("mobile") String mobile,
                              @Param("userName") String userName, @Param("status") String status,
                              @Param("orgCode") Integer orgCode,
                              @Param("userCategory")String userCategory,@Param("diagnosisLevel")String diagnosisLevel,
                              @Param("duty")String duty);

    Integer getUserTotal(@Param("deptId") Integer deptId, @Param("mobile") String mobile,
                     @Param("userName") String userName, @Param("status") String status,
                         @Param("orgCode") Integer orgCode);

    // 修改指定科室id下用户的科室名
    int updateUserDept(@Param("deptId") Integer deptId,@Param("deptName")String deptName);
    int updateUserOrg(@Param("orgCode") Integer orgCode,@Param("orgName")String orgName);
}
