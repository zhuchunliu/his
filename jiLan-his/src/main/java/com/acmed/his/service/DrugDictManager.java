package com.acmed.his.service;

import com.acmed.his.consts.DicTypeEnum;
import com.acmed.his.dao.DrugDictMapper;
import com.acmed.his.model.Drug;
import com.acmed.his.model.DrugDict;
import com.acmed.his.pojo.mo.DrugDictMo;
import com.acmed.his.util.PageResult;
import com.acmed.his.util.PinYinUtil;
import com.acmed.his.util.UUIDUtil;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

/**
 * Created by Darren on 2018-03-22
 **/
@Service
public class DrugDictManager {

    @Autowired
    private DrugDictMapper drugDictMapper;

    @Autowired
    private BaseInfoManager baseInfoManager;

    @Autowired
    private CommonManager commonManager;

    public PageResult<DrugDict> getDrugDictList(String name, String category, Integer isHandle,Integer pageNum, Integer pageSize) {
        PageResult pageResult = new PageResult();
        Page page= PageHelper.startPage(pageNum,pageSize);

        pageResult.setData(drugDictMapper.getDrugDictList(name,category,isHandle));
        pageResult.setTotal(page.getTotal());
        return pageResult;
    }

    public void delDrugDict(String id) {
        DrugDict drug = drugDictMapper.selectByPrimaryKey(id);
        drug.setRemoved("1");
        drugDictMapper.updateByPrimaryKey(drug);
    }

    public void saveDrugDict(DrugDictMo mo) {
        if (mo.getId() != null) {
            DrugDict drug = drugDictMapper.selectByPrimaryKey(mo.getId());
            BeanUtils.copyProperties(mo, drug);
            drug.setPinYin(Optional.ofNullable(drug.getName()).map(PinYinUtil::getPinYinHeadChar).orElse(null));
            drug.setGoodsPinYin(Optional.ofNullable(drug.getGoodsName()).map(PinYinUtil::getPinYinHeadChar).orElse(null));
            drug.setSpec(String.format("%s%s/%s*%s/%s",
                    drug.getDose(),
                    null == drug.getDoseUnit() ? "" : baseInfoManager.getDicItem(DicTypeEnum.UNIT.getCode(), drug.getDoseUnit().toString()).getDicItemName(),
                    null == drug.getMinUnit() ? "" : baseInfoManager.getDicItem(DicTypeEnum.UNIT.getCode(), drug.getMinUnit().toString()).getDicItemName(),
                    drug.getConversion(),
                    null == drug.getUnit() ? "" : baseInfoManager.getDicItem(DicTypeEnum.UNIT.getCode(), drug.getUnit().toString()).getDicItemName()
            ));

            drug.setIsHandle(1);
            drugDictMapper.updateByPrimaryKey(drug);
        } else {
            DrugDict drug = new DrugDict();
            BeanUtils.copyProperties(mo, drug);
            drug.setId(UUIDUtil.generate());
            String categoryName = baseInfoManager.getDicItem(DicTypeEnum.DRUG_CLASSIFICATION.getCode(), drug.getCategory().toString()).getDicItemName();
            drug.setSpec(String.format("%s%s/%s*%s/%s",
                    drug.getDose(),
                    null == drug.getDoseUnit() ? "" : baseInfoManager.getDicItem(DicTypeEnum.UNIT.getCode(), drug.getDoseUnit().toString()).getDicItemName(),
                    null == drug.getMinUnit() ? "" : baseInfoManager.getDicItem(DicTypeEnum.UNIT.getCode(), drug.getMinUnit().toString()).getDicItemName(),
                    drug.getConversion(),
                    null == drug.getUnit() ? "" : baseInfoManager.getDicItem(DicTypeEnum.UNIT.getCode(), drug.getUnit().toString()).getDicItemName()
            ));
            drug.setPinYin(Optional.ofNullable(drug.getName()).map(PinYinUtil::getPinYinHeadChar).orElse(null));
            drug.setGoodsPinYin(Optional.ofNullable(drug.getGoodsName()).map(PinYinUtil::getPinYinHeadChar).orElse(null));
            drug.setRemoved("0");
            drug.setIsHandle(1);
            drug.setSerialNum(Integer.parseInt(commonManager.getNextVal("drugdict_serialnum")));
            drugDictMapper.insert(drug);
        }
    }
}
