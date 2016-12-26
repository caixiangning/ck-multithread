package org.codethink.synch.deadlock;

import java.util.concurrent.CountDownLatch;

import org.junit.Test;

/**
 * 
 * 死锁代码
 * 所谓死锁： 是指两个或两个以上的进程在执行过程中，由于竞争资源或者由于彼此通信而造成的一种阻塞的现象，
 * 若无外力作用，它们都将无法推进下去。
 * 
 * @author CaiXiangNing
 * @date 2016年12月26日
 * @email caixiangning@gmail.com
 */
public class DeadLockTest {
	
	class Task {
		private Object lock1 = new Object();
		private Object lock2 = new Object();
		
		// 锁定对象lock1，然后睡眠2秒，之后锁定lock2
		public void doTask1() throws InterruptedException {
			System.out.println("doTask1方法开始执行!");
			synchronized (lock1) {
				Thread.sleep(2000);
				synchronized (lock2) {
					Thread.sleep(2000);
				}
			}
			System.out.println("doTask1方法执行结束!");
		}

		// 锁定对象lock2，然后睡眠2秒，之后锁定lock1
		public void doTask2() throws InterruptedException {
			System.out.println("doTask2方法开始执行!");
			synchronized (lock2) {
				Thread.sleep(2000);
				synchronized (lock1) {
					Thread.sleep(2000);
				}
			}
			System.out.println("doTask2方法执行结束!");
		}
	}

	// 本线程类任务是执行一个同步方法
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
			try {
				task.doTask1();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		// 重写run方法实现
		@Override
		public void run() {
			// TODO Auto-generated method stub
			doTask();
		}
	}

	// 本线程类任务是执行一个包含同步代码块的方法
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
			try {
				task.doTask2();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		// 重写run方法实现
		@Override
		public void run() {
			// TODO Auto-generated method stub
			doTask();
		}
	}

	// 测试死锁
	@Test
	public void testSynch() {
		// 创建一个对象
		Task task = new Task();
		// 两个线程注入同一个类的实例
		ThreadA threadA = new ThreadA(task, "thread1");
		ThreadB threadB = new ThreadB(task, "thread2");
		
		// 两个线程会因为互相等待对方释放对象锁而导致出现死锁现象
		threadA.start();
		threadB.start();

		CountDownLatch countDownLatch = new CountDownLatch(2);
		try {
			countDownLatch.await();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
