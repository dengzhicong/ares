package com.ares.queue;

import java.util.LinkedList;
import java.util.Queue;

import com.ares.actor.Actor;
import com.ares.actor.DelayActor;
import com.ares.log.LogUtil;
import com.ares.thread.IExecutor;

/**
 * 可执行的ACTION队列
 * 
 * @author admin
 *
 * @param <T>
 */
public class ExecutorActorQueue<T extends Runnable> implements IActorQueue {

	/** 任务队列实体 */
	private Queue<Actor> queue;
	/** 执行器 */
	protected IExecutor executor;
	/** 队列名称 */
	protected final String queueName;
	/** 清空队列标记 */
	protected volatile boolean clearQueueFlag = false;

	/**
	 * @param executor         :执行器
	 * @param queueName：队列前缀名称
	 */
	public ExecutorActorQueue(IExecutor executor, String queueName) {
		this.executor = executor;
		this.queue = new LinkedList<Actor>();
		this.queueName = queueName;
	}

	@Override
	public void enDelayQueue(DelayActor delayAction) {
		executor.executeDelayAction(delayAction);
	}

	@Override
	public void stop() {
		synchronized (queue) {
			clearQueueFlag = true;
		}
	}

	@Override
	public Queue<Actor> getActionQueue() {
		return queue;
	}

	@Override
	public void enqueue(Actor actor) {
		if (clearQueueFlag == true) {
			return;
		}
		boolean canExec = false;
		synchronized (queue) {
			queue.add(actor);
			if (queue.size() == 1) {
				canExec = true;
			} else if (queue.size() > 30) {
				LogUtil.error("线程名：{} ,队列名：{} ,当前队列数：{}", Thread.currentThread().getName(), queueName, queue.size());
			}
		}
		if (canExec) {
			executor.execute(actor);
		}

	}

	@Override
	public void dequeue(Actor actor) {
		Actor nextCmdTask = null;
		synchronized (queue) {
			if (queue.size() == 0) {
				LogUtil.error(queueName + "queue.size() is 0.");
			}
			Actor temp = queue.remove();
			if (temp != actor) {
				LogUtil.error(queueName + "queue error. temp " + temp.toString() + ", cmd : " + actor.toString());
			}
			if (queue.size() != 0) {
				nextCmdTask = queue.peek();
			}
		}

		if (clearQueueFlag) {
			this.queue.clear();
			return;
		}
		if (nextCmdTask != null) {
			executor.execute(nextCmdTask);
		}
	}

	@Override
	public boolean isStop() {
		return false;
	}

}
