//package com.dap.sequence.client.test;
//
//import java.util.concurrent.CountDownLatch;
//import java.util.concurrent.TimeUnit;
//
///**
// * @author lyf
// * @date 2024/3/15
// */
//public class MultiThreadTest {
//
//
//    public synchronized void sayHello1(String name) throws InterruptedException {
//        System.out.println("start time 1:" + System.currentTimeMillis());
//        TimeUnit.MILLISECONDS.sleep(200);
//        System.out.println("say hello1" + name);
//        System.out.println("end time 1:" + System.currentTimeMillis());
//    }
//
//
//    public synchronized void sayHello2(String name) throws InterruptedException {
//        System.out.println("start time2:" + System.currentTimeMillis());
//        TimeUnit.MILLISECONDS.sleep(200);
//        System.out.println("say hello2" +name);
//        System.out.println("end time2:" + System.currentTimeMillis());
//    }
//
//
//    public static void main(String[] args) {
//        CountDownLatch countDownLatch = new CountDownLatch(1);
//        MultiThreadTest multiThreadTest = new MultiThreadTest();
//        Thread thread1  =new Thread(()->{
//            try {
//                countDownLatch.await();
//                multiThreadTest.sayHello1("xiaoming");
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//        });
//
//        thread1.start();;
//
//        Thread thread2  =new Thread(()->{
//            try {
//                countDownLatch.await();
//                multiThreadTest.sayHello2("xiaoming");
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//        });
//        thread2.start();;
//
//        countDownLatch.countDown();
//
//    }
//}
