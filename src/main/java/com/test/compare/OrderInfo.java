package com.test.compare;

import java.util.Comparator;

/**
 * @author: liqinchao
 * @Date: 2020/1/20 14:47
 * @Description:
 */
public class OrderInfo implements Comparator<OrderInfo>{
        public String id;
    public String title;

    @Override
    public int compare(OrderInfo o1, OrderInfo o2) {
        return o1.id.compareTo(o2.id);
    }
}
