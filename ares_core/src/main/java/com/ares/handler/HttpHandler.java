package com.ares.handler;

import java.util.Map;

import com.ares.json.JSONUtil;

/**
 * http请求
 *
 * @author fanjiawei
 * @date 2017-03-31 QQ:359135103
 */
public abstract class HttpHandler {

	/**
	 * 执行请求命令的逻辑
	 * 
	 * @param params
	 * @return
	 */
	public abstract String doRequest(Map<String, String> params);

	/**
	 * 生成通用返回数据返回到web页面
	 * 
	 * @param statu       HttpResult中的状态码
	 * @param description
	 * @return
	 */
	protected String writeToWeb(int statu, Object description) {
		return JSONUtil.getJSONString(new HttpResult(statu, description));
	}

}
