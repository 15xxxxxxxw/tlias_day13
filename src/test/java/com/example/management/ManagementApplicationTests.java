package com.example.management;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@SpringBootTest
public class ManagementApplicationTests {

	String secret = "itcastitcastitcastitcast12345678";
	@Test
	public void testGenJwt() {
		//创建Map存放数据
		Map<String, Object> claims = new HashMap<>();
		claims.put("id", 10);
		claims.put("username", "itheima");

		//构建签名密钥，用HS256加密
		//随机生成密钥
		// SecretKey key = Keys.secretKeyFor(SignatureAlgorithm.HS256);
		//固定密钥
		SecretKey key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
		//构建JWT
		String jwt = Jwts.builder()
				.addClaims(claims)//添加内容
				.setExpiration(new Date(System.currentTimeMillis() + 60 * 1000))//过期时间
				.signWith(key)//加密
				.compact();//将上面所有东西组合起来

		System.out.println(jwt);//输出jwt
		//eyJhbGciOiJIUzI1NiJ9.eyJpZCI6MTAsInVzZXJuYW1lIjoiaXRoZWltYSIsImV4cCI6MTc3MzMyNzMwM30.kDlamtibCM-jzg29NDp4Qo1ufpXKeuW8SODwJ5Ueu_E
		// Header部分：{"alg": "HS256"}
		/* Payload负载部分（存放真正的数据）：
		{"id": 10,
  		"username": "itheima",
  		"exp": 1773327303
		}*/
		//Signature（签名）：验证token是否被篡改，是否是系统自己签发的
	}

	@Test
	public void testParseJwt(){
		SecretKey key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));

		String jwt = "生成的jwt（待修改";
		Claims claims = Jwts.parser()
				.verifyWith(key)
				.build()
				.parseSignedClaims(jwt)
				.getPayload();

		System.out.println("claims = " + claims);
		System.out.println("id = " + claims.get("id"));
		System.out.println("username = " + claims.get("username"));
		System.out.println("exp = " + claims.getExpiration());
	}
}
