package com.ares.message;

import java.lang.reflect.Method;
import java.util.Arrays;

import com.google.protobuf.Message;

/**
 * 内部通信消息
 * 
 */
public class InnerMessage implements IMessage {

	public static final int HEAD_SIZE = 20;

	/**
	 * 协议号
	 */
	private int msgId;

	/**
	 * 消息字节
	 */
	private byte[] byteData;

	/**
	 * 消息发送者的ID -1：为系统消息
	 */
	private long senderId;

	/**
	 * 服务器内部使用的sessionID -1： 为未确定玩家，或系统消息
	 */
	private long sessionId;

	/**
	 * 客户端ip地址
	 */
	private String ip;

	public InnerMessage() {
		super();
	}

	/**
	 * 利用反射将消息反序列回来 1：此处我不建议使用，总觉得反射比较消耗性能
	 * 
	 * @param clazz
	 * @param bytes
	 * @return
	 * @throws Exception
	 */
	public Message buildMessage(Class<? extends Message> clazz) throws Exception {
		Method parseFromMethod = clazz.getDeclaredMethod("parseFrom", new Class<?>[] { byte[].class });
		Object object = parseFromMethod.invoke(null, byteData);
		return (Message) object;
	}

	public InnerMessage(int msgId, byte[] byteData, long senderId, long sessionId) {
		super();
		this.msgId = msgId;
		this.byteData = byteData;
		this.senderId = senderId;
	}

	@Override
	public int getMsgId() {
		return msgId;
	}

	public void setMsgId(int msgId) {
		this.msgId = msgId;
	}

	@Override
	public byte[] getByteData() {
		return byteData;
	}

	public void setByteData(byte[] byteData) {
		this.byteData = byteData;
	}

	public long getSenderId() {
		return senderId;
	}

	public void setSenderId(long senderId) {
		this.senderId = senderId;
	}

	public long getSessionId() {
		return sessionId;
	}

	public void setSessionId(long sessionId) {
		this.sessionId = sessionId;
	}

	@Override
	public String toString() {
		return "InnerMessage [msgId=" + msgId + ", byteData=" + Arrays.toString(byteData) + ", senderId=" + senderId
				+ ", sessionId=" + sessionId + "]";
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

}
