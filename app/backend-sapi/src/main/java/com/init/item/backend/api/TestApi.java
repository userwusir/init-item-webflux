package com.init.item.backend.api;

import com.init.item.common.dto.sapi.backend.Test;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@Tag(name = "测试后台sapi")
public interface TestApi {

    @GetMapping("/users/{name}")
    @Operation(summary = "测试后台sapi-get")
    Mono<Test> getUserBaseInfo(@PathVariable("name") String name);

    @PostMapping("/users")
    @Operation(summary = "测试后台sapi-post")
    Mono<Test> postUserBaseInfo(@RequestBody @Valid Test test);

    @PostMapping(value = "/form-data", consumes = "multipart/form-data")
    @Operation(summary = "测试后台sapi-form-data")
    Mono<String> postFormData(@RequestPart("file") FilePart part,
                              @RequestPart("name") String name);
}
