package com.lqc.util;

public class PrintUtils {

    public static void prtArray4Int(int[] arr) {
        if (null == arr || arr.length == 0) {
            System.out.println(String.format("The array(%s) is null", arr));
        }
        for (int i = 0; i < arr.length; i++) {
            System.out.print(String.format("arr[%s]=%s,", i, arr[i]));
        }
        System.out.println();
    }
}
