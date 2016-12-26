package org.codethink.commu.waitnotify;

import java.util.Calendar;
import java.util.concurrent.CountDownLatch;

import org.junit.Test;

/**
 * 
 * 等待/通知机制实现类
 * Java的等待/通知机制是通过wait/notify方法实现的。
 * wait方法和notify方法都是Object的方法，在调用之前都需要获得对象级别的对象锁。
 * 也就是说这两个方法都必须在同步方法或者同步代码块中调用。wait使线程停止运行，而notify使停止的线程继续运行。
 * 在执行wait方法之后，当前线程立即释放对象锁。而执行notify的线程不会马上释放该对象锁，
 * 要等到执行notify方法的线程将程序执行完，也就是退出同步代码块后，当前线程才会释放锁。
 * 
 * @author CaiXiangNing
 * @date 2016年12月26日
 * @email caixiangning@gmail.com
 */
public class WaitNotifyTest {
	// 等待线程类
	class WaitThread extends Thread{
		private Object lock;
		// 通过构造器注入锁定对象
		public WaitThread(Object lock) {
			// TODO Auto-generated constructor stub
			this.lock = lock;
		}
		
		@Override
		public void run() {
			// TODO Auto-generated method stub
			synchronized (lock) {
				System.out.println("等待线程开始等待时间: " + Calendar.getInstance().getTime());
				try {
					lock.wait();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				System.out.println("等待线程执行完毕时间: " + Calendar.getInstance().getTime());
			}
		}
	}
	
	// 通知线程类
	class NotifyThread extends Thread{
		private Object lock;
		// 通过构造器注入锁定对象
		public NotifyThread(Object lock) {
			// TODO Auto-generated constructor stub
			this.lock = lock;
		}
		
		@Override
		public void run() {
			// TODO Auto-generated method stub
			synchronized (lock) {
				System.out.println("通知线程开始通知时间: " + Calendar.getInstance().getTime());
				lock.notify();
				// 通知线程通知后延迟1秒继续执行
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				System.out.println("通知线程执行完毕时间: " + Calendar.getInstance().getTime());
			}
		}
	}
	
	@Test
	public void testWaitNotify(){
		Object lock = new Object();
		WaitThread waitThread = new WaitThread(lock);
		waitThread.start();
		
		// 主线程休眠3秒后才执行通知线程，避免并发时先执行了通知线程
		try {
			Thread.sleep(3000);
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		NotifyThread notifyThread = new NotifyThread(lock);
		notifyThread.start();
		
		CountDownLatch countDownLatch = new CountDownLatch(2);
		try {
			countDownLatch.await();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
