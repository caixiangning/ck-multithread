package org.codethink.commu.waitnotify;

import java.util.Calendar;
import java.util.concurrent.CountDownLatch;

import org.junit.Test;

/**
 * 
 * 使用wait/notify实现生产者/消费者模式(一个生产者和一个消费者交替执行)
 * 
 * @author CaiXiangNing
 * @date 2016年12月27日
 * @email caixiangning@gmail.com
 */
public class ProducerConsumerTest {
	// 生产或者消费的产品
	private Object producerObject = null;

	// 生产者线程类
	class ProducerThread extends Thread {
		private Object lock;

		// 通过构造器注入锁定对象
		public ProducerThread(Object lock) {
			// TODO Auto-generated constructor stub
			this.lock = lock;
		}

		@Override
		public void run() {
			// TODO Auto-generated method stub
			synchronized (lock) {
				while(true){
					// 产品不为空则进入等待状态
					if (producerObject != null) {
						try {
							lock.wait();
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					} 
					// 产品为空则生产产品并唤醒消费者线程
					else {
						try {
							Thread.sleep(1000);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						producerObject = new Object();
						System.out.println("生产者生产1个产品,生产时间：" + Calendar.getInstance().getTime());
						lock.notify();
					}
				}
			}
		}
	}

	// 消费者线程类
	class ConsumerThread extends Thread {
		private Object lock;

		// 通过构造器注入锁定对象
		public ConsumerThread(Object lock) {
			// TODO Auto-generated constructor stub
			this.lock = lock;
		}

		@Override
		public void run() {
			// TODO Auto-generated method stub
			synchronized (lock) {
				while(true){
					// 产品为空则消费者进入等待状态
					if (producerObject == null) {
						try {
							lock.wait();
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					} 
					// 产品不为空则消费者消费产品
					else {
						producerObject = null;
						System.out.println("消费者消费1个产品,消费时间：" + Calendar.getInstance().getTime());
						lock.notify();
					}
				}
			}
		}
	}

	@Test
	public void testProducerConsumer() {
		// 对象锁
		Object lock = new Object();
		// 启动生产者线程
		ProducerThread producerThread = new ProducerThread(lock);
		producerThread.start();
		
		// 启动消费者线程
		ConsumerThread consumerThread = new ConsumerThread(lock);
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
