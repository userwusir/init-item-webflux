package com.init.item.web.handler;

import com.init.item.common.dto.Result;
import com.init.item.common.enums.Code;
import com.init.item.common.valid.CustomValidator;
import com.init.item.dao.entity.Test;
import com.init.item.test.service.TestService;
import com.init.item.test.service.req.TestReq;
import lombok.RequiredArgsConstructor;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.http.codec.multipart.FormFieldPart;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.springframework.web.server.ServerWebInputException;
import reactor.core.publisher.Mono;

import java.util.Objects;

@Component
@RequiredArgsConstructor
public class TestHandler {
    private final TestService testService;
    private final CustomValidator validator;

    public Mono<ServerResponse> test(ServerRequest request) {
        return ServerResponse.ok().body(Mono.just(Result.success("test success")), Result.class);
    }

    public Mono<ServerResponse> test2(ServerRequest request) {
        return request.bodyToMono(TestReq.class)
                .doOnNext(validator::validate)
                .flatMap(req -> ServerResponse.ok().body(Mono.just(Result.success(req.getName())), Result.class))
                .onErrorResume(ServerWebInputException.class, e -> ServerResponse.badRequest()
                        .body(Mono.just(Result.fail(Code.BAD_REQUEST, e.getReason())), Result.class));
    }

    public Mono<ServerResponse> test3(ServerRequest request) {
        return request.exchange().getMultipartData()
                .flatMap(multipartData -> {
                    FilePart filePart = (FilePart) multipartData.toSingleValueMap().get("file");
                    FormFieldPart fieldPart = (FormFieldPart) multipartData.toSingleValueMap().get("name");
                    return ServerResponse.ok().body(Mono.just(Result.success(Objects.requireNonNull(filePart.filename()) + " " + fieldPart.value())), Result.class);
                });
    }

    public Mono<ServerResponse> test4(ServerRequest request) {
        return ServerResponse.ok().body(testService.findById(), Test.class);
    }
}
