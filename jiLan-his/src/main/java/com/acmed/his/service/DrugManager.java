package com.acmed.his.service;

import com.acmed.his.consts.DicTypeEnum;
import com.acmed.his.dao.DrugDictMapper;
import com.acmed.his.dao.DrugMapper;
import com.acmed.his.model.DicItem;
import com.acmed.his.model.Drug;
import com.acmed.his.model.DrugDict;
import com.acmed.his.model.Manufacturer;
import com.acmed.his.pojo.mo.DrugImportMo;
import com.acmed.his.pojo.mo.DrugMo;
import com.acmed.his.pojo.vo.UserInfo;
import com.acmed.his.util.PageResult;
import com.acmed.his.util.PinYinUtil;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.CellRangeAddressList;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * DrugManager
 * 药品
 * @author jimson
 * @date 2017/11/21
 */
@Service
public class DrugManager {
    @Autowired
    private DrugMapper drugMapper;

    @Autowired
    private DrugDictMapper drugDictMapper;

    @Autowired
    private CommonManager commonManager;

    @Autowired
    private BaseInfoManager baseInfoManager;

    @Autowired
    private ManufacturerManager manufacturerManager;

    /**
     * 添加药品
     * @param mo 药
     * @return 0失败 1成功
     */
    public int saveDrug(DrugMo mo,UserInfo userInfo){
        String date = LocalDateTime.now().toString();
        if (mo.getId()!=null){
            Drug drug = drugMapper.selectByPrimaryKey(mo.getId());
            BeanUtils.copyProperties(mo,drug);
            drug.setModifyAt(date);
            drug.setModifyBy(userInfo.getId().toString());
            drug.setPinYin(Optional.ofNullable(drug.getName()).map(PinYinUtil::getPinYinHeadChar).orElse(null));
            drug.setGoodsPinYin(Optional.ofNullable(drug.getGoodsName()).map(PinYinUtil::getPinYinHeadChar).orElse(null));
            drug.setSpec(String.format("%s%s/%s*%s/%s",
                    drug.getDose(),
                    null == drug.getDoseUnit()?"":baseInfoManager.getDicItem(DicTypeEnum.UNIT.getCode(),drug.getDoseUnit().toString()).getDicItemName(),
                    null == drug.getMinUnit()?"":baseInfoManager.getDicItem(DicTypeEnum.UNIT.getCode(),drug.getMinUnit().toString()).getDicItemName(),
                    drug.getConversion(),
                    null == drug.getUnit()?"":baseInfoManager.getDicItem(DicTypeEnum.UNIT.getCode(),drug.getUnit().toString()).getDicItemName()
                    ));
            return drugMapper.updateByPrimaryKeySelective(drug);
        }else {
            Drug drug = new Drug();
            BeanUtils.copyProperties(mo,drug);
            drug.setOrgCode(userInfo.getOrgCode());
            String categoryName = baseInfoManager.getDicItem(DicTypeEnum.DRUG_CLASSIFICATION.getCode(),drug.getCategory().toString()).getDicItemName();
            String key = PinYinUtil.getPinYinHeadChar(categoryName)+new java.text.DecimalFormat("000000").format(userInfo.getOrgCode());
            drug.setDrugCode(key+String.format("%06d",Integer.parseInt(commonManager.getNextVal(key))));
            drug.setSpec(String.format("%s%s/%s*%s/%s",
                    drug.getDose(),
                    null == drug.getDoseUnit()?"":baseInfoManager.getDicItem(DicTypeEnum.UNIT.getCode(),drug.getDoseUnit().toString()).getDicItemName(),
                    null == drug.getMinUnit()?"":baseInfoManager.getDicItem(DicTypeEnum.UNIT.getCode(),drug.getMinUnit().toString()).getDicItemName(),
                    drug.getConversion(),
                    null == drug.getUnit()?"":baseInfoManager.getDicItem(DicTypeEnum.UNIT.getCode(),drug.getUnit().toString()).getDicItemName()
            ));
            drug.setPinYin(Optional.ofNullable(drug.getName()).map(PinYinUtil::getPinYinHeadChar).orElse(null));
            drug.setGoodsPinYin(Optional.ofNullable(drug.getGoodsName()).map(PinYinUtil::getPinYinHeadChar).orElse(null));
            drug.setNum(0);
            drug.setMinNum(0);
            drug.setMinNum(0);
            drug.setCreateAt(date);
            drug.setCreateBy(date);
            drug.setIsValid(1);
            drug.setRemoved("0");
            return drugMapper.insert(drug);
        }

    }

    /**
     * 根据id查询药品详情
     * @param id 药品id
     * @return 药品详情
     */
    public Drug getDrugById(Integer id){
        return drugMapper.selectByPrimaryKey(id);
    }



    /**
     * 模糊搜索药品库信息
     * @param pageNum
     * @param pageSize
     * @param name
     * @param category
     * @return
     */
    public PageResult<Drug> getDrugList(Integer orgCode, String name, String category,String isValid, Integer pageNum, Integer pageSize ) {
        PageResult pageResult = new PageResult();
        Page page = PageHelper.startPage(pageNum,pageSize);
        pageResult.setData(drugMapper.getDrugList(orgCode,name,category,isValid));
        pageResult.setTotal(page.getTotal());
        return pageResult;
    }

    /**
     * 模糊搜索药品库信息
     * @param name
     * @param category
     * @return
     */
    public Integer getDrugTotal(Integer orgCode,String name, String category,String isValid ) {

        return drugMapper.getDrugTotal(orgCode,name,category,isValid);

    }

    /**
     * 删除药品信息
     * @param id
     * @param info
     */
    public void delDrug(Integer id, UserInfo info) {
        Drug drug = drugMapper.selectByPrimaryKey(id);
        drug.setModifyAt(LocalDateTime.now().toString());
        drug.setModifyBy(info.getId().toString());
        drug.setRemoved("1");
        drugMapper.updateByPrimaryKey(drug);
    }

    /**
     * 启用、禁用药品信息
     * @param id
     * @param info
     */
    public void switchDrug(Integer id, UserInfo info) {
        Drug drug = drugMapper.selectByPrimaryKey(id);
        drug.setModifyAt(LocalDateTime.now().toString());
        drug.setModifyBy(info.getId().toString());
        drug.setIsValid(1 == drug.getIsValid()?0:1);
        drugMapper.updateByPrimaryKey(drug);
    }

    /**
     * 获取药品字典表
     * @param orgCode
     * @param name
     * @param category
     * @param pageNum
     * @param pageSize
     * @return
     */
    public PageResult<DrugDict> getDrugDictList(Integer orgCode, String name, String category, Integer pageNum, Integer pageSize) {
        Page page = PageHelper.startPage(pageNum,pageSize);
        List<DrugDict> list = drugDictMapper.getOrgDrugDictList(orgCode,name,category);

        PageResult result = new PageResult();
        result.setTotal(page.getTotal());
        result.setData(list);
        return result;

    }

    public int getDrugDictTotal(Integer orgCode, String name, String category) {
        return drugDictMapper.getDrugDictTotal(orgCode,name,category);
    }

    /**
     * 批量添加药品信息
     * @param codes
     */
    @Transactional
    public List<Drug> saveDrugByDict(String[] codes,UserInfo info,boolean isValid) {
        List<Drug> drugList = Lists.newArrayList();
        for(String code :codes){
            DrugDict dict = drugDictMapper.selectByPrimaryKey(code);
            Drug drug = new Drug();
            BeanUtils.copyProperties(dict,drug,"id");
            drug.setOrgCode(info.getOrgCode());
            String categoryName = baseInfoManager.getDicItem(DicTypeEnum.DRUG_CLASSIFICATION.getCode(),drug.getCategory().toString()).getDicItemName();
            String key = PinYinUtil.getPinYinHeadChar(categoryName)+new java.text.DecimalFormat("000000").format(info.getOrgCode());
            drug.setDrugCode(key+String.format("%06d",Integer.parseInt(commonManager.getNextVal(key))));
            drug.setPinYin(PinYinUtil.getPinYinHeadChar(dict.getName()));
            drug.setGoodsPinYin(PinYinUtil.getPinYinHeadChar(dict.getGoodsName()));
            drug.setDictId(dict.getId());
            drug.setIsValid(isValid?1:0);
            drug.setNum(0);
            drug.setMinNum(0);
            drug.setDoseNum(0d);
            drug.setRemoved("0");
            drug.setCreateAt(LocalDateTime.now().toString());
            drug.setCreateBy(info.getId().toString());
            drugMapper.insert(drug);
            drugList.add(drug);
        }
        return drugList;

    }



    public void importDrug(String[] ids, UserInfo info) {
        List<DrugDict> drugDictList = drugDictMapper.filterDrug(ids,info.getOrgCode());
        for(DrugDict dict :drugDictList){
            Drug drug = new Drug();
            BeanUtils.copyProperties(dict,drug,"id");
            drug.setOrgCode(info.getOrgCode());
            String categoryName = baseInfoManager.getDicItem(DicTypeEnum.DRUG_CLASSIFICATION.getCode(),drug.getCategory().toString()).getDicItemName();
            String key = PinYinUtil.getPinYinHeadChar(categoryName)+new java.text.DecimalFormat("000000").format(info.getOrgCode());
            drug.setDrugCode(key+String.format("%06d",Integer.parseInt(commonManager.getNextVal(key))));
            drug.setPinYin(PinYinUtil.getPinYinHeadChar(dict.getName()));
            drug.setGoodsPinYin(PinYinUtil.getPinYinHeadChar(dict.getGoodsName()));
            drug.setDictId(dict.getId());
            drug.setIsValid(0);
            drug.setNum(0);
            drug.setMinNum(0);
            drug.setDoseNum(0d);
            drug.setRemoved("0");
            drug.setCreateAt(LocalDateTime.now().toString());
            drug.setCreateBy(info.getId().toString());
            drugMapper.insert(drug);
        }
    }


    @Transactional
    public void importDrug(List<DrugImportMo> list , UserInfo info) {

        List<DicItem> unitItmList = baseInfoManager.getDicItemsByDicTypeCode(DicTypeEnum.UNIT.getCode());
        Map<String, Integer> unitItemName = Maps.newHashMap();
        unitItmList.forEach(obj -> {
            unitItemName.put(obj.getDicItemName(),Integer.parseInt(obj.getDicItemCode()));
        });

        List<DicItem> drugFormList = baseInfoManager.getDicItemsByDicTypeCode(DicTypeEnum.DRUG_FORM.getCode());
        Map<String, Integer> drugFormName = Maps.newHashMap();
        drugFormList.forEach(obj -> {
            drugFormName.put(obj.getDicItemName(),Integer.parseInt(obj.getDicItemCode()));
        });

        List<DicItem> categoryList = baseInfoManager.getDicItemsByDicTypeCode(DicTypeEnum.DRUG_CLASSIFICATION.getCode());
        Map<String, Integer> categoryName = Maps.newHashMap();
        categoryList.forEach(obj -> {
            categoryName.put(obj.getDicItemName(),Integer.parseInt(obj.getDicItemCode()));
        });

        List<DicItem> useageList = baseInfoManager.getDicItemsByDicTypeCode(DicTypeEnum.USEAGE.getCode());
        Map<String, Integer> useageName = Maps.newHashMap();
        useageList.forEach(obj -> {
            useageName.put(obj.getDicItemName(),Integer.parseInt(obj.getDicItemCode()));
        });

        List<DicItem> frequencyList = baseInfoManager.getDicItemsByDicTypeCode(DicTypeEnum.DRUG_FREQUENCY.getCode());
        Map<String, Integer> frequencyName = Maps.newHashMap();
        frequencyList.forEach(obj -> {
            frequencyName.put(obj.getDicItemName(),Integer.parseInt(obj.getDicItemCode()));
        });

        for(DrugImportMo mo : list){
            Drug drug = new Drug();
            BeanUtils.copyProperties(mo,drug);
            drug.setOrgCode(info.getOrgCode());
            String key = PinYinUtil.getPinYinHeadChar(mo.getCategoryName())+new java.text.DecimalFormat("000000").format(info.getOrgCode());
            drug.setDrugCode(key+String.format("%06d",Integer.parseInt(commonManager.getNextVal(key))));
            drug.setPinYin(Optional.ofNullable(drug.getName()).map(PinYinUtil::getPinYinHeadChar).orElse(null));
            drug.setGoodsPinYin(Optional.ofNullable(drug.getGoodsName()).map(PinYinUtil::getPinYinHeadChar).orElse(null));

            drug.setCategory(categoryName.get(mo.getCategoryName()));
            drug.setDrugForm(drugFormName.get(mo.getDrugFormName()));
            drug.setUnit(unitItemName.get(mo.getUnitName()));
            drug.setMinUnit(unitItemName.get(mo.getMinUnitName()));
            drug.setDoseUnit(unitItemName.get(mo.getDoseUnitName()));
            drug.setUseage(useageName.get(mo.getUseageName()));
            drug.setFrequency(frequencyName.get(mo.getFrequencyName()));
            drug.setSingleDoseUnit(unitItemName.get(mo.getSingleDoseUnitName()));

            List<Manufacturer> manufacturerList = manufacturerManager.getManufacturerEqualName(mo.getManufacturerName(),info);
            if(null != manufacturerList && 0 != manufacturerList.size()){
                drug.setManufacturer(manufacturerList.get(0).getId());
            }else{
                Manufacturer manufacturer = new Manufacturer();
                manufacturer.setName(mo.getManufacturerName());
                manufacturer.setPinYin(Optional.ofNullable(manufacturer.getName()).map(PinYinUtil::getPinYinHeadChar).orElse(null));
                manufacturer = manufacturerManager.saveManufacturer(manufacturer,info);
                drug.setManufacturer(manufacturer.getId());
            }

            drug.setSpec(String.format("%s%s/%s*%s/%s",
                    drug.getDose(),
                    null == drug.getDoseUnit()?"":baseInfoManager.getDicItem(DicTypeEnum.UNIT.getCode(),drug.getDoseUnit().toString()).getDicItemName(),
                    null == drug.getMinUnit()?"":baseInfoManager.getDicItem(DicTypeEnum.UNIT.getCode(),drug.getMinUnit().toString()).getDicItemName(),
                    drug.getConversion(),
                    null == drug.getUnit()?"":baseInfoManager.getDicItem(DicTypeEnum.UNIT.getCode(),drug.getUnit().toString()).getDicItemName()
            ));

            drug.setDictId(null);
            drug.setIsValid(1);
            drug.setNum(0);
            drug.setMinNum(0);
            drug.setDoseNum(0d);
            drug.setRemoved("0");
            drug.setCreateAt(LocalDateTime.now().toString());
            drug.setCreateBy(info.getId().toString());
            drugMapper.insert(drug);
        }
    }

    /**
     *
     *
     * @return
     */
    public Workbook getTemplet() {

        HSSFWorkbook book =  new HSSFWorkbook();

        //文件初始化
        HSSFSheet sheet = book.createSheet("药品模板");

        HSSFCellStyle titleStyle = book.createCellStyle();
        titleStyle.setBorderBottom(BorderStyle.THIN);
        titleStyle.setBorderLeft(BorderStyle.THIN);
        titleStyle.setBorderRight(BorderStyle.THIN);
        titleStyle.setBorderTop(BorderStyle.THIN);
        titleStyle.setAlignment(HorizontalAlignment.CENTER);
        titleStyle.setVerticalAlignment(VerticalAlignment.CENTER);

        //在第一行第一个单元格，插入下拉框

        sheet.addMergedRegion(new CellRangeAddress(0,1,0,0));
        sheet.addMergedRegion(new CellRangeAddress(0,1,1,1));
        sheet.addMergedRegion(new CellRangeAddress(0,1,2,2));
        sheet.addMergedRegion(new CellRangeAddress(0,1,3,3));
        sheet.addMergedRegion(new CellRangeAddress(0,1,4,4));
        sheet.addMergedRegion(new CellRangeAddress(0,1,5,5));
        sheet.addMergedRegion(new CellRangeAddress(0,1,6,6));
        sheet.addMergedRegion(new CellRangeAddress(0,0,7,11));
        sheet.addMergedRegion(new CellRangeAddress(0,1,12,12));
        sheet.addMergedRegion(new CellRangeAddress(0,1,13,13));
        sheet.addMergedRegion(new CellRangeAddress(0,1,14,14));
        sheet.addMergedRegion(new CellRangeAddress(0,1,15,15));

        HSSFRow first = sheet.createRow(0);
        first.setRowStyle(titleStyle);
        first.createCell(0).setCellValue("药品通用名");
        first.createCell(1).setCellValue("商品名");
        first.createCell(2).setCellValue("药品分类");
        first.createCell(3).setCellValue("剂型");
        first.createCell(4).setCellValue("生产企业");
        first.createCell(5).setCellValue("商品条码");
        first.createCell(6).setCellValue("批准文号");
        first.createCell(7).setCellValue("药品规格");
        for(int index =8 ;index <12; index++){
            first.createCell(index);
        }
        first.createCell(12).setCellValue("药品用法");
        first.createCell(13).setCellValue("用药频次");
        first.createCell(14).setCellValue("单次用量");
        first.createCell(15).setCellValue("单次用量单位");

        HSSFRow second = sheet.createRow(1);
        for(int index =0 ;index <7; index++){
            second.createCell(index);
        }
        for(int index =11 ;index <16; index++){
            second.createCell(index);
        }

        second.createCell(7).setCellValue("大包装单位");
        second.createCell(8).setCellValue("小包装单位");
        second.createCell(9).setCellValue("换算量");
        second.createCell(10).setCellValue("剂量");
        second.createCell(11).setCellValue("剂量单位");

        //普通写入操作
        for(int index =0; index < 2; index ++){
            for(int col = 0; col < 16; col++ ){
                Cell child = sheet.getRow(index).getCell(col);
                if(null != child) child.setCellStyle(titleStyle);

            }
        }

        List<DicItem> unitItmList = baseInfoManager.getDicItems(DicTypeEnum.UNIT.getCode());
        String[] unit = new String[unitItmList.size()];
        for(int index =0; index < unitItmList.size(); index++){
            unit[index] = unitItmList.get(index).getDicItemName();
        }

        List<DicItem> drugFormList = baseInfoManager.getDicItems(DicTypeEnum.DRUG_FORM.getCode());
        String[] drugForm = new String[drugFormList.size()];
        for(int index =0; index < drugFormList.size(); index++){
            drugForm[index] = drugFormList.get(index).getDicItemName();
        }

        List<DicItem> categoryList = baseInfoManager.getDicItems(DicTypeEnum.DRUG_CLASSIFICATION.getCode());
        String[] category = new String[categoryList.size()];
        for(int index =0; index < categoryList.size(); index++){
            category[index] = categoryList.get(index).getDicItemName();
        }

        List<DicItem> useageList = baseInfoManager.getDicItems(DicTypeEnum.USEAGE.getCode());
        String[] useage = new String[useageList.size()];
        for(int index =0; index < useageList.size(); index++){
            useage[index] = useageList.get(index).getDicItemName();
        }

        List<DicItem> frequencyList = baseInfoManager.getDicItems(DicTypeEnum.DRUG_FREQUENCY.getCode());
        String[] frequency = new String[frequencyList.size()];
        for(int index =0; index < frequencyList.size(); index++){
            frequency[index] = frequencyList.get(index).getDicItemName();
        }

        this.setValidate(book,sheet,category,2,2,2);
        this.setValidate(book,sheet,drugForm,2,3,3);
        this.setValidate(book,sheet,unit,2,7,7);
        this.setValidate(book,sheet,unit,2,8,8);
        this.setValidate(book,sheet,unit,2,11,11);
        this.setValidate(book,sheet,useage,2,12,12);
        this.setValidate(book,sheet,frequency,2,13,13);
        this.setValidate(book,sheet,unit,2,15,15);

        // TODO Excel整数限制
        this.setDataValidate(book,sheet,9,Integer.class);
        this.setDataValidate(book,sheet,10,Double.class);
        this.setDataValidate(book,sheet,14,Integer.class);
        for(int index = 0; index < 16; index++ ) {
            sheet.setColumnWidth(index,4000);
        }
        return book;

    }

    private void setDataValidate(HSSFWorkbook workbook,HSSFSheet sheet,int col,Class clazz){
        HSSFCellStyle contextstyle =workbook.createCellStyle();
        if(clazz == Integer.class){
            HSSFDataFormat df = workbook.createDataFormat();
            contextstyle.setDataFormat(df.getBuiltinFormat("#,#0"));//数据格式只显示整数

        }
        if(clazz == Double.class){
            HSSFDataFormat df = workbook.createDataFormat();
            contextstyle.setDataFormat(df.getBuiltinFormat("#,##0.00"));//保留两位小数点

        }
        sheet.setDefaultColumnStyle(col,contextstyle);
    }

    /**
     *
     *
     * @return
     */
    private void setValidate(HSSFWorkbook workbook,HSSFSheet sheet,String[] value,int startRow,int firstCol , int lastCol){
        if(value.length > 10){
            HSSFWorkbook book = (HSSFWorkbook)workbook;
            HSSFSheet hiddenSheet = book.createSheet("sheet"+firstCol);
            int cellNum = 0;
            for (int i = 0, length = value.length; i < length; i++) { // 循环赋值（为了防止下拉框的行数与隐藏域的行数相对应来获取>=选中行数的数组，将隐藏域加到结束行之后）
                hiddenSheet.createRow(i).createCell(cellNum).setCellValue(value[i]);
            }

            Name category1Name = book.createName();
            category1Name.setNameName("abc"+firstCol);
            category1Name.setRefersToFormula(hiddenSheet.getSheetName() + "!A1:A" + (value.length)); // A1:A代表隐藏域创建第?列createCell(?)时。以A1列开始A行数据获取下拉数组

            DVConstraint constraint = DVConstraint.createFormulaListConstraint(category1Name.getRefersToFormula());
            CellRangeAddressList regions = new CellRangeAddressList(startRow, Integer.MAX_VALUE, firstCol, lastCol);
            sheet.addValidationData(new HSSFDataValidation(regions, constraint));
            book.setSheetHidden(book.getSheetIndex(hiddenSheet), true);
            return;
        }

        DVConstraint constraint = DVConstraint.createExplicitListConstraint(value);
        CellRangeAddressList regions = new CellRangeAddressList(startRow, Integer.MAX_VALUE, firstCol, lastCol);
        sheet.addValidationData(new HSSFDataValidation(regions, constraint));

    }
}
