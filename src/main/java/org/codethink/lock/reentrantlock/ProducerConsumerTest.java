package org.codethink.lock.reentrantlock;

import java.util.Calendar;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.junit.Test;

/**
 * 
 * 使用wait/signal实现生产者/消费者模式(一个生产者和一个消费者交替执行)
 * 
 * @author CaiXiangNing
 * @date 2016年12月28日
 * @email caixiangning@gmail.com
 */
public class ProducerConsumerTest {

	private Lock lock = new ReentrantLock();
	Condition condition = lock.newCondition();

	// 生产或者消费的产品
	private Object producerObject = null;

	// 生产者线程类
	class ProducerThread extends Thread {

		@Override
		public void run() {
			// TODO Auto-generated method stub
			try {
				lock.lock();
				while (true) {
					// 产品不为空则进入等待状态
					if (producerObject != null) {
						condition.await();
					}
					// 产品为空则生产产品并唤醒消费者线程
					else {
						Thread.sleep(1000);
						producerObject = new Object();
						System.out.println("生产者生产1个产品,生产时间："
								+ Calendar.getInstance().getTime());
						condition.signal();
					}
				}
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally {
				lock.unlock();
			}
		}
	}

	// 消费者线程类
	class ConsumerThread extends Thread {

		@Override
		public void run() {
			// TODO Auto-generated method stub
			try {
				lock.lock();
				while (true) {
					// 产品为空则消费者进入等待状态
					if (producerObject == null) {
						condition.await();
					}
					// 产品不为空则消费者消费产品
					else {
						producerObject = null;
						System.out.println("消费者消费1个产品,消费时间："
								+ Calendar.getInstance().getTime());
						condition.signal();
					}
				}
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally {
				lock.unlock();
			}
		}
	}

	@Test
	public void testProducerConsumer() {
		// 启动生产者线程
		ProducerThread producerThread = new ProducerThread();
		producerThread.start();

		// 启动消费者线程
		ConsumerThread consumerThread = new ConsumerThread();
		consumerThread.start();

		CountDownLatch countDownLatch = new CountDownLatch(2);
		try {
			countDownLatch.await();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
