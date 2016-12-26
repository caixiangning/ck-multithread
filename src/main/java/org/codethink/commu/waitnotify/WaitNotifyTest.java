package org.codethink.commu.waitnotify;

import java.util.Calendar;
import java.util.concurrent.CountDownLatch;

import org.junit.Test;

/**
 * 
 * 等待/通知机制实现类
 * 
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
