package com.test.compare;

import lombok.Data;

import java.util.Comparator;

/**
 * @author: liqinchao
 * @Date: 2020/1/20 14:47
 * @Description:
 */
@Data
public class OrderInfo implements Comparator<OrderInfo> {
    private String id;
    private String title;

    public OrderInfo() {
    }

    public OrderInfo(String id, String title) {
        this.id = id;
        this.title = title;
    }

    @Override
    public int compare(OrderInfo o1, OrderInfo o2) {
        return o1.id.compareTo(o2.id);
    }
}
