package com.lqc.util;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.streaming.SXSSFSheet;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by wgj on 2018/5/31.
 * 数据导出到excel工具类
 */
public class ExcelGenerateHelper {
    /**
     * 标题单元格格式
     */
    public final static String CELL_STYLE_HEADER = "headerCell";
    /**
     * 字符单元格格式
     */
    public final static String CELL_STYLE_STRING = "stringCell";
    /**
     * 数字单元格格式
     */
    public final static String CELL_STYLE_NUMBER = "numberCell";
    /**
     * 日期单元格格式
     */
    public final static String CELL_STYLE_DATE = "dateCell";

    public Map<String, CellStyle> styles;
    private int rowIndex = 0;

    public static ExcelGenerateHelper getInstance(Workbook wb) {
        return new ExcelGenerateHelper(wb);
    }

    private ExcelGenerateHelper(Workbook wb) {
        createCellStyles(wb);
    }

    /**
     * 生成表头
     *
     * @param sheet
     * @param titleArray
     */
    public void generateHeader(SXSSFSheet sheet, String[] titleArray) {
        generateHeader(sheet, titleArray, 0, StringUtils.EMPTY, StringUtils.EMPTY);
    }

    /**
     * 生成表头
     *
     * @param sheet
     * @param titleArray
     * @param colStartIndex 指定列的开始索引
     * @param valuePrefix   单元格值的前缀
     * @param valueSuffix   单元格值的后缀
     */
    public void generateHeader(SXSSFSheet sheet, String[] titleArray, int colStartIndex, String valuePrefix, String valueSuffix) {
        Row row = sheet.createRow(rowIndex);
        row.setHeightInPoints(30);
        CellStyle headerStyle = styles.get(CELL_STYLE_HEADER);
        for (int i = 0; i < titleArray.length; i++) {
            Cell cell = row.createCell(i + colStartIndex);
            cell.setCellStyle(headerStyle);
            cell.setCellValue(valuePrefix + titleArray[i] + valueSuffix);
        }
    }

    /**
     * 动态合并单元格
     *
     * @param sheet
     * @param headerNum
     */
    public void mergeCell(SXSSFSheet sheet, String[] headerNum) {
        for (int i = 0; i < headerNum.length; i++) {
            String[] temp = headerNum[i].split(",");
            Integer firstRow = Integer.parseInt(temp[0]);
            Integer lastRow = Integer.parseInt(temp[1]);
            Integer firstCol = Integer.parseInt(temp[2]);
            Integer lastCol = Integer.parseInt(temp[3]);
            sheet.addMergedRegion(new CellRangeAddress(firstRow, lastRow, firstCol, lastCol));
        }
    }

    /**
     * 创建所有的单元格格式
     *
     * @param wb
     * @return
     */
    private Map<String, CellStyle> createCellStyles(Workbook wb) {
        styles = new HashMap<>();
        DataFormat df = wb.createDataFormat();

        // --字体设定 --//

        // 普通字体
        Font normalFont = wb.createFont();
        normalFont.setFontHeightInPoints((short) 10);

        // 加粗字体
        Font boldFont = wb.createFont();
        boldFont.setFontHeightInPoints((short) 10);
        boldFont.setBold(true);

        // 蓝色加粗字体
        Font blueBoldFont = wb.createFont();
        blueBoldFont.setFontHeightInPoints((short) 10);
        blueBoldFont.setBold(true);
        blueBoldFont.setColor(IndexedColors.BLUE.getIndex());

        // 黑色加粗字体
        Font blackBoldFont = wb.createFont();
        blackBoldFont.setFontHeightInPoints((short) 10);
        blackBoldFont.setBold(true);
        blackBoldFont.setColor(IndexedColors.BLACK.getIndex());

        // --Cell Style设定-- //

        // 标题格式
        CellStyle headerStyle = wb.createCellStyle();
        headerStyle.setFont(blackBoldFont);
        headerStyle.setAlignment(HorizontalAlignment.CENTER);
        headerStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        setBorder(headerStyle);
        styles.put(CELL_STYLE_HEADER, headerStyle);

        // 字符串格式
        CellStyle stringCellStyle = wb.createCellStyle();
        stringCellStyle.setFont(normalFont);
        stringCellStyle.setAlignment(HorizontalAlignment.CENTER);
        stringCellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        stringCellStyle.setWrapText(true);//设置为自动换行
        setBorder(stringCellStyle);
        styles.put(CELL_STYLE_STRING, stringCellStyle);

        // 数字格式
        CellStyle numberCellStyle = wb.createCellStyle();
        numberCellStyle.setFont(normalFont);
        setBorder(numberCellStyle);
        styles.put(CELL_STYLE_NUMBER, numberCellStyle);

        // 日期格式
        CellStyle dateCellStyle = wb.createCellStyle();
        dateCellStyle.setFont(normalFont);
        dateCellStyle.setAlignment(HorizontalAlignment.CENTER);
        dateCellStyle.setVerticalAlignment(VerticalAlignment.BOTTOM);
        dateCellStyle.setDataFormat(df.getFormat("yyyy-MM-dd HH:mm:ss"));
        setBorder(dateCellStyle);
        styles.put(CELL_STYLE_DATE, dateCellStyle);

        return styles;
    }

    /**
     * 为单元格样式设置边框
     *
     * @param cellStyle
     * @return
     */
    public CellStyle setBorder(CellStyle cellStyle) {
        // 上边框
        cellStyle.setBorderTop(BorderStyle.THIN);
        cellStyle.setTopBorderColor(IndexedColors.BLACK.getIndex());

        // 下边框
        cellStyle.setBorderBottom(BorderStyle.THIN);
        cellStyle.setBottomBorderColor(IndexedColors.BLACK.getIndex());

        // 左边框
        cellStyle.setBorderLeft(BorderStyle.THIN);
        cellStyle.setLeftBorderColor(IndexedColors.BLACK.getIndex());

        // 右边框
        cellStyle.setBorderRight(BorderStyle.THIN);
        cellStyle.setRightBorderColor(IndexedColors.BLACK.getIndex());

        return cellStyle;
    }

    /**
     * 创建字符串单元格
     *
     * @param row
     * @param colIndex
     * @param stringValue
     * @return
     */
    public Cell createStringCell(Row row, int colIndex, String stringValue) {
        CellStyle stringCellStyle = styles.get(CELL_STYLE_STRING);
        return createCell(row, colIndex, stringCellStyle, stringValue, null, null);

    }

    /**
     * 去除边框
     *
     * @param cell
     */
    public void removeBorder(Cell cell) {
        cell.getCellStyle().setBorderTop(BorderStyle.NONE);
        cell.getCellStyle().setBorderBottom(BorderStyle.NONE);
        cell.getCellStyle().setBorderLeft(BorderStyle.NONE);
        cell.getCellStyle().setBorderRight(BorderStyle.NONE);
    }

    /**
     * 创建数字单元格
     *
     * @param row
     * @param colIndex
     * @param doubleValue
     * @return
     */
    public Cell createDoubleCell(Row row, int colIndex, Double doubleValue) {
        CellStyle numberCellStyle = styles.get(CELL_STYLE_NUMBER);
        return createCell(row, colIndex, numberCellStyle, StringUtils.EMPTY, doubleValue, null);

    }

    /**
     * 创建日期单元格
     *
     * @param row
     * @param colIndex
     * @param dateValue
     * @return
     */
    public Cell createDateCell(Row row, int colIndex, Date dateValue) {
        CellStyle dateCellStyle = styles.get(CELL_STYLE_DATE);
        return createCell(row, colIndex, dateCellStyle, StringUtils.EMPTY, null, dateValue);

    }

    /**
     * 创建单元格
     *
     * @param row
     * @param colIndex
     * @param cellStyle
     * @param stringValue
     * @param doubleValue
     * @param dateValue
     * @return
     */
    private Cell createCell(Row row, int colIndex, CellStyle cellStyle, String stringValue, Double doubleValue,
                            Date dateValue) {
        Cell cell = row.createCell(colIndex);
        cell.setCellStyle(cellStyle);
        if (StringUtils.isNotBlank(stringValue)) {
            cell.setCellValue(stringValue);
        } else if (doubleValue != null) {
            cell.setCellValue(doubleValue);
        } else if (dateValue != null) {
            cell.setCellValue(dateValue);
        }

        return cell;
    }

    public int getRowIndex() {
        return rowIndex;
    }

    public void setRowIndex(int rowIndex) {
        this.rowIndex = rowIndex;
    }
}
