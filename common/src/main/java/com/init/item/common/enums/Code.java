package com.init.item.common.enums;

import lombok.Getter;

/**
 * service code
 */
@Getter
public enum Code {
    /**
     * 返回状态码
     */
    SUCCESS(200, "success"),
    BAD_REQUEST(400, "Bad Request"),
    TOKEN_AUTHORIZE_FAIL(401, "Unauthorized"),
    REFRESH_TOKEN_FAIL(403, "Refresh Token Fail"),
    NOT_FOUND(404, "Not Found"),
    OMP_TOKEN_AUTHORIZE_FAIL(411, "OMP Unauthorized"),
    TOKEN_SOURCE_FAIL(416, "Token Source Error"),
    VALIDATE_FAIL(417, "参数校验失败"),
    INNER_SERVICE_FAIL(500, "Internal Server Error"),
    REMOTE_SERVICE_FAIL(503, "Remote Service Unavailable"),
    RATE_LIMIT(601, "服务器繁忙，请稍后再试");


    public final int code;
    public final String msg;

    Code(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}
