package com.acmed.his.api;

import com.acmed.his.constants.StatusCode;
import com.acmed.his.consts.DicTypeEnum;
import com.acmed.his.dao.ManufacturerMapper;
import com.acmed.his.exceptions.BaseException;
import com.acmed.his.model.DicItem;
import com.acmed.his.model.Drug;
import com.acmed.his.pojo.mo.DrugImportMo;
import com.acmed.his.model.Org;
import com.acmed.his.pojo.mo.DrugMo;
import com.acmed.his.pojo.mo.DrugQueryMo;
import com.acmed.his.pojo.mo.DrugZYQueryMo;
import com.acmed.his.pojo.vo.DrugDictVo;
import com.acmed.his.pojo.vo.DrugListVo;
import com.acmed.his.pojo.vo.DrugVo;
import com.acmed.his.pojo.zy.ZYDrugVo;
import com.acmed.his.service.BaseInfoManager;
import com.acmed.his.service.DrugManager;
import com.acmed.his.service.OrgManager;
import com.acmed.his.service.ZhangYaoManager;
import com.acmed.his.support.AccessInfo;
import com.acmed.his.support.AccessToken;
import com.acmed.his.support.WithoutToken;
import com.acmed.his.util.PageBase;
import com.acmed.his.util.PageResult;
import com.acmed.his.util.ResponseResult;
import com.acmed.his.util.ResponseUtil;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * DrugApi
 *
 * @author jimson
 * @date 2017/11/23
 */
@Api(tags = "药品管理")
@RequestMapping("/drug")
@RestController
public class DrugApi {

    private Logger logger = LoggerFactory.getLogger(DrugDictApi.class);

    @Autowired
    private DrugManager drugManager;

    @Autowired
    private ManufacturerMapper manufacturerMapper;

    @Autowired
    private BaseInfoManager baseInfoManager;

    @Autowired
    private ZhangYaoManager zhangYaoManager;



    @ApiOperation(value = "药品信息列表")
    @PostMapping("/list")
    public ResponseResult<PageResult<DrugListVo>> getDrugList(@RequestBody(required = false) PageBase<DrugQueryMo> pageBase,
                                                              @AccessToken AccessInfo info){
        PageResult<Drug> pageResult = drugManager.getDrugList(info.getUser().getOrgCode(),
                Optional.ofNullable(pageBase.getParam()).map(DrugQueryMo::getName).orElse(null),
                Optional.ofNullable(pageBase.getParam()).map(DrugQueryMo::getCategory).orElse(null),
                Optional.ofNullable(pageBase.getParam()).map(DrugQueryMo::getIsValid).orElse(null),
                pageBase.getPageNum(), pageBase.getPageSize());

        List<DicItem> unitItmList = baseInfoManager.getDicItemsByDicTypeCode(DicTypeEnum.UNIT.getCode());
        Map<String, String> unitItemName = Maps.newHashMap();
        unitItmList.forEach(obj -> {
            unitItemName.put(obj.getDicItemCode(), obj.getDicItemName());
        });

        List<DicItem> drugFormList = baseInfoManager.getDicItemsByDicTypeCode(DicTypeEnum.DRUG_FORM.getCode());
        Map<String, String> drugFormName = Maps.newHashMap();
        drugFormList.forEach(obj -> {
            drugFormName.put(obj.getDicItemCode(), obj.getDicItemName());
        });

        List<DicItem> classficationList = baseInfoManager.getDicItemsByDicTypeCode(DicTypeEnum.DRUG_CLASSIFICATION.getCode());
        Map<String, String> classficationName = Maps.newHashMap();
        classficationList.forEach(obj -> {
            classficationName.put(obj.getDicItemCode(), obj.getDicItemName());
        });

        List<DicItem> useageList = baseInfoManager.getDicItemsByDicTypeCode(DicTypeEnum.USEAGE.getCode());
        Map<String, String> useageName = Maps.newHashMap();
        useageList.forEach(obj -> {
            useageName.put(obj.getDicItemCode(), obj.getDicItemName());
        });

        List<DicItem> prescriptionList = baseInfoManager.getDicItemsByDicTypeCode(DicTypeEnum.PRESCRIPTION_TYPE.getCode());
        Map<String, String> prescriptionName = Maps.newHashMap();
        prescriptionList.forEach(obj -> {
            prescriptionName.put(obj.getDicItemCode(), obj.getDicItemName());
        });

        List<DrugListVo> voList = Lists.newArrayList();
        pageResult.getData().forEach(drug->{
            DrugListVo vo = new DrugListVo();
            BeanUtils.copyProperties(drug,vo);
            vo.setCategoryName(null == drug.getCategory()?"":classficationName.get(drug.getCategory().toString()));
            vo.setDrugFormName(null == drug.getDrugForm()?"":drugFormName.get(drug.getDrugForm().toString()));
            vo.setUnitName(null == drug.getUnit()?"":unitItemName.get(drug.getUnit().toString()));
            vo.setMinUnitName(null==drug.getMinUnit()?"":unitItemName.get(drug.getMinUnit().toString()));
            vo.setDoseUnitName(null==drug.getDoseUnit()?"":unitItemName.get(drug.getDoseUnit().toString()));
            vo.setSingleDoseUnitName(null == drug.getSingleDoseUnit()?"":unitItemName.get(drug.getSingleDoseUnit().toString()));
            vo.setUseageName(null == drug.getUseage()?"":useageName.get(drug.getUseage().toString()));
            vo.setPrescriptionTypeName(null == drug.getPrescriptionType()?"":prescriptionName.get(drug.getPrescriptionType().toString()));
            vo.setManufacturerName(Optional.ofNullable(drug.getManufacturer()).map(obj->manufacturerMapper.selectByPrimaryKey(obj)).
                    map(obj->obj.getName()).orElse(""));

            if(null != drug.getNum() && 0 != drug.getNum()){
                vo.setNumName(Optional.ofNullable(vo.getNumName()).orElse("")+drug.getNum()+
                        (null == drug.getUnit()?"":unitItemName.get(drug.getUnit().toString())));
            }
            if(null != drug.getMinNum() && 0 != drug.getMinNum()){
                vo.setNumName(Optional.ofNullable(vo.getNumName()).orElse("")+drug.getMinNum()+
                        (null == drug.getMinUnit()?"":unitItemName.get(drug.getMinUnit().toString())));
            }
            if(null != drug.getDoseNum() && 0 != drug.getDoseNum()){
                vo.setNumName(Optional.ofNullable(vo.getNumName()).orElse("")+
                        (0==drug.getDoseNum()*10%1? String.valueOf((int)Math.floor(drug.getDoseNum())):String.valueOf(drug.getDoseNum()))+
                        (null == drug.getDoseUnit()?"":unitItemName.get(drug.getDoseUnit().toString())));
            }
            vo.setNeedSupplement(null == drug.getConversion()?1:0);
            voList.add(vo);
        });

        PageResult result = new PageResult();
        result.setTotal(pageResult.getTotal());
        result.setData(voList);
        return ResponseUtil.setSuccessResult(result);


    }

    @ApiOperation(value = "获取药品字典表")
    @PostMapping("/dict")
    public ResponseResult<PageResult<DrugDictVo>> getDrugDictList(@RequestBody(required = false) PageBase<DrugQueryMo> pageBase,
                                                                  @AccessToken AccessInfo info){

        return ResponseUtil.setSuccessResult(drugManager.getDrugDictList(info.getUser().getOrgCode(),
                Optional.ofNullable(pageBase.getParam()).map(DrugQueryMo::getName).orElse(null),
                Optional.ofNullable(pageBase.getParam()).map(DrugQueryMo::getCategory).orElse(null),
                pageBase.getPageNum(), pageBase.getPageSize()));

    }



    @ApiOperation(value = "删除")
    @DeleteMapping("/del")
    public ResponseResult delDrug(@ApiParam("药品主键")@RequestParam(value = "id") Integer id,
                                  @AccessToken AccessInfo info){
        drugManager.delDrug(id,info.getUser());
        return ResponseUtil.setSuccessResult();
    }

    @ApiOperation(value = "启用、禁用药品信息")
    @PostMapping("/switch")
    public ResponseResult swithDrug(@ApiParam("{\"id\":\"\"} id：药品主键") @RequestBody String param,
                                  @AccessToken AccessInfo info){
        if(org.apache.commons.lang3.StringUtils.isEmpty(param) || null == JSONObject.parseObject(param).get("id")){
            return ResponseUtil.setParamEmptyError("id");
        }
        drugManager.switchDrug(JSONObject.parseObject(param).getInteger("id"),info.getUser());
        return ResponseUtil.setSuccessResult();
    }

    @ApiOperation(value = "批量添加药品信息")
    @PostMapping("/batch")
    public ResponseResult saveDrug(@ApiParam("{\"codes\":\"XXX,XXX\"}  code为药品编码集合，逗号间隔") @RequestBody String param,
                                   @AccessToken AccessInfo info){

        if(StringUtils.isEmpty(param) || null == JSONObject.parseObject(param).get("codes")){
            return ResponseUtil.setParamEmptyError("codes");
        }
        drugManager.saveDrugByDict(JSONObject.parseObject(param).get("codes").toString().split(","),info.getUser(),false);
        return ResponseUtil.setSuccessResult();
    }


    @ApiOperation(value = "导入-来源药品字典")
    @PostMapping("/import")
    public ResponseResult importDrugByDict(@RequestParam("file") MultipartFile file ,
                           @AccessToken AccessInfo info) throws IOException{
        String name = file.getOriginalFilename();
        if(!name.contains(".")){
            throw new BaseException(StatusCode.FAIL,"请上传正确的Excel文件");
        }
        String suffix = name.substring(name.lastIndexOf(".")+1,name.length());
        if(!suffix.equalsIgnoreCase("xlsx") &&
                !suffix.equalsIgnoreCase("xls")){
            throw new BaseException(StatusCode.FAIL,"请上传正确的Excel文件");
        }
        Workbook book = null;
        if(suffix.equalsIgnoreCase("xlsx")){
            book = new XSSFWorkbook(file.getInputStream());
        }else{
            book = new HSSFWorkbook(file.getInputStream());
        }
        Sheet sheet = book.getSheetAt(0);
        StringBuilder builder = new StringBuilder();
        if(0 == sheet.getLastRowNum()){
            return ResponseUtil.setSuccessResult();
        }
        for(int index =1; index <= sheet.getLastRowNum();index ++){
            builder.append((int)(sheet.getRow(index).getCell(0).getNumericCellValue())+",");
        }
        String ids = Optional.ofNullable(builder.toString()).map(obj->obj.substring(0,obj.length()-1)).orElse(null);
        if(null != ids) {
            drugManager.importDrug(builder.toString().split(","), info.getUser());
        }
        return ResponseUtil.setSuccessResult();

    }


    @ApiOperation("导出-模板")
    @GetMapping("/export/templet")
    @WithoutToken
    public void getTemplet(HttpServletResponse response) throws Exception{
        Workbook book = drugManager.getTemplet();
        try {
            response.setHeader("content-Type", "application/vnd.ms-excel");
            response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode("药品模板.xls", "utf-8"));

            book.write(response.getOutputStream());
        } catch (IOException ex) {
            ex.printStackTrace();
            logger.error(ex.getMessage(),ex);
            ex.printStackTrace();
        }finally {
            book.close();
        }
    }

    @ApiOperation(value = "导入-来源药品信息")
    @PostMapping("/import/templet")
    public ResponseResult importDrug(@RequestParam("file") MultipartFile file ,
                                     @AccessToken AccessInfo info) throws IOException{
        String name = file.getOriginalFilename();
        if(!name.contains(".")){
            throw new BaseException(StatusCode.FAIL,"请上传正确的Excel文件");
        }
        String suffix = name.substring(name.lastIndexOf(".")+1,name.length());
        if(!suffix.equalsIgnoreCase("xlsx") &&
                !suffix.equalsIgnoreCase("xls")){
            throw new BaseException(StatusCode.FAIL,"请上传正确的Excel文件");
        }
        Workbook book = null;
        if(suffix.equalsIgnoreCase("xlsx")){
            book = new XSSFWorkbook(file.getInputStream());
        }else{
            book = new HSSFWorkbook(file.getInputStream());
        }
        Sheet sheet = book.getSheetAt(0);
        if(1 == sheet.getLastRowNum()){
            return ResponseUtil.setSuccessResult();
        }


        List<DrugImportMo> list = Lists.newArrayList();
        for(int index =2; index <= sheet.getLastRowNum();index ++){
            DrugImportMo mo = new DrugImportMo();
            Row row = sheet.getRow(index);
            mo.setName(this.getRowValue(row.getCell(0),String.class));
            mo.setGoodsName(this.getRowValue(row.getCell(1),String.class));
            mo.setCategoryName(this.getRowValue(row.getCell(2),String.class));
            mo.setDrugFormName(this.getRowValue(row.getCell(3),String.class));
            mo.setManufacturerName(this.getRowValue(row.getCell(4),String.class));
            mo.setBarcode(this.getRowValue(row.getCell(5),String.class));
            mo.setApprovalNumber(this.getRowValue(row.getCell(6),String.class));
            mo.setUnitName(this.getRowValue(row.getCell(7),String.class));
            mo.setMinUnitName(this.getRowValue(row.getCell(8),String.class));
            mo.setConversion(this.getRowValue(row.getCell(9),Integer.class));
            mo.setDose(this.getRowValue(row.getCell(10),Double.class));
            mo.setDoseUnitName(this.getRowValue(row.getCell(11),String.class));
            mo.setUseageName(this.getRowValue(row.getCell(12),String.class));
            mo.setFrequencyName(this.getRowValue(row.getCell(13),String.class));
            mo.setSingleDose(this.getRowValue(row.getCell(14),Double.class));
            mo.setSingleDoseUnitName(this.getRowValue(row.getCell(15),String.class));
            list.add(mo);

        }

        drugManager.importDrug(list, info.getUser());
        return ResponseUtil.setSuccessResult();

    }


    @ApiOperation(value = "更新药品信息")
    @PostMapping("/update")
    public ResponseResult saveDrug(@RequestBody DrugMo mo,@AccessToken AccessInfo info){
        drugManager.saveDrug(mo,info.getUser());
        return ResponseUtil.setSuccessResult();
    }


    @ApiOperation(value = "根据id查询药品详情")
    @GetMapping("/detail")
    public ResponseResult<DrugVo> selectDrugsById(@ApiParam("药品id") @RequestParam("id") Integer id){
        Drug drug = drugManager.getDrugById(id);
        DrugVo vo = new DrugVo();
        BeanUtils.copyProperties(drug,vo);
        vo.setCategoryName(null==drug.getCategory()?"":baseInfoManager.getDicItem(DicTypeEnum.DRUG_CLASSIFICATION.getCode(),drug.getCategory().toString()).getDicItemName());
        vo.setDrugFormName(null==drug.getDrugForm()?"":baseInfoManager.getDicItem(DicTypeEnum.DRUG_FORM.getCode(),drug.getDrugForm().toString()).getDicItemName());
        vo.setUnitName(null==drug.getUnit()?"":baseInfoManager.getDicItem(DicTypeEnum.UNIT.getCode(),drug.getUnit().toString()).getDicItemName());
        vo.setMinUnitName(null==drug.getMinUnit()?"":baseInfoManager.getDicItem(DicTypeEnum.UNIT.getCode(),drug.getMinUnit().toString()).getDicItemName());
        vo.setDoseUnitName(null==drug.getDoseUnit()?"":baseInfoManager.getDicItem(DicTypeEnum.UNIT.getCode(),drug.getDoseUnit().toString()).getDicItemName());
        vo.setUseageName(null==drug.getUseage()?"":baseInfoManager.getDicItem(DicTypeEnum.USEAGE.getCode(),drug.getUseage().toString()).getDicItemName());
        vo.setFrequencyName(null == drug.getFrequency()?"":baseInfoManager.getDicItem(DicTypeEnum.DRUG_FREQUENCY.getCode(),drug.getFrequency().toString()).getDicItemName());
        vo.setSingleDoseUnitName(null == drug.getSingleDoseUnit()?"":baseInfoManager.getDicItem(DicTypeEnum.DRUG_FREQUENCY.getCode(),drug.getSingleDoseUnit().toString()).getDicItemName());

        vo.setManufacturerName(Optional.ofNullable(drug.getManufacturer()).map(obj->manufacturerMapper.selectByPrimaryKey(obj)).
                map(obj->obj.getName()).orElse(""));
        return ResponseUtil.setSuccessResult(vo);
    }





    private <T> T getRowValue(Cell cell, Class<T> clazz){
        if(cell.getCellTypeEnum() == CellType.STRING){
            return (T)cell.getStringCellValue();
        }
        if(cell.getCellTypeEnum() == CellType.NUMERIC){
            if(clazz == Integer.class){
                return (T)new Integer(Integer.parseInt(String.format("%.0f",cell.getNumericCellValue())));
            }
            if(clazz == Double.class){
                return (T)new Double(cell.getNumericCellValue());
            }
            return (T)String.valueOf(new BigDecimal(cell.getNumericCellValue()));
        }
        return null;
    }

}
