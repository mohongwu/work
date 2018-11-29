package com.smec.appmanager.manager.SmecRetrofit;

/**
 * Created by xupeizuo on 2017/7/3.
 */

public class HttpResult<T> {

    private String code;
    private T data;
    private String message;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
