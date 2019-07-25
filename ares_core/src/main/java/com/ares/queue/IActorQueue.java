package com.ares.queue;

import java.util.Queue;

import com.ares.actor.Actor;
import com.ares.actor.DelayActor;

/**
 * action 队列接口
 * 
 * @author Noah
 *
 */
public interface IActorQueue {

	/**
	 * 添加延时执行任务
	 * 
	 * @param delayAction
	 */
	public void enDelayQueue(DelayActor delayAction);

	/**
	 * 清空队列
	 */
	public void stop();

	/**
	 * 获取队列
	 * 
	 * @return
	 */
	public Queue<Actor> getActionQueue();

	/**
	 * 入队
	 * 
	 * @param cmd
	 */
	public void enqueue(Actor cmd);

	/**
	 * 出队
	 * 
	 * @param cmd
	 */
	public void dequeue(Actor cmd);

	/**
	 * 是否已经停止
	 * 
	 * @return
	 */
	public boolean isStop();

}
