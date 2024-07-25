package com.init.item.test.service;

import com.init.item.dao.entity.Test;
import com.init.item.dao.repository.TestRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class TestService {
    private final TestRepository testRepository;

    public Mono<Test> findById() {
        return testRepository.findById(10);
    }
}
