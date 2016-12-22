package org.codethink.synch.synchmethod;

import java.util.concurrent.CountDownLatch;

import org.junit.Test;

/**
 * 
 * 多线程操作同一对象与多个对象、同步方法与非同步方法间相关问题
 * 
 * 多线程操作同一个对象，两个同步方法同步执行
 * 多线程操作同一个对象，同步方法与非同步方法之间异步执行
 * 多线程操作多个对象，无论同步方法还是非同步方法之间均异步执行
 * 
 * @author CaiXiangNing
 * @date 2016年12月22日
 * @email caixiangning@gmail.com
 */
public class SynchMethodLockObjectTest {
	
	class SafeThreadClass{
		
		// synchronized关键字修饰的同步方法
		synchronized public void synchMethod1(){
			System.out.println(Thread.currentThread().getName() + "线程进入synchMethod1方法，进入时间：     "+System.currentTimeMillis());
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			System.out.println(Thread.currentThread().getName() + "线程离开synchMethod1方法，离开时间：     "+System.currentTimeMillis());
		}
		
		// synchronized关键字修饰的同步方法
		synchronized public void synchMethod2(){
			System.out.println(Thread.currentThread().getName() + "线程进入synchMethod2方法，进入时间：     "+System.currentTimeMillis());
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			System.out.println(Thread.currentThread().getName() + "线程离开synchMethod2方法，离开时间：     "+System.currentTimeMillis());
		}
		
		// 普通方法
		public void generalMethod1(){
			System.out.println(Thread.currentThread().getName() + "线程进入generalMethod1方法，进入时间："+System.currentTimeMillis());
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			System.out.println(Thread.currentThread().getName() + "线程离开generalMethod1方法，离开时间："+System.currentTimeMillis());
		}
		
		// 普通方法
		public void generalMethod2(){
			System.out.println(Thread.currentThread().getName() + "线程进入generalMethod2方法，进入时间："+System.currentTimeMillis());
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			System.out.println(Thread.currentThread().getName() + "线程离开generalMethod2方法，离开时间："+System.currentTimeMillis());
		}
	}
	
	// 包含同步方法的线程类
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
			safeThreadClass.synchMethod1();
		}
		
		// 重写run方法实现
		@Override
		public void run() {
			// TODO Auto-generated method stub
			task();
		}
	}
	
	//包含同步方法的线程类
	class ThreadB extends Thread{
		private SafeThreadClass safeThreadClass;
		// 通过构造器注入UnsafeThreadClass对象
		public ThreadB(SafeThreadClass safeThreadClass, String threadName) {
			// TODO Auto-generated constructor stub
			this.safeThreadClass = safeThreadClass;
			this.setName(threadName);
		}
		
		// 业务操作方法
		public void task(){
			safeThreadClass.synchMethod2();
		}
		
		// 重写run方法实现
		@Override
		public void run() {
			// TODO Auto-generated method stub
			task();
		}
	}
	
	// 仅包含非同步方法的线程类
	class ThreadC extends Thread{
		private SafeThreadClass safeThreadClass;
		// 通过构造器注入UnsafeThreadClass对象
		public ThreadC(SafeThreadClass safeThreadClass, String threadName) {
			// TODO Auto-generated constructor stub
			this.safeThreadClass = safeThreadClass;
			this.setName(threadName);
		}
		
		// 业务操作方法
		public void task(){
			safeThreadClass.generalMethod1();
		}
		
		// 重写run方法实现
		@Override
		public void run() {
			// TODO Auto-generated method stub
			task();
		}
	}
	
	// 仅包含非同步方法的线程类
	class ThreadD extends Thread{
		private SafeThreadClass safeThreadClass;
		// 通过构造器注入UnsafeThreadClass对象
		public ThreadD(SafeThreadClass safeThreadClass, String threadName) {
			// TODO Auto-generated constructor stub
			this.safeThreadClass = safeThreadClass;
			this.setName(threadName);
		}
		
		// 业务操作方法
		public void task(){
			safeThreadClass.generalMethod2();
		}
		
		// 重写run方法实现
		@Override
		public void run() {
			// TODO Auto-generated method stub
			task();
		}
	}
	
	// 多线程操作同一个对象，两个同步方法同步执行
	@Test
	public void testSafeThread1(){
		// 创建一个对象
		SafeThreadClass safeThreadClass = new SafeThreadClass();
		// 两个线程注入同一个类的实例
		ThreadA threadA = new ThreadA(safeThreadClass, "thread1");
		threadA.start();
		ThreadB threadB = new ThreadB(safeThreadClass, "thread2");
		threadB.start();
		
		CountDownLatch countDownLatch = new CountDownLatch(1);
		try {
			countDownLatch.await();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	// 多线程操作同一个对象，同步方法与非同步方法之间异步执行
	@Test
	public void testSafeThread2(){
		// 创建一个对象
		SafeThreadClass safeThreadClass = new SafeThreadClass();
		// 两个线程注入两个实例
		ThreadA threadA = new ThreadA(safeThreadClass, "thread1");
		threadA.start();
		ThreadC threadC = new ThreadC(safeThreadClass, "thread2");
		threadC.start();
		
		CountDownLatch countDownLatch = new CountDownLatch(1);
		try {
			countDownLatch.await();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	// 多线程操作多个对象，无论同步方法还是非同步方法之间均异步执行
	@Test
	public void testSafeThread3(){
		// 创建两个对象
		SafeThreadClass safeThreadClass1 = new SafeThreadClass();
		SafeThreadClass safeThreadClass2 = new SafeThreadClass();
		// 两个线程注入两个实例
		ThreadA threadA = new ThreadA(safeThreadClass1, "thread1");
		threadA.start();
		ThreadB threadB = new ThreadB(safeThreadClass2, "thread2");
		threadB.start();
		
		CountDownLatch countDownLatch = new CountDownLatch(1);
		try {
			countDownLatch.await();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
