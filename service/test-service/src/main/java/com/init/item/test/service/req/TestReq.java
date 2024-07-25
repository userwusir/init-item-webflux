package com.init.item.test.service.req;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
@Schema(name = "TestReq", description = "测试请求参数")
public class TestReq {
    @Schema(description = "姓名")
    @NotBlank(message = "姓名不能为空")
    private String name;
}
