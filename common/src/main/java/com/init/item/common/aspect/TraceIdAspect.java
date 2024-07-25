package com.init.item.common.aspect;

import com.init.item.common.consts.Const;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Aspect
@Component
public class TraceIdAspect {


    @Pointcut("@annotation(com.init.item.common.annotation.TraceId)")
    public void traceIdPointCut() {
    }

    @Before("traceIdPointCut()")
    public void beforeCut() {
        String traceId = MDC.get(Const.TRACE_ID_KEY);
        if (StringUtils.isBlank(traceId)) {
            MDC.put(Const.TRACE_ID_KEY, UUID.randomUUID().toString());
        }
    }

    @After("traceIdPointCut()")
    public void afterCut() {
        MDC.remove(Const.TRACE_ID_KEY);
    }
}

