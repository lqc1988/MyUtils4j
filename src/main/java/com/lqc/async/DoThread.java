package com.lqc.async;

/**
 * @Author: lqc
 * @Date: 2020/6/22 16:16
 * @Description:
 */
public class DoThread extends Thread {
    public DoThread() {
        super.setName("测试线程");
    }

    @Override
    public void run() {
        DoThing doThing = new DoThing();
//        doThing.doSomeThing0(this.getName());
        doThing.doSomeThing3(this.getName());
        super.run();
    }
}
