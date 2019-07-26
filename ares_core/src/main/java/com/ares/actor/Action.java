package com.ares.actor;

import com.ares.log.LogUtil;
import com.ares.queue.IActionQueue;

/**
 * 运行的基本单位
 */
public abstract class Action implements Runnable {

	/**
	 * 队列线程池
	 */
	private IActionQueue queue;
	/**
	 * 创建时间
	 */
	protected long createTime;

	public Action(IActionQueue queue) {
		this.queue = queue;
		this.createTime = System.currentTimeMillis();
	}

	@Override
	public void run() {
		if (queue != null) {
			long start = System.currentTimeMillis();
			try {
				execute();
				long end = System.currentTimeMillis();
				long interval = end - start;
				long leftTime = end - createTime;
				if (interval >= 1000) {
					LogUtil.warn("execute action : {}, interval : {}, leftTime : {}, size: {} ", this.toString(),
							interval, leftTime, queue.getActionQueue().size());
				}
			} catch (Exception e) {
				LogUtil.error("run action execute exception. action : " + this.toString(), e);
			} finally {
				queue.dequeue(this);
			}
		}
	}

	public IActionQueue getActorQueue() {
		return queue;
	}

	/**
	 * 执行函数
	 */
	public abstract void execute();

}
