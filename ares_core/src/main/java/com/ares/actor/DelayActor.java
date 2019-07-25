package com.ares.actor;

import com.ares.queue.IActorQueue;

/**
 * 延迟执行actor
 * 
 * @author admin
 *
 */
public abstract class DelayActor extends Actor {
	/**
	 * 执行时间点
	 */
	protected long execTime;
	/**
	 * 延迟时间
	 */
	protected int delay;

	public DelayActor(IActorQueue queue, int delay) {
		super(queue);
		this.execTime = System.currentTimeMillis() + delay;
		this.delay = delay;
	}

	/**
	 * 
	 * @param queue
	 * @param startTime : 开始时间
	 * @param delay     : 推迟时间（单位：毫秒）
	 */
	public DelayActor(IActorQueue queue, long startTime, int delay) {
		super(queue);
		execTime = startTime + delay;
	}

	/**
	 * 判断是否时间到。可以执行。
	 * 
	 * @param curTime
	 * @return ：true： 可以执行
	 */
	public boolean canExec(long curTime) {
		if (curTime >= execTime) {
			createTime = curTime;
			return true;
		}
		return false;
	}
}
