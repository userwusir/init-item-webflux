package com.init.item.web.controller;

import com.init.item.common.dto.sapi.backend.Test;
import com.init.item.test.service.req.TestReq;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Tag(name = "测试接口")
@RestController
@RequestMapping("/web")
public class TestController {

    @GetMapping("/hello")
    @Operation(summary = "hello world")
    public Mono<String> hello() {
        return Mono.just("Hello World");
    }

    @PostMapping("/hello2")
    @Operation(summary = "测试接口-body")
    public Mono<String> hello2(@RequestBody @Valid TestReq testReq) {
        return Mono.just("Hello World2");
    }

    @PostMapping("/hello")
    @Operation(summary = "测试接口-form-body")
    public Mono<String> hello3(@RequestPart("file") FilePart part,
                               @RequestPart("name") String name) {
        return Mono.just(part.filename() + " " + name);
    }

    @GetMapping("/hello4/{name}")
    @Operation(summary = "测试接口-sapi")
    public Mono<Test> hello4(@PathVariable String name) {
        return WebClient.create().get()
                .uri("http://localhost:8003" + "/users/{name}", name)
                .retrieve().bodyToMono(Test.class);
    }
}
