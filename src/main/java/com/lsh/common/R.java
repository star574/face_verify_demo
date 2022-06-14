package com.lsh.common;

/**
 * @ClassName: R
 * @Description:
 * @Date: 2022/6/14 11:49
 * @Author: shihengluo574@gmail.com
 * @Version: 1.0
 */
public class R<T> {
	private int code;
	private String msg;
	private T data;

	public static <T> R<T> success() {
		return new R<T>().setCode(200).setMsg("操作成功");
	}

	public static <T> R<T> error() {
		return new R<T>().setCode(500).setMsg("操作失败");
	}

	public static <T> R<T> success(T data) {
		return (R<T>) success().setData(data);
	}

	public static <T> R<T> error(String msg) {
		return (R<T>) error().setMsg(msg);
	}

	public int getCode() {
		return code;
	}

	public R<T> setCode(int code) {
		this.code = code;
		return this;
	}

	public String getMsg() {
		return msg;
	}

	public R<T> setMsg(String msg) {
		this.msg = msg;
		return this;
	}

	public T getData() {
		return data;
	}

	public R<T> setData(T data) {
		this.data = data;
		return this;
	}
}
