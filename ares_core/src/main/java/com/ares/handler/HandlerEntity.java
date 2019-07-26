package com.ares.handler;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import com.ares.thread.ThreadType;
import com.google.protobuf.Message;

/**
 * 消息处理注解
 *
 * @author JiangZhiYong
 * @date 2017-03-30 QQ:359135103
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface HandlerEntity {
	/**
	 * TCP 消息id
	 */
	int mid() default 0;

	/**
	 * 
	 * @return 描述
	 */
	String desc() default "";

	/**
	 * 调用的线程
	 * 
	 * @return
	 */
	ThreadType thread() default ThreadType.SYSTEM;

	/**
	 * tcp 请求的消息类
	 * 
	 * @return
	 */
	Class<? extends Message> msg() default Message.class;

}
