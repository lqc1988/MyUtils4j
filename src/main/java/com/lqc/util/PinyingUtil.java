package com.lqc.util;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import org.apache.commons.lang3.StringUtils;

/**
 * @className:PinyingUtil.java
 * @classDescription:拼音操作工具类
 */

public class PinyingUtil {

    /**
     * 将字符串转换成拼音数组
     *
     * @param src
     * @return
     */
    public static String[] stringToPinyin(String src) {
        return stringToPinyin(src, null);
    }

    /**
     * 将字符串转换成拼音数组
     *
     * @param src
     * @return
     */
    public static String[] stringToPinyin(String src, String separator) {
        boolean isPolyphone = StringUtils.isNotBlank(separator);
        return stringToPinyin(src, isPolyphone, separator);
    }

    /**
     * 将字符串转换成拼音数组
     *
     * @param src
     * @param isPolyphone 是否查出多音字的所有拼音
     * @param separator   多音字拼音之间的分隔符
     * @return
     */
    public static String[] stringToPinyin(String src, boolean isPolyphone, String separator) {
        // 判断字符串是否为空
        if (StringUtils.isBlank(src)) {
            return null;
        }
        char[] srcChar = src.toCharArray();
        int srcCount = srcChar.length;
        String[] srcStr = new String[srcCount];
        for (int i = 0; i < srcCount; i++) {
            srcStr[i] = charToPinyin(srcChar[i], isPolyphone, separator);
        }
        return srcStr;
    }


    /**
     * 将单个字符转换成拼音
     *
     * @param src
     * @param isPolyphone 是否查出多音字，默认是查出多音字的第一个字符
     * @param separator   多音字之间用特殊符号间隔起来
     * @return
     */
    public static String charToPinyin(char src, boolean isPolyphone, String separator) {
        // 创建汉语拼音处理类
        HanyuPinyinOutputFormat defaultFormat = new HanyuPinyinOutputFormat();
        // 输出设置，大小写，音标方式
        defaultFormat.setCaseType(HanyuPinyinCaseType.LOWERCASE);
        defaultFormat.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
        StringBuffer tmpPinyin = new StringBuffer();
        // 如果是中文
        if (src > 128) {
            try {
                // 转换得出结果
                String[] wordArr = PinyinHelper.toHanyuPinyinStringArray(src, defaultFormat);
                if (isPolyphone && null != separator) {
                    for (int i = 0; i < wordArr.length; i++) {
                        tmpPinyin.append(wordArr[i]);
                        if (wordArr.length != (i + 1)) {
                            tmpPinyin.append(separator);
                        }
                    }
                } else {
                    tmpPinyin.append(wordArr[0]);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            tmpPinyin.append(src);
        }

        return tmpPinyin.toString();

    }

    /**
     * 汉字转拼音，提取首字母缩写
     *
     * @param hanzi
     * @return
     */
    public static String hanziToEN(String hanzi) {
        StringBuilder en = new StringBuilder();
        if (StringUtils.isNotBlank(hanzi)) {
            String pinyin = hanziToPinyin(hanzi, false, ",");
            if (StringUtils.isNotBlank(pinyin)) {
                String[] pyArr = pinyin.split(",");
                if (null != pyArr) {
                    if (pyArr.length > 1) {
                        for (String py : pyArr) {
                            en.append(py.substring(0, 1));
                        }
                    } else {
                        en.append(pyArr[0]);
                    }
                } else {
                    en.append(pinyin);
                }
            }
        }
        return en.toString();
    }

    public static void main(String[] args) {
//		System.out.println(CommonUtil.stringFilter("1234"));
        String src = "测试数据";
        System.out.println("原文：" + src);
        System.out.println("拼音：" + hanziToPinyin(src));
        System.out.println("缩写：" + hanziToEN(src));
    }

    public static String hanziToPinyin(String src) {
        return hanziToPinyin(src, " ");
    }

    /**
     * 将汉字转换成拼音
     *
     * @param src
     * @param separator
     * @return
     */
    public static String hanziToPinyin(String src, String separator) {
        boolean isPolyphone = StringUtils.isNotBlank(separator);
        return hanziToPinyin(src, isPolyphone, separator);
    }

    /**
     * 将汉字转换成拼音
     *
     * @param src
     * @param separator
     * @return
     */
    public static String hanziToPinyin(String src, boolean isPolyphone, String separator) {
        StringBuilder sb = new StringBuilder();
        //过滤特殊字符
        String dealStr = CommonUtil.stringFilter(src);
        if (StringUtils.isNotBlank(dealStr)) {
            // 创建汉语拼音处理类
            HanyuPinyinOutputFormat format = new HanyuPinyinOutputFormat();
            // 输出设置，大小写，音标方式
            format.setCaseType(HanyuPinyinCaseType.LOWERCASE);
            format.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
            String[] pyArr = stringToPinyin(dealStr, isPolyphone, separator);
            if (null != pyArr && pyArr.length > 0) {
                int tmpC = 0;
                for (String py : pyArr) {
                    if (tmpC > 0 && null != separator) {
                        sb.append(separator);
                    }
                    sb.append(py);
                    tmpC++;
                }
            } else {
                sb.append(src);
            }
        } else {
            sb.append(src);
        }
        return sb.toString();
    }

    /**
     * 将字符串数组转换成字符串
     *
     * @param str
     * @param separator 各个字符串之间的分隔符
     * @return
     */
    public static String stringArrayToString(String[] str, String separator) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < str.length; i++) {
            sb.append(str[i]);
            if (str.length != (i + 1)) {
                sb.append(separator);
            }
        }
        return sb.toString();
    }

    /**
     * 简单的将各个字符数组之间连接起来
     *
     * @param str
     * @return
     */
    public static String stringArrayToString(String[] str) {
        return stringArrayToString(str, "");
    }

    /**
     * 将字符数组转换成字符串
     *
     * @param ch
     * @param separator 各个字符串之间的分隔符
     * @return
     */
    public static String charArrayToString(char[] ch, String separator) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < ch.length; i++) {
            sb.append(ch[i]);
            if (ch.length != (i + 1)) {
                sb.append(separator);
            }
        }
        return sb.toString();
    }

    /**
     * 将字符数组转换成字符串
     *
     * @param ch
     * @return
     */
    public static String charArrayToString(char[] ch) {
        return charArrayToString(ch, " ");
    }

    /**
     * 取汉字的首字母
     *
     * @param src
     * @param isCapital 是否是大写
     * @return
     */
    public static char[] getHeadByChar(char src, boolean isCapital) {
        // 如果不是汉字直接返回
        if (src <= 128) {
            return new char[]{src};
        }
        // 获取所有的拼音
        String[] pinyingStr = PinyinHelper.toHanyuPinyinStringArray(src);
        // 创建返回对象
        int polyphoneSize = pinyingStr.length;
        char[] headChars = new char[polyphoneSize];
        int i = 0;
        // 截取首字符
        for (String s : pinyingStr) {
            char headChar = s.charAt(0);
            // 首字母是否大写，默认是小写
            if (isCapital) {
                headChars[i] = Character.toUpperCase(headChar);
            } else {
                headChars[i] = headChar;
            }
            i++;
        }

        return headChars;
    }

    /**
     * 取汉字的首字母(默认是大写)
     *
     * @param src
     * @return
     */
    public static char[] getHeadByChar(char src) {
        return getHeadByChar(src, true);
    }

    /**
     * 查找字符串首字母
     *
     * @param src
     * @return
     */
    public static String[] getHeadByString(String src) {
        return getHeadByString(src, true);
    }

    /**
     * 查找字符串首字母
     *
     * @param src
     * @param isCapital 是否大写
     * @return
     */
    public static String[] getHeadByString(String src, boolean isCapital) {
        return getHeadByString(src, isCapital, null);
    }

    /**
     * 查找字符串首字母
     *
     * @param src
     * @param isCapital 是否大写
     * @param separator 分隔符
     * @return
     */
    public static String[] getHeadByString(String src, boolean isCapital, String separator) {
        //过滤特殊字符
        src = CommonUtil.stringFilter(src);
        char[] chars = src.toCharArray();
        String[] headString = new String[chars.length];
        int i = 0;
        for (char ch : chars) {

            char[] chs = getHeadByChar(ch, isCapital);
            StringBuffer sb = new StringBuffer();
            if (null != separator) {
                int j = 1;

                for (char ch1 : chs) {
                    sb.append(ch1);
                    if (j != chs.length) {
                        sb.append(separator);
                    }
                    j++;
                }
            } else {
                sb.append(chs[0]);
            }
            headString[i] = sb.toString();
            i++;
        }
        return headString;
    }

    public static void getGBKpy(String hz) {
        System.out.println("---------------------------" + stringArrayToString(getHeadByString(hz)));
        System.out.println("++++++++++++++++++++++++++++" + hanziToPinyin(hz));
    }
}
