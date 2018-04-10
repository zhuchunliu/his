package com.acmed.his.service;

import com.acmed.his.HisApplication;
import com.acmed.his.dao.DrugDictMapper;
import com.acmed.his.model.Drug;
import com.acmed.his.model.DrugDict;
import com.acmed.his.model.Manufacturer;
import com.acmed.his.model.Supply;
import com.acmed.his.pojo.mo.DrugMo;
import com.acmed.his.util.PinYinUtil;
import io.swagger.annotations.ApiModelProperty;
import net.sourceforge.pinyin4j.PinyinHelper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.time.LocalDateTime;
import java.util.List;


/**
 * DrugManagerTest
 *
 * @author jimson
 * @date 2017/11/22
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = HisApplication.class)
public class DrugManagerTest {

    @Autowired
    private DrugManager drugManager;

    @Autowired
    private ManufacturerManager manufacturerManager;

    @Autowired
    private SupplyManager supplyManager;

    @Autowired
    private DrugDictMapper drugDictMapper;

    @Test
    public void addDrug() throws Exception {
        DrugMo drug = new DrugMo();
//        drug.setDrugCode("123");
//        drug.setOrgCode(1);
//        drug.setName("药药药");
//        drug.setSpec("瓶");
//        drug.setCategory("0");
//        drug.setClassification("0");
//        drug.setPackUnit("bao");
//        drug.setUnit("2");
//        drug.setPackNum(1);
//        drug.setDrugForm("计量");
//        drug.setManufacturer(1);
//        drug.setUseage("sdfdsf");
//        drug.setBid(21.1);
//        drug.setRetailPrice(23.0);
//        drug.setMarkonpercent(1.0);
//        drug.setMemo("备注");
//        drug.setCreateAt(LocalDateTime.now().toString());
//        drug.setModifyAt(LocalDateTime.now().toString());
//        drug.setCreateBy(1+"");
//        drug.setModifyBy("1");
        System.out.println(drugManager.saveDrug(drug,null));
    }

    @Test
    public void getDrugsByDrug() throws Exception {
        Drug drug = new Drug();
        drug.setId(2);
//        drugManager.getDrugsByDrug(drug);
    }
    @Test
    public void getDrugsByPinYinLike() throws Exception {
//        drugManager.getDrugsByPinYinLike("Y");
    }

    @Test
    public void saveManufacturer() throws Exception {
        Manufacturer m = new  Manufacturer();
        //m.setId(1);
        m.setName("xYx药品生产厂商");
        m.setAddress("江苏苏州");
        m.setLicenceNo("ISO11112");
        m.setScope("范围");
        manufacturerManager.saveManufacturer(m,null);
    }

    @Test
    public void getManufacturerById() throws Exception {
        Manufacturer m = new  Manufacturer();
        m.setId(1);
        manufacturerManager.getManufacturerById(1);
    }

    @Test
    public void getManufacturerLikeName() throws Exception {
        manufacturerManager.getManufacturerLikeName("y",null);
    }

    @Test
    public void getAllManufacturers() throws Exception {
        manufacturerManager.getAllManufacturers();
    }

    @Test
    public void saveSupply() throws Exception {
        Supply supply = new Supply();
        int i= 1;
        //supply.setId(i);
        supply.setSupplyerName("供货商"+i);
        supply.setAbbrName("ghs"+i);
        supply.setAddress("苏州+"+i+"+号");
        supply.setLinkMan("王"+i);
        supply.setPinYin("py"+i);
        supply.setMobile(123+i+"");
        supply.setBusiscope("业务范围"+i);
        supply.setComment("备注"+i);
        supplyManager.saveSupply(supply,null);
    }

    @Test
    public void getSupplyById() throws Exception {
        supplyManager.getSupplyById(1);
    }

    @Test
    public void getAllSupply() throws Exception {
        supplyManager.getAllSupply();
    }


    @Test
    public void setDrugDictName(){
        List<DrugDict> list = drugDictMapper.selectAll();
        for(DrugDict drugDict : list){
            drugDict.setPinYin(PinYinUtil.getPinYinHeadChar(drugDict.getName()));
            drugDict.setGoodsPinYin(PinYinUtil.getPinYinHeadChar(drugDict.getGoodsName()));
            drugDictMapper.updateByPrimaryKey(drugDict);
            System.err.println(drugDict.getId()+"====");
        }
    }

    public static void main(String[] args) {
        System.err.println(PinYinUtil.getPinYinHeadChar("注射用重组人Ⅱ型肿瘤坏死因子受体-抗体融合蛋白"));
    }

}