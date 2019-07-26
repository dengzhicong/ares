package com.ares.message;

import java.lang.reflect.InvocationTargetException;

import com.google.protobuf.GeneratedMessage;

import io.netty.channel.Channel;

/**
 *
 * <b>接收消息的结构体.</b>
 * <p>
 * Description...
 * <p>
 * <b>Sample:</b>
 *
 * 
 * @version 1.0.0
 */
public class RMessage implements IMessage {
	/**
	 * 包头占用长度
	 */
	public static final short HEAD_SIZE = 8;

	/**
	 * 消息ID
	 */
	private int msgId = 0; // 消息ID

	/**
	 * 消息字节
	 */
	private byte[] byteData;

	/**
	 * 消息序号
	 */
	private int order = 0; // 消息序号

	/**
	 * 时间
	 */
	private long times = 0;

	public int getOrder() {
		return order;
	}

	public void setOrder(int order) {
		this.order = order;
	}

	public long getTimes() {
		return times;
	}

	public void setTimes(long times) {
		this.times = times;
	}

	public byte[] getByteData() {
		return byteData;
	}

	public void setByteData(byte[] byteData) {
		this.byteData = byteData;
	}

	public int getMsgId() {
		return msgId;
	}

	public void setMsgId(int msgId) {
		this.msgId = msgId;
	}

}
