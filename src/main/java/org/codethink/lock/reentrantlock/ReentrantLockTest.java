package org.codethink.lock.reentrantlock;

import java.util.Calendar;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.junit.Test;

/**
 * 
 * ReentrantLock实现同步
 * 调用ReentrantLock对象的lock方法获取锁，调用unlock方法释放锁
 * 
 * @author CaiXiangNing
 * @date 2016年12月28日
 * @email caixiangning@gmail.com
 */
public class ReentrantLockTest {
	
	class Task {
		private Lock lock = new ReentrantLock();
		
		public void synchMethod1() {
			// 调用ReentrantLock对象的lock方法获取锁
			lock.lock();
			System.out.println(Thread.currentThread().getName()
					+ "线程进入synchMethod1方法，进入时间：     "
					+ Calendar.getInstance().getTime());
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			System.out.println(Thread.currentThread().getName()
					+ "线程离开synchMethod1方法，离开时间：     "
					+ Calendar.getInstance().getTime());
			// 调用ReentrantLock对象的unlock方法释放锁
			lock.unlock();
		}

		// synchronized关键字修饰的同步方法
		public void synchMethod2() {
			// 调用ReentrantLock对象的lock方法获取锁
			lock.lock();
			System.out.println(Thread.currentThread().getName()
					+ "线程进入synchMethod2方法，进入时间：     "
					+ Calendar.getInstance().getTime());
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			System.out.println(Thread.currentThread().getName()
					+ "线程离开synchMethod2方法，离开时间：     "
					+ Calendar.getInstance().getTime());
			// 调用ReentrantLock对象的unlock方法释放锁
			lock.unlock();
		}
	}
	
	class ThreadA extends Thread {
		private Task task;

		// 通过构造器注入Task对象
		public ThreadA(Task task, String threadName) {
			// TODO Auto-generated constructor stub
			this.task = task;
			this.setName(threadName);
		}

		// 业务操作方法
		public void doTask() {
			task.synchMethod1();
		}

		// 重写run方法实现
		@Override
		public void run() {
			// TODO Auto-generated method stub
			doTask();
		}
	}

	class ThreadB extends Thread {
		private Task task;

		// 通过构造器注入Task对象
		public ThreadB(Task task, String threadName) {
			// TODO Auto-generated constructor stub
			this.task = task;
			this.setName(threadName);
		}

		// 业务操作方法
		public void doTask() {
			task.synchMethod2();
		}

		// 重写run方法实现
		@Override
		public void run() {
			// TODO Auto-generated method stub
			doTask();
		}
	}

	// 测试使用ReentrantLock对象来实现同步
	@Test
	public void testReentrantLock() {
		// 创建一个对象
		Task task = new Task();
		// 两个线程注入同一个类的实例
		ThreadA threadA = new ThreadA(task, "thread1");
		threadA.start();
		ThreadB threadB = new ThreadB(task, "thread2");
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
