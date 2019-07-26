package com.ares.thread;

import com.ares.actor.Action;
import com.ares.actor.DelayAction;

/**
 * 通用执行actor队列的线程池
 */
public class CommonExecutor extends BaseExecutor<Action> implements IExecutor {
	/**
	 * 延迟/定时 检测线程
	 */
	private DelayCheckThread delayCheckThread;

	public CommonExecutor(int corePoolSize, int maxPoolSize, int keepAliveTime, int cacheSize, String prefix) {
		super(corePoolSize, maxPoolSize, keepAliveTime, cacheSize, prefix);
		this.delayCheckThread = new DelayCheckThread(prefix);
		this.delayCheckThread.start();
	}

	/**
	 * 执行延时/定时actor
	 * 
	 * @param delayActor
	 */
	public void executeDelayAction(DelayAction delayActor) {
		delayCheckThread.addActor(delayActor);
	}
}
