package com.lqc.async;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @Author: lqc
 * @Date: 2020/6/22 16:16
 * @Description:
 */
public class DoThing {
    private static Logger logger = LoggerFactory.getLogger(ThreadTest.class);
    /**
     * 特殊的instance变量
     */
    private byte[] lock = new byte[0];

    synchronized void doSomeThing0(String threadName) {
        try {
            logger.info("..thread-{} start ...", threadName);
            Thread.sleep(2000L);
        } catch (Exception e) {
            e.printStackTrace();
        }
        logger.info("..thread-{} end ...", threadName);
    }

    synchronized static void doSomeThing1(String threadName) {
        try {
            logger.info("..thread-{} start ...", threadName);
            Thread.sleep(2000L);
        } catch (Exception e) {
            e.printStackTrace();
        }
        logger.info("..thread-{} end ...", threadName);
    }

    void doSomeThing2(String threadName) {
        synchronized (lock) {
            try {
                logger.info("..thread-{} start ...", threadName);
                Thread.sleep(2000L);
            } catch (Exception e) {
                e.printStackTrace();
            }
            logger.info("..thread-{} end ...", threadName);
        }
    }
    void doSomeThing3(String threadName) {
        synchronized (this.getClass().getName()) {
            try {
                logger.info("{} start ...", threadName);
                Thread.sleep(2000L);
            } catch (Exception e) {
                e.printStackTrace();
            }
            logger.info("{} end ...", threadName);
        }
    }

}
