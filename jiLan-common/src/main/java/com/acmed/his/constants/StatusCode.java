package com.acmed.his.constants;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 类说明 
 * @author Eric
 * @version 创建时间：2017年3月28日 下午4:17:08 
 */
public enum StatusCode {
	// -1为通用失败（根据ApiResult.java中的构造方法注释而来）
	FAIL(-1, "common fail"),
	// 0为成功
	SUCCESS(0, "success"),
	EMPTY_SIGN (10001,"签名为空"),
	EMPTY_TIMESTAMP (10002,"时间戳为空"),
	EMPTY_APP_KEY (10003,"App Key为空"),
	ERROR_TIMESTAMP (10004,"时间出错误"),
	OUT_TIMESTAMP (10005,"时间戳超时"),
	ERROR_AUTH (10006,"认证失败"),
	
	ERROR_PARAM (10007,"参数错误"),
	ERROR_RETRY (10008,"重复发送"),
	
	EMPTY_PHONE (10009,"手机号为空"),
	RROR_PHONE (10010,"手机号不合法"),
	REPEAT_PHONE(10011,"手机号重复"),

	ERROR_PASSWD (10012,"密码错误"),

	ERROR_REFRESHTOKEN (10013,"refreshToken不存在或已失效"),
	ERROR_REPEATRECORD(10014, "记录重复"),
	ERROR_GETOPENIDECORD(10015,"获取openid异常"),
	ERROR_TOKEN (10016,"Token不存在或已失效"),
	ERROR_PREMISSION (10017,"尚未获取该权限"),
	ERROR_ORDER (10018,"订单不存在"),
	ERROR_IS_PAY (10019,"请不要重复支付"),
	ERROR_REFUND_ERR (10020,"退款失败"),
	ERROR_QINIU_ERR (10021,"七牛异常"),
	ERROR_ORDER_ERR (10022,"订单创建失败"),
	ERROR_PAY_INIT_ERR (10023,"支付初始化失败"),
	ERROR_COLLECTION(10024,"收款失败"),
	ERROR_IS_REFUND (10025,"请不要重复退款"),
	ERROR_DATA_EMPTY (10026,"没有查询到对应数据"),
	ERROR_IS_NOT_PAY (10027,"未支付"),
	ERROR_APPLY_NUM_OUT (10028,"医生挂号达到上限"),
	ERROR_FORBIDDEN(10029,"用户已经被禁用，请联系管理员"),
	ERROR_OPENID_NULL(10030,"还未绑定微信"),
	ERROR_OPENID_NEED_BIND(10031,"请绑定微信");

	private static final Logger LOGGER = LoggerFactory.getLogger(StatusCode.class);

	private int code;
	private String msg;

	StatusCode(int code, String msg){
		this.code = code;
		this.msg = msg;
	}

	public static int getCode(String define){
		try {
			return StatusCode.valueOf(define).code;
		} catch (IllegalArgumentException e) {
			LOGGER.error("undefined error code: {}", define);
			return FAIL.getErrorCode();
		}
	}

	public static String getMsg(String define){
		try {
			return StatusCode.valueOf(define).msg;
		} catch (IllegalArgumentException e) {
			LOGGER.error("undefined error code: {}", define);
			return FAIL.getErrorMsg();
		}

	}

	public static String getMsg(int code){
		for(StatusCode err : StatusCode.values()){
			if(err.code==code){
				return err.msg;
			}
		}
		return "errorCode not defined ";
	}

	public int getErrorCode(){
		return code;
	}

	public String getErrorMsg(){
		return msg;
	}
}
 