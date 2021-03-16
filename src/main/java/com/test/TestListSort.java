package com.test;

import com.test.compare.BillOrder;
import com.test.compare.OrderInfo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TestListSort {
    public static void main(String[] args) throws Exception {
        List<BillOrder> billOrderList = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            OrderInfo orderInfo = new OrderInfo();
            orderInfo.setId(i + "");
            orderInfo.setTitle(i + "aaa");
            BillOrder billOrder = new BillOrder();
            billOrder.orderInfo = orderInfo;
            billOrder.goods = i + "bbb";
            billOrderList.add(billOrder);
        }
        for (int i = 0; i < 3; i++) {
            OrderInfo orderInfo = new OrderInfo();
            orderInfo.setId("2");
            orderInfo.setTitle(i + "eee");
            BillOrder billOrder = new BillOrder();
            billOrder.orderInfo = orderInfo;
            billOrder.goods = i + "fff";
            billOrderList.add(billOrder);
        }
        System.out.println("排序前:=====");
        for (BillOrder billOrder : billOrderList) {
            System.out.println("billOrder.goods：" + billOrder.goods + "，orderInfo.id：" + billOrder.orderInfo.getId() +
                    " ，orderInfo.title：" + billOrder.orderInfo.getTitle());
        }

        //ListSortUtil.sort(billOrderList,"orderInfo",null);
        Collections.sort(billOrderList, billOrderList.get(0));
        System.out.println("排序后:=====");
        for (BillOrder billOrder : billOrderList) {
            System.out.println("billOrder.goods：" + billOrder.goods + "，orderInfo.id：" + billOrder.orderInfo.getId() +
                    " ，orderInfo.title：" + billOrder.orderInfo.getTitle());
        }
    }


}
