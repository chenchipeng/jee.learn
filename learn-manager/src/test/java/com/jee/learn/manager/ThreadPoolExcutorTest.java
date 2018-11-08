package com.jee.learn.manager;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * 线程池测试<br/>
 * 参考:https://blog.csdn.net/kusedexingfu/article/details/72491864
 * 
 * @author ccp
 * @version 1.0<br/>
 *          修改记录:<br/>
 *          1.2018年11月8日 下午2:39:21 ccp 新建
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = LearnManagerApplication.class)
public class ThreadPoolExcutorTest {

    private static final Logger log = LoggerFactory.getLogger(ThreadPoolExcutorTest.class);
    private static final int THREAD_COUNT = 6;

    // 线程计数器 将线程数量初始化 每执行完成一条线程，调用countDown()使计数器减1 主线程调用方法await()使其等待，当计数器为0时才被执行
    private CountDownLatch latch = new CountDownLatch(THREAD_COUNT);

    @Autowired
    private ThreadPoolTaskExecutor applicationTaskExecutor;

    public static void main(String[] args) {
        try {
            boundedThreadPool();
        } catch (Exception e) {
            log.debug("", e);
        }
    }

    @Test
    public void applicationTaskExecutorTest() {

        int model = 1;

        for (int i = 1; i <= THREAD_COUNT; i++) {
            try {
                applicationTaskExecutor.execute(new FooThread("任务" + i));
            } catch (Exception e) {
                log.debug("", e);
            }
        }
        
        if (model == 1) {
            try {
                Thread.sleep(30L * 1000L);
            } catch (InterruptedException e) {
                log.debug("", e);
            }

            for (int i = 1; i <= THREAD_COUNT; i++) {
                try {
                    applicationTaskExecutor.execute(new FooThread("任务" + i));
                } catch (Exception e) {
                    log.debug("", e);
                }
            }
        }

        try {
            latch.await();
        } catch (Exception e) {
            log.debug("", e);
        }
    }

    /** 有界队列线程池 */
    private static void boundedThreadPool() throws InterruptedException {

        // 1.初始的poolSize < corePoolSize，提交的runnable任务，会直接做为new一个Thread的参数，立马执行 。
        // 2.当提交的任务数超过了corePoolSize，会将当前的runable提交到一个block queue中。
        // 3.有界队列满了之后，如果poolSize < maximumPoolsize时，会尝试new
        // 一个Thread的进行救急处理，立马执行对应的runnable任务。
        // 4.如果3中也无法处理了，就会走到第四步执行reject操作

        int corePoolSize = 1;
        int maximumPoolSize = 2;
        long keepAliveTime = 1L;
        BlockingQueue<Runnable> workQueue = new ArrayBlockingQueue<>(3);

        ThreadPoolExecutor threadPool = new ThreadPoolExecutor(corePoolSize, maximumPoolSize, keepAliveTime,
                TimeUnit.SECONDS, workQueue);

        for (int i = 1; i <= THREAD_COUNT; i++) {
            threadPool.execute(new FooThread("任务" + i));
        }

        threadPool.shutdown();

        // 分析：<线程是两个两个执行的，没仔细看还真难看出>
        // 线程池的corePoolSize为1，任务1提交后，线程开始执行，corePoolSize
        // 数量用完，接着任务2、3、4提交，放到了有界队列中，此时有界队列也满了。继续提交任务5，由于当前运行的线程数poolSize <
        // maximumPoolsize,线程池尝试new一个新的线程来执行任务5，所以任务5会接着执行。当继续提交任务6,时，poolSize达到了maximumPoolSize，有界队列也满了，所以线程池执行了拒绝操作。
    }

    /** 样例线程 */
    static class FooThread implements Runnable {

        public String name;

        public FooThread(String name) {
            this.name = name;
        }

        @Override
        public void run() {
            log.debug("{}", name);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
    }

}
