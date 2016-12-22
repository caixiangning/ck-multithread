package org.codethink.synch.synchmethod;

import java.util.concurrent.CountDownLatch;

import org.junit.Test;

/**
 * 
 * 线程安全：synchronized修饰的同步方法
 * 多线程操作同一个对象则有一个锁，同步方法同步执行，多线程操作多个对象则有多个锁，同步方法异步执行互不干扰。
 * 
 * @author CaiXiangNing
 * @date 2016年12月22日
 * @email caixiangning@gmail.com
 */
public class SynchMethodTest {


	class SafeThreadClass{
		private int num;
		
		// synchronized关键字修饰的同步方法
		synchronized public void add(int addNum){
			System.out.println(Thread.currentThread().getName() + "线程执行add方法!");
			num+=addNum;
			// 为了体现并发访问会出现非线程安全问题，特意延迟2秒
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			System.out.println(Thread.currentThread().getName()+ ":num= " + num);
		}
	}
	
	class ThreadA extends Thread{
		private SafeThreadClass safeThreadClass;
		// 通过构造器注入UnsafeThreadClass对象
		public ThreadA(SafeThreadClass safeThreadClass, String threadName) {
			// TODO Auto-generated constructor stub
			this.safeThreadClass = safeThreadClass;
			this.setName(threadName);
		}
		
		// 业务操作方法
		public void task(){
			safeThreadClass.add(100);
		}
		
		// 重写run方法实现
		@Override
		public void run() {
			// TODO Auto-generated method stub
			task();
		}
	}
	
	// 多线程操作同一个对象则有一个锁，同步方法同步执行
	@Test
	public void testSafeThread1(){
		// 创建一个对象
		SafeThreadClass safeThreadClass = new SafeThreadClass();
		// 两个线程注入同一个类的实例
		ThreadA thread1 = new ThreadA(safeThreadClass, "thread1");
		thread1.start();
		ThreadA thread2 = new ThreadA(safeThreadClass, "thread2");
		thread2.start();
		
		CountDownLatch countDownLatch = new CountDownLatch(1);
		try {
			countDownLatch.await();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	// 多线程操作多个对象则有多个锁，同步方法异步执行互不干扰
	@Test
	public void testSafeThread2(){
		// 创建两个个对象
		SafeThreadClass safeThreadClass1 = new SafeThreadClass();
		SafeThreadClass safeThreadClass2 = new SafeThreadClass();
		// 两个线程注入两个实例
		ThreadA thread1 = new ThreadA(safeThreadClass1, "thread1");
		thread1.start();
		ThreadA thread2 = new ThreadA(safeThreadClass2, "thread2");
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
