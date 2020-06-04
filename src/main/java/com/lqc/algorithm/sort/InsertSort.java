package com.lqc.algorithm.sort;

import com.lqc.util.PrintUtils;

/**
 * @Author: lqc
 * @Date: 2020/5/29 13:54
 * @Description: 插入排序
 */

public class InsertSort {
    public static void main(String[] args) {
        int[] input = new int[]{5, 2, 3, 2, 1};
        insertionSort(input);
    }

    /**
     * @param input
     * @return
     */
    public static void insertionSort(int[] input) {
        if (input == null || input.length <= 1) {
            return;
        }
        PrintUtils.prtArray4Int(input);
        for (int i = 1; i < input.length; i++) {
            int tmp = input[i];
            int j = i - 1;
            while (j >= 0 && input[j] > tmp) {
                input[j + 1] = input[j];
                j--;
            }
            input[j + 1] = tmp;
        }
        PrintUtils.prtArray4Int(input);
    }
}
