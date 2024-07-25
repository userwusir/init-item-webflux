package com.init.item.common.exception;


import com.init.item.common.enums.Code;

/**
 * 接口限流异常类
 */
public class RateLimitException extends BaseException {
    public RateLimitException(Code code) {
        super(code);
    }

    public RateLimitException(Code code, String message) {
        super(code, message);
    }

    public RateLimitException(Code code, Throwable throwable) {
        super(code, throwable);
    }
}
