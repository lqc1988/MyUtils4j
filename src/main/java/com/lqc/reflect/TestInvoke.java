package com.lqc.reflect;

import com.lqc.service.MemberService;

import java.lang.reflect.Method;
import java.util.Objects;

/**
 * @Author: lqc
 * @Date: 6/19/2021 4:56 PM
 * @Description:
 */
public class TestInvoke {


    public static void main(String[] args) {
        try {
            invokeMethod("com.lqc.service.MemberService", "calcAge", 22, 33);
            invokeMethod(new MemberService(), "hello", "李世民", 1);
            invokeMethod(MemberService.class, "test");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Object invokeMethod(String clazzName, String methodName, Object... args) throws Exception {
        Class ownerClass = Class.forName(clazzName);
        System.out.println("clazz name:" + clazzName);
        return invokeMethod(ownerClass, methodName, args);
    }

    public static Object invokeMethod(Object owner, String methodName, Object... args) throws Exception {
        Class ownerClass = owner.getClass();
        System.out.println("Object:" + owner);
        return invokeMethod(ownerClass, methodName, args);
    }

    public static Object invokeMethod(Class ownerClass, String methodName, Object... args) throws Exception {
        Class[] argsClass = null;
        if (Objects.nonNull(args)) {
            argsClass = new Class[args.length];

            for (int i = 0, j = args.length; i < j; i++) {
                argsClass[i] = args[i].getClass();
            }
        }
        Method method = ownerClass.getMethod(methodName, argsClass);
        return method.invoke(ownerClass.newInstance(), args);
    }
}
