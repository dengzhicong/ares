package com.ares.handler;

import com.ares.log.LogUtil;
import com.ares.message.PbMessage;
import com.google.protobuf.AbstractMessage.Builder;
import com.google.protobuf.Message;

import io.netty.channel.Channel;

/**
 * Tcp 处理器
 */
public abstract class TcpHandler implements IHandler {
    protected Channel session;
    protected long createTime;
    private PbMessage message;
    protected long rid; // 角色ID
    protected Object pbMessage;
    private long sessionId;//服务器内部使用的sessionID -1： 为未确定玩家，或系统消息

    public PbMessage getMessage() {
        return message;
    }

    /**
     * 获取消息
     *
     * @return
     */
    @SuppressWarnings("unchecked")
	public <T extends Message> T getMsg() {
        return (T) pbMessage;
    }

    public void setMessage(PbMessage message, Class<? extends Message> clazz) {
        this.message = message;
        this.rid = message.getSenderId();
        this.sessionId = message.getSessionId();
        try {
            pbMessage = this.message.buildMessage(clazz);
        } catch (Exception e) {
            System.out.println(clazz.toString());
            e.printStackTrace();
            LogUtil.error("解析PB消息出错", e);
        }
    }

    /**
     * 发送回应消息
     *
     * @param msgId
     * @param messageBuilder
     */
    protected void sendMsg(int msgId, Builder<?> messageBuilder) {
//        MessageUtil.sendInnerMessage(msgId, messageBuilder, getSession(), message.getSessionId(), this.rid);
    }

    /**
     * 返回通用错误消息
     */

//    protected void sendErrorMsg(int errorCode, Action action) {
//        MessageUtil.sendErrorMsg(this.rid, errorCode, this.session, action, this.sessionId);
//    }

//+++++++++++++++++++++++++++++++++++++++++++

    public long getRid() {
        return rid;
    }

    public void setRid(long rid) {
        this.rid = rid;
    }

    public Channel getSession() {
        return session;
    }

    public void setSession(Channel session) {
        this.session = session;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    @Override
    public Object getParameter() {
        return null;
    }

    @Override
    public void setParameter(Object parameter) {

    }
}
