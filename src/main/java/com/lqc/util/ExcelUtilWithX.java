package com.lqc.util;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import vo.ErrorDataExcel;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.math.BigDecimal;
import java.sql.ResultSet;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

/**
 * @author : liqinchao
 * @CreateTime : 2018/4/16 16:14
 * @Description :Excel文件处理工具类: 2007+之后版本
 * 包括填充数据到普通excel、模板excel文件,单元格格式处理
 */
public class ExcelUtilWithX {
    /**
     * 填充数据到普通的excel文件中
     *
     * @param rs
     * @param wb
     * @param headers
     *
     */
    public static void fillExcelData(ResultSet rs, XSSFWorkbook wb, String[] headers
            , String datePattern) throws Exception {
        XSSFSheet sheet = wb.createSheet();
        XSSFRow row = sheet.createRow(0);
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
     * @param templateFile 模板文件（包含路径）
     * @return
     *
     */
    public static Workbook fillExcelDataWithTemplate(ResultSet rs, String templateFile
            , String datePattern) throws Exception {
        //首先:从本地磁盘读取模板excel文件,然后读取第一个sheet
        InputStream inp = util.ExcelUtilWithX.class.getResourceAsStream(templateFile);
        XSSFWorkbook wb = new XSSFWorkbook(inp);
        XSSFSheet sheet = wb.getSheetAt(0);

        datePattern = StringUtils.isBlank(datePattern) ? "yyyy-MM-dd" : datePattern;
        //开始写入数据到模板中: 需要注意的是,因为行头以及设置好,故而需要跳过行头
        int cellNums = sheet.getRow(0).getLastCellNum();
        int rowIndex = 1;
        while (rs.next()) {
            XSSFRow row = sheet.createRow(rowIndex++);
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
     * @param cell
     * @return
     */
    public static String formatCell(XSSFCell cell) {
        DecimalFormat df = new DecimalFormat("0");
        if (cell == null) {
            return "";
        } else {
            if (cell.getCellTypeEnum() == CellType.BOOLEAN) {
                return String.valueOf(cell.getBooleanCellValue());
            } else if (cell.getCellTypeEnum() == CellType.NUMERIC) {
                return String.valueOf(df.format(cell.getNumericCellValue()));
            } else {
                return String.valueOf(cell.getStringCellValue());
            }
        }
    }

    /**
     * 处理单元格格式的第二种方式: 包括如何对单元格内容是日期的处理
     *
     * @param cell
     * @return
     */
    public static String formatCell2(XSSFCell cell) {
        if (cell.getCellTypeEnum() == CellType.BOOLEAN) {
            return String.valueOf(cell.getBooleanCellValue());
        } else if (cell.getCellTypeEnum() == CellType.NUMERIC) {
            //针对单元格式为日期格式
            if (DateUtil.isCellDateFormatted(cell)) {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                return sdf.format(DateUtil.getJavaDate(cell.getNumericCellValue())).toString();
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
    public static String formatCell3(XSSFCell cell) {
        if (cell == null) {
            return "";
        }
        if (cell.getCellTypeEnum() == CellType.NUMERIC) {
            String numStr="0";
            double num = cell.getNumericCellValue();
            //日期格式的处理
            if (DateUtil.isCellDateFormatted(cell)) {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                return sdf.format(DateUtil.getJavaDate(num)).toString();
            }
            double eps = 1e-10;
            boolean isInteger = num - Math.floor(num) < eps;
            if (!isInteger){
                BigDecimal bdNum = new BigDecimal(new Double(num).toString());
                bdNum.setScale(2, BigDecimal.ROUND_HALF_EVEN);
                numStr=bdNum.toPlainString();
            }else{
                DecimalFormat decimalFormat = new DecimalFormat("###################.###########");
                numStr=decimalFormat.format(num);
            }
            return numStr;
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

    /**
     * 生成导入失败记录的错误文件
     *
     * @param errMap
     * @param errorFileName
     * @param rowHead
     * @param cellMap       单元格映射
     * @return
     *
     */
    public static String generateErrorFile(Map<Integer, ErrorDataExcel> errMap, String errorFileName
            , XSSFRow rowHead, Map<String, Integer> cellMap) throws Exception {
        int cellNum = cellMap.size();
        String errorFilePathOut = "";
        //错误信息收集处理
        if (null != errMap && !errMap.isEmpty()) {
            String dir1 = CommonUtil.formatDateToStr(new Date(), "yyyyMMdd");
            errorFilePathOut = dir1 + "/" + errorFileName;
            String errorFileDir = ConstUtil.FILE_PATH_ERROR + dir1 + "/";
            File errPath = new File(errorFileDir);
            if (!errPath.exists()) {
                errPath.mkdirs();
            }
            errorFileDir = errorFileDir + errorFileName;
            XSSFWorkbook wbError = new XSSFWorkbook();
            XSSFSheet sheetError = wbError.createSheet();
            int tmpC = 0;
            XSSFRow rowHeadError = sheetError.createRow(0);
            for (int i = 0; i < cellNum; i++) {
                Cell cell = rowHeadError.createCell(i);
                cell.setCellValue(util.ExcelUtilWithX.formatCell3(rowHead.getCell(i)));
            }
            for (Integer key : errMap.keySet()) {
                tmpC++;
                XSSFRow rowError = sheetError.createRow(tmpC);
                ErrorDataExcel errorDataExcel = errMap.get(key);
                for (int i = 0; i < cellNum + 1; i++) {
                    Cell cell = rowError.createCell(i);
                    if (i == cellNum) {
                        cell.setCellValue(errorDataExcel.msg);
                    } else {
                        if (null != errorDataExcel.row) {
                            String temp = formatCell3(errorDataExcel.row.getCell(i));
                            //判断是以.0结尾的数字并处理
                            if (CommonUtil.isNumeric(temp.replace(".", ""))
                                    && temp.lastIndexOf(".") > -1
                                    && temp.endsWith("0")
                                    && temp.lastIndexOf("0") == temp.length() - 1) {
                                temp = temp.substring(0, temp.lastIndexOf("."));
                            }
                            cell.setCellValue(temp);
                        } else {
                            cell.setCellValue("");
                        }
                    }
                }
            }
            FileOutputStream out = new FileOutputStream(errorFileDir);
            wbError.write(out);
            out.close();
        }
        return errorFilePathOut;
    }
}
