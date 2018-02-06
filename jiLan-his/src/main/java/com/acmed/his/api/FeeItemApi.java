package com.acmed.his.api;

import com.acmed.his.consts.DicTypeEnum;
import com.acmed.his.pojo.mo.FeeItemMo;
import com.acmed.his.pojo.vo.FeeItemVo;
import com.acmed.his.service.BaseInfoManager;
import com.acmed.his.service.FeeItemManager;
import com.acmed.his.support.AccessInfo;
import com.acmed.his.support.AccessToken;
import com.acmed.his.util.ResponseResult;
import com.acmed.his.util.ResponseUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;

/**
 * Created by Darren on 2017-11-22
 **/
@RestController
@Api(tags = "收费项目管理")
@RequestMapping("/fee")
public class FeeItemApi {

    @Autowired
    private FeeItemManager feeItemManager;

    @Autowired
    private BaseInfoManager baseInfoManager;

    @ApiOperation(value = "新增/编辑 收费项目信息")
    @PostMapping("/save")
    public ResponseResult saveFeeItem(@ApiParam("id等于null:新增; feeItemCode不等于null：编辑") @RequestBody FeeItemMo mo,
                                      @AccessToken AccessInfo info){
        Map<String, Object> paramMap = new WeakHashMap<>();
        paramMap.put("feeCategory",mo.getFeeCategory());
        paramMap.put("category",mo.getCategory());
        ResponseResult result = ResponseUtil.getParamEmptyError(paramMap) ;
        if(null != result){
            return  result;
        }
        feeItemManager.saveFeeItem(mo,info.getUser());
        return ResponseUtil.setSuccessResult();
    }

    @ApiOperation(value = "获取收费项目列表")
    @GetMapping("/list")
    public ResponseResult<List<FeeItemVo>> getFeeItemList(@AccessToken AccessInfo info,
                                                          @ApiParam("费用类别大项") @RequestParam(value = "feeCategory",required = false) String feeCategory,
                                                          @ApiParam("费用类型细项") @RequestParam(value = "category",required = false) String category,
                                                          @ApiParam("费用类型细项名称") @RequestParam(value = "categoryName",required = false) String categoryName){
        List<FeeItemVo> list = new ArrayList<>();

        feeItemManager.getFeeItemList(info.getUser().getOrgCode(),feeCategory,category,categoryName).forEach((obj)->{
            FeeItemVo mo = new FeeItemVo();
            BeanUtils.copyProperties(obj,mo);
            list.add(mo);
        });
        return ResponseUtil.setSuccessResult(list);
    }

    @ApiOperation(value = "获取收费项目详情")
    @GetMapping("/detail")
    public ResponseResult<FeeItemVo> getFeeItemDetail(@ApiParam("收费项目主键") @RequestParam("id") Integer id){
        FeeItemVo mo = new FeeItemVo();
        BeanUtils.copyProperties(feeItemManager.getFeeItemDetail(id),mo);
        mo.setFeeCategoryName(baseInfoManager.getDicItem(DicTypeEnum.FEE_ITEM.getCode(),mo.getFeeCategory()).getDicItemName());
        mo.setCategoryName(baseInfoManager.getDicItem(mo.getFeeCategory(),mo.getCategory()).getDicItemName());
        return ResponseUtil.setSuccessResult(mo);
    }

    @ApiOperation(value = "删除收费项目信息")
    @DeleteMapping("/del")
    public ResponseResult delFeeItem(@ApiParam("收费项目主键") @RequestParam("id") Integer id,
                                     @AccessToken AccessInfo info){
        feeItemManager.delFeeItem(id,info.getUser());
        return ResponseUtil.setSuccessResult();
    }
}
