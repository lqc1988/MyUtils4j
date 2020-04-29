package com.lqc.util;

import org.apache.commons.text.RandomStringGenerator;

import java.util.Random;

import static org.apache.commons.text.CharacterPredicates.DIGITS;
import static org.apache.commons.text.CharacterPredicates.LETTERS;

/**
 * ClassName : RandomUtils
 * Author : liqinchao
 * CreateTime : 2019/3/18 18:11
 * Description : 随机数工具类
 */
public class RandomUtils {

    /**
     * 生成随机整数
     *
     * @param min 最小
     * @param max 最大
     * @return
     */
    public static int randomNum(int min, int max) {
        Random random = new Random();
        return random.nextInt(max) % (max - min + 1) + min;
    }
    /**
     * 纯数字随机字符串
     *
     * @param count
     * @return
     */
    public static String randomNumeric(int count) {
        RandomStringGenerator generator = new RandomStringGenerator.Builder()
                .withinRange('0', '9')
                .build();
        return generator.generate(count);
    }

    /**
     * 数字+字母随机字符串
     *
     * @param count
     * @return
     */
    public static String randomAlphanumeric(int count) {
        RandomStringGenerator generator = new RandomStringGenerator.Builder()
                .withinRange('0', 'z')
                .filteredBy(LETTERS, DIGITS)
                .build();
        return generator.generate(count);
    }

    /**
     * 纯字母随机字符串
     *
     * @param count
     * @return
     */
    public static String randomAlphabetic(int count) {
        RandomStringGenerator generator = new RandomStringGenerator.Builder()
                .withinRange('a', 'z')
                .build();
        return generator.generate(count);
    }
}
