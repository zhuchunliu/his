package com.acmed.his.api;

import com.acmed.his.consts.DicTypeEnum;
import com.acmed.his.dao.ManufacturerMapper;
import com.acmed.his.model.DicItem;
import com.acmed.his.model.Drug;
import com.acmed.his.pojo.mo.DrugStockPrice;
import com.acmed.his.pojo.vo.DrugStockVo;
import com.acmed.his.service.BaseInfoManager;
import com.acmed.his.service.DrugStockManager;
import com.acmed.his.support.AccessInfo;
import com.acmed.his.support.AccessToken;
import com.acmed.his.util.PageBase;
import com.acmed.his.util.PageResult;
import com.acmed.his.util.ResponseResult;
import com.acmed.his.util.ResponseUtil;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;
import java.util.Optional;

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


    @Autowired
    private ManufacturerMapper manufacturerMapper;

    @ApiOperation(value = "库存查询")
    @PostMapping("/list")
    public ResponseResult<PageResult<DrugStockVo>> stock(@RequestBody(required = false) PageBase<String> page,
                                                         @AccessToken AccessInfo info){

        List<DicItem> dicItemList = baseInfoManager.getDicItemsByDicTypeCode(DicTypeEnum.UNIT.getCode());
        Map<String,String> dicItemName = Maps.newHashMap();
        dicItemList.forEach(obj->{
            dicItemName.put(obj.getDicItemCode(),obj.getDicItemName());
        });

        List<Drug> list = drugStockManager.getStockList(page.getPageNum(),page.getPageSize(),page.getParam(),info.getUser());
        List<DrugStockVo> voList = Lists.newArrayList();
        for(Drug drug:list){
            DrugStockVo vo = new DrugStockVo();
            BeanUtils.copyProperties(drug,vo);

            vo.setManufacturerName(null == drug.getManufacturer()?"":manufacturerMapper.selectByPrimaryKey(drug.getManufacturer()).getName());
            vo.setUnitName(dicItemName.get(drug.getUnit().toString()));
            vo.setMinUnitName(null == drug.getMinUnit()?"":dicItemName.get(drug.getMinUnit().toString()));
            vo.setDoseUnitName(null == drug.getDoseUnit()?"":dicItemName.get(drug.getDoseUnit().toString()));
            vo.setRetailPriceName(drug.getRetailPrice()+"元"+
                    (null == drug.getUnit()?"":"/"+dicItemName.get(drug.getUnit().toString())));
            if(null != drug.getMinPriceUnitType() && null != drug.getMinRetailPrice()){
                vo.setMinRetailPriceName(drug.getMinRetailPrice()+"元"+
                        (1==drug.getMinPriceUnitType()?
                                (null == drug.getMinUnit()?"":"/"+dicItemName.get(drug.getMinUnit().toString())):
                                (null == drug.getDoseUnit()?"":"/"+dicItemName.get(drug.getDoseUnit().toString()))));
            }
            if(null != drug.getNum() && 0 != drug.getNum()){
                vo.setNumName(Optional.ofNullable(vo.getNumName()).orElse("")+drug.getNum()+
                        (null == drug.getUnit()?"":"/"+dicItemName.get(drug.getUnit().toString())));
            }
            if(null != drug.getMinNum() && 0 != drug.getMinNum()){
                vo.setNumName(Optional.ofNullable(vo.getNumName()).orElse("")+drug.getMinNum()+
                        (null == drug.getMinUnit()?"":"/"+dicItemName.get(drug.getMinUnit().toString())));
            }
            if(null != drug.getDoseNum() && 0 != drug.getDoseNum()){
                vo.setNumName(Optional.ofNullable(vo.getNumName()).orElse("")+
                        (0==drug.getDoseNum()*10%1? String.valueOf((int)Math.floor(drug.getDoseNum())):String.valueOf(drug.getDoseNum()))+
                        (null == drug.getDoseUnit()?"":"/"+dicItemName.get(drug.getDoseUnit().toString())));
            }

            voList.add(vo);
        }
        Integer total = drugStockManager.getStockTotal(page.getParam(),info.getUser());
        PageResult result = new PageResult();
        result.setData(voList);
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
