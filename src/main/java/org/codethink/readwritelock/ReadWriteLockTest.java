package org.codethink.readwritelock;

import java.util.Calendar;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import org.junit.Test;

/**
 * 
 * 读写锁测试
 * 读写锁有两个锁，一个是读操作相关的锁，也称为共享锁；另外一个是写操作相关的锁，也叫排他锁。
 * 也就是多个读锁之间不互斥，读锁和写锁互斥，写锁与写锁互斥。
 * 
 * @author CaiXiangNing
 * @date 2016年12月30日
 * @email caixiangning@gmail.com
 */
public class ReadWriteLockTest {
	class Task {
		private ReentrantReadWriteLock reentrantReadWriteLock = new ReentrantReadWriteLock();

		public void ReadMethod() {
			try {
				reentrantReadWriteLock.readLock().lock();
				System.out.println(Thread.currentThread().getName()
					+ "线程获得读锁时间： "
					+ Calendar.getInstance().getTime());
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally{
				System.out.println(Thread.currentThread().getName()
						+ "线程失去读锁时间： "
						+ Calendar.getInstance().getTime());
				reentrantReadWriteLock.readLock().unlock();
			}
		}
		
		public void WriteMethod() {
			try {
				reentrantReadWriteLock.writeLock().lock();
				System.out.println(Thread.currentThread().getName()
					+ "线程获得写锁时间： "
					+ Calendar.getInstance().getTime());
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally{
				System.out.println(Thread.currentThread().getName()
						+ "线程失去写锁时间： "
						+ Calendar.getInstance().getTime());
				reentrantReadWriteLock.writeLock().unlock();
			}
		}
	}

	// 读写锁读线程
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
			task.ReadMethod();
		}

		// 重写run方法实现
		@Override
		public void run() {
			// TODO Auto-generated method stub
			doTask();
		}
	}

	// 读写锁写线程
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
			task.WriteMethod();
		}

		// 重写run方法实现
		@Override
		public void run() {
			// TODO Auto-generated method stub
			doTask();
		}
	}

	// 读写锁读读共享
	@Test
	public void testReadReadLock() throws InterruptedException {
		// 创建一个对象
		Task task = new Task();
		// 两个线程注入同一个类的实例
		ThreadA thread1 = new ThreadA(task, "thread1");
		thread1.start();
		ThreadA thread2 = new ThreadA(task, "thread2");
		thread2.start();

		CountDownLatch countDownLatch = new CountDownLatch(1);
		countDownLatch.await();
	}
	
	// 测试读写锁写写互斥
	@Test
	public void testWriteWriteLock() throws InterruptedException {
		// 创建一个对象
		Task task = new Task();
		// 两个线程注入同一个类的实例
		ThreadB thread1 = new ThreadB(task, "thread1");
		thread1.start();
		ThreadB thread2 = new ThreadB(task, "thread2");
		thread2.start();

		CountDownLatch countDownLatch = new CountDownLatch(1);
		countDownLatch.await();
	}
	
	// 测试读写锁读写互斥
	@Test
	public void testReadWriteLock() throws InterruptedException {
		// 创建一个对象
		Task task = new Task();
		// 两个线程注入同一个类的实例
		ThreadA thread1 = new ThreadA(task, "thread1");
		thread1.start();
		Thread.sleep(1000);
		ThreadB thread2 = new ThreadB(task, "thread2");
		thread2.start();
		
		CountDownLatch countDownLatch = new CountDownLatch(1);
		countDownLatch.await();
	}
	
	// 测试读写锁写锁互斥
	@Test
	public void testWriteReadLock() throws InterruptedException {
		// 创建一个对象
		Task task = new Task();
		// 两个线程注入同一个类的实例
		ThreadB thread1 = new ThreadB(task, "thread1");
		thread1.start();
		Thread.sleep(1000);
		ThreadA thread2 = new ThreadA(task, "thread2");
		thread2.start();
		
		CountDownLatch countDownLatch = new CountDownLatch(1);
		countDownLatch.await();
	}
}
