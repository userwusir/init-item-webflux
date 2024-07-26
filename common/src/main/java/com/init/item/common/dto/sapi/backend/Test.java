package com.init.item.common.dto.sapi.backend;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "测试类")
public class Test {
    @Schema(description = "姓名")
    private String name;
    @Schema(description = "年龄")
    private int age;
}
