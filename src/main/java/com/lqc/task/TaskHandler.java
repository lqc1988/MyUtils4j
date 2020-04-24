package com.lqc.task;

import com.lqc.utils.CommonUtil;
import org.apache.commons.lang3.concurrent.BasicThreadFactory;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.concurrent.*;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;

/**
 * @author: liqinchao
 * @Date: 2020/4/21 16:52
 * @Description:
 */


public class TaskHandler {
    public void syncHandler() {
        System.out.println(CommonUtil.nowStr() + "---start task--syncHandler");
        Date start = new Date();
        TaskService taskService = new TaskService();
        taskService.doTask1();
        taskService.doTask2();
        taskService.doTask3();
        ;
        System.out.println(String.format("同步执行take time：%ss \r\n", (new Date().getTime() - start.getTime()) / 1000));
        System.out.println(CommonUtil.nowStr() + "---end task--syncHandler");
    }

    /**
     * 启用三个线程单独处理3个任务，等处理完成之后，在处理其他事情主线程需要等待，俗称伪异步
     */
    public void asyncHandler() {
        System.out.println(CommonUtil.nowStr() + "---start task--asyncHandler");
        Date start = new Date();
        TaskService taskService = new TaskService();
        ExecutorService executorService = Executors.newFixedThreadPool(3);
        Callable<String>[] callableList = new Callable[]{() -> taskService.doTask1(), () -> taskService.doTask2(), () -> taskService.doTask3()};
        try {
            List<Future<String>> futures = executorService.invokeAll(Arrays.asList(callableList));
            List<String> results = futures.parallelStream().map(stringFuture -> {
                try {
                    return stringFuture.get();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return "";
            }).collect(Collectors.toList());
            System.out.println("results :" + results);
            System.out.println(String.format("异步多线程take time: %ss \r\n", (new Date().getTime() - start.getTime()) / 1000));
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            executorService.shutdown();
        }
        System.out.println(CommonUtil.nowStr() + "---end task--asyncHandler");
    }

    /**
     * 异步编排不阻塞主线程，当任务执行完之后，通过通知返回结果
     */
    public void completableFuture() {
        System.out.println(CommonUtil.nowStr() + "---start task--completableFuture");
        Date start = new Date();
        TaskService taskService = new TaskService();
        CompletableFuture<String> completableFuture1 = CompletableFuture.supplyAsync(() -> taskService.doTask1());
        CompletableFuture<String> completableFuture2 = CompletableFuture.supplyAsync(() -> taskService.doTask2());
        CompletableFuture<String> completableFuture3 = CompletableFuture.supplyAsync(() -> taskService.doTask3());
        List<CompletableFuture<String>> futureList = Arrays.asList(completableFuture1, completableFuture2, completableFuture3);
        CompletableFuture<Void> doneFuture = CompletableFuture.allOf(completableFuture1, completableFuture2, completableFuture3);
        try {
            doneFuture.whenComplete((aVoid, throwable) -> {
                List results = futureList.stream().map(CompletableFuture::join).collect(Collectors.toList());
                System.out.println(CommonUtil.nowStr() + "---results :" + results);
            });
            System.out.println(String.format("异步编排take time: %ss ", (new Date().getTime() - start.getTime()) / 1000));
            Thread.sleep(4000L);
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println(CommonUtil.nowStr() + "---end task--completableFuture");
    }

    /**
     * 异步
     */
    public void scheduleTask() {
        System.out.println(CommonUtil.nowStr() + "---start task--scheduleTask");
        ScheduledExecutorService executorService = new ScheduledThreadPoolExecutor(1,
                new BasicThreadFactory.Builder().namingPattern("example-schedule-pool-%d").daemon(true).build());
        executorService.execute(() -> {
            System.out.println("执行....");
            try {
                TaskService taskService = new TaskService();
                taskService.doTask1();
                taskService.doTask2();
                taskService.doTask3();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        System.out.println(CommonUtil.nowStr() + "---end task--scheduleTask");
        try {
            Thread.sleep(4000L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void comTask() {
        System.out.println(CommonUtil.nowStr() + "---start task--comTask");
        CompletableFuture.runAsync(() -> {
            System.out.println("执行....");
        }).handle((p, t) -> {
            if (null != t) {
                System.err.println("comTask出现异常：" + t.getMessage());
                t.printStackTrace();
            }else{
                System.out.println("comTask执行完成...");
            }
            return null;
        });
        System.out.println(CommonUtil.nowStr() + "---end task--comTask");
        try {
            Thread.sleep(4000L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        TaskHandler handler = new TaskHandler();
        //handler.syncHandler();
        //handler.asyncHandler();
        //handler.completableFuture();
        //handler.scheduleTask();
        handler.comTask();
    }
}
