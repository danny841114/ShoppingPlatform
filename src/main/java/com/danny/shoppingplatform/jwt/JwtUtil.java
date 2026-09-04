package com.danny.shoppingplatform.jwt;

import com.danny.shoppingplatform.dto.user.CustomUserDetails;
import com.danny.shoppingplatform.model.User;
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
import java.util.List;

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

    public String generateTokenByUser(User user, String currentRole) {
        Long memberId = user.getMember() != null ? user.getMember().getId() : null;
        Long vendorId = user.getVendor() != null ? user.getVendor().getId() : null;

        return generateToken(
                user.getAccount(),
                user.getId(),
                memberId,
                vendorId,
                user.getRoles(),
                currentRole
        );
    }

    public String generateTokenByUserDetails(CustomUserDetails userDetails, String currentRole) {
        return generateToken(
                userDetails.getUsername(),
                userDetails.getUserId(),
                userDetails.getMemberId(),
                userDetails.getVendorId(),
                userDetails.getRoles(),
                currentRole
        );
    }

    private String generateToken(String account,
                                 Long userId,
                                 Long memberId,
                                 Long vendorId,
                                 List<String> roles,
                                 String currentRole) {
        return Jwts.builder()
                .setSubject(account)
                .claim("userId", userId)
                .claim("memberId", memberId)
                .claim("vendorId", vendorId)
                .claim("roles", roles)
                .claim("currentRole", currentRole)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
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
