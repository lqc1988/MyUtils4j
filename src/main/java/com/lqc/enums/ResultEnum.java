package com.lqc.enums;

/**
 * ClassName : ResultEnum
 * Author : liqinchao
 * CreateTime : 2019/3/18 18:11
 * Description : 通用响应结果枚举
 */
public enum ResultEnum {
	/**
	 *
	 */
	ERR(100, "操作失败，请联系客服"),
	ERR_PARAM(101, "参数错误"),
	EXIST(102, "数据已存在"),
	EXIST_NOT(103, "数据不存在"),
	SIGN_INVALID(104, "签名无效"),
	SUCCESS(200, "成功"),
	UN_AUTH(400, "没有权限"),
	LOGIN_TIMEOUT(401, "登录超时") ,
	LOGIN_OTHER(402, "账号已在别处登录，请重新登录"),
	NOT_FOUND(404, "资源未找到"),
	NOT_WX(405, "非微信请求");
	int value;

	String display;

	ResultEnum(int value, String display) {
		this.value = value;
		this.display = display;
	}

	public static ResultEnum valueOf(int value) {
		for (ResultEnum c : ResultEnum.values()) {
			if (c.getValue() == value) {
				return c;
			}
		}
		return null;
	}

	public int getValue() {
		return value;
	}

	public String getDisplay() {
		return display;
	}
}
