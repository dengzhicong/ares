package com.ares.handler;

import com.ares.message.PbMessage;
import com.google.protobuf.Message;

import io.netty.channel.Channel;

/**
 * 处理器接口
 */
public interface IHandler extends Runnable {

	/**
	 * 会话
	 *
	 * @return
	 */
	Channel getSession();

	/**
	 * 会话
	 *
	 * @param session
	 */
	void setSession(Channel session);

	/**
	 * 请求消息
	 *
	 * @return
	 */
	PbMessage getMessage();

	/**
	 * 消息
	 *
	 * @return
	 */
	void setMessage(PbMessage message, Class<? extends Message> clazz);

	/**
	 * 创建时间
	 *
	 * @param time
	 */
	void setCreateTime(long time);

	/**
	 * 创建时间
	 */
	long getCreateTime();

	/**
	 * http 参数
	 *
	 * @return
	 */
	Object getParameter();

	/**
	 * http 参数
	 *
	 * @return
	 */
	void setParameter(Object parameter);
}
