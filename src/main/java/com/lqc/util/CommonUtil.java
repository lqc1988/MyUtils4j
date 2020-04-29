package com.lqc.util;

import com.lqc.enums.ResultEnum;
import com.lqc.exception.MyException;
import com.lqc.vo.WeekInfo;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;

import javax.servlet.http.HttpServletRequest;
import java.beans.PropertyDescriptor;
import java.io.*;
import java.math.BigDecimal;
import java.net.*;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CommonUtil {
    private static Logger logger = LoggerFactory.getLogger(CommonUtil.class);
    private static Pattern numPattern = Pattern.compile("[0-9]*");

    /**
     * 当前格式化时间字符串
     *
     * @return
     */
    public static String nowStr() {
        return formatDateToStr(new Date(), "yyyy-MM-dd HH:mm:ss");
    }

    /**
     * 获取真实的请求IP地址
     *
     * @param request
     * @return
     */
    public static String getIP(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        if (StringUtils.isBlank(ip) || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (StringUtils.isBlank(ip) || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (StringUtils.isBlank(ip) || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_CLIENT_IP");
        }
        if (StringUtils.isBlank(ip) || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (StringUtils.isBlank(ip) || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }

    /**
     * 获取本机IP
     *
     * @return
     */
    public static String getLocalIP() {
        try {
            Enumeration<NetworkInterface> allNetInterfaces = NetworkInterface.getNetworkInterfaces();
            while (allNetInterfaces.hasMoreElements()) {
                NetworkInterface netInterface = allNetInterfaces.nextElement();
                Enumeration<InetAddress> addresses = netInterface.getInetAddresses();
                while (addresses.hasMoreElements()) {
                    InetAddress ip = addresses.nextElement();
                    if (ip != null
                            && ip instanceof Inet4Address
                            && !ip.isLoopbackAddress()
                            && ip.getHostAddress().indexOf(":") == -1) {
                        //loopback地址即本机地址，IPv4的loopback范围是127.0.0.0 ~ 127.255.255.255
                        System.out.println("本机的IP = " + ip.getHostAddress());
                        return ip.getHostAddress();
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * 根据指定的新字符替换指定的原字符
     *
     * @param orgStr  原字符串
     * @param orgSign 指定的原字符
     * @param rpSign  指定的新字符
     * @return 替换后的字符串
     */
    public static String replaceStr(String orgStr, String orgSign, String rpSign) {
        if (null != orgStr) {
            orgStr = replaceTineBrackets(orgStr);
            orgStr = orgStr.replaceAll(orgSign, rpSign);
        }
        return orgStr;
    }

    /**
     * 替换尖括号为html编码
     *
     * @param str
     * @return
     */
    public static String replaceTineBrackets(String str) {
        if (str == null) {
            return null;
        }

        String tmp = str.replace("<", "&lt;").replace(">", "&gt;");
        return tmp;
    }

    /**
     * 判断integer类型数据是否为空
     *
     * @param obj
     * @return
     */
    public static boolean isIntegerNull(Integer obj) {
        return (null == obj || StringUtils.isBlank(obj.toString()));
    }

    /**
     * 判断str类型由数字构成
     *
     * @param str
     * @return
     */
    public static boolean isNumeric(String str) {
        return numPattern.matcher(str).matches();
    }

    /**
     * 比较两个时间的分钟差
     *
     * @param bigTime   大时间
     * @param smallTime 小时间
     * @return
     */
    public static int compareMinute(Date bigTime, Date smallTime) throws Exception {
        if (null == bigTime || null == smallTime) {
            throw new MyException(ResultEnum.ERR_PARAM.getDisplay());
        }
        return (int) ((bigTime.getTime() - smallTime.getTime()) / (1000 * 60));
    }


    /**
     * 比较两个时间的天数差
     *
     * @param bigTime   大时间
     * @param smallTime 小时间
     * @return
     */
    public static long compareDay(Date bigTime, Date smallTime) {
        return sameDate(bigTime, smallTime) ? 0 : (bigTime.getTime() - smallTime.getTime()) / (1000 * 60 * 60 * 24) + 1;
    }

    /**
     * 比较两个java.util.Date 的日期（年月日）是否相同（忽略时、分、秒）
     *
     * @param d1
     * @param d2
     * @return
     */
    public static boolean sameDate(Date d1, Date d2) {
        LocalDate localDate1 = ZonedDateTime.ofInstant(d1.toInstant(), ZoneId.systemDefault()).toLocalDate();
        LocalDate localDate2 = ZonedDateTime.ofInstant(d2.toInstant(), ZoneId.systemDefault()).toLocalDate();
        return localDate1.isEqual(localDate2);
    }

    /**
     * 判断年月是否相同
     *
     * @param d1
     * @param d2
     * @return
     */
    public static boolean sameYearMonth(Date d1, Date d2) {
        Calendar c1 = Calendar.getInstance();
        c1.setTime(d1);
        Calendar c2 = Calendar.getInstance();
        c2.setTime(d2);
        return c1.get(Calendar.YEAR) == c2.get(Calendar.YEAR) && c1.get(Calendar.MONTH) == c2.get(Calendar.MONTH);
    }


    /**
     * 将时间字符串转换成指定格式
     *
     * @param dateTimeStr
     * @param pattern     格式，默认yyyyMMddHHmmss
     * @return
     */
    public static Date formatStrToDate(String dateTimeStr, String pattern) throws Exception {
        pattern = (StringUtils.isBlank(pattern)) ? "yyyyMMddHHmmss" : pattern;
        SimpleDateFormat df = new SimpleDateFormat(pattern);
        return df.parse(dateTimeStr);
    }

    /**
     * 将时间转换成指定格式
     *
     * @param pattern 格式，默认yyyyMMddHHmmss
     * @return
     */
    public static String formatDateToStr(Date dateTime, String pattern) {
        pattern = (StringUtils.isBlank(pattern)) ? "yyyyMMddHHmmss" : pattern;
        DateFormat df = new SimpleDateFormat(pattern);
        return df.format(dateTime);
    }

    /**
     * 将时间转换成指定格式
     *
     * @param pattern 格式，默认yyyyMMddHHmmss
     * @return
     */
    public static Date formatDate(Date dateTime, String pattern) throws Exception {
        pattern = (StringUtils.isBlank(pattern)) ? "yyyyMMddHHmmss" : pattern;
        DateFormat df = new SimpleDateFormat(pattern);
        return df.parse(df.format(dateTime));
    }

    /**
     * 日期增加天数
     *
     * @param oDate   原日期
     * @param addDays 增加天数
     * @return
     * @throws Exception
     */
    public static Date addDay(Date oDate, int addDays) throws Exception {
        Calendar cal = Calendar.getInstance();
        cal.setTime(oDate);
        cal.add(Calendar.DATE, addDays);
        return cal.getTime();
    }

    /**
     * 将时间转换成指定格式字符串
     *
     * @param orgDate
     * @param pattern 格式，默认yyyy-MM-dd
     * @return
     */
    public static String formatDateStr(String orgDate, String pattern) throws Exception {
        pattern = (StringUtils.isBlank(pattern)) ? "yyyy-MM-dd" : pattern;
        DateFormat df = new SimpleDateFormat(pattern);
        return df.format(df.parse(orgDate));
    }

    /**
     * 返回当天年月的字符串
     *
     * @return
     */
    public static String curMonth() {
        return formatDateToStr(new Date(), "yyyyMM");
    }

    /**
     * 返回上月 年月的字符串
     *
     * @return
     */
    public static String lastMonth() {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.MONTH, -1);
        return formatDateToStr(cal.getTime(), "yyyyMM");
    }

    /**
     * 返回当天年月日的字符串
     *
     * @return
     */
    public static String today() {
        return formatDateToStr(new Date(), "yyyyMMdd");
    }

    /**
     * 返回当前时间精确到秒的字符串
     *
     * @return
     */
    public static String now() {
        return formatDateToStr(new Date(), null);
    }


    /**
     * 获取指定日期的00:00:00时间串
     *
     * @param date
     * @return
     */
    public static String getStartTime(Date date) {
        return formatDateToStr(date, "yyyy-MM-dd") + " 00:00:00";
    }

    /**
     * 获取指定日期的23:59:59时间串
     *
     * @param date
     * @return
     */
    public static String getEndTime(Date date) {
        return formatDateToStr(date, "yyyy-MM-dd") + " 23:59:59";
    }

    /**
     * 获取指定日期的00:00:00时间串
     *
     * @param dateStr
     * @return
     */
    public static String getStartTimeFromStr(String dateStr) throws Exception {
        Date date = formatStrToDate(dateStr, "yyyy-MM-dd");
        return formatDateToStr(date, "yyyy-MM-dd") + " 00:00:00";
    }

    /**
     * 获取指定日期的23:59:59时间串
     *
     * @param dateStr
     * @return
     */
    public static String getEndTimeFromStr(String dateStr) throws Exception {
        Date date = formatStrToDate(dateStr, "yyyy-MM-dd");
        return formatDateToStr(date, "yyyy-MM-dd") + " 23:59:59";
    }

    /**
     * 获取指定月的第一天
     *
     * @param monthDate 指定月
     * @param pattern   返回日期的格式，eg:	yyyy-MM-dd HH:mm:ss
     * @return
     */
    public static String getFirstDayOfMonth(Date monthDate, String pattern) {
        pattern = (StringUtils.isBlank(pattern)) ? "yyyy-MM-dd HH:mm:ss" : pattern;
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(monthDate);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        //将小时至0
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        //将分钟至0
        calendar.set(Calendar.MINUTE, 0);
        //将秒至0
        calendar.set(Calendar.SECOND, 0);
        //将毫秒至0
        calendar.set(Calendar.MILLISECOND, 0);
        //获得当前月第一天
        Date sdate = calendar.getTime();
        return formatDateToStr(sdate, pattern);
    }

    /**
     * 获取指定月的最后一天
     *
     * @param monthDate 指定月
     * @param pattern   返回日期的格式，eg:	yyyy-MM-dd HH:mm:ss
     * @return
     */
    public static String getLastDayOfMonth(Date monthDate, String pattern) {
        pattern = (StringUtils.isBlank(pattern)) ? "yyyy-MM-dd HH:mm:ss" : pattern;
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(monthDate);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        //将小时至0
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        //将分钟至0
        calendar.set(Calendar.MINUTE, 0);
        //将秒至0
        calendar.set(Calendar.SECOND, 0);
        //将毫秒至0
        calendar.set(Calendar.MILLISECOND, 0);
        //将当前月加1；
        calendar.add(Calendar.MONTH, 1);
        //在当前月的下一月基础上减去1毫秒
        calendar.add(Calendar.MILLISECOND, -1);
        //获得当前月最后一天
        Date edate = calendar.getTime();

        return formatDateToStr(edate, pattern);
    }


    /**
     * double数字格式化为指定长度
     *
     * @param orgNum 原数字
     * @param len    长度，默认2
     * @return
     */
    public static double scaleDouble(double orgNum, int len) {
        BigDecimal bg = new BigDecimal(orgNum);
        if (len < 2) {
            return orgNum;
        }
        return bg.setScale(len, BigDecimal.ROUND_HALF_EVEN).doubleValue();
    }

    /**
     * 十六进制编码转字符串
     *
     * @param org
     * @return
     */
    public static String hex2Str(String org) {
        if (StringUtils.isBlank(org)) {
            return org;
        }
        if ("0x".equals(org.substring(0, 2))) {
            org = org.substring(2);
        }
        byte[] baKeyword = new byte[org.length() / 2];
        for (int i = 0; i < baKeyword.length; i++) {
            try {
                baKeyword[i] = (byte) (0xff & Integer.parseInt(org.substring(i * 2, i * 2 + 2), 16));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        try {
            org = new String(baKeyword, "utf-8");
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        return org;
    }

    /**
     * 字符串转16进制编码
     *
     * @param org
     * @return
     */
    public static String str2Hex(String org) {
        if (StringUtils.isBlank(org)) {
            return "0x0";
        }
        String str = "";
        for (int i = 0; i < org.length(); i++) {
            int ch = org.charAt(i);
            str = str + Integer.toHexString(ch);
        }
        return "0x" + str;// 0x表示十六进制
    }

    /**
     * 打印十六进制字符串
     *
     * @param b
     */
    public static void printHex(byte[] b) {
        for (int i = 0; i < b.length; i++) {
            String hex = Integer.toHexString(b[i] & 0xFF);
            if (hex.length() == 1) {
                hex = '0' + hex;
            }
            System.out.print(hex.toUpperCase());
        }
    }

    public static void main(String[] args) {
        try {
//			ArrayList<WeekInfo> weekList = getWeekInfoList(null, null,false);
//			for (WeekInfo weekInfo : weekList) {
//				// 打印周几
//				System.out.println(weekInfo.dayOfWeek + "==" + weekInfo.yearmonthday);
//			}
//			System.out.println(FileSize("../bigeyeUploadFile/1.png"));
//			String of="E://test/2.txt";
//			String nf="D://tt/cc.txt";
//			renameFile(of, nf, false);
//			System.out.println(formatDateToStr(new Date(), null)+"--start.");
//			for (int i = 0; i < 10; i++) {
//				if (i>0) {
//					Thread.sleep(i*1000);
//					System.out.println(formatDateToStr(new Date(), null)+"--i.");
//				}
//			}
//			System.out.println(formatDateToStr(new Date(), null)+"--end.");
//			Calendar calendar=Calendar.getInstance();
//			calendar.setTime(formatStringToDate("2018-01-11 00:00:00",null));
//			System.out.println(compareMinDate(new Date(),calendar.getTime()));
//			String aa="测试｛吖吖｝正则（ee).;-。、【cc】[xx]";
//			System.out.println("zz==="+stringFilter(aa));
//            System.out.println(dealFloatNum(1000f,2));
//            String aa = "体验馆 二十次卡会员";
//            System.out.println("aaa=" + findZhNumToInt(aa));
//            System.out.println("aaa=" + lastMonth());
            System.out.println("aaa=" + checkMobile("15910873370e"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取指定时间所属当年第几周
     *
     * @param date
     * @return
     */
    public static int getWeekOfYear(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setFirstDayOfWeek(Calendar.MONDAY);
        cal.setTime(date);
        return cal.get(Calendar.WEEK_OF_YEAR);
    }

    /**
     * 取当前为周几
     *
     * @param now
     * @return
     */
    public static int getDayOfWeek(Calendar now) {
        // 一周第一天是否为星期天
        boolean isFirstSunday = (now.getFirstDayOfWeek() == Calendar.SUNDAY);
        // 获取周几
        int weekDay = now.get(Calendar.DAY_OF_WEEK);
        // 若一周第一天为星期天，则-1
        if (isFirstSunday) {
            weekDay = weekDay - 1;
            if (weekDay == 0) {
                weekDay = 7;
            }
        }
        return weekDay;
    }

    /**
     * 取一周时间
     *
     * @param today
     * @param weekList
     * @param next     是否取下周时间，false则取本周，true取下周
     * @return
     */
    public static List<WeekInfo> getWeekList(Calendar today, List<WeekInfo> weekList, boolean next) {
        if (null != weekList && weekList.size() == 7) {
            // 取完一周时间，结束递归
            return weekList;
        }
        if (null == today) {
            // 取本周第一天
            weekList = new ArrayList<>();
            today = getCurrentMonday(); // 本周星期一的Calendar
            if (next) {
                // 加7天
                today.add(Calendar.DAY_OF_MONTH, 7);
            }
        }
        WeekInfo weekInfo = new WeekInfo();
        weekInfo.dayOfWeek = getDayOfWeek(today);
        try {
            weekInfo.yearMonthDay = formatDateToStr(today.getTime(), "yyyy-MM-dd");
        } catch (Exception e) {
            e.printStackTrace();
        }
        weekList.add(weekInfo);
        // 加1天
        today.add(Calendar.DAY_OF_MONTH, 1);
        // 递归
        return getWeekList(today, weekList, next);
    }

    /**
     * 获得本周星期一的日期
     *
     * @return
     */
    public static Calendar getCurrentMonday() {
        int mondayPlus = getMondayPlus();
        GregorianCalendar currentDate = new GregorianCalendar();
        currentDate.add(Calendar.DATE, mondayPlus);
        Date monday = currentDate.getTime();
        Calendar c = Calendar.getInstance();
        c.setTime(monday);
        return c;
    }

    /**
     * 获得当前日期与本周一相差的天数
     *
     * @return
     */
    private static int getMondayPlus() {
        Calendar cd = Calendar.getInstance();
        // 获得今天是一周的第几天，星期日是第一天，星期二是第二天......
        int dayOfWeek = cd.get(Calendar.DAY_OF_WEEK);
        if (dayOfWeek == 1) {
            return -6;
        } else {
            return 2 - dayOfWeek;
        }
    }


    /**
     * 判断操作系统是否为windows
     *
     * @return
     */
    public static boolean isWin() {
        return System.getProperty("os.name").indexOf("win") >= 0;
    }

    /**
     * 获取对象属性为null的属性名数组
     *
     * @param bean
     * @param ignoreList 忽略字段列表
     * @return
     */
    public static String[] getNullPropertyNames(Object bean, List<String> ignoreList) throws Exception {
        Set<String> emptyNames = getNullPropertySet(bean);
        if (null != ignoreList && !ignoreList.isEmpty()) {
            emptyNames.addAll(ignoreList);
        }
        String[] result = new String[emptyNames.size()];
        return emptyNames.toArray(result);
    }

    /**
     * 获取对象属性为null的属性名数组
     *
     * @param bean
     * @return
     */
    public static String[] getNullPropertyNames(Object bean) throws Exception {
        Set<String> emptyNames = getNullPropertySet(bean);
        String[] result = new String[emptyNames.size()];
        return emptyNames.toArray(result);
    }

    /**
     * 获取实体中值为null的字段名集合
     *
     * @param bean
     * @return
     */
    public static Set<String> getNullPropertySet(Object bean) {
        Set<String> emptyNames = new HashSet<>();
        final BeanWrapper src = new BeanWrapperImpl(bean);
        PropertyDescriptor[] pds = src.getPropertyDescriptors();
        for (PropertyDescriptor pd : pds) {
            String pdName = pd.getName();
            Object srcValue = src.getPropertyValue(pdName);
            if (srcValue == null || StringUtils.isBlank(String.valueOf(srcValue))) {
                emptyNames.add(pdName);
            }
        }
        return emptyNames;
    }

    /**
     * 过滤特殊字符
     *
     * @param str
     * @return
     */
    public static String stringFilter(String str) {
        String result = str;
        try {
            // 只允许字母和数字 // String regEx ="[^a-zA-Z0-9]";
            // 清除掉所有特殊字符
            String regEx = "[`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]";
            Pattern p = Pattern.compile(regEx);
            Matcher m = p.matcher(str);
            result = m.replaceAll("").trim();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 处理float数值，四舍五入
     *
     * @param num 原数值
     * @param len 保留位数
     * @return
     */
    public static float scaleFloat(Float num, int len) {
        float result = 0f;
        num = (null == num) ? 0f : num;
        len = (len < 0) ? 2 : len;
//		DecimalFormat df = new DecimalFormat("#.00");
//		result=Float.parseFloat(df.format(num));
        NumberFormat nf = NumberFormat.getNumberInstance();
        nf.setMaximumFractionDigits(len);
        result = Float.parseFloat(nf.format(num).replaceAll(",", ""));
        return result;
    }

    static String[] calcLast(String left, String s, String g) {
        if (left.indexOf("十") != -1) {
            String[] gArr = left.split("十");
            s = gArr[0];
            if (left.indexOf("百") != -1) {
                s = s.split("百")[1];
            }
            if (gArr.length > 1) {
                g = left.split("十")[1];
            }
        }
        return new String[]{s, g};
    }

    /**
     * 从中文字符串中找出数字并转换
     * 局限：仅转换第一个符合条件的数字,十万及以上不支持
     * 例："五个三万八千两百七十零八次卡" 只会找到五-->>5
     * "三万八千两百七十零八次卡" 则 八千两百七十零八次-->>38278
     *
     * @param zhStr
     * @return
     */
    public static int zhNum2Int(String zhStr) {
        int result = 0;
        String regEx1 = "([一二两三四五六七八九]{1}万)?零?([一二两三四五六七八九]{1}千)?零?([一二两三四五六七八九]{1}百)?零?([一二三四五六七八九]{1}十)?零?[一二三四五六七八九]?";
        Pattern p = Pattern.compile(regEx1);
        Matcher m = p.matcher(zhStr);
        int w_int, q_int, b_int, s_int, g_int;
        String mStr = "";
        while (m.find()) {
            mStr = m.group();
            if (StringUtils.isNotBlank(mStr)) {
                break;
            }
        }
        if (StringUtils.isBlank(mStr)) {
            return result;
        }
        System.out.println("mStr1==" + mStr);
        String w = "", q = "", b = "", s = "", g = "";
        if (mStr.indexOf("万") != -1) {
            String[] wArr = mStr.split("万");
            w = wArr[0];
            if (wArr.length > 1) {
                mStr = wArr[1];
                if (mStr.indexOf("零") != -1) {
                    String left = mStr.substring(0, mStr.indexOf("零"));
                    if (left.indexOf("千") != -1) {
                        q = left.split("千")[0];
                    }
                    if (left.indexOf("百") != -1) {
                        b = left.split("百")[0];
                        if (left.indexOf("千") != -1) {
                            b = b.split("千")[1];
                        }
                    }
                    String[] lastArr = calcLast(left, s, g);
                    s = lastArr[0];
                    g = lastArr[1];
                    mStr = mStr.substring(mStr.indexOf("零") + 1, mStr.length());
                }
            }
        }
        if (StringUtils.isNotBlank(mStr) && mStr.indexOf("千") != -1) {
            String[] qArr = mStr.split("千");
            q = qArr[0];
            if (qArr.length > 1) {
                mStr = qArr[1];
                if (mStr.indexOf("零") != -1) {
                    String left = mStr.substring(0, mStr.indexOf("零"));
                    if (left.indexOf("百") != -1) {
                        b = left.split("百")[0];
                        if (left.indexOf("千") != -1) {
                            b = b.split("千")[1];
                        }
                    }
                    String[] lastArr = calcLast(left, s, g);
                    s = lastArr[0];
                    g = lastArr[1];
                    mStr = mStr.substring(mStr.indexOf("零") + 1, mStr.length());
                }
            }
        }
        if (StringUtils.isNotBlank(mStr) && mStr.indexOf("百") != -1) {
            String[] bArr = mStr.split("百");
            b = bArr[0];
            if (bArr.length > 1) {
                mStr = bArr[1];
                if (mStr.indexOf("零") != -1) {
                    String left = mStr.substring(0, mStr.indexOf("零"));
                    String[] lastArr = calcLast(left, s, g);
                    s = lastArr[0];
                    g = lastArr[1];
                    mStr = mStr.substring(mStr.indexOf("零") + 1, mStr.length());
                }
            }
        }
        String[] lastArr = calcLast(mStr, s, g);
        s = lastArr[0];
        g = lastArr[1];
        if (StringUtils.isNotBlank(mStr) && StringUtils.isBlank(w)
                && StringUtils.isBlank(q) && StringUtils.isBlank(b)
                && StringUtils.isBlank(s)) {
            g = mStr;
        }
        System.out.println("w=" + w + ",q=" + q + ",b=" + b + ",s=" + s + ",g=" + g);
        w_int = parseZHtoInteger(w);
        q_int = parseZHtoInteger(q);
        b_int = parseZHtoInteger(b);
        s_int = parseZHtoInteger(s);
        g_int = parseZHtoInteger(g);
        System.out.println("w_int=" + w_int + ",q_int=" + q_int + ",b_int=" + b_int + ",s_int=" + s_int + ",g_int=" + g_int);
        result = w_int * 10000 + q_int * 1000 + b_int * 100 + s_int * 10 + g_int;
        System.out.println("result1=" + result);
        return result;
    }

    /**
     * 中文数字转换为阿拉伯数字
     *
     * @param zh
     * @return
     */
    public static int parseZHtoInteger(String zh) {
        int r = 0;
        if (StringUtils.isBlank(zh)) {
            return r;
        }
        zh = zh.replaceAll("零", "0")
                .replaceAll("一", "1")
                .replaceAll("二", "2")
                .replaceAll("两", "2")
                .replaceAll("三", "3")
                .replaceAll("四", "4")
                .replaceAll("五", "5")
                .replaceAll("六", "6")
                .replaceAll("七", "7")
                .replaceAll("八", "8")
                .replaceAll("九", "9");
        r = Integer.parseInt(zh);
        return r;
    }

    /**
     * 生成不带 - 符号的UUID字符串
     *
     * @return
     */
    public static String getUUID() {
        return getUUID(false);
    }

    /**
     * 生成UUID字符串
     *
     * @param withLine 是否包含分割线
     * @return
     */
    public static String getUUID(boolean withLine) {
        String uuid = UUID.randomUUID().toString();
        if (!withLine) {
            uuid = uuid.replaceAll("-", "");
        }
        return uuid;
    }


    /**
     * 获取请求的完整URL
     *
     * @param request
     * @return
     * @throws Exception
     */
    public static String getURL(HttpServletRequest request) {
        String host = request.getRemoteHost();
        String uri = request.getRequestURI();
        String protocol = "http://";
        if (request.isSecure()) {
            protocol = "https://";
        }
        return protocol + host + uri;
    }


    /**
     * 获取请求的协议+域名
     *
     * @param request
     * @return
     * @throws Exception
     */
    public static String getDomain(HttpServletRequest request) {
        String host = request.getRemoteHost();
        String protocol = "http://";
        if (request.isSecure()) {
            protocol = "https://";
        }
        return protocol + host;
    }

    /**
     * 获取url中的host
     *
     * @param
     * @return
     */
    public static String getUrlHost(String url) {
        String host = null;
        try {
            host = new URL(url).getHost().toLowerCase();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return host;
    }

    /**
     * 获取url中的域名
     *
     * @param
     * @return
     */
    public static String getUrlDomain(String url) {
        try {
            //获取值转换为小写
            String host = new URL(url).getHost().toLowerCase();//news.hexun.com
            Pattern pattern = Pattern.compile("[^\\.]+(\\.com\\.cn|\\.net\\.cn|\\.org\\.cn|\\.gov\\.cn|\\.com" +
                    "|\\.net|\\.cn|\\.org|\\.cc|\\.me|\\.tel|\\.mobi|\\.asia|\\.biz|\\.info|\\.name|\\.tv" +
                    "|\\.hk|\\.公司|\\.中国|\\.网络)");
            Matcher matcher = pattern.matcher(host);
            while (matcher.find()) {
                return matcher.group();
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 中国电信号段 133、149、153、173、177、180、181、189、199
     * 中国联通号段 130、131、132、145、155、156、166、175、176、185、186
     * 中国移动号段 134(0-8)、135、136、137、138、139、147、150、151、152、157、158、159、178、182、183、184、187、188、198
     * 其他号段
     * 14号段以前为上网卡专属号段，如中国联通的是145，中国移动的是147等等。
     * 虚拟运营商
     * 电信：1700、1701、1702
     * 移动：1703、1705、1706
     * 联通：1704、1707、1708、1709、171
     * 卫星通信：1349
     *
     * @param phone
     * @return
     */
    public static boolean isPhone(String phone) throws Exception {
        boolean flag = false;
        final int length = 11;
        String regex = "^((13[0-9])|(14[5,7,9])|(15([0-3]|[5-9]))|(166)|(17[0,1,3,5,6,7,8])|(18[0-9])|(19[8|9]))\\d{8}$";
        if (phone.length() != length) {
            throw new MyException("手机号应为11位数字");
        } else {
            Pattern p = Pattern.compile(regex);
            Matcher m = p.matcher(phone);
            flag = m.matches();
        }
        return flag;
    }

    /**
     * 格式化BigDecimal保留位数
     *
     * @param num 原数字
     * @param len 保留位数
     * @return
     */
    public static BigDecimal scaleBigDecimal(BigDecimal num, int len) throws Exception {
        if (null == num) {
            throw new MyException(ResultEnum.ERR_PARAM.getDisplay());
        }
        String scaleStr = "#.00";//默认保留两位
        if (len > 2) {
            for (int i = 0; i < len - 2; i++) {
                scaleStr += "0";
            }
        }
        DecimalFormat df = new DecimalFormat(scaleStr);
        return new BigDecimal(df.format(num));
    }

    /**
     * BigDecimal转String
     *
     * @param num 原数字
     * @param len 保留位数
     * @return
     */
    public static String bigDecimal2Str(BigDecimal num, int len) throws Exception {
        if (null == num) {
            throw new MyException(ResultEnum.ERR_PARAM.getDisplay());
        }
        String scaleStr = "#.00";//默认保留两位
        if (len > 2) {
            for (int i = 0; i < len - 2; i++) {
                scaleStr += "0";
            }
        }
        DecimalFormat df = new DecimalFormat(scaleStr);
        return df.format(num);
    }

    /**
     * 字符串转为UTF-8格式
     *
     * @param str
     * @return
     */
    public static String str2UTF8(String str) throws Exception {
        if (str.equals(new String(str.getBytes("iso8859-1"), "iso8859-1"))) {
            str = new String(str.getBytes("iso8859-1"), "utf-8");
            logger.debug("处理为UTF-8后：" + str);
        }
        return str;
    }


    /**
     * 处理中文文件名
     *
     * @param request
     * @param fileNameCN
     * @return
     * @throws Exception
     */
    public static String dealZhFileName(HttpServletRequest request, String fileNameCN) throws Exception {
        if ((request.getHeader("User-Agent").toLowerCase().indexOf("firefox") > 0)) {
            fileNameCN = new String(fileNameCN.getBytes("UTF-8"), "iso-8859-1");
        } else {
            fileNameCN = URLEncoder.encode(fileNameCN, "UTF-8");
        }
        return fileNameCN;
    }
    /**
     * 生成文件
     *
     * @return
     */
    public static String mkFileDir(String path, String fileName) {
        SimpleDateFormat df1 = new SimpleDateFormat("yyyy");
        SimpleDateFormat df2 = new SimpleDateFormat("MMdd");
        String dir1 = df1.format(new Date());
        String dir2 = df2.format(new Date());
        String cPath = dir1 + "/" + dir2 + "/";
        String localFilePath = path + cPath;
        String uuid = UUID.randomUUID().toString();
        fileName = uuid + "." + getExtensionName(fileName);
        return localFilePath + fileName;
    }

    /**
     * 生成文件(保留原名)
     *
     * @return
     */
    public static String mkFileDir(String path) {
        SimpleDateFormat df1 = new SimpleDateFormat("yyyyMM");
        SimpleDateFormat df2 = new SimpleDateFormat("dd");
        String dir1 = df1.format(new Date());
        String dir2 = df2.format(new Date());
        String cPath = dir1 + "/" + dir2 + "/";
        String localFilePath = path + cPath;
        return localFilePath;
    }

    /**
     * 生成文件(前缀时间)
     *
     * @return
     */
    public static String mkFileDirByTime(String path) {
        SimpleDateFormat df1 = new SimpleDateFormat("yyyyMM");
        SimpleDateFormat df2 = new SimpleDateFormat("dd");
        String dir1 = df1.format(new Date());
        String dir2 = df2.format(new Date());
        String cPath = dir1 + "/" + dir2 + "/" + System.currentTimeMillis();
        String localFilePath = path + cPath;
        return localFilePath;
    }

    /**
     * 获取文件的扩展名
     *
     * @param filename
     * @return
     */
    public static String getExtensionName(String filename) {
        if ((filename != null) && (filename.length() > 0)) {
            int dot = filename.lastIndexOf('.');
            if ((dot > -1) && (dot < (filename.length() - 1))) {
                return filename.substring(dot + 1);
            }
        }
        return filename;
    }

    /**
     * 字符串首字母如果转换为小写
     *
     * @return
     */
    public static String firstTolowCae(String str) throws Exception {
        StringBuilder stringBuilder = new StringBuilder();
        if (StringUtils.isNotBlank(str) && str.length() > 0) {
            String s = str.substring(0, 1).toLowerCase();
            stringBuilder.append(s);
        }
        if (StringUtils.isNotBlank(str) && str.length() > 1) {
            String substring = str.substring(1);
            stringBuilder.append(substring);
        }
        return stringBuilder.toString();
    }

    /**
     * List深度copy
     *
     * @param src
     * @param <T>
     * @return
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public static <T> List<T> deepCopy(List<T> src) throws IOException, ClassNotFoundException {
        ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
        ObjectOutputStream out = new ObjectOutputStream(byteOut);
        out.writeObject(src);

        ByteArrayInputStream byteIn = new ByteArrayInputStream(byteOut.toByteArray());
        ObjectInputStream in = new ObjectInputStream(byteIn);
        @SuppressWarnings("unchecked")
        List<T> dest = (List<T>) in.readObject();
        return dest;
    }

    /**
     * Date translate to LocalDate
     *
     * @Param [import_day]
     * @Return java.time.LocalDate
     * @Author wanggaojian
     * @Date 2019/10/21
     * @Time 14:24
     */
    public static LocalDate DateToLocalDate(Date import_day) {
        if (import_day == null) {
            return null;
        }
        Instant instant = import_day.toInstant();
        ZoneId zoneId = ZoneId.systemDefault();
        return instant.atZone(zoneId).toLocalDate();
    }

    /**
     * 组装账单起止时间
     *
     * @param billMonth
     * @return
     * @throws Exception
     */
    public static Map<String, String> assembleBillMonthMap(Integer billMonth) throws Exception {
        Map<String, String> billMonthMap = new HashMap<>();
        String month_start = billMonth.toString();
        if (month_start.length() != 6) {
            throw new MyException(ResultEnum.ERR_PARAM.getDisplay());
        }
        String start = month_start.substring(0, 4) + "-" + month_start.substring(4, 6) + "-01 00:00:00";
        String month_end = (++billMonth).toString();
        String end = month_end.substring(0, 4) + "-" + month_end.substring(4, 6) + "-01 00:00:00";
        billMonthMap.put("start", start);
        billMonthMap.put("end", end);
        return billMonthMap;
    }

    /**
     * 处理账期，默认上月
     *
     * @return
     */
    public static Integer assembleBillMonth(Integer billMonth) {
        if (null == billMonth) {
            billMonth = Integer.parseInt(lastMonth());
        }
        return billMonth;
    }

    public static boolean checkMobile(String tel) {
        if (StringUtils.isBlank(tel)) {
            return false;
        }
        String regExp = "^1\\d{10}$";
        Pattern p = Pattern.compile(regExp);
        Matcher m = p.matcher(tel);
        return m.matches();
    }

    /**
     * 手机号中间4位替换为****
     *
     * @param phone
     * @return
     * @throws Exception
     */
    public static String encryptPhone(String phone) throws Exception {
        if (StringUtils.isBlank(phone)) {
            throw new MyException("参数不能为空");
        }
        return phone.replaceAll("(\\d{3})\\d{4}(\\d{4})", "$1****$2");
    }
}
