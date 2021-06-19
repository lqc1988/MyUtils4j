package com.lqc.service;

/**
 * @Author: lqc
 * @Date: 6/19/2021 4:54 PM
 * @Description:
 */
public class MemberService {
    public int calcAge(Integer a, Integer b) {
        System.out.println("calcAge....,a=" + a + ",b=" + b);
        return a + b;
    }
    public void hello(String name,Integer gender) {
        System.out.println("hello.....,name=" + name + ",gender=" + gender);
    }
    public void test() {
        System.out.println("test......");
    }
}
