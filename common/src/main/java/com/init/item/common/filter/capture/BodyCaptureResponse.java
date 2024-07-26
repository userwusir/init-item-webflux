package com.init.item.common.filter.capture;

import jakarta.annotation.Nonnull;
import org.reactivestreams.Publisher;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.http.server.reactive.ServerHttpResponseDecorator;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

/**
 * 捕获响应对象，获取响应体内容
 */
public class BodyCaptureResponse extends ServerHttpResponseDecorator {

    private final StringBuilder body = new StringBuilder();

    public BodyCaptureResponse(ServerHttpResponse delegate) {
        super(delegate);
    }


    @Nonnull
    @Override
    public Mono<Void> writeWith(@Nonnull Publisher<? extends DataBuffer> body) {
        return super.writeWith(Flux.from(body).doOnNext(this::capture));
    }

    private void capture(DataBuffer buffer) {
        // 读取buffer中的字节
        ByteBuffer byteBuffer = ByteBuffer.allocate(buffer.readableByteCount());
        buffer.toByteBuffer(byteBuffer);
        this.body.append(StandardCharsets.UTF_8.decode(byteBuffer));
    }

    public String getFullBody() {
        return this.body.toString();
    }

}