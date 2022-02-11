package com.lqc.util;

import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.sql.Timestamp;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * List按照指定字段排序工具类
 */

public class ListSortUtil {
	/**
	 * @param <T>
	 * @param targetList 目标排序List
	 * @param sortField  排序字段(实体类属性名)
	 * @param sortMode   排序方式（asc or  desc）
	 */
	public static <T> void sort(final List<T> targetList, final String sortField, final String sortMode) {
		Collections.sort(targetList, (obj1, obj2) -> {
			int retVal;
			try {
				//首字母转大写
				Class clazz = obj1.getClass();
				Field field = ReflectionUtils.findField(clazz, sortField);
				field.setAccessible(true); //设置成可访问状态
				String typeName = field.getType().getName();
				Object v1 = field.get(obj1); //获取field的值
				Object v2 = field.get(obj2); //获取field的值
				retVal = compareEXE(v1, v2, sortMode, typeName);
			} catch (Exception e) {
				e.printStackTrace();
				throw new RuntimeException();
			}
			return retVal;
		});
	}

	/**
	 * 执行比较
	 *
	 * @param v1
	 * @param v2
	 * @param sortMode
	 * @return
	 */
	static int compareEXE(Object v1, Object v2, String sortMode, String typeName) {
		boolean ASC_order = (sortMode == null || "ASC".equalsIgnoreCase(sortMode));
		int retVal;
		if (null == v1 || null == v2) {
			return -1;
		}
		typeName = typeName.toLowerCase();
		//判断字段数据类型，并比较大小
		if (typeName.endsWith("string")) {
			String value1 = v1.toString();
			String value2 = v2.toString();
			retVal = ASC_order ? value1.compareTo(value2) : value2.compareTo(value1);
		} else if (typeName.endsWith("short")) {
			Short value1 = Short.parseShort(v1.toString());
			Short value2 = Short.parseShort(v2.toString());
			retVal = ASC_order ? value1.compareTo(value2) : value2.compareTo(value1);
		} else if (typeName.endsWith("byte")) {
			Byte value1 = Byte.parseByte(v1.toString());
			Byte value2 = Byte.parseByte(v2.toString());
			retVal = ASC_order ? value1.compareTo(value2) : value2.compareTo(value1);
		} else if (typeName.endsWith("char")) {
			Integer value1 = (int) (v1.toString().charAt(0));
			Integer value2 = (int) (v2.toString().charAt(0));
			retVal = ASC_order ? value1.compareTo(value2) : value2.compareTo(value1);
		} else if (typeName.endsWith("int") || typeName.endsWith("integer")) {
			Integer value1 = Integer.parseInt(v1.toString());
			Integer value2 = Integer.parseInt(v2.toString());
			retVal = ASC_order ? value1.compareTo(value2) : value2.compareTo(value1);
		} else if (typeName.endsWith("long")) {
			Long value1 = Long.parseLong(v1.toString());
			Long value2 = Long.parseLong(v2.toString());
			retVal = ASC_order ? value1.compareTo(value2) : value2.compareTo(value1);
		} else if (typeName.endsWith("float")) {
			Float value1 = Float.parseFloat(v1.toString());
			Float value2 = Float.parseFloat(v2.toString());
			retVal = ASC_order ? value1.compareTo(value2) : value2.compareTo(value1);
		} else if (typeName.endsWith("double")) {
			Double value1 = Double.parseDouble(v1.toString());
			Double value2 = Double.parseDouble(v2.toString());
			retVal = ASC_order ? value1.compareTo(value2) : value2.compareTo(value1);
		} else if (typeName.endsWith("boolean")) {
			Boolean value1 = Boolean.parseBoolean(v1.toString());
			Boolean value2 = Boolean.parseBoolean(v2.toString());
			retVal = ASC_order ? value1.compareTo(value2) : value2.compareTo(value1);
		} else if (typeName.endsWith("date")) {
			Date value1 = (Date) (v1);
			Date value2 = (Date) (v2);
			retVal = ASC_order ? value1.compareTo(value2) : value2.compareTo(value1);
		} else if (typeName.endsWith("timestamp")) {
			Timestamp value1 = (Timestamp) (v1);
			Timestamp value2 = (Timestamp) (v2);
			retVal = ASC_order ? value1.compareTo(value2) : value2.compareTo(value1);
		} else {
			//调用对象的compareTo()方法比较大小
			retVal = ASC_order ? v1.toString().compareTo(v2.toString()) : v2.toString().compareTo(v1.toString());
		}
		return retVal;
	}

	/**
	 * Java 8 利用lambda表达式排序
	 *
	 * @param <T>
	 * @param targetList 目标排序List
	 * @param sortField  排序字段(实体类属性名)
	 * @param sortMode   排序方式（asc or  desc）
	 */
	public static <T> void sortNew(List<T> targetList, String sortField, String sortMode) {
		try {
//            Field curField= curClass.getDeclaredField(sortField);
//            curField.setAccessible(true);
//            String typeName = curField.getType().getName().toLowerCase(); //转换成小写
			targetList.sort((obj1, obj2) -> {
				int retVal = 0;
				try {
					Class clazz = obj1.getClass();
					Field curField = clazz.getDeclaredField(sortField);
					curField.setAccessible(true);
					String typeName = curField.getType().getName().toLowerCase(); //转换成小写
					Object v1 = curField.get(obj1);
					Object v2 = curField.get(obj2);
					retVal = compareEXE(v1, v2, sortMode, typeName);
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				} catch (NoSuchFieldException e) {
					e.printStackTrace();
				}
				return retVal;
			});
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}  
