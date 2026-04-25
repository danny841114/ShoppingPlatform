package com.danny.shoppingplatform.jwt;

import com.danny.shoppingplatform.model.Member;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
public class JwtUtil {
    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration}")
    private long expiration;

    private Key key;

    @PostConstruct
    public void init() {
        this.key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secret));
    }

    public String generateToken(Member member) {
        return Jwts.builder()
                .setSubject(member.getAccount()) // 使用者帳號
                .claim("role", member.getRole()) // 角色
                .setIssuedAt(new Date()) // 設定發行時間
                .setExpiration(new Date(System.currentTimeMillis() + expiration)) // 設定過期時間
                .signWith(key, SignatureAlgorithm.HS256) // 使用金鑰與演算法簽名
                .compact(); // 建立token並回傳字串
    }

    public boolean validateToken(String token) {
        try {
            extractClaims(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    public Claims extractClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}
