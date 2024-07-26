package com.init.item.web.router;

import com.init.item.dao.entity.Test;
import com.init.item.test.service.req.TestReq;
import com.init.item.web.handler.TestHandler;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.SchemaProperty;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.RouterOperation;
import org.springdoc.core.annotations.RouterOperations;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

@Configuration
@RequiredArgsConstructor
public class AppRouterConfig {

    private final TestHandler testHandler;

    /**
     * 参考 <a href="https://springdoc.org/#spring-webfluxwebmvc-fn-with-functional-endpoints"/a>
     */
    @RouterOperations({
            @RouterOperation(path = "/test", method = RequestMethod.GET, beanClass = TestHandler.class, beanMethod = "test",
                    operation = @Operation(operationId = "1", summary = "hello world", tags = {"测试handler"})),
            @RouterOperation(path = "/test2", method = RequestMethod.POST, beanClass = TestHandler.class, beanMethod = "test2",
                    operation = @Operation(operationId = "2", summary = "测试body", tags = {"测试handler"},
                            requestBody = @RequestBody(required = true, content = @Content(schema = @Schema(implementation = TestReq.class))))),
            @RouterOperation(path = "/test3", method = RequestMethod.POST, beanClass = TestHandler.class, beanMethod = "test3",
                    consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
                    operation = @Operation(operationId = "3", summary = "测试form-body", tags = {"测试handler"},
                            requestBody = @RequestBody(
                                    required = true, content = @Content(
                                    mediaType = MediaType.MULTIPART_FORM_DATA_VALUE,
                                    schemaProperties = {
                                            @SchemaProperty(name = "file", schema = @Schema(format = "binary")),
                                            @SchemaProperty(name = "name", schema = @Schema(type = "string")),
                                    })
                            )
                    )
            ),
            @RouterOperation(path = "/test4", method = RequestMethod.GET, beanClass = TestHandler.class, beanMethod = "test4",
                    operation = @Operation(operationId = "4", summary = "测试r2dbc", tags = {"测试handler"},
                            responses = {@ApiResponse(content = @Content(schema = @Schema(implementation = Test.class)))})),
    })
    @Bean
    public RouterFunction<ServerResponse> routerFunction() {
        return RouterFunctions.route()
                .GET("/test", testHandler::test)
                .POST("/test2", testHandler::test2)
                .POST("/test3", testHandler::test3)
                .GET("/test4", testHandler::test4)
                .build();
    }
}
