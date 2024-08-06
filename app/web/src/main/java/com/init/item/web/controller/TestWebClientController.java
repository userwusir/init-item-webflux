package com.init.item.web.controller;

import com.init.item.common.dto.sapi.backend.Test;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpEntity;
import org.springframework.http.MediaType;
import org.springframework.http.client.MultipartBodyBuilder;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.http.codec.multipart.Part;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Tag(name = "测试后台sapi接口")
@RestController
@RequestMapping("/backend-sapi")
public class TestWebClientController {
    @GetMapping("/hello/{name}")
    @Operation(summary = "测试接口-get")
    public Mono<Test> hello4(@PathVariable String name) {
        return WebClient.create().get()
                .uri("http://localhost:8003/backend" + "/users/{name}", name)
                .retrieve().bodyToMono(Test.class);
    }

    @PostMapping("/hello2")
    @Operation(summary = "测试接口-post")
    public Mono<Test> hello5(@RequestBody @Valid Test test) {
        return WebClient.create().post()
                .uri("http://localhost:8003/backend" + "/users")
                .body(Mono.just(test), Test.class)
                .retrieve().bodyToMono(Test.class);
    }

    @PostMapping(value = "/hello3")
    @Operation(summary = "测试接口-form-data")
    public Mono<String> hello6(@RequestBody Flux<Part> parts) {
        // todo
        // https://docs.spring.io/spring-framework/reference/web/webflux-webclient/client-body.html
        return WebClient.create().post()
                .uri("http://localhost:8003/backend" + "/users/upload")
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .body(parts, Part.class)
                .retrieve().bodyToMono(String.class);
    }
}
