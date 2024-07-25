package com.init.item.dao.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;

@Table(value = "test", schema = "mytest")
@Data
public class Test {
    @Id
    private Integer id;
    private String message;
    private LocalDateTime time;
}
