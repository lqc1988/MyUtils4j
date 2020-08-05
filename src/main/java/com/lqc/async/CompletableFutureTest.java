package com.lqc.async;

import com.lqc.util.CommonUtil;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.Random;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * @Author: lqc
 * @Date: 2020/6/4
 * @Description: CompletableFuture 使用详解
 * CompletableFuture 提供了四个静态方法来创建一个异步操作。
 * public static CompletableFuture<Void> runAsync(Runnable runnable)
 * public static CompletableFuture<Void> runAsync(Runnable runnable, Executor executor)
 * public static <U> CompletableFuture<U> supplyAsync(Supplier<U> supplier)
 * public static <U> CompletableFuture<U> supplyAsync(Supplier<U> supplier, Executor executor)
 * <p>
 * 没有指定Executor的方法会使用ForkJoinPool.commonPool() 作为它的线程池执行异步代码。
 * 如果指定线程池，则使用指定的线程池运行。以下所有的方法都类同。
 * runAsync方法不支持返回值。
 * supplyAsync可以支持返回值。
 */
public class CompletableFutureTest {
    private static Logger logger = LoggerFactory.getLogger(CompletableFutureTest.class);

    public static void main(String[] args) {
        try {
            logger.info("..call start ...");
//            runAsync(0);
//            supplyAsync();
            whenComplete();
//            thenApply();
            logger.info("..call end ...");
            Thread.sleep(10000L);
            logger.info("..call finish ...");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    static Long doSomeThing(int time) {
        logger.info("..thread start ...");
        try {
            File txt = new File("/data/1.txt");
            FileUtils.write(txt, CommonUtil.nowStr() + "..time=" + time + "..thread start ...\n", "utf-8", true);
            Thread.sleep(2000L);
            FileUtils.write(txt, CommonUtil.nowStr() + "..time=" + time + "..thread end ...\n", "utf-8", true);
        } catch (Exception e) {
            e.printStackTrace();
        }
        logger.info("..thread end ...");
        System.out.println(CommonUtil.nowStr() + "..run end ...");
        return 233L;
    }

    /**
     * 无返回值
     *
     * @Param: [retry]
     * @Date: 2020/6/8 10:58
     * @Return: void
     */
    public static void runAsync(int retry) throws Exception {
        logger.info("..run before ...");
        try {
            final int time = retry;
            logger.info("..run start ...");
            CompletableFuture<Void> future = CompletableFuture.runAsync(() -> doSomeThing(time));
            logger.info("..run after 1 ...");
//            future.join();
//            future.get();
            future.get(1, TimeUnit.SECONDS);
            logger.info("..run after 2 ...");
        } catch (TimeoutException e) {
            retry++;
            if (retry < 3) {
                runAsync(retry);
            } else {
                throw e;
            }
        }
    }

    /**
     * 有返回值
     *
     * @Param: []
     * @Date: 2020/6/4 17:26
     * @Return: void
     */
    public static void supplyAsync(int retry) throws Exception {
        logger.info("..run before ...");
        try {
            final int time = retry;
            logger.info("..run start ...");
            CompletableFuture<Long> future = CompletableFuture.supplyAsync(() -> doSomeThing(time));
            logger.info("..run after 1 ...");
//            future.join();
//            future.get();
            future.get(1, TimeUnit.SECONDS);
            logger.info("..run after 2 ...");
        } catch (TimeoutException e) {
            retry++;
            if (retry < 3) {
                runAsync(retry);
            } else {
                throw e;
            }
        }
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
            logger.info("..thread start ...");
            try {
                Thread.sleep(2000L);
                int i = 12 / 0;
            } catch (Exception e) {
                e.printStackTrace();
                logger.error("..执行异常：", e);
            }

            if (new Random().nextInt() % 2 >= 0) {
            }
            logger.info("..thread end ...");
        });

        future.whenComplete((t, a) -> logger.info("..执行完成！"));
        future.exceptionally(t -> {
            logger.error("..执行失败：", t);
            return null;
        });
        logger.info("..run after 1 ...");
        Thread.sleep(2000L);
        logger.info("..run after 2 ...");
    }

    /**
     * thenApply 方法
     * 当一个线程依赖另一个线程时，可以使用 thenApply 方法来把这两个线程串行化。
     * public <U> CompletableFuture<U> thenApply(Function<? super T,? extends U> fn)
     * public <U> CompletableFuture<U> thenApplyAsync(Function<? super T,? extends U> fn)
     * public <U> CompletableFuture<U> thenApplyAsync(Function<? super T,? extends U> fn, Executor executor)
     * <p>
     * Function<? super T,? extends U>
     * T：上一个任务返回结果的类型
     * U：当前任务的返回值类型
     *
     * @Param: []
     * @Date: 2020/6/8 13:53
     * @Return: void
     */
    private static void thenApply() throws Exception {
        CompletableFuture<Long> future = CompletableFuture.supplyAsync(() -> {
            long result = new Random().nextInt(100);
            logger.info("result1=" + result);
            return result;
        }).thenApply((t) -> {
            long result = t * 5;
            logger.info("result2=" + result);
            return result;
        });

//        long result = future.get();
//        System.out.println(result);
    }
}
