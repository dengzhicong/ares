package com.ares.queue;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.ares.actor.Action;
import com.ares.thread.CommonExecutor;

/**
 * 队列管理器
 * 
 * @author admin
 *
 */
public class ExecutorQueueManager {
	private static ExecutorQueueManager manager = new ExecutorQueueManager();

	public static ExecutorQueueManager getInstance() {
		return manager;
	}

	private int cpuNum;

	/**
	 * CPU核
	 */
	public static final int CPU_NUM = Math.max(Runtime.getRuntime().availableProcessors() * 2, 8);

	/**
	 * action执行器
	 */
	public static final CommonExecutor commonExecutor;

	static {
		int cpuNum=Math.max(Runtime.getRuntime().availableProcessors() * 2, 8);
		
		int corePoolSize = cpuNum;
		int maxPoolSize = cpuNum + 32;
		int keepAliveTime = 5;
		int cacheSize = 1024;
		commonExecutor = new CommonExecutor(corePoolSize, maxPoolSize, keepAliveTime, cacheSize, "WORD_ACTOR_EXECUTOR");
	}

	/**
	 * 默认Action执行队列 1:执行系统排队action 2:执行系统延迟/定时action 3:执行系统循环执行action
	 */
	public static final ExecutorActionQueue defaultActionQueue = new ExecutorActionQueue(commonExecutor,
			"common_actor_queue");

	/** key:队列对应的唯一标识 */
	public Map<String, ExecutorActionQueue> pool = new ConcurrentHashMap<>();

	public void addPool(String key, ExecutorActionQueue queue) {
		pool.put(key, queue);
	}

	public Map<String, ExecutorActionQueue> queryPool() {
		return pool;
	}

	/**
	 * 执行actor
	 * 
	 * @param key   如果是玩家id则id%CPU_NUM的值，如果是“-1”则是使用公共队列执行
	 * @param actor
	 */
	public void exec(String key, Action actor) {
		if (!pool.containsKey(key)) {
			ExecutorActionQueue actorQueue = new ExecutorActionQueue(commonExecutor, "acotr_queue_" + key);
			pool.put(key, actorQueue);
		}

		pool.get(key).enqueue(actor);
	}

	// =========================================
	public int getCpuNum() {
		return cpuNum;
	}

	public void setCpuNum(int cpuNum) {
		this.cpuNum = cpuNum;
	}
}
