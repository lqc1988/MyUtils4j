package com.lqc.vo;

import org.apache.poi.xssf.usermodel.XSSFRow;

/**
 * @author : liqinchao
 * @CreateTime : 2018/8/1 9:32
 * @Description :excel导入错误信息对象
 */
public class ErrorDataExcel {
    public XSSFRow row;
    public String msg;

    @Override
    public String toString() {
        return "ErrorDataExcel{" +
                "row=" + row +
                ", msg='" + msg + '\'' +
                '}';
    }
}
