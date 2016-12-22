package org.codethink.synch.threadsafe;

import java.util.concurrent.CountDownLatch;

import org.junit.Test;

/**
 * 
 * 线程安全问题：非线程安全会在多个线程对同一个对象中的实例变量进行并发访问时发生，产生的后果是脏读，
 * 也就是取得的数据是被更改过的。而线程安全是指获得的实例变量的值是经过同步处理的。不会出现脏读现象。
 * 
 * 如果其中一个线程对该对象的实例变量操作完成则不会引发这种问题，如果是尚未完成完整的操作，则会引发非线程安全问题
 * @author CaiXiangNing
 * @date 2016年12月22日
 * @email caixiangning@gmail.com
 */
public class ThreadSafeTest {
	
	// 多个线程并发访问对象中的实例变量会存在非线程安全问题
	class UnsafeThreadClass{
		private int num;
		
		public void add(int addNum){
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
	
	class ThreadB extends Thread{
		private UnsafeThreadClass unsafeThreadClass;
		// 通过构造器注入UnsafeThreadClass对象
		public ThreadB(UnsafeThreadClass unsafeThreadClass, String threadName) {
			// TODO Auto-generated constructor stub
			this.unsafeThreadClass = unsafeThreadClass;
			this.setName(threadName);
		}
		
		// 业务操作方法
		public void task(){
			unsafeThreadClass.add(100);
		}
		
		// 重写run方法实现
		@Override
		public void run() {
			// TODO Auto-generated method stub
			task();
		}
	}
	
	@Test
	public void testUnsafeThread(){
		// 创建一个对象
		UnsafeThreadClass unsafeThreadClass = new UnsafeThreadClass();
		// 两个线程注入同一个类的实例，然后对该对象的实例进行操作
		ThreadB thread1 = new ThreadB(unsafeThreadClass, "thread1");
		thread1.start();
		ThreadB thread2 = new ThreadB(unsafeThreadClass, "thread2");
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
