package com.init.item.backend.api;

import com.init.item.common.dto.sapi.backend.Test;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.http.codec.multipart.Part;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.stream.Collectors;

/**
 * 后台 server-api 实现
 */
@RestController
public class TestApiImpl implements TestApi {
    @Override
    public Mono<Test> getUserBaseInfo(String name) {
        Test test = new Test();
        test.setName(name);
        test.setAge(24);
        return Mono.just(test);
    }

    @Override
    public Mono<Test> postUserBaseInfo(Test test) {
        return Mono.just(test);
    }

    @Override
    public Mono<String> postFormData(Flux<Part> parts) {
        return parts.ofType(FilePart.class).flatMap(filePart -> Mono.just(filePart.filename())).collect(Collectors.joining(", "));
    }
}
