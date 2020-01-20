package com.test.to;

import java.util.Comparator;

/**
 * @author: liqinchao
 * @Date: 2020/1/20 14:47
 * @Description:
 */
public class BillOrder implements Comparator<BillOrder> {
    public OrderInfo orderInfo;
    public String goods;

    @Override
    public int compare(BillOrder o1, BillOrder o2) {
        return o1.orderInfo.id.compareTo(o2.orderInfo.id);
    }
}
