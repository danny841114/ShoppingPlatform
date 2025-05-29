package com.danny.shoppingplatform.jwt;

import com.danny.shoppingplatform.model.Member;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
public class JwtUtil {
    private static final String secret = "MySuperSecretKeyForJwtShouldBeAtLeast32Chars";

    private Key key;

    @PostConstruct
    public void init() {
        this.key = Keys.hmacShaKeyFor(secret.getBytes()); // 初始化金鑰物件
    }

    public String generateToken(Member member) {
        return Jwts.builder()
                .setSubject(member.getAccount()) // 使用者帳號
                .claim("role", member.getRole()) // 角色
                .setIssuedAt(new Date()) // 設定發行時間
                .setExpiration(new Date(System.currentTimeMillis() + 86400000)) // 設定過期時間(1天)
                .signWith(key, SignatureAlgorithm.HS256) // 使用金鑰與演算法簽名
                .compact(); // 建立token並回傳字串
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(key) // 驗證使用相同金鑰
                    .build()
                    .parseClaimsJws(token); // 嘗試解析token
            return true;
        } catch (JwtException e) {
            return false;
        }
    }

    public Claims extractClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody(); // 拿出payload資料
    }

    public String getAccount(String token) {
        return extractClaims(token).getSubject();
    }

    public String getRole(String token) {
        return (String) extractClaims(token).get("role");
    }
}
