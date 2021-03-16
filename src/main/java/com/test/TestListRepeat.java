package com.test;

import com.test.compare.OrderInfo;
import com.test.compare.UserInfo;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @ClassName TestListRepeat
 * @Description TODO
 * @Author liqinchao
 * @Date 2021/3/16 10:37
 * @Version 1.0
 **/
public class TestListRepeat {

    public static void main(String[] args) {
        checkRepeat();
    }

    static void checkRepeat() {
        List<OrderInfo> list = new ArrayList<>();
        OrderInfo o1 = new OrderInfo("11", "ee");
        OrderInfo o2 = new OrderInfo("22", "ee");
        OrderInfo o3 = new OrderInfo("33", "ee");
        OrderInfo o4 = new OrderInfo("11", "EE");
        list.add(o1);
        list.add(o2);
        list.add(o3);
        list.add(o4);
        int size = list.size();
        int count1 = list.stream().map(OrderInfo::getId).distinct().collect(Collectors.toList()).size();
        System.out.println("list.size=" + size + "  count=" + count1);
        int count2 = list.stream().distinct().collect(Collectors.toList()).size();
        System.out.println("list.size=" + size + "  count=" + count2);


        List<UserInfo> list2 = new ArrayList<>();
        UserInfo u1 = UserInfo.valueOf("11","ee");
        UserInfo u2 = UserInfo.valueOf("11","ee");
        UserInfo u3 = UserInfo.valueOf("11","EE");
        UserInfo u4 = UserInfo.valueOf("22","EE");
        list2.add(u1);
        list2.add(u2);
        list2.add(u3);
        list2.add(u4);
        int size2 = list2.size();
        int count4 = list2.stream().distinct().collect(Collectors.toList()).size();
        System.out.println("list2.size=" + size2 + "  count=" + count4);
    }
}
