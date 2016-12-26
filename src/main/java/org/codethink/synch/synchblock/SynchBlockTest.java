package org.codethink.synch.synchblock;

import java.util.Calendar;
import java.util.concurrent.CountDownLatch;

import org.junit.Test;

/**
 * 
 * 同步方法和同步代码块执行效率比较
 * 同步方法需要等待较长时间,同步代码块对其进行改进,仅包含存在竞争的代码块
 * 
 * @author CaiXiangNing
 * @date 2016年12月23日
 * @email caixiangning@gmail.com
 */
public class SynchBlockTest {
	
	class Task {
		// synchronized关键字修饰的同步方法
		synchronized public void doTask1() {
			System.out.println(Thread.currentThread().getName() + "开始执行任务，起始时间：" + Calendar.getInstance().getTime());
			longTimeTask();
			shortTimeTask();
			System.out.println(Thread.currentThread().getName() + "结束执行任务，终止时间：" + Calendar.getInstance().getTime());
		}
		
		// synchronized关键字修饰的同步代码块方法
		public void doTask2(){
			System.out.println(Thread.currentThread().getName() + "开始执行任务，起始时间：" + Calendar.getInstance().getTime());
			longTimeTask();
			synchronized (this) {
				shortTimeTask();
			}
			System.out.println(Thread.currentThread().getName() + "结束执行任务，终止时间：" + Calendar.getInstance().getTime());
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

		// 耗时较少的短任务(存在竞争)
		public void shortTimeTask() {

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

	// 测试多线程使用同步方法执行较长时间任务的效率
	@Test
	public void testSynch1() {
		// 创建一个对象
		Task task = new Task();
		// 两个线程注入同一个类的实例
		ThreadA threadA1 = new ThreadA(task, "thread1");
		ThreadA threadA2 = new ThreadA(task, "thread2");
		
		threadA1.start();
		threadA2.start();
		
		CountDownLatch countDownLatch = new CountDownLatch(2);
		try {
			countDownLatch.await();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	// 测试多线程使用同步代码块执行较长时间任务的效率
	@Test
	public void testSynch2() {
		// 创建一个对象
		Task task = new Task();
		// 两个线程注入同一个类的实例
		ThreadB threadB1 = new ThreadB(task, "thread1");
		ThreadB threadB2 = new ThreadB(task, "thread2");
		
		threadB1.start();
		threadB2.start();
		
		CountDownLatch countDownLatch = new CountDownLatch(2);
		try {
			countDownLatch.await();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
