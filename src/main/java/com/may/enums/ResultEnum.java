package com.may.enums;

/**
 * ClassName: ResultEnum 
 * CreateTime 2017年12月20日 下午1:57:55 
 * author : liqinchao
 * Description: 返回的json数据代码
 * 
 */
public enum ResultEnum {
	ERROR(0, "操作失败"),SUCCESS(1, "成功"), UN_AUTH(2, "没有权限")
	, ERR_PARAM(3, "参数错误"), EXIST(7, "数据已存在"), EXIST_NOT(8, "数据不存在")
	,TIMEOUT(9, "登录超时") , LOG_OTHER(10, "账号已在别处登录，请重新登录")
	, SIGN_EXPIRE(11, "签名无效"), ERROR_WEIXIN(12, "非微信请求");
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
