package com.ares.message;

public interface IMessage {

	/**
	 * 协议号
	 * 
	 * @return
	 */
	public int getMsgId();

	/**
	 * 字节内容
	 * 
	 * @return
	 */
	public byte[] getByteData();
}
