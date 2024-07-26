package com.init.item.common.filter;

import com.init.item.common.filter.capture.BodyCaptureExchange;
import com.init.item.common.filter.capture.BodyCaptureRequest;
import com.init.item.common.filter.capture.BodyCaptureResponse;
import com.init.item.common.properties.IgnoreLoggingProperties;
import com.init.item.util.convert.ObjectMappers;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;
import reactor.util.annotation.NonNull;

import java.util.List;
import java.util.Map;

/**
 * 所有handler、controller出入参日志记录
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class LogFilter implements WebFilter {
    private final IgnoreLoggingProperties ignorePattern;
    private final static int MAX_LENGTH = 5000;

    @Override
    public @NonNull Mono<Void> filter(@NonNull ServerWebExchange exchange, @NonNull WebFilterChain chain) {
        if (isIgnored(exchange)) {
            return chain.filter(exchange);
        }

        // 装饰器模式，捕获Exchange来获取请求和响应的body
        BodyCaptureExchange bodyCaptureExchange = new BodyCaptureExchange(exchange);
        return chain.filter(bodyCaptureExchange)
                .doOnEach(signal -> {
                    if (signal.isOnComplete() || signal.isOnError()) {
                        log(bodyCaptureExchange);
                    }
                });
    }

    private boolean isIgnored(ServerWebExchange serverWebExchange) {
        String path = serverWebExchange.getRequest().getPath().toString();
        AntPathMatcher antPathMatcher = new AntPathMatcher();
        return ignorePattern.getIgnorePatterns().stream().anyMatch(pattern -> antPathMatcher.match(pattern, path));
    }

    private void log(final BodyCaptureExchange bodyCaptureExchange) {
        BodyCaptureRequest bodyCaptureRequest = bodyCaptureExchange.getRequest();
        BodyCaptureResponse bodyCaptureResponse = bodyCaptureExchange.getResponse();
        String requestStr = ObjectMappers.writeAsJsonStr(bodyCaptureRequest.getQueryParams()).orElse("{}");
        List<Map.Entry<String, List<String>>> token = bodyCaptureRequest.getHeaders().entrySet()
                .stream().filter(stringListEntry -> stringListEntry.getKey().equals("token")).toList();
        String resp = bodyCaptureResponse.getFullBody().length() > MAX_LENGTH ? "length: " + bodyCaptureResponse.getFullBody().length() : bodyCaptureResponse.getFullBody();
        log.info("params: {}，body: {}，form-urlencoded: {}，form-data: {}，method: {}，path: {}，token: {}，response: {}",
                requestStr,
                bodyCaptureRequest.getFullBody(),
                bodyCaptureExchange.getFullFormUrlencoded(),
                bodyCaptureExchange.getFullFormData(),
                bodyCaptureRequest.getMethod(),
                bodyCaptureRequest.getPath(),
                token,
                resp);
    }

}
