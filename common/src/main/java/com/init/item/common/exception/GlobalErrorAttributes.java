package com.init.item.common.exception;

import com.init.item.common.consts.Const;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.reactive.error.DefaultErrorAttributes;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebExchangeBindException;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.server.ServerWebInputException;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import static com.init.item.common.enums.Code.BAD_REQUEST;
import static com.init.item.common.enums.Code.INNER_SERVICE_FAIL;

/**
 * 自定义异常处理返回
 */
@Component
public class GlobalErrorAttributes extends DefaultErrorAttributes {

    /**
     * 参数异常、业务异常、其他异常
     */
    private static final Class<?>[] classes = {WebExchangeBindException.class, BaseException.class, ServerWebInputException.class};

    /**
     * 异常处理器Map，新增异常处理直接put进去
     */
    private static final Map<Class<?>, Function<Object, Map<String, Object>>> handlers = new HashMap<>();
    private static final Logger log = LoggerFactory.getLogger(GlobalErrorAttributes.class);

    static {
        handlers.put(WebExchangeBindException.class, obj -> handleArgument((WebExchangeBindException) obj));
        handlers.put(ServerWebInputException.class, obj -> handleArgument((ServerWebInputException) obj));
        handlers.put(BaseException.class, obj -> handleBaseException((BaseException) obj));
    }

    @Override
    public Map<String, Object> getErrorAttributes(ServerRequest request, ErrorAttributeOptions options) {
        Throwable error = getError(request);
        Map<String, Object> errorAttributes = processIfInstanceOfAny(error, classes);
        errorAttributes.put("traceId", MDC.get(Const.TRACE_ID_KEY));
        errorAttributes.put("path", request.path());
        return errorAttributes;
    }

    public static Map<String, Object> processIfInstanceOfAny(Throwable error, Class<?>[] classes) {
        return Arrays.stream(classes)
                .filter(clazz -> clazz.isInstance(error))
                .findFirst()
                .map(clazz -> handleInstance(error, clazz))
                .orElseGet(() -> handleOther(error));
    }

    private static Map<String, Object> handleInstance(Throwable error, Class<?> clazz) {
        Function<Object, Map<String, Object>> handler = handlers.get(clazz);
        if (handler == null) {
            return handleOther(error);
        }
        return handler.apply(error);
    }

    /**
     * 处理参数异常
     */
    private static Map<String, Object> handleArgument(ServerWebInputException error) {
        Map<String, Object> errorAttributes = new HashMap<>();
        errorAttributes.put("code", BAD_REQUEST.code);
        errorAttributes.put("error", error.getReason());
        log.error("参数异常：", error);
        return errorAttributes;
    }

    /**
     * 处理参数异常
     */
    private static Map<String, Object> handleArgument(WebExchangeBindException error) {
        Map<String, Object> errorAttributes = new HashMap<>();
        errorAttributes.put("code", BAD_REQUEST.code);
        StringBuffer errorStr = new StringBuffer();
        error.getAllErrors().forEach(it -> {
            errorStr.append(it.getDefaultMessage()).append(";");
        });
        errorAttributes.put("error", errorStr);
        log.error("参数异常：", error);
        return errorAttributes;
    }

    /**
     * 处理业务异常
     */
    private static Map<String, Object> handleBaseException(BaseException error) {
        Map<String, Object> errorAttributes = new HashMap<>();
        errorAttributes.put("code", error.getCode());
        errorAttributes.put("error", error.getMessage());
        log.error("业务异常：", error);
        return errorAttributes;
    }

    /**
     * 处理其他异常，统一500
     */
    private static Map<String, Object> handleOther(Throwable error) {
        Map<String, Object> errorAttributes = new HashMap<>();
        errorAttributes.put("code", INNER_SERVICE_FAIL.code);
        errorAttributes.put("error", INNER_SERVICE_FAIL.msg);
        log.error("未知异常：", error);
        return errorAttributes;
    }
}
