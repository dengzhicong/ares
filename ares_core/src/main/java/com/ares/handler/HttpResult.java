package com.ares.handler;

/**
 * 管理后台操作时返回的结果
 */
public class HttpResult {
	public static final int STATE_SUCCESS = 200;
	public static final int STATE_ERROR = 404;

	int code;
	Object content;

	public HttpResult() {

	}

	public HttpResult(int code, Object content) {
		this.code = code;
		this.content = content;
	}

	public void setHttpResult(int code, Object content) {
		this.code = code;
		this.content = content;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public Object getContent() {
		return content;
	}

	public void setContent(Object content) {
		this.content = content;
	}

}
