package com.init.item.common.exception;


import com.init.item.common.enums.Code;

/**
 * 自定义业务异常，继承此类
 */
public abstract class BaseException extends RuntimeException{

    private final Code code;

    public Integer getCode() {
        return code.code;
    }

    public String getMsg() {
        return code.msg;
    }

    public Code getErrorCode() {
        return code;
    }

    public BaseException(Code code) {
        super(code.msg);
        this.code = code;
    }

    public BaseException(Code code, String message) {
        super(message);
        this.code = code;
    }

    public BaseException(Code code, Throwable throwable) {
        super(throwable);
        this.code = code;
    }
}
