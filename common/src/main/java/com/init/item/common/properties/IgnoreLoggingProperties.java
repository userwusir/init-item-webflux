package com.init.item.common.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

/**
 * 忽略接口出入参日志的配置类
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "log.ignore.com-out")
public class IgnoreLoggingProperties {
    private boolean enabled = true;
    private List<String> ignorePatterns = new ArrayList<>();
}
