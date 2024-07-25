package com.init.item.util.auth;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.apache.commons.codec.binary.Base64;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * 总的来说，工具类中有三个方法
 * 获取JwtToken，获取JwtToken中封装的信息，验证JwtToken是否存在
 */
public class JwtUtil {
    private final static String SECRET_KEY = "sZ4rF9uM5oN9yJ4p";

    /**
     * 这里就是产生jwt字符串的地方
     * jwt字符串包括三个部分
     * 1. header
     * 当前字符串的类型，一般都是“JWT”
     * 哪种算法加密，“HS256”或者其他的加密算法
     * 所以一般都是固定的，没有什么变化
     * 2. payload
     * 一般有四个最常见的标准字段（下面有）
     * iat：签发时间，也就是这个jwt什么时候生成的
     * jti：JWT的唯一标识
     * iss：签发人，一般都是username或者userId
     * exp：过期时间
     * 参数: 签发人，存在时间，一些其他的信息。返回值是JwtToken对应的字符串
     */
    public static String genToken(String iss, long seconds, Map<String, Object> claims) {
        //iss签发人，ttlMillis生存时间，claims是指还想要在jwt中存储的一些非隐私信息
        if (claims == null) {
            claims = new HashMap<>(1);
        }
        Instant now = Instant.now();
        String key = Base64.encodeBase64String(SECRET_KEY.getBytes());
        Algorithm algorithm = Algorithm.HMAC256(key);

        JWTCreator.Builder builder = JWT.create();
        // claims是指还想要在jwt中存储的一些非隐私信息
        builder.withClaim("claims", claims)
                // 这个是JWT的唯一标识，一般设置成唯一的，这个方法可以生成唯一标识
                .withJWTId(UUID.randomUUID().toString())
                // 签发时间
                .withIssuedAt(Date.from(now))
                // 签发人，也就是JWT是给谁的（逻辑上一般都是username或者userId）
                .withIssuer(iss);
        if (seconds >= 0) {
            Date exp = Date.from(now.plus(seconds, ChronoUnit.SECONDS));
            builder.withExpiresAt(exp);
        }
        return builder.sign(algorithm);
    }

    /**
     * 相对于encode的方向，传入jwtToken生成对应的username和password等字段。Claim就是一个map
     * 也就是拿到荷载部分所有的键值对
     * 参数: JwtToken。返回值是荷载部分的键值对
     */
    public static DecodedJWT decodeToken(String jwtToken) {
        String key = Base64.encodeBase64String(SECRET_KEY.getBytes());
        Algorithm algorithm = Algorithm.HMAC256(key);
        // 得到 DefaultJwtParser
        return JWT.require(algorithm).build()
                .verify(jwtToken);
    }


    /**
     * 判断jwtToken是否合法
     * 参数: JwtToken。返回值是这个JwtToken是否存在
     */
    public static boolean verifyToken(String jwtToken) {
        String key = Base64.encodeBase64String(SECRET_KEY.getBytes());
        Algorithm algorithm = Algorithm.HMAC256(key);
        try {
            JWTVerifier verifier = JWT.require(algorithm).build();
            //判断合法的标准：1. 头部和荷载部分没有篡改过。2. 没有过期
            // 校验不通过会抛出异常
            verifier.verify(jwtToken);
        } catch (Exception e) {
            return false;
        }
        return true;
    }
}
