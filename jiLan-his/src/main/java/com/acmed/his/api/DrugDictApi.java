package com.acmed.his.api;

import com.acmed.his.constants.StatusCode;
import com.acmed.his.consts.DicTypeEnum;
import com.acmed.his.dao.DrugDictMapper;
import com.acmed.his.dao.ManufacturerMapper;
import com.acmed.his.exceptions.BaseException;
import com.acmed.his.model.DicItem;
import com.acmed.his.model.DrugDict;
import com.acmed.his.pojo.mo.DrugDictMo;
import com.acmed.his.pojo.mo.DrugDictQueryMo;
import com.acmed.his.pojo.vo.DrugDictDetailVo;
import com.acmed.his.pojo.vo.DrugDictListVo;
import com.acmed.his.pojo.vo.DrugListVo;
import com.acmed.his.service.BaseInfoManager;
import com.acmed.his.service.DrugDictManager;
import com.acmed.his.support.AccessInfo;
import com.acmed.his.support.AccessToken;
import com.acmed.his.support.WithoutToken;
import com.acmed.his.util.PageBase;
import com.acmed.his.util.PageResult;
import com.acmed.his.util.ResponseResult;
import com.acmed.his.util.ResponseUtil;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.ibatis.annotations.Param;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.*;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import tk.mybatis.mapper.entity.Example;

import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Created by Darren on 2018-03-22
 **/
@Api(tags = "药品字典管理")
@RequestMapping("/drugdict")
@RestController
public class DrugDictApi {

    @Autowired
    private DrugDictManager drugDictManager;

    @Autowired
    private DrugDictMapper drugDictMapper;

    @Autowired
    private BaseInfoManager baseInfoManager;

    @Autowired
    private ManufacturerMapper manufacturerMapper;

    @ApiOperation(value = "药品信息列表")
    @PostMapping("/list")
    public ResponseResult<PageResult<DrugDictListVo>> getDrugList(@RequestBody(required = false) PageBase<DrugDictQueryMo> pageBase) {

        PageResult<DrugDict> pageResult = drugDictManager.getDrugDictList(Optional.ofNullable(pageBase.getParam()).map(DrugDictQueryMo::getName).orElse(null),
                Optional.ofNullable(pageBase.getParam()).map(DrugDictQueryMo::getCategory).orElse(null),
                Optional.ofNullable(pageBase.getParam()).map(DrugDictQueryMo::getIsHandle).orElse(null),
                pageBase.getPageNum(), pageBase.getPageSize());

        List<DicItem> dicItemList = baseInfoManager.getDicItemsByDicTypeCode(DicTypeEnum.UNIT.getCode());
        Map<String, String> dicItemName = Maps.newHashMap();
        dicItemList.forEach(obj -> {
            dicItemName.put(obj.getDicItemCode(), obj.getDicItemName());
        });

        List<DrugDictListVo> voList = Lists.newArrayList();
        pageResult.getData().forEach(dict -> {
            DrugDictListVo vo = new DrugDictListVo();
            BeanUtils.copyProperties(dict, vo);
            vo.setCategoryName(null == dict.getCategory() ? "" : baseInfoManager.getDicItem(DicTypeEnum.DRUG_CLASSIFICATION.getCode(), dict.getCategory().toString()).getDicItemName());
            vo.setDrugFormName(null == dict.getDrugForm() ? "" : baseInfoManager.getDicItem(DicTypeEnum.DRUG_FORM.getCode(), dict.getDrugForm().toString()).getDicItemName());
            vo.setUnitName(null == dict.getUnit() ? "" : baseInfoManager.getDicItem(DicTypeEnum.UNIT.getCode(), dict.getUnit().toString()).getDicItemName());
            vo.setMinUnitName(null == dict.getMinUnit() ? "" : baseInfoManager.getDicItem(DicTypeEnum.UNIT.getCode(), dict.getMinUnit().toString()).getDicItemName());
            vo.setDoseUnitName(null == dict.getDoseUnit() ? "" : baseInfoManager.getDicItem(DicTypeEnum.UNIT.getCode(), dict.getDoseUnit().toString()).getDicItemName());
            vo.setUseageName(null == dict.getUseage() ? "" : baseInfoManager.getDicItem(DicTypeEnum.USEAGE.getCode(), dict.getUseage().toString()).getDicItemName());
            vo.setPrescriptionTypeName(null == dict.getPrescriptionType() ? "" : baseInfoManager.getDicItem(DicTypeEnum.PRESCRIPTION_TYPE.getCode(), dict.getPrescriptionType().toString()).getDicItemName());
            vo.setManufacturerName(Optional.ofNullable(dict.getManufacturer()).map(obj -> manufacturerMapper.selectByPrimaryKey(obj)).
                    map(obj -> obj.getName()).orElse(""));

            voList.add(vo);
        });
        PageResult result = new PageResult();
        result.setTotal(pageResult.getTotal());
        result.setData(voList);
        return ResponseUtil.setSuccessResult(result);

    }


    @ApiOperation(value = "删除")
    @DeleteMapping("/del")
    public ResponseResult delDrugDict(@ApiParam("药品字典主键") @RequestParam(value = "id") Integer id,
                                      @AccessToken AccessInfo info) {
        drugDictManager.delDrugDict(id);
        return ResponseUtil.setSuccessResult();
    }


    @ApiOperation(value = "更新药品信息")
    @PostMapping("/update")
    public ResponseResult saveDrugDict(@RequestBody DrugDictMo mo, @AccessToken AccessInfo info) {
        drugDictManager.saveDrugDict(mo);
        return ResponseUtil.setSuccessResult();
    }


    @ApiOperation(value = "根据id查询药品详情")
    @GetMapping("/detail")
    public ResponseResult<DrugDictDetailVo> selectDrugsById(@ApiParam("药品id") @RequestParam("id") Integer id) {
        DrugDict drug = drugDictMapper.selectByPrimaryKey(id);
        DrugDictDetailVo vo = new DrugDictDetailVo();
        BeanUtils.copyProperties(drug, vo);
        vo.setCategoryName(null == drug.getCategory() ? "" : baseInfoManager.getDicItem(DicTypeEnum.DRUG_CLASSIFICATION.getCode(), drug.getCategory().toString()).getDicItemName());
        vo.setDrugFormName(null == drug.getDrugForm() ? "" : baseInfoManager.getDicItem(DicTypeEnum.DRUG_FORM.getCode(), drug.getDrugForm().toString()).getDicItemName());
        vo.setUnitName(null == drug.getUnit() ? "" : baseInfoManager.getDicItem(DicTypeEnum.UNIT.getCode(), drug.getUnit().toString()).getDicItemName());
        vo.setMinUnitName(null == drug.getMinUnit() ? "" : baseInfoManager.getDicItem(DicTypeEnum.UNIT.getCode(), drug.getMinUnit().toString()).getDicItemName());
        vo.setDoseUnitName(null == drug.getDoseUnit() ? "" : baseInfoManager.getDicItem(DicTypeEnum.UNIT.getCode(), drug.getDoseUnit().toString()).getDicItemName());
        vo.setUseageName(null == drug.getUseage() ? "" : baseInfoManager.getDicItem(DicTypeEnum.USEAGE.getCode(), drug.getUseage().toString()).getDicItemName());
        vo.setFrequencyName(null == drug.getFrequency() ? "" : baseInfoManager.getDicItem(DicTypeEnum.DRUG_FREQUENCY.getCode(), drug.getFrequency().toString()).getDicItemName());

        vo.setManufacturerName(Optional.ofNullable(drug.getManufacturer()).map(obj -> manufacturerMapper.selectByPrimaryKey(obj)).
                map(obj -> obj.getName()).orElse(""));
        return ResponseUtil.setSuccessResult(vo);
    }

    @ApiOperation(value = "药品信息列表")
    @GetMapping("/export")
    @WithoutToken
    public void export(@Param("type 0对应03版Excel,1对应07版Excel;默认07版")
                       @RequestParam(value = "type", defaultValue = "1") Integer type,
                       @Param("药品分类 参考字典表：category")
                       @RequestParam(value = "category",required = false) String category,
                       HttpServletResponse response) throws IOException {

        Workbook book = 1 == type ? new XSSFWorkbook() : new HSSFWorkbook();
        try {
            response.setHeader("content-Type", "application/vnd.ms-excel");
            response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode("药品字典", "utf-8"));

            List<DrugDict> list = drugDictMapper.getExportDate(category);


            Sheet sheet = book.createSheet("药品字典");
            //标题
            Row row = sheet.createRow(0);
            CellStyle style = book.createCellStyle();
            Font font = book.createFont();
            font.setBold(true);
            style.setFont(font);
            row.setRowStyle(style);
            row.createCell(0).setCellValue("药品ID");
            row.createCell(1).setCellValue("通用名");
            row.createCell(2).setCellValue("商品名");
            row.createCell(3).setCellValue("剂型");
            row.createCell(4).setCellValue("生成企业");
            row.createCell(5).setCellValue("规格");


            for (int index = 0; index < list.size(); index++) {
                DrugDict drugDict = list.get(index);
                sheet.createRow(index + 1);
                row.createCell(0).setCellValue(drugDict.getId());
                row.createCell(1).setCellValue(drugDict.getName());
                row.createCell(2).setCellValue(drugDict.getGoodsName());
                row.createCell(3).setCellValue(drugDict.getDrugFormName());
                row.createCell(4).setCellValue(drugDict.getManufacturerName());
                row.createCell(5).setCellValue(drugDict.getSpec());
            }
            book.write(response.getOutputStream());


        }catch (Exception ex){
            throw new BaseException(StatusCode.FAIL,"字典导出失败");
        }finally {
            book.close();
        }
    }
}
