package com.init.item.backend.api;

import com.init.item.common.dto.sapi.backend.Test;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

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
}
