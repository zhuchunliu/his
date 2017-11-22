package com.acmed.his.service;

import com.acmed.his.HisApplication;
import com.acmed.his.model.dto.PreTitleDto;
import com.acmed.his.pojo.mo.PreInspectMo;
import com.acmed.his.pojo.mo.PreMedicineMo;
import com.acmed.his.pojo.vo.PreMedicineVo;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Darren on 2017-11-22
 **/
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = HisApplication.class)
public class PrescriptionManagerTest {

    @Autowired
    private PrescriptionManager prescriptionManager;

    @Test
    public void savePreMedicine() {
        PreMedicineMo mo = new PreMedicineMo();
        mo.setApplyId(1);
        mo.setId(4);
        List<PreMedicineMo.Item> itemList = new ArrayList<>();
        PreMedicineMo.Item item = new PreMedicineMo().new Item();
        item.setDrugId(1);
        item.setCourse(1);
        item.setFrequency(300);
        itemList.add(item);

        item = new PreMedicineMo().new Item();
        item.setDrugId(2);
        item.setFrequency(500);
        itemList.add(item);

        List<PreMedicineMo.Charge> chargeList = new ArrayList<>();
        PreMedicineMo.Charge charge = new PreMedicineMo().new Charge();
        charge.setCategory("1");
        charge.setFee(112d);
        chargeList.add(charge);

        charge = new PreMedicineMo().new Charge();
        charge.setCategory("2");
        charge.setFee(130d);
        chargeList.add(charge);

        mo.setChargeList(chargeList);
        mo.setItemList(itemList);

        prescriptionManager.savePreMedicine(mo);
    }

    @Test
    public void savePreInspect() {
        PreInspectMo mo = new PreInspectMo();
        mo.setApplyId(1);
        mo.setId(2);

        List<PreInspectMo.Inspect> inspectList = new ArrayList<>();
        PreInspectMo.Inspect inspect = new PreInspectMo().new Inspect();
        inspect.setPart("大腿-a");
        inspectList.add(inspect);

        inspect = new PreInspectMo().new Inspect();
        inspect.setPart("手臂-a");
        inspectList.add(inspect);

        List<PreInspectMo.Charge> chargeList = new ArrayList<>();
        PreInspectMo.Charge charge = new PreInspectMo().new Charge();
        charge.setCategory("1");
        charge.setFee(230d);
        chargeList.add(charge);

        charge = new PreInspectMo().new Charge();
        charge.setCategory("2");
        charge.setFee(200d);
        chargeList.add(charge);

        mo.setChargeList(chargeList);
        mo.setInspectList(inspectList);

        prescriptionManager.savePreInspect(mo);
    }

    @Test
    public void getPreByApply() {
        prescriptionManager.getPreByApply(1).forEach((obj)->System.err.println(obj));
    }


    @Test
    public void getPreMedicine() {
        System.err.println(prescriptionManager.getPreMedicine(1));
    }

    @Test
    public void getPreInspect() {
        System.err.println(prescriptionManager.getPreInspect(2));
    }
}
