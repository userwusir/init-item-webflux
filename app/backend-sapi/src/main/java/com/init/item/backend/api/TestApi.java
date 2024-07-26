package com.init.item.backend.api;

import com.init.item.common.dto.sapi.backend.Test;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import reactor.core.publisher.Mono;

@Tag(name = "测试后台sapi")
public interface TestApi {

    @GetMapping("/users/{name}")
    @Operation(summary = "测试后台sapi")
    Mono<Test> getUserBaseInfo(@PathVariable("name") String name);
}
