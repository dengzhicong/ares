package com.ares.message;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.google.protobuf.Message;

/**
 * 服务器和客户端,服务器和服务器直接数据传输的对象
 **/
public class PbMessage implements IMessage {
//	private short				len;							// 数据包长度
//	private int					mid;							// 协议号
//	private long				sessionId;						// sessionID(为随机数或玩家id，客户端没有该字段)
//	private short				order;							// (客户端没有该字段)
//	private short				encrypt;						// 是否加密(0:加密，1：未加密；inner没有加密字段)
//	private byte[]				keys;							// AES公钥（此处用了ECC加密，客户端需要用公钥解密）
//	private byte[]				bytes;							// 数据体(如果是加密，那么byte数组已经被AES加密了,需用AES公钥解密)

	/**
	 * 缓存proto对象,避免每次反射创建proto对象
	 */
	static Map<String, Message> map = new ConcurrentHashMap<>();

	/**
	 * 包头占用长度(len(short:2)+mid(int:4)+sessionId(long:8)+order(int:4)=18->需要排除长度字段的字节)
	 */
	public static final short INNER_HEAD_SIZE = 16;
	/**
	 * 包头占用长度{len(short:2)+mid(int:4)+encrypt(short:2)+keyLen(short:2)=10->需要排除长度字段的字节}<br>
	 * keyLen：是公钥字节数组长度
	 */
	public static final short CLIENT_HEAD_SIZE = 8;

	/**
	 * 消息ID
	 */
	private int msgId = 0; // 消息ID

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
	 * 时间
	 */
	private long times = 0;

	/**
	 * -1：服务器之间通信 0：客户端与服务器之间通信
	 */
	private int order = 0;

	/**
	 * 将反射得到的Message对象缓存起来，避免每次都反射创建Message对象
	 * 
	 * @param clazz
	 * @return
	 * @throws Exception
	 */
	public Message buildMessage(Class<? extends Message> clazz) throws Exception {
		// Method parseFromMethod = clazz.getDeclaredMethod("parseFrom", new
		// Class<?>[]{byte[].class});
		// Object object = parseFromMethod.invoke(null, byteData);

		Message message = map.get(clazz.getName());
		if (message == null) {
			// newBuilder 为静态变量，即使没有 message 的具体实例也可以 invoke
			Method method = clazz.getMethod("newBuilder");
			Object obj = method.invoke(null, new Object[] {});
			Message.Builder msgBuilder = (Message.Builder) obj;
			// 该方法可以忽略某些require字段，避免报错
			message = msgBuilder.buildPartial();

			// 使用全类名作为key值
			map.put(clazz.getName(), message);
		}

		return message.getParserForType().parseFrom(byteData);
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

	public long getTimes() {
		return times;
	}

	public void setTimes(long times) {
		this.times = times;
	}

	public int getOrder() {
		return order;
	}

	public void setOrder(int order) {
		this.order = order;
	}

	@Override
	public String toString() {
		return "PbMessage{" + "msgId=" + msgId + ", byteData=" + Arrays.toString(byteData) + ", senderId=" + senderId
				+ ", sessionId=" + sessionId + ", times=" + times + ", order=" + order + '}';
	}
}
