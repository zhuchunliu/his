package com.acmed.his.service;

import com.acmed.his.HisApplication;
import com.acmed.his.model.dto.PreTitleDto;
import com.acmed.his.pojo.mo.PreInspectMo;
import com.acmed.his.pojo.mo.PreMedicineMo;
import com.acmed.his.pojo.vo.PreMedicineVo;
import com.acmed.his.pojo.vo.UserInfo;
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
        PreMedicineMo.Item item = new PreMedicineMo.Item();
        item.setDrugId(1);
        item.setDose(1);
        item.setFrequency(300);
        itemList.add(item);

        item = new PreMedicineMo.Item();
        item.setDrugId(2);
        item.setFrequency(500);
        itemList.add(item);

        List<PreMedicineMo.Charge> chargeList = new ArrayList<>();
        PreMedicineMo.Charge charge = new PreMedicineMo.Charge();
        charge.setCategory("1");
        chargeList.add(charge);

        charge = new PreMedicineMo.Charge();
        charge.setCategory("2");
        chargeList.add(charge);

        mo.setChargeList(chargeList);
        mo.setItemList(itemList);

        prescriptionManager.savePreMedicine(mo,new UserInfo());
    }

    @Test
    public void savePreInspect() {
        PreInspectMo mo = new PreInspectMo();
        mo.setApplyId(1);
        mo.setId(2);

        List<PreInspectMo.Inspect> inspectList = new ArrayList<>();
        PreInspectMo.Inspect inspect = new PreInspectMo.Inspect();
        inspect.setPart("大腿-a");
        inspectList.add(inspect);

        inspect = new PreInspectMo.Inspect();
        inspect.setPart("手臂-a");
        inspectList.add(inspect);

        List<PreInspectMo.Charge> chargeList = new ArrayList<>();
        PreInspectMo.Charge charge = new PreInspectMo.Charge();
        charge.setCategory("1");
        chargeList.add(charge);

        charge = new PreInspectMo.Charge();
        charge.setCategory("2");
        chargeList.add(charge);

        mo.setChargeList(chargeList);
        mo.setInspectList(inspectList);

        prescriptionManager.savePreInspect(mo,new UserInfo()
        );
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
