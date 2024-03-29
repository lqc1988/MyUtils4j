package com.lqc.async;

import org.apache.commons.lang3.concurrent.BasicThreadFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.*;

/**
 * @Author: lqc
 * @Date: 2020/6/22 14:56
 * @Description:
 */
public class ThreadTest {
    private static Logger logger = LoggerFactory.getLogger(ThreadTest.class);

    public static void main(String[] args) {
        logger.info("开始测试-------------------------1");
//        completeExe();
////        logger.info("--------------------------------2");
//        threadExe();
//        logger.info("--------------------------------3");
//        queueExe();
        threadSubmit();
        logger.info("结束测试-------------------------3");
    }

    static void threadExe() {
        try {
            for (int i = 0; i < 10; i++) {
                final String threadName = "线程-->>" + i;
                DoThread doThread = new DoThread();
                doThread.setName(threadName);
                doThread.start();
            }
            Thread.sleep(12000L);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    static void threadSubmit() {
        logger.info("start...");
        ExecutorService executorService = init();
        Future<?> submit = executorService.submit(() -> {
            logger.info("ing...");
            try {
                Thread.sleep(3000L);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        logger.info("continue...");
//        try {
//            submit.get();
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        } catch (ExecutionException e) {
//            e.printStackTrace();
//        }
        logger.info("end...");
    }

    static ExecutorService init() {
        return new ThreadPoolExecutor(2, 10,
                60L, TimeUnit.SECONDS, new LinkedBlockingDeque<>(),
                Executors.defaultThreadFactory(), new ThreadPoolExecutor.AbortPolicy());
    }

    static void completeExe() {
        try {
            for (int i = 0; i < 10; i++) {
                DoThing doThing = new DoThing();
                final String threadName = "CompletableFuture-->>" + i;
                CompletableFuture.runAsync(() -> doThing.doSomeThing0(threadName));
//                CompletableFuture.runAsync(() -> DoThing.doSomeThing1(threadName));
//                CompletableFuture.runAsync(() -> doThing.doSomeThing2(threadName));
//                CompletableFuture.runAsync(() -> doThing.doSomeThing3(threadName));
            }
            Thread.sleep(120000L);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    static void queueExe() {
        try {
            //org.apache.commons.lang3.concurrent.BasicThreadFactory

//            ExecutorService executorService = new ThreadPoolExecutor(1,3,
//                    1L,TimeUnit.SECONDS,
//                    new LinkedBlockingQueue(3),
//                    Executors.defaultThreadFactory(),
//                    new ThreadPoolExecutor.AbortPolicy());
            for (int i = 0; i < 10; i++) {
                ScheduledExecutorService executorService = new ScheduledThreadPoolExecutor(1,
                        new BasicThreadFactory.Builder().namingPattern("测试-线程池-pool-%d").daemon(true).build());
                DoThing doThing = new DoThing();
                final String threadName = "queueExe-->>" + i;
                executorService.execute(() -> doThing.doSomeThing3(threadName));
            }
            Thread.sleep(120000L);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
