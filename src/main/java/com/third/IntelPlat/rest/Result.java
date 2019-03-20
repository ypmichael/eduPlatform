package com.third.IntelPlat.rest;

import com.google.gson.Gson;
import com.google.gson.internal.Primitives;

public class Result
{
	
	public static final int STATUS_SUCCESS = 1;
	public static final int STATUS_FAILED = 0;
	
	public <T> T getBody(Class<T> classOfT) {
		Gson json = new Gson();
	    return Primitives.wrap(classOfT).cast(json.fromJson((String)content, classOfT));
	}	
	
	private int status;//值为0或1,0表示失败；1表示成功
	
	private String info = "OK";//返回状态说明，status为0时，info返回错误原因，否则返回“OK”
	
	private String code = "";//返回状态说明,1000代表正确,详情参阅info状态表
	
	private Object content;

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getInfo() {
		return info;
	}

	public void setInfo(String info) {
		this.info = info;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public Object getContent() {
		return content;
	}

	public void setContent(Object content) {
		this.content = content;
	}
}
