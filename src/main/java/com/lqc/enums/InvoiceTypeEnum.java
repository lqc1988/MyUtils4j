package com.lqc.enums;

/**
 * @Author: lqc
 * @Date: 2020/8/5 16:36
 * @Description: 发票类型：0-专票，1-普票
 */
public enum InvoiceTypeEnum {
    /**
     * 0-专票
     */
    SPECIAL(0, "专票"),
    /**
     * 1-普票
     */
    GENERAL(1, "普票");

    int value;
    String display;

    InvoiceTypeEnum(int value, String display) {
        this.value = value;
        this.display = display;
    }

    public static InvoiceTypeEnum valueOf(int value) {
        for (InvoiceTypeEnum c : InvoiceTypeEnum.values()) {
            if (c.getValue() == value) {
                return c;
            }
        }
        return null;
    }
    public static InvoiceTypeEnum displayOf(String value) {
        for (InvoiceTypeEnum c : InvoiceTypeEnum.values()) {
            if (c.getDisplay() == value) {
                return c;
            }
        }
        return null;
    }

    public int getValue() {
        return value;
    }

    public String getDisplay() {
        return display;
    }
}
