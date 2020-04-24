package com.lqc.task;

import com.lqc.utils.CommonUtil;

import java.util.Date;

/**
 * @author: liqinchao
 * @Date: 2020/4/21 16:51
 * @Description:
 */

public class TaskService {
    public String doTask1() {
        try {
            Thread.sleep(1000L);
            System.out.println(CommonUtil.nowStr() +"---do task 1");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return "do task 1 success.";
    }

    public String doTask2() {
        try {
            Thread.sleep(2000L);
            System.out.println(CommonUtil.nowStr() +"---do task 2");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return "do task 2 success.";
    }

    public String doTask3() {
        try {
            Thread.sleep(3000L);
            System.out.println(CommonUtil.nowStr() +"---do task 3");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return "do task 3 success.";
    }
}
