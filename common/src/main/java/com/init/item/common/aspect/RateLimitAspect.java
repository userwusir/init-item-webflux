package com.init.item.common.aspect;

import com.init.item.common.annotation.RateLimit;
import com.init.item.common.enums.Code;
import com.init.item.common.exception.RateLimitException;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.scripting.support.ResourceScriptSource;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Aspect
//@Component
public class RateLimitAspect {

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    private DefaultRedisScript<Long> redisScript = new DefaultRedisScript<>();

    @PostConstruct
    public void init() {
        redisScript = new DefaultRedisScript<>();
        redisScript.setResultType(Long.class);
        redisScript.setScriptSource(new ResourceScriptSource(new ClassPathResource("lua/rateLimit.lua")));
    }

    @Pointcut("@annotation(com.init.item.common.annotation.RateLimit)")
    private void check() {

    }

    @Before("check()")
    public void before(JoinPoint joinPoint) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();

        //拿到RedisLimit注解，如果存在则说明需要限流
        RateLimit redisLimit = method.getAnnotation(RateLimit.class);

        if (redisLimit != null) {
            //获取redis的key
            String key = redisLimit.key();
            String className = method.getDeclaringClass().getName();

            String limitKey = key + className + method.getName();

            log.info(limitKey);

            if (StringUtils.isEmpty(key)) {
                log.error("令牌桶={}，获取令牌失败", key);
                throw new RateLimitException(Code.RATE_LIMIT);
            }

            long limit = redisLimit.permitsPerSecond();

            long expire = redisLimit.expire();

            List<String> keys = new ArrayList<>();
            keys.add(key);

            Long count = stringRedisTemplate.execute(redisScript, keys, String.valueOf(limit), String.valueOf(expire));

            log.info("Access try count is {} for key={}", count, key);

            if (count != null && count == 0) {
                log.debug("令牌桶={}，获取令牌失败", key);
                throw new RateLimitException(Code.RATE_LIMIT);
            }
        }

    }
}