package com.acmed.his.dao;

import com.acmed.his.model.AccompanyingInvitation;
import com.acmed.his.model.dto.InvitationDto;
import com.acmed.his.util.TkMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * AccompanyingInvitationMapper
 *
 * @author jimson
 * @date 2017/12/27
 */
public interface AccompanyingInvitationMapper extends TkMapper<AccompanyingInvitation> {
    List<InvitationDto> getInvitationDtoByUserId(@Param("userId") Integer userId);
}
