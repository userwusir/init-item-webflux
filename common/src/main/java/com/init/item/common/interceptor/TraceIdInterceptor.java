package com.init.item.common.interceptor;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;
import reactor.util.annotation.NonNull;

import java.util.UUID;

import static com.init.item.common.consts.Const.TRACE_ID_KEY;


@Component
@Slf4j
public class TraceIdInterceptor implements WebFilter {
    @Override
    public @NonNull Mono<Void> filter(@NonNull ServerWebExchange exchange, @NonNull WebFilterChain chain) {
        return Mono.deferContextual(ctx -> {
            addTraceId(exchange);

            return chain.filter(exchange)
                    .doFinally(signalType -> {
                        MDC.clear();
                    });
        });
    }

    private void addTraceId(ServerWebExchange exchange) {
        String traceId = exchange.getRequest().getHeaders().getFirst(TRACE_ID_KEY);
        if (traceId != null && !traceId.isBlank()) {
            MDC.put(TRACE_ID_KEY, traceId);
        } else {
            traceId = UUID.randomUUID().toString();
            MDC.put(TRACE_ID_KEY, traceId);
        }
        exchange.getAttributes().put(TRACE_ID_KEY, traceId);
    }
}
