package com.lqc.util;

import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.Objects;

/**
 * @Classname ReflectUtils
 * @Description 反射工具类
 * @Date 2021/7/9 17:11
 * @author mayi
 */
public class ReflectUtils {

    /**
     * 通过反射运行Java方法
     *
     * @param clazzName  类名（包路径+类名）
     * @param methodName 方法名
     * @param args       入参（可变参数）
     * @return
     * @throws Exception
     */
    public static Object invokeMethod(String clazzName, String methodName, Object... args) throws Exception {
        Class ownerClass = Class.forName(clazzName);
        System.out.println("clazz name:" + clazzName);
        return invokeMethod(ownerClass, methodName, args);
    }

    /**
     * 通过反射运行Java方法
     *
     * @param owner      对象
     * @param methodName 方法名
     * @param args       入参（可变参数）
     * @return
     * @throws Exception
     */
    public static Object invokeMethod(Object owner, String methodName, Object... args) throws Exception {
        Class ownerClass = owner.getClass();
        System.out.println("Object:" + owner);
        return invokeMethod(ownerClass, methodName, args);
    }

    /**
     * 通过反射运行Java方法
     *
     * @param ownerClass Java类
     * @param methodName 方法名
     * @param args       入参（可变参数）
     * @return
     * @throws Exception
     */
    public static Object invokeMethod(Class ownerClass, String methodName, Object... args) throws Exception {
        Class[] argsClass = null;
        if (Objects.nonNull(args)) {
            argsClass = new Class[args.length];

            for (int i = 0, j = args.length; i < j; i++) {
                argsClass[i] = args[i].getClass();
            }
        }
        Method method = ownerClass.getMethod(methodName, argsClass);
        method.setAccessible(true);
        return method.invoke(ownerClass.newInstance(), args);
    }


    /**
     * 反射取值 BigDecimal
     */
    public static <T> BigDecimal getMethodValueByClass(T t, String methodName) {
        Class clazz = t.getClass();
        try {
            return (BigDecimal) clazz.getMethod(methodName).invoke(t);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return BigDecimal.ZERO;
    }

    /**
     * 反射赋值 BigDecimal
     */
    public static void setMethodValueByClass(Object obj, Class cls, String MethodName, BigDecimal val) {
        Method method = null;
        try {
            method = cls.getMethod(MethodName, BigDecimal.class);
            method.invoke(obj, val);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
