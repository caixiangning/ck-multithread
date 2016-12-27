package org.codethink.lock.reentrantlock;

import java.util.Calendar;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.junit.Test;

/**
 * 
 * ReentrantLock类实现等待/通知机制
 * 
 * 关键字synchronized与wait和notify方法相结合可以实现等待/通知模式，
 * 类ReentrantLock可以实现同样的功能，但需要借助于Condition对象。
 * 使用ReentranLock类实现等待/通知机制与wait/notify实现等待/通知机制不同的是，
 * 后者通知的线程是JVM随机的，而是用ReentranLock结合Condition类可以实现选择性通知
 * 
 * @author CaiXiangNing
 * @date 2016年12月28日
 * @email caixiangning@gmail.com
 */
public class WaitSignalTest {
	private Lock lock = new ReentrantLock();
	private Condition condition = lock.newCondition();

	// 等待线程类
	class WaitThread extends Thread {

		@Override
		public void run() {
			// TODO Auto-generated method stub
			try {
				// 使用await或者signal之前一定要获得锁，并且用完之后要释放
				lock.lock();
				System.out.println("等待线程开始等待时间: "
						+ Calendar.getInstance().getTime());
				condition.await();
				System.out.println("等待线程执行完毕时间: "
						+ Calendar.getInstance().getTime());
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally{
				// 使用unlock释放锁
				lock.unlock();
			}
		}
	}

	// 通知线程类
	class SignalThread extends Thread {

		@Override
		public void run() {
			// TODO Auto-generated method stub
			// 通知线程通知后延迟1秒继续执行
			try {
				// 使用await或者signal之前一定要获得锁，并且用完之后要释放
				lock.lock();
				Thread.sleep(1000);
				System.out.println("通知线程开始通知时间: "
						+ Calendar.getInstance().getTime());
				condition.signal();
				System.out.println("通知线程执行完毕时间: "
						+ Calendar.getInstance().getTime());
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally{
				// 使用unlock释放锁
				lock.unlock();
			}
		}
	}

	@Test
	public void testWaitSignal() {
		WaitThread waitThread = new WaitThread();
		waitThread.start();

		// 主线程休眠3秒后才执行通知线程，避免并发时先执行了通知线程
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		SignalThread signalThread = new SignalThread();
		signalThread.start();

		CountDownLatch countDownLatch = new CountDownLatch(2);
		try {
			countDownLatch.await();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
