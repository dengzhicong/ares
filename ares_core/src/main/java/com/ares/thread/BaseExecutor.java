package com.ares.thread;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import com.ares.action.Action;

/**
 * 基础线程构造器
 */
public class BaseExecutor<T extends Runnable> implements IExecutor {
	private ThreadPoolExecutor pool;

	/**
	 * 
	 * @param corePoolSize  最小线程数，包括空闲线程
	 * @param maxPoolSize   最大线程数
	 * @param keepAliveTime 当线程数大于核心时，终止多余的空闲线程等待新任务的最长时间
	 * @param cacheSize     执行队列大小
	 * @param prefix        线程池前缀名称
	 */
	public BaseExecutor(int corePoolSize, int maxPoolSize, int keepAliveTime, int cacheSize, String prefix) {

		TimeUnit unit = TimeUnit.MINUTES;
		/**
		 * 任务队列
		 */
		LinkedBlockingQueue<Runnable> workQueue = new LinkedBlockingQueue<Runnable>();
		/**
		 * 队例满到无法接受新任务时。直接抛弃
		 */
		RejectedExecutionHandler handler = new ThreadPoolExecutor.DiscardPolicy();

		if (prefix == null) {
			prefix = "";
		}
		ThreadFactory threadFactory = new Threads(prefix);
		pool = new ThreadPoolExecutor(corePoolSize, maxPoolSize, keepAliveTime, unit, workQueue, threadFactory,
				handler);
	}

	/**
	 * 停止所有线程
	 */
	public void stop() {
		if (!pool.isShutdown()) {
			pool.shutdown();
		}
	}

	/**
	 * 执行
	 */
	@Override
	public void execute(Action actor) {
		pool.execute(actor);
	}

	public ThreadPoolExecutor getPool() {
		return pool;
	}

	/**
	 * 
	 * 线程工厂 定义线程创建方式
	 * 
	 * @author Noah
	 *
	 */
	static class Threads implements ThreadFactory {
		/**
		 * 线程池编号
		 */
		static final AtomicInteger poolNumber = new AtomicInteger(1);
		/**
		 * 线程组
		 */
		final ThreadGroup group;
		/**
		 * 线程数编号
		 */
		final AtomicInteger threadNumber = new AtomicInteger(1);
		/**
		 * 线程组前缀
		 */
		final String namePrefix;

		/**
		 * 创建线程接口实现 线程非守护线程 线程优化级为：5
		 */
		public Thread newThread(Runnable runnable) {
			Thread thread = new Thread(group, runnable,
					(new StringBuilder()).append(namePrefix).append(threadNumber.getAndIncrement()).toString(), 0L);
			if (thread.isDaemon())
				thread.setDaemon(false);
			if (thread.getPriority() != 5)
				thread.setPriority(5);
			return thread;
		}

		/**
		 * 
		 * 构造方法
		 * 
		 * @param prefix ：线程组前缀
		 * 
		 */
		Threads(String prefix) {
			SecurityManager securitymanager = System.getSecurityManager();
			group = securitymanager == null ? Thread.currentThread().getThreadGroup()
					: securitymanager.getThreadGroup();
			namePrefix = (new StringBuilder()).append("pool-").append(poolNumber.getAndIncrement()).append("-")
					.append(prefix).append("-thread-").toString();
		}
	}

}
