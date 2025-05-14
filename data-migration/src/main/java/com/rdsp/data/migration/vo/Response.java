package com.rdsp.data.migration.vo;

import lombok.Data;

import java.io.Serializable;


/**
 * 返回结果集封装
 **/
@Data
public class Response<T> implements Serializable {

	/**
	 * 序列化标识
	 */
	private static final long serialVersionUID = 4893280118017319089L;

	/**
	 * 成功码.
	 */
	public static final int SUCCESS_CODE = 200;

	/**
	 * 成功信息.
	 */
	public static final String SUCCESS_MESSAGE = "成功";

	/**
	 * 错误码.
	 */
	public static final int ERROR_CODE = 500;

	/**
	 * 错误信息.
	 */
	public static final String ERROR_MESSAGE = "失败";

	/**
	 * 编码.
	 */
	private int code;

	/**
	 * 信息.
	 */
	private String message;

	/**
	 * 结果数据
	 */
	private T result;


	Response() {
		this(SUCCESS_CODE, SUCCESS_MESSAGE);
	}


	Response(int code, String message) {
		this(code, message, null);
	}


	Response(int code, String message, T result) {
		super();
		this.code(code).message(message).result(result);
	}

	private Response<T> code(int code) {
		this.setCode(code);
		return this;
	}


	private Response<T> message(String message) {
		this.setMessage(message);
		return this;
	}

	public Response<T> result(T result) {
		this.setResult(result);
		return this;
	}




	public static <E> Response<E> wrap(int code, String message, E o) {
		return new Response<>(code, message, o);
	}

	public static <E> Response<E> wrap(int code, String message) {
		return wrap(code, message, null);
	}

	public static <E> Response<E> error() {
		return wrap(Response.ERROR_CODE, Response.ERROR_MESSAGE);
	}

	public static <E> Response<E> error(String message) {
		return wrap(Response.ERROR_CODE, message);
	}

	public static <E> Response<E> error(E o) {
		return wrap(Response.ERROR_CODE, Response.ERROR_MESSAGE,o);
	}

	public static <E> Response<E> error(E o, String message) {
		return wrap(Response.ERROR_CODE, message,o);
	}
	public static <E> Response<E> error(int code, String message) {
		return wrap(code, message);
	}

	public static <E> Response<E> ok() {
		return new Response<E>();
	}

	public static <E> Response<E> ok(E o) {
		return new Response<>(Response.SUCCESS_CODE, Response.SUCCESS_MESSAGE, o);
	}

	public static <E> Response<E> ok(E o, String message) {
		return new Response<>(Response.SUCCESS_CODE, message, o);
	}
}
