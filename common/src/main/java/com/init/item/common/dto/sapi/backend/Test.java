package com.init.item.common.dto.sapi.backend;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@Schema(name = "test", description = "测试类")
public class Test {
    @Schema(description = "姓名")
    @NotBlank(message = "姓名不能为空")
    private String name;
    @Schema(description = "年龄")
    @NotNull(message = "年龄不能为空")
    private Integer age;
}
