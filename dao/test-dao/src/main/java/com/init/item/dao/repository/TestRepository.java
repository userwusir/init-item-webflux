package com.init.item.dao.repository;

import com.init.item.dao.entity.Test;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TestRepository extends ReactiveCrudRepository<Test, Integer> {
}
