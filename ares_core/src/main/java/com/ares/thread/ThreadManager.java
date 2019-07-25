package com.ares.thread;

import com.ares.actor.Actor;
import com.ares.queue.ExecutorActorQueue;

/**
 * 线程池管理器
 */
public class ThreadManager {

	/**
	 * CPU核
	 */
	public static final int CPU_NUM = Math.max(Runtime.getRuntime().availableProcessors() * 2, 8);

	/**
	 * action执行器
	 */
	public static final CommonExecutor commonExecutor;

	static {
		int corePoolSize = CPU_NUM;
		int maxPoolSize = CPU_NUM + 32;
		int keepAliveTime = 5;
		int cacheSize = 64;
		commonExecutor = new CommonExecutor(corePoolSize, maxPoolSize, keepAliveTime, cacheSize, "WORD_ACTOR_EXECUTOR");
	}

	/**
	 * 默认Action执行队列 1:执行系统排队action 2:执行系统延迟/定时action 3:执行系统循环执行action
	 */
	public static final ExecutorActorQueue<Actor> defaultActionQueue = new ExecutorActorQueue<>(commonExecutor,
			"defaultActionQueue");

}
