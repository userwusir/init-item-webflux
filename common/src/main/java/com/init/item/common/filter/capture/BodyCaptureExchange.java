package com.init.item.common.filter.capture;

import jakarta.annotation.Nonnull;
import org.springframework.http.codec.multipart.Part;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.ServerWebExchangeDecorator;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;

/**
 * 捕获请求Exchange
 */
public class BodyCaptureExchange extends ServerWebExchangeDecorator {

    private final BodyCaptureRequest bodyCaptureRequest;
    private final BodyCaptureResponse bodyCaptureResponse;
    private final MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
    private final MultiValueMap<String, Part> multipartData = new LinkedMultiValueMap<>();

    public BodyCaptureExchange(ServerWebExchange exchange) {
        super(exchange);
        this.bodyCaptureRequest = new BodyCaptureRequest(exchange.getRequest());
        this.bodyCaptureResponse = new BodyCaptureResponse(exchange.getResponse());
    }

    @Nonnull
    @Override
    public BodyCaptureRequest getRequest() {
        return bodyCaptureRequest;
    }

    @Nonnull
    @Override
    public BodyCaptureResponse getResponse() {
        return bodyCaptureResponse;
    }

    @Nonnull
    @Override
    public Mono<MultiValueMap<String, String>> getFormData() {
        return super.getFormData().doOnNext(this::capture);
    }

    @Nonnull
    @Override
    public Mono<MultiValueMap<String, Part>> getMultipartData() {
        return super.getMultipartData().doOnNext(this::captureMultipart);
    }

    /**
     * application/x-www-form-urlencoded
     */
    public Map<String, List<String>> getFullFormUrlencoded() {
        return formData;
    }

    /**
     * multipart/form-data
     */
    public Map<String, List<Part>> getFullFormData() {
        return multipartData;
    }

    private void capture(MultiValueMap<String, String> multiValueMap) {
        formData.putAll(multiValueMap);
    }
    private void captureMultipart(MultiValueMap<String, Part> stringPartMultiValueMap) {
        multipartData.putAll(stringPartMultiValueMap);
    }

}