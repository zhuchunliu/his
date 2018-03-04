package com.acmed.his.api;

import com.acmed.his.consts.DicTypeEnum;
import com.acmed.his.model.DicItem;
import com.acmed.his.model.dto.DrugStockDto;
import com.acmed.his.pojo.mo.DrugStockPrice;
import com.acmed.his.service.BaseInfoManager;
import com.acmed.his.service.DrugStockManager;
import com.acmed.his.support.AccessInfo;
import com.acmed.his.support.AccessToken;
import com.acmed.his.util.PageBase;
import com.acmed.his.util.PageResult;
import com.acmed.his.util.ResponseResult;
import com.acmed.his.util.ResponseUtil;
import com.google.common.collect.Maps;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * Created by Darren on 2018-03-02
 **/
@Api(tags = "药品库存")
@RequestMapping("/stock")
@RestController
public class DrugStockApi {

    @Autowired
    private DrugStockManager drugStockManager;


    @Autowired
    private BaseInfoManager baseInfoManager;

    @ApiOperation(value = "库存查询")
    @PostMapping("/list")
    public ResponseResult<PageResult<DrugStockDto>> stock(@RequestBody(required = false) PageBase<String> page,
                                                          @AccessToken AccessInfo info){

        List<DicItem> dicItemList = baseInfoManager.getDicItemsByDicTypeCode(DicTypeEnum.UNIT.getCode());
        Map<String,String> dicItemName = Maps.newHashMap();
        dicItemList.forEach(obj->{
            dicItemName.put(obj.getDicItemCode(),obj.getDicItemName());
        });

        List<DrugStockDto> list = drugStockManager.getStockList(page.getPageNum(),page.getPageSize(),page.getParam(),info.getUser());
        for(DrugStockDto dto:list){
            dto.setRetailPriceName(dto.getRetailPrice()+"元/"+dicItemName.get(dto.getUnit()));
            if(null != dto.getMinRetailUnit() && null != dto.getMinRetailPrice()){
                dto.setMinRetailPriceName(dto.getMinRetailPrice()+"元/"+(1==dto.getMinRetailUnit()?dicItemName.get(dto.getMinUnit()):dicItemName.get(dto.getDoseUnit())));
            }
        }
        Integer total = drugStockManager.getStockTotal(page.getParam(),info.getUser());
        PageResult result = new PageResult();
        result.setData(list);
        result.setTotal((long)total);
        return ResponseUtil.setSuccessResult(result);
    }



    @ApiOperation(value = "调价")
    @PostMapping("/modify/price")
    public ResponseResult modifyPrice(@RequestBody DrugStockPrice drugStockPrice,
                                      @AccessToken AccessInfo info){

        drugStockManager.modifyPrice(drugStockPrice,info.getUser());
        return ResponseUtil.setSuccessResult();
    }
}
