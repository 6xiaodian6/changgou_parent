package com.changgou.system.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

/**
 * @description: 校验工具
 * @author: yuandian
 * @createTime: 2019-11-10 20:48:41
 **/
public class JwtUtil {
    /**
     * 设置有效期为 60 *60 *1000 一个小时
     */
    public static final Long JWT_TTL = 3600000L;
    public static final String JWT_KEY = "itcast";

    /**
     * 生成加密后的秘钥
     *
     * @return 返回加密后的秘钥
     */
    public static SecretKey generalKey() {
        byte[] bytes = Base64.getDecoder().decode(JwtUtil.JWT_KEY);
        SecretKey key = new SecretKeySpec(bytes, 0, bytes.length, "AES");
        return key;
    }
     public static Claims parseJWT(String jwt) throws Exception{
         SecretKey secretKey = generalKey();
         return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(jwt).getBody();
     }
}
