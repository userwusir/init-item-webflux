package com.init.item.common.dto;

import com.init.item.common.consts.Const;
import com.init.item.common.enums.Code;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.slf4j.MDC;

@Getter
@Setter
@AllArgsConstructor
public class Result<T> {
    /**
     * 状态码
     */
    private int code;
    /**
     * 响应信息
     */
    private String msg;
    /**
     * 响应数据
     */
    private Object data;
    /**
     * 响应时间
     */
    private String traceId;

    public Result() {
        this.traceId = MDC.get(Const.TRACE_ID_KEY);
    }

    public Result(Code code) {
        this.code = code.getCode();
        this.msg = code.getMsg();
        this.data = null;
    }

    public Result(int code, String msg) {
        this.code = code;
        this.msg = msg;
        this.data = null;
    }

    public Result(Code code, T data) {
        this.code = code.getCode();
        this.msg = code.getMsg();
        this.data = data;
    }

    public static <T> Result<T> success() {
        Result<T> result = new Result<>();
        result.setCode(Code.SUCCESS.getCode());
        result.setMsg(Code.SUCCESS.getMsg());
        return result;
    }

    public static <T> Result<T> success(T data) {
        Result<T> result = new Result<>();
        result.setCode(Code.SUCCESS.getCode());
        result.setMsg(Code.SUCCESS.getMsg());
        result.setData(data);
        return result;
    }

    public static <T> Result<T> success(int code, String msg, T data) {
        Result<T> result = new Result<>();
        result.setCode(code);
        result.setMsg(msg);
        result.setData(data);
        return result;
    }

    public static <T> Result<T> fail(Code code) {
        Result<T> result = new Result<>();
        result.setCode(code.getCode());
        result.setMsg(code.getMsg());
        result.setData(null);
        return result;
    }

    public static <T> Result<T> fail(Code code, T data) {
        Result<T> result = new Result<>();
        result.setCode(code.getCode());
        result.setMsg(code.getMsg());
        result.setData(data);
        return result;
    }

    public static <T> Result<T> fail(int code, String msg) {
        Result<T> result = new Result<>();
        result.setCode(code);
        result.setMsg(msg);
        return result;
    }

    public static <T> Result<T> fail(int code, String msg, T data) {
        Result<T> result = new Result<>();
        result.setCode(code);
        result.setMsg(msg);
        result.setData(data);
        return result;
    }
}
