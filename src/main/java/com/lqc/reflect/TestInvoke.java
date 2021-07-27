package com.lqc.reflect;

import com.lqc.service.MemberService;
import com.lqc.util.ReflectUtils;

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
            ReflectUtils.invokeMethod("com.lqc.service.MemberService", "calcAge", 22, 33);
            ReflectUtils.invokeMethod(new MemberService(), "hello", "李世民", 1);
            ReflectUtils.invokeMethod(MemberService.class, "test");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
