package com.example.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Map;

public class JwtUtils {
    private static String SECRET= "tdcgdyfzdhhk==wdhjqkdeb==xwwyytyngybdzj";
    private static Long expire = 43200000L;
    private static SecretKey KEY =Keys.hmacShaKeyFor(SECRET.getBytes(StandardCharsets.UTF_8));
    //生成令牌Jwt
    public static String generateJwt(Map<String,Object> claims){
        return  Jwts.builder()
                .addClaims(claims)
                .setExpiration(new Date(System.currentTimeMillis() + expire))
                .signWith(KEY)
                .compact();
    }

    //解析jwt
    public static Claims parseJWT(String jwt){
        return Jwts.parser()
                .verifyWith(KEY)
                .build()
                .parseSignedClaims(jwt)
                .getPayload();
    }
}
