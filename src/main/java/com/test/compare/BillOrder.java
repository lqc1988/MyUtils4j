package com.test.compare;

import lombok.Data;

import java.util.Comparator;

/**
 * @author: liqinchao
 * @Date: 2020/1/20 14:47
 * @Description:
 */
@Data
public class BillOrder implements Comparator<BillOrder> {
    public OrderInfo orderInfo;
    public String goods;

    @Override
    public int compare(BillOrder o1, BillOrder o2) {
        return o1.orderInfo.getId().compareTo(o2.orderInfo.getId());
    }
}
