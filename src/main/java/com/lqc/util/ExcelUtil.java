package com.lqc.util;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import java.io.InputStream;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author : liqinchao
 * @CreateTime : 2018/4/16 16:14
 * @Description :Excel文件处理工具类: 包括填充数据到普通excel、模板excel文件,单元格格式处理
 */
public class ExcelUtil {
    /**
     * 填充数据到普通的excel文件中
     *
     * @param rs
     * @param wb
     * @param headers
     *
     */
    public static void fillExcelData(ResultSet rs, Workbook wb, String[] headers
            , String datePattern) throws Exception {
        Sheet sheet = wb.createSheet();
        Row row = sheet.createRow(0);
        //先填充行头 : "编号","姓名","电话","Email","QQ","出生日期"
        for (int i = 0; i < headers.length; i++) {
            row.createCell(i).setCellValue(headers[i]);
        }
        datePattern = StringUtils.isBlank(datePattern) ? "yyyy-MM-dd" : datePattern;
        //再填充数据
        int rowIndex = 1;
        while (rs.next()) {
            row = sheet.createRow(rowIndex++);
            for (int i = 0; i < headers.length; i++) {
                Object objVal = rs.getObject(i + 1);
                if (objVal instanceof Date) {
                    row.createCell(i).setCellValue(CommonUtil.formatDate((Date) objVal, datePattern));
                } else {
                    row.createCell(i).setCellValue(objVal.toString());
                }
            }
        }
    }

    /**
     * 填充数据到模板excel文件
     *
     * @param rs
     * @param templateFile  模板文件（包含路径）
     * @return
     *
     */
    public static Workbook fillExcelDataWithTemplate(ResultSet rs, String templateFile
            , String datePattern) throws Exception {
        //首先:从本地磁盘读取模板excel文件,然后读取第一个sheet
        InputStream inp = ExcelUtil.class.getResourceAsStream(templateFile);
        POIFSFileSystem fs = new POIFSFileSystem(inp);
        Workbook wb = new HSSFWorkbook(fs);
        Sheet sheet = wb.getSheetAt(0);

        datePattern = StringUtils.isBlank(datePattern) ? "yyyy-MM-dd" : datePattern;
        //开始写入数据到模板中: 需要注意的是,因为行头以及设置好,故而需要跳过行头
        int cellNums = sheet.getRow(0).getLastCellNum();
        int rowIndex = 1;
        while (rs.next()) {
            Row row = sheet.createRow(rowIndex++);
            for (int i = 0; i < cellNums; i++) {
                Object objVal = rs.getObject(i + 1);
                if (objVal instanceof Date) {
                    row.createCell(i).setCellValue(CommonUtil.formatDate((Date) objVal, datePattern));
                } else {
                    row.createCell(i).setCellValue(objVal.toString());
                }
            }
        }
        return wb;
    }

    /**
     * 处理单元格格式的简单方式
     *
     * @param hssfCell
     * @return
     */
    public static String formatCell(HSSFCell hssfCell) {
        if (hssfCell == null) {
            return "";
        } else {
            if (hssfCell.getCellTypeEnum() == CellType.BOOLEAN) {
                return String.valueOf(hssfCell.getBooleanCellValue());
            } else if (hssfCell.getCellTypeEnum() == CellType.NUMERIC) {
                return String.valueOf(hssfCell.getNumericCellValue());
            } else {
                return String.valueOf(hssfCell.getStringCellValue());
            }
        }
    }

    /**
     * 处理单元格格式的第二种方式: 包括如何对单元格内容是日期的处理
     *
     * @param cell
     * @return
     */
    public static String formatCell2(HSSFCell cell) {
        if (cell.getCellTypeEnum() == CellType.BOOLEAN) {
            return String.valueOf(cell.getBooleanCellValue());
        } else if (cell.getCellTypeEnum() == CellType.NUMERIC) {
            //针对单元格式为日期格式
            if (HSSFDateUtil.isCellDateFormatted(cell)) {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                return sdf.format(HSSFDateUtil.getJavaDate(cell.getNumericCellValue())).toString();
            }
            return String.valueOf(cell.getNumericCellValue());
        } else {
            return cell.getStringCellValue();
        }
    }

    /**
     * 处理单元格格式的第三种方法:比较全面
     *
     * @param cell
     * @return
     */
    public static String formatCell3(HSSFCell cell) {
        if (cell == null) {
            return "";
        }
        if (cell.getCellTypeEnum() == CellType.NUMERIC) {
            //日期格式的处理
            if (HSSFDateUtil.isCellDateFormatted(cell)) {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                return sdf.format(HSSFDateUtil.getJavaDate(cell.getNumericCellValue())).toString();
            }

            return String.valueOf(cell.getNumericCellValue());
        }

        if (cell.getCellTypeEnum() == CellType.STRING) {
            //字符串
            return cell.getStringCellValue();
        }

        if (cell.getCellTypeEnum() == CellType.FORMULA) {
            // 公式
            return cell.getCellFormula();
        }

        if (cell.getCellTypeEnum() == CellType.BLANK) {
            // 空白
            return "";
        }

        if (cell.getCellTypeEnum() == CellType.BOOLEAN) {
            // 布尔取值
            return cell.getBooleanCellValue() + "";
        }

        if (cell.getCellTypeEnum() == CellType.ERROR) {
            //错误类型
            return cell.getErrorCellValue() + "";
        }
        return "";
    }
}
