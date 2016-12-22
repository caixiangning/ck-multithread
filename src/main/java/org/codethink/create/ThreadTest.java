package org.codethink.create;

import java.util.concurrent.CountDownLatch;

import org.junit.Test;

/**
 * 
 * 创建线程两种方式：1、继承Thread类;2、实现Runnable接口.建议使用第二种方式
 * 启动线程两种方式：1、new Thread().start()
 * 2、new Thread(new Runnable()).start()
 * 
 * @author CaiXiangNing
 * @date 2016年12月21日
 * @email caixiangning@gmail.com
 */
public class ThreadTest {
	
	// 线程需要执行的任务方法
	public void task(){
		System.out.println("MyThread - START");
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("MyThread - END");
	}
	
	// 创建线程方式1：继承Thread类
	class MyThread extends Thread{
		@Override
		public void run() {
			// TODO Auto-generated method stub
			task();
		}
	}
	
	// 创建线程方式2：实现Runnable接口
	class MyRunnable implements Runnable{
		public void run() {
			// TODO Auto-generated method stub
			task();
		}
	}
	
	// 测试线程的创建以及启动的方法
	@Test
	public void testThread(){
		
		// 创建线程方式1：继承Thread类 
		// 调用方式：调用该线程类Thread对象的start方法
		Thread thread1 = new MyThread();
		thread1.start();
		
		// 创建线程方式2：实现Runnable接口 
		// 调用方式：先创建Runnable对象，然后使用Thread(Runnable target)构建线程类Thread对象最后调用其start方法
		Runnable myRunnable = new MyRunnable();
		Thread thread2 = new Thread(myRunnable);
		thread2.start();
		
		CountDownLatch countDownLatch = new CountDownLatch(1);
		try {
			countDownLatch.await();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
