package org.codethink.synch.synchblock;

import java.util.Calendar;
import java.util.concurrent.CountDownLatch;

import org.junit.Test;

/**
 * 
 * 同步方法和同步代码块关系
 * synchronized (this)代码块和同步方法一样都是锁定当前对象的。
 * 因此调用同步方法或者synchronized (this)代码块时，调用是顺序执行的，也就是同步阻塞的。
 * 具体说是当调用同步方法时，对其他同步方法或synchronized (this)同步代码块调用呈阻塞状态。
 * 同理，调用synchronized (this)同步代码块时，对其他同步方法或synchronized (this)同步代码块调用也呈阻塞状态。
 * 
 * @author CaiXiangNing
 * @date 2016年12月23日
 * @email caixiangning@gmail.com
 */
public class SynchBlockLockObjectTest {
	
	class Task {
		// synchronized关键字修饰的同步方法
		synchronized public void doTask1() {
			System.out.println(Thread.currentThread().getName() + "执行同步方法，起始时间：" + Calendar.getInstance().getTime());
			longTimeTask();
			System.out.println(Thread.currentThread().getName() + "执行同步方法，终止时间：" + Calendar.getInstance().getTime());
		}

		// synchronized关键字修饰的同步代码块方法
		public void doTask2() {
			synchronized (this) {
				System.out.println(Thread.currentThread().getName() + "执行同步代码块，起始时间：" + Calendar.getInstance().getTime());
				longTimeTask();
				System.out.println(Thread.currentThread().getName() + "执行同步代码块，终止时间：" + Calendar.getInstance().getTime());
			}
		}

		// 耗时三秒的长任务(不存在竞争)
		public void longTimeTask() {
			try {
				Thread.sleep(3000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
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
			task.doTask1();
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
			task.doTask2();
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
	public void testSynch1() {
		// 创建一个对象
		Task task = new Task();
		// 两个线程注入同一个类的实例
		ThreadA threadA = new ThreadA(task, "thread1");
		ThreadB threadB = new ThreadB(task, "thread2");

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
