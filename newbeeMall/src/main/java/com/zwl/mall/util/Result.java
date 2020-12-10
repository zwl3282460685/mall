package com.zwl.mall.util;

import java.io.Serializable;

/**
 * 后端数据格式的封装
 * @author zwl
 */
public class Result<T> implements Serializable {
    private static final long serialVersionUID = 1L;
    private int resultCode; //业务码，比如成功、失败、权限不足等code
    private String message; //返回信息，后端在进行业务处理后返回给前端一个提示信息，可自行定义
    private T data; //数据结果，泛型，可以是列表、单个对象、数字、布尔值等

    public Result() {
    }

    public Result(int resultCode, String message) {
        this.resultCode = resultCode;
        this.message = message;
    }

    public int getResultCode() {
        return resultCode;
    }

    public void setResultCode(int resultCode) {
        this.resultCode = resultCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "Result{" +
                "resultCode=" + resultCode +
                ", message='" + message + '\'' +
                ", data=" + data +
                '}';
    }
}
