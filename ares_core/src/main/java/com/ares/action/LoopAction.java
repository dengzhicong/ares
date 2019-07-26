package com.ares.action;

import com.ares.queue.IActionQueue;

/**
 * 循环多次执行Actor
 * 
 * @author admin
 *
 */
public abstract class LoopAction extends DelayAction {
	/**
	 * 计数
	 */
	private int count;
	/**
	 * 延迟时间
	 */
	private int delay;

	/**
	 * 创建
	 * 
	 * @param queue 所属队列
	 * @param delay 间隔多长时间执行一次（第一次执行会在首次延迟时到的时候执行）
	 * @param count ：执行次数
	 */
	public LoopAction(IActionQueue queue, int delay, int count) {
		super(queue, delay);
		this.count = count;
		this.delay = delay;
	}

	/**
	 * 
	 * @param queue     ：所属队列
	 * @param startTime ：开始时间
	 * @param delay     ： 间隔多长时间执行一次（第一次执行会在首次延迟时到的时候执行）
	 * @param count     ：执行次数
	 */
	public LoopAction(IActionQueue queue, long startTime, int delay, int count) {
		super(queue, startTime, delay);
		this.count = count;
		this.delay = delay;
	}

	@Override
	public void execute() {
		if (count <= 0) {
			return;
		}
		count--;
		loopExecute();
		this.execTime = System.currentTimeMillis() + this.delay;

		super.getActorQueue().enDelayQueue(this);
	}

	/**
	 * 循环执行接口
	 */
	public abstract void loopExecute();
}
