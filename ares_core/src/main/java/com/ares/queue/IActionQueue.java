package com.ares.queue;

import java.util.Queue;

import com.ares.action.Action;
import com.ares.action.DelayAction;

/**
 * action 队列接口
 * 
 * @author Noah
 *
 */
public interface IActionQueue {

	/**
	 * 添加延时执行任务
	 * 
	 * @param delayAction
	 */
	public void enDelayQueue(DelayAction delayAction);

	/**
	 * 清空队列
	 */
	public void stop();

	/**
	 * 获取队列
	 * 
	 * @return
	 */
	public Queue<Action> getActionQueue();

	/**
	 * 入队
	 * 
	 * @param cmd
	 */
	public void enqueue(Action cmd);

	/**
	 * 出队
	 * 
	 * @param cmd
	 */
	public void dequeue(Action cmd);

	/**
	 * 是否已经停止
	 * 
	 * @return
	 */
	public boolean isStop();

}
