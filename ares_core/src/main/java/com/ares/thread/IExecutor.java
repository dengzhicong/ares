package com.ares.thread;

import com.ares.action.Action;
import com.ares.action.DelayAction;

/**
 * 执行器接口定义
 * 
 * @author Noah
 *
 * @param <T>
 */
public interface IExecutor {

	/**
	 * 执行任务
	 * 
	 * @param actor
	 */
	public void execute(Action actor);

	/**
	 * 停止所有线程
	 */
	public void stop();

	/**
	 * 执行延迟/定时 action(带延迟执行的线程执行器接口)<br>
	 * <b>有些类不需要实现该方法，所以用default</b>
	 * 
	 * @param delayAction
	 */
	public default void executeDelayAction(DelayAction delayAction) {
	};
}
