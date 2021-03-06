package com.acmed.his.api;

import com.acmed.his.consts.DicTypeEnum;
import com.acmed.his.dao.DrugMapper;
import com.acmed.his.dao.ManufacturerMapper;
import com.acmed.his.model.DicItem;
import com.acmed.his.model.Drug;
import com.acmed.his.model.DrugStock;
import com.acmed.his.model.dto.DrugWarnDto;
import com.acmed.his.pojo.mo.DrugStockPrice;
import com.acmed.his.pojo.vo.DrugStockVo;
import com.acmed.his.pojo.vo.DrugStockWarnVo;
import com.acmed.his.pojo.vo.DrugWarnVo;
import com.acmed.his.service.BaseInfoManager;
import com.acmed.his.service.DrugStockManager;
import com.acmed.his.support.AccessInfo;
import com.acmed.his.support.AccessToken;
import com.acmed.his.util.PageBase;
import com.acmed.his.util.PageResult;
import com.acmed.his.util.ResponseResult;
import com.acmed.his.util.ResponseUtil;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
    private DrugMapper drugMapper;

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

        PageResult<Drug> pageResult = drugStockManager.getStockList(page.getPageNum(),page.getPageSize(),page.getParam(),info.getUser());
        List<DrugStockVo> voList = Lists.newArrayList();
        for(Drug drug:pageResult.getData()){
            DrugStockVo vo = new DrugStockVo();
            BeanUtils.copyProperties(drug,vo);

            vo.setManufacturerName(null == drug.getManufacturer()?"":manufacturerMapper.selectByPrimaryKey(drug.getManufacturer()).getName());
            vo.setUnitName(null == drug.getUnit()?"":dicItemName.get(drug.getUnit().toString()));
            vo.setMinUnitName(null == drug.getMinUnit()?"":dicItemName.get(drug.getMinUnit().toString()));
            vo.setDoseUnitName(null == drug.getDoseUnit()?"":dicItemName.get(drug.getDoseUnit().toString()));
            if(null != drug.getRetailPrice()) {
                vo.setRetailPriceName(drug.getRetailPrice() + "元" +
                        (null == drug.getUnit() ? "" : "/" + dicItemName.get(drug.getUnit().toString())));
            }
            if(null != drug.getMinPriceUnitType() && null != drug.getMinRetailPrice()){
                vo.setMinRetailPriceName(drug.getMinRetailPrice()+"元"+
                        (1==drug.getMinPriceUnitType()?
                                (null == drug.getMinUnit()?"":"/"+dicItemName.get(drug.getMinUnit().toString())):
                                (null == drug.getDoseUnit()?"":"/"+dicItemName.get(drug.getDoseUnit().toString()))));
            }
            if(null != drug.getNum() && 0 != drug.getNum()){
                vo.setNumName(Optional.ofNullable(vo.getNumName()).orElse("")+drug.getNum()+
                        (null == drug.getUnit()?"":dicItemName.get(drug.getUnit().toString())));
            }
            if(null != drug.getMinNum() && 0 != drug.getMinNum()){
                vo.setNumName(Optional.ofNullable(vo.getNumName()).orElse("")+drug.getMinNum()+
                        (null == drug.getMinUnit()?"":dicItemName.get(drug.getMinUnit().toString())));
            }
            if(null != drug.getDoseNum() && 0 != drug.getDoseNum()){
                vo.setNumName(Optional.ofNullable(vo.getNumName()).orElse("")+
                        (0==drug.getDoseNum()*10%1? String.valueOf((int)Math.floor(drug.getDoseNum())):String.valueOf(drug.getDoseNum()))+
                        (null == drug.getDoseUnit()?"":dicItemName.get(drug.getDoseUnit().toString())));
            }

            voList.add(vo);
        }
        PageResult result = new PageResult();
        result.setData(voList);
        result.setTotal(pageResult.getTotal());
        return ResponseUtil.setSuccessResult(result);
    }



    @ApiOperation(value = "调价")
    @PostMapping("/modify/price")
    public ResponseResult modifyPrice(@RequestBody DrugStockPrice drugStockPrice,
                                      @AccessToken AccessInfo info){

        drugStockManager.modifyPrice(drugStockPrice,info.getUser());
        return ResponseUtil.setSuccessResult();
    }


    @ApiOperation(value = "库存预警列表")
    @PostMapping("/warn/list")
    public ResponseResult<PageResult<DrugWarnVo>> warnList(@RequestBody(required = false) PageBase<String> page,
                                                           @AccessToken AccessInfo info){
        List<DrugWarnDto> dtoList = drugStockManager.getWarnList(page.getPageNum(),page.getPageSize(),page.getParam(),info.getUser());

        List<DicItem> dicItemList = baseInfoManager.getDicItemsByDicTypeCode(DicTypeEnum.UNIT.getCode());
        Map<String,String> dicItemName = Maps.newHashMap();
        dicItemList.forEach(obj->{
            dicItemName.put(obj.getDicItemCode(),obj.getDicItemName());
        });

        List<DrugWarnVo> list = Lists.newArrayList();
        dtoList.forEach(drug->{
            DrugWarnVo vo = new DrugWarnVo();
            BeanUtils.copyProperties(drug,vo);
            vo.setManufacturerName(null == drug.getManufacturer()?"":manufacturerMapper.selectByPrimaryKey(drug.getManufacturer()).getName());
            if(null != drug.getSafetyNum() && 0 != drug.getSafetyNum()) {
                vo.setSafetyNumName(drug.getSafetyNum() +
                        (null == drug.getUnit() ? "" : dicItemName.get(drug.getUnit().toString())));
            }
            if(null != drug.getNum() && 0 != drug.getNum()){
                vo.setNumName(Optional.ofNullable(vo.getNumName()).orElse("")+drug.getNum()+
                        (null == drug.getUnit()?"":dicItemName.get(drug.getUnit().toString())));
            }
            if(null != drug.getMinNum() && 0 != drug.getMinNum()){
                vo.setNumName(Optional.ofNullable(vo.getNumName()).orElse("")+drug.getMinNum()+
                        (null == drug.getMinUnit()?"":dicItemName.get(drug.getMinUnit().toString())));
            }
            if(null != drug.getDoseNum() && 0 != drug.getDoseNum()){
                vo.setNumName(Optional.ofNullable(vo.getNumName()).orElse("")+
                        (0==drug.getDoseNum()*10%1? String.valueOf((int)Math.floor(drug.getDoseNum())):String.valueOf(drug.getDoseNum()))+
                        (null == drug.getDoseUnit()?"":dicItemName.get(drug.getDoseUnit().toString())));
            }

            if(null != drug.getExpireNum() && 0 != drug.getExpireNum()){
                vo.setExpireNumName(Optional.ofNullable(vo.getExpireNumName()).orElse("")+drug.getExpireNum()+
                        (null == drug.getUnit()?"":dicItemName.get(drug.getUnit().toString())));
            }
            if(null != drug.getExpireMinNum() && 0 != drug.getExpireMinNum()){
                vo.setExpireNumName(Optional.ofNullable(vo.getExpireNumName()).orElse("")+drug.getExpireMinNum()+
                        (null == drug.getMinUnit()?"":dicItemName.get(drug.getMinUnit().toString())));
            }
            if(null != drug.getExpireDoseNum() && 0 != drug.getExpireDoseNum()){
                vo.setExpireNumName(Optional.ofNullable(vo.getExpireNumName()).orElse("")+
                        (0==drug.getExpireDoseNum()*10%1? String.valueOf((int)Math.floor(drug.getExpireDoseNum())):String.valueOf(drug.getExpireDoseNum()))+
                        (null == drug.getDoseUnit()?"":dicItemName.get(drug.getDoseUnit().toString())));
            }
            if(drug.getWarnType() == 2 && (null != drug.getSafetyNum() && drug.getNum() < drug.getSafetyNum())){
                vo.setWarnType(3);
            }
            list.add(vo);
        });
        Integer total = drugStockManager.getWarnTotal(page.getParam(),info.getUser());
        PageResult result = new PageResult();
        result.setData(list);
        result.setTotal((long)total);
        return ResponseUtil.setSuccessResult(result);
    }


    @ApiOperation(value = "过期药详情")
    @GetMapping("/warn/drug")
    public ResponseResult<DrugStockWarnVo> warnList(@ApiParam("药品id") @RequestParam("drugId") Integer drugId,
                                                    @ApiParam("是否过滤过期药 1：是，0：否 默认为1") @RequestParam(value = "type",defaultValue = "1") Integer type){
        List<DrugStock> drugStockList = drugStockManager.getWarnDrug(drugId,type);
        Drug drug = drugMapper.selectByPrimaryKey(drugId);

        List<DicItem> dicItemList = baseInfoManager.getDicItemsByDicTypeCode(DicTypeEnum.UNIT.getCode());
        Map<String,String> dicItemName = Maps.newHashMap();
        dicItemList.forEach(obj->{
            dicItemName.put(obj.getDicItemCode(),obj.getDicItemName());
        });

        List<DrugStockWarnVo> list = Lists.newArrayList();
        drugStockList.forEach(stock -> {
            DrugStockWarnVo vo = new DrugStockWarnVo();
            BeanUtils.copyProperties(stock,vo);
            if(null != stock.getNum() && 0 != stock.getNum()){
                vo.setNumName(Optional.ofNullable(vo.getNumName()).orElse("")+stock.getNum()+
                        (null == drug.getUnit()?"":dicItemName.get(drug.getUnit().toString())));
            }
            if(null != stock.getMinNum() && 0 != stock.getMinNum()){
                vo.setNumName(Optional.ofNullable(vo.getNumName()).orElse("")+stock.getMinNum()+
                        (null == drug.getMinUnit()?"":dicItemName.get(drug.getMinUnit().toString())));
            }
            if(null != stock.getDoseNum() && 0 != stock.getDoseNum()){
                vo.setNumName(Optional.ofNullable(vo.getNumName()).orElse("")+
                        (0==stock.getDoseNum()*10%1? String.valueOf((int)Math.floor(stock.getDoseNum())):String.valueOf(stock.getDoseNum()))+
                        (null == drug.getDoseUnit()?"":dicItemName.get(drug.getDoseUnit().toString())));
            }
            list.add(vo);
        });
        return ResponseUtil.setSuccessResult(list);
    }

    @ApiOperation(value = "是否需要预警")
    @GetMapping("/warn")
    public ResponseResult needWarn(@AccessToken AccessInfo info){
        return ResponseUtil.setSuccessResult(ImmutableMap.of("needWarn",drugStockManager.isNeedWarn(info.getUser().getOrgCode())));
    }



}
