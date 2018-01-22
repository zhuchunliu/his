package com.acmed.his.api;

import com.acmed.his.model.Supply;
import com.acmed.his.model.SupplyVsOrg;
import com.acmed.his.service.SupplyVsOrgManager;
import com.acmed.his.support.AccessInfo;
import com.acmed.his.support.AccessToken;
import com.acmed.his.util.ResponseResult;
import com.acmed.his.util.ResponseUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * OrgVsSupplyApi
 * 机构，渠道管理api
 * @author jimson
 * @date 2018/1/22
 */
@RestController
@RequestMapping("supplyvsorg")
@Api(tags = "供应商机构关系管理api")
public class SupplyVsOrgApi {
    @Autowired
    private SupplyVsOrgManager supplyVsOrgManager;

    @ApiOperation(value = "机构批量添加 渠道")
    @GetMapping("addSupplys")
    public ResponseResult add(@AccessToken AccessInfo info,@ApiParam("机构id   用英文逗号拼接") @RequestParam(value = "supplyIds") String supplyIds){
        if (StringUtils.isNotEmpty(supplyIds)) {
            String[] split = supplyIds.split(",");
            List<Integer> supplys = new ArrayList<>();
            for (int i = 0; i < split.length; i++) {
                supplys.add(Integer.valueOf(split[i]));
            }
            supplyVsOrgManager.addSupplys(info.getUserId().toString(), supplys, info.getUser().getOrgCode());
        }
        return ResponseUtil.setSuccessResult();
    }

    @ApiOperation(value = "获取机构关联的渠道渠道列表")
    @GetMapping("supplylist")
    public ResponseResult<List<Supply>> getOrgSupplys(@AccessToken AccessInfo info, @ApiParam("不传表示所有   0 表示 未删除状态的  1表示已删除状态的") @RequestParam(value = "removed" ,required = false) String removed){
        return ResponseUtil.setSuccessResult(supplyVsOrgManager.getSupplyByOrgCode(info.getUser().getOrgCode(),removed));
    }


    @ApiOperation(value = "机构移除以关联渠道")
    @GetMapping("removeSupply")
    public ResponseResult removeSupply(@AccessToken AccessInfo info, @ApiParam("渠道id") @RequestParam(value = "supplyId" ) Integer supplyId){
        Integer userId = info.getUserId();
        Integer orgCode = info.getUser().getOrgCode();
        SupplyVsOrg param = new SupplyVsOrg();
        param.setOrgCode(orgCode);
        param.setSupplyId(supplyId);
        List<SupplyVsOrg> bySupplyVsOrg = supplyVsOrgManager.getBySupplyVsOrg(param);
        if(bySupplyVsOrg.size()!=0){
            SupplyVsOrg supplyVsOrg = bySupplyVsOrg.get(0);
            SupplyVsOrg param1 = new SupplyVsOrg();
            param1.setModifyBy(userId.toString());
            param1.setRemoved("1");
            param1.setId(supplyVsOrg.getId());
            supplyVsOrgManager.update(param1);
        }
        return ResponseUtil.setSuccessResult();
    }
}
