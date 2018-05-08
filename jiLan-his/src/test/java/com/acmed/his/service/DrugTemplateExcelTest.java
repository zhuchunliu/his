package com.acmed.his.service;

import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;

import java.io.FileOutputStream;

/**
 * Created by Darren on 2018-05-07
 **/
public class DrugTemplateExcelTest {

    public static void main(String[] args) throws Exception{
        new DrugTemplateExcelTest().test();
    }




    public void test() throws Exception{


        //文件初始化
        HSSFWorkbook book = new HSSFWorkbook();
        HSSFSheet sheet = book.createSheet("new sheet");

        CellStyle titleStyle = book.createCellStyle();
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

//        first.getCell(0).setCellStyle();

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


        HSSFCellStyle contextstyle =book.createCellStyle();
//        contextstyle.setDataFormat(HSSFDataFormat.getNumberOfBuiltinBuiltinFormats());//数据格式只显示整数
//        contextstyle.setDataFormat(HSSFDataFormat.getBuiltinFormat("#,#0"));
        contextstyle.setDataFormat((short)Integer.reverse(HSSFDataFormat.getNumberOfBuiltinBuiltinFormats()));
        sheet.setColumnWidth(0, 4000);
        Cell cell = sheet.createRow(2).createCell(0);
        cell.setCellValue(1);
        cell.setCellStyle(contextstyle);
//        sheet.setDefaultColumnStyle(0,contextstyle);

//        //普通写入操作
//        for(int index =0; index < 2; index ++){
//            for(int col = 0; col < 16; col++ ){
//                Cell child = sheet.getRow(index).getCell(col);
//                if(null != child) {
//                    child.setCellStyle(titleStyle);
//                }
//            }
//        }



//        for(int index = 0; index < 16; index++ ){
//            sheet.setColumnWidth(index,4000);


        //写入文件

        FileOutputStream fileOut = new FileOutputStream("E://workbook.xls");

        book.write(fileOut);

        fileOut.close();

        System.out.println("Over");

    }

    private Cell getCell(Cell cell, CellStyle style){
        cell.setCellStyle(style);
        return cell;
    }
}
