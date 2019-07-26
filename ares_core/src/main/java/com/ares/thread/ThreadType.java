package com.ares.thread;

public enum ThreadType {
	/** 系统消息线程 */
	SYSTEM,
	/** 玩家使用的线程 */
	USER,
	/** 有数据竞争的线程 */
	SYNC,
	/** 耗时线程 */
	IO;
}
