package org.codethink.lock.reentrantlock;

import java.util.Calendar;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.junit.Test;

/**
 * 
 * 使用Conditon实现通知部分线程
 * 
 * @author CaiXiangNing
 * @date 2016年12月28日
 * @email caixiangning@gmail.com
 */
public class WaitSignalSpecifyThreadTest {
	
	private Lock lock = new ReentrantLock();
	Condition conditionA = lock.newCondition();
	Condition conditionB = lock.newCondition();
	
	class Task {
		
		public void awaitA() {
			try {
				lock.lock();
				System.out.println(Thread.currentThread().getName()
						+ "线程执行awaitA方法，起始时间：" + Calendar.getInstance().getTime());
				conditionA.await();
				System.out.println(Thread.currentThread().getName()
						+ "线程执行awaitA方法，终止时间：" + Calendar.getInstance().getTime());
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally {
				lock.unlock();
			}
		}

		public void awaitB() {
			try {
				lock.lock();
				System.out.println(Thread.currentThread().getName()
						+ "线程执行awaitB方法，起始时间：" + Calendar.getInstance().getTime());
				conditionB.await();
				System.out.println(Thread.currentThread().getName()
						+ "线程执行awaitB方法，终止时间：" + Calendar.getInstance().getTime());
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally {
				lock.unlock();
			}
		}

		public void signalA() {
			try {
				lock.lock();
				System.out.println(Thread.currentThread().getName()
						+ "线程通知线程A，起始时间：" + Calendar.getInstance().getTime());
				conditionA.signalAll();
			} finally {
				lock.unlock();
			}
		}
		
		public void signalB() {
			try {
				lock.lock();
				System.out.println(Thread.currentThread().getName()
						+ "线程通知线程B，起始时间：" + Calendar.getInstance().getTime());
				conditionA.signalAll();
			} finally {
				lock.unlock();
			}
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

		// 业务操作方法进行单独封装
		public void doTask() {
			task.awaitA();
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
			task.awaitB();
		}

		// 重写run方法实现
		@Override
		public void run() {
			// TODO Auto-generated method stub
			doTask();
		}
	}

	// 测试同步方法和同步代码块相互阻塞关系
	@Test
	public void testWaitSignalSpecifyThread() {
		// 创建一个对象
		Task task = new Task();
		// 两个线程注入同一个类的实例
		ThreadA threadA = new ThreadA(task, "thread1");
		ThreadB threadB = new ThreadB(task, "thread2");

		threadA.start();
		threadB.start();
		
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		// 通知/唤醒线程A
		task.signalA();
		
		CountDownLatch countDownLatch = new CountDownLatch(2);
		try {
			countDownLatch.await();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
