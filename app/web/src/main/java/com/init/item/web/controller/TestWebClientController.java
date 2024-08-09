package com.init.item.web.controller;

import com.init.item.common.dto.sapi.backend.Test;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.http.codec.multipart.FilePartEvent;
import org.springframework.http.codec.multipart.FormPartEvent;
import org.springframework.http.codec.multipart.PartEvent;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Tag(name = "测试后台sapi接口")
@RestController
@RequestMapping("/backend-sapi")
public class TestWebClientController {
    @GetMapping("/hello/{name}")
    @Operation(summary = "测试接口-get")
    public Mono<Test> hello(@PathVariable String name) {
        return WebClient.create().get()
                .uri("http://localhost:8003/backend" + "/users/{name}", name)
                .retrieve().bodyToMono(Test.class);
    }

    @PostMapping("/hello2")
    @Operation(summary = "测试接口-post")
    public Mono<Test> hello2(@RequestBody @Valid Test test) {
        return WebClient.create().post()
                .uri("http://localhost:8003/backend" + "/users")
                .body(Mono.just(test), Test.class)
                .retrieve().bodyToMono(Test.class);
    }

    @PostMapping(value = "/hello3")
    @Operation(summary = "测试接口-form-data")
    public Mono<String> hello3(@RequestPart("file") FilePart part,
                               @RequestPart("name") String name) {
        // https://docs.spring.io/spring-framework/reference/web/webflux-webclient/client-body.html
        // https://github.com/entzik/reactive-spring-boot-examples/blob/master/src/main/java/com/thekirschners/springbootsamples/reactiveupload/ReactiveUploadResource.java
        return WebClient.create().post()
                .uri("http://localhost:8003/backend" + "/form-data")
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .body(Flux.concat(FormPartEvent.create("name", name),
                        FilePartEvent.create("file", part.filename(), MediaType.MULTIPART_FORM_DATA, part.content())), PartEvent.class)
                .retrieve().bodyToMono(String.class);
    }
}
