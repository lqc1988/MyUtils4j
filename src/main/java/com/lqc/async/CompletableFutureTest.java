package com.lqc.async;

import com.lqc.util.CommonUtil;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.util.Collections;
import java.util.Random;
import java.util.concurrent.*;

/**
 * @Author: lqc
 * @Date: 2020/6/4
 * @Description: CompletableFuture 使用详解
 */
public class CompletableFutureTest {
    private static Logger logger = LoggerFactory.getLogger(CompletableFutureTest.class);

    public static void main(String[] args) {
        try {
            System.out.println(CommonUtil.nowStr() + "..call start ...");
            runAsync(0);
//            supplyAsync();
//            whenComplete();
            System.out.println(CommonUtil.nowStr() + "..call end ...");
            Thread.sleep(10000L);
            System.out.println(CommonUtil.nowStr() + "..thread final ...");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 无返回值
     *
     * @Param: []
     * @Date: 2020/6/4 17:26
     * @Return: void
     */
    public static void runAsync(int retry) throws Exception {
        System.out.println(CommonUtil.nowStr() + "..run before ...");
        try {
            final int time = retry;
            CompletableFuture<Void> future = CompletableFuture.runAsync(() -> {
//        CompletableFuture.runAsync(() -> {
                System.out.println(CommonUtil.nowStr() + "..run start ...");
                logger.info("..run start ...");
                try {
                    File txt = new File("/data/1.txt");
                    FileUtils.write(txt, CommonUtil.nowStr() + "..time=" + time + "..run start ...\n", "utf-8", true);
                    Thread.sleep(2000L);
                    FileUtils.write(txt, CommonUtil.nowStr() + "..time=" + time + "..run end ...\n", "utf-8", true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                logger.info("..run end ...");
                System.out.println(CommonUtil.nowStr() + "..run end ...");
            });
            System.out.println(CommonUtil.nowStr() + "..run after 1 ...");
//        future.join();
//        future.get();
            future.get(1, TimeUnit.SECONDS);
        } catch (TimeoutException e) {
            retry++;
            if (retry < 3) {
                runAsync(retry);
            } else {
                throw e;
            }
        }
        System.out.println(CommonUtil.nowStr() + "..run after 2 ...");
    }

    /**
     * 有返回值
     *
     * @Param: []
     * @Date: 2020/6/4 17:26
     * @Return: void
     */
    public static void supplyAsync() throws Exception {
        System.out.println(CommonUtil.nowStr() + "..run before ...");
        CompletableFuture<Long> future = CompletableFuture.supplyAsync(() -> {
            System.out.println(CommonUtil.nowStr() + "..run start ...");
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
            }
            System.out.println(CommonUtil.nowStr() + "..run end ...");
            return System.currentTimeMillis();
        });
        System.out.println(CommonUtil.nowStr() + "..run after 1 ...");
        long time = future.get();
        System.out.println("time = " + time);
        System.out.println(CommonUtil.nowStr() + "..run after 2 ...");
    }

    /**
     * 计算结果完成时的回调方法
     * 当CompletableFuture的计算结果完成，或者抛出异常的时候，可以执行特定的Action。主要是下面的方法：
     * public CompletableFuture<T> whenComplete(BiConsumer<? super T,? super Throwable> action)
     * public CompletableFuture<T> whenCompleteAsync(BiConsumer<? super T,? super Throwable> action)
     * public CompletableFuture<T> whenCompleteAsync(BiConsumer<? super T,? super Throwable> action, Executor executor)
     * public CompletableFuture<T> exceptionally(Function<Throwable,? extends T> fn)
     * <p>
     * 可以看到Action的类型是BiConsumer<? super T,? super Throwable>它可以处理正常的计算结果，或者异常情况。
     * whenComplete 和 whenCompleteAsync 的区别：
     * whenComplete：是执行当前任务的线程执行继续执行 whenComplete 的任务。
     * whenCompleteAsync：是执行把 whenCompleteAsync 这个任务继续提交给线程池来进行执行。
     *
     * @Param: []
     * @Date: 2020/6/4 17:39
     * @Return: void
     */
    public static void whenComplete() throws Exception {
        CompletableFuture<Void> future = CompletableFuture.runAsync(() -> {
            System.out.println(CommonUtil.nowStr() + "..run start ...");
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
            }
            if (new Random().nextInt() % 2 >= 0) {
                int i = 12 / 0;
            }
            System.out.println(CommonUtil.nowStr() + "..run end ...");
        });

        future.whenComplete((t, a) -> System.out.println(CommonUtil.nowStr() + "..执行完成！"));
        future.exceptionally(t -> {
            System.out.println(CommonUtil.nowStr() + "..执行失败！" + t.getMessage());
            return null;
        });
        System.out.println(CommonUtil.nowStr() + "..run after 1 ...");
        TimeUnit.SECONDS.sleep(2);
        System.out.println(CommonUtil.nowStr() + "..run after 2 ...");
    }
}
