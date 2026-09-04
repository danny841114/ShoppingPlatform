package com.danny.shoppingplatform.jwt;

import com.danny.shoppingplatform.dto.user.CustomUserDetails;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Component
public class JwtFilter extends OncePerRequestFilter {
    private final JwtUtil jwtUtil;

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain) throws ServletException, IOException {

        try {
            String token = resolveToken(request);

            if (token != null
                    && jwtUtil.validateToken(token)
                    && SecurityContextHolder.getContext().getAuthentication() == null) {
                Claims claims = jwtUtil.extractClaims(token);

                String account = claims.getSubject();
                Long userId = claims.get("userId", Long.class);
                Long memberId = claims.get("memberId", Long.class);
                Long vendorId = claims.get("vendorId", Long.class);

                List<String> roles = extractStringList(claims, "roles");

                String currentRole = claims.get("currentRole", String.class);
                List<GrantedAuthority> authorities = List.of(new SimpleGrantedAuthority("ROLE_" + currentRole));

                log.debug("[JwtFilter] account: {}, userId: {}, memberId:{}, vendorId: {}, roles: {}, currentRole: {}",
                        account, userId, memberId, vendorId, roles, currentRole);

                CustomUserDetails customUserDetails = new CustomUserDetails(
                        account, userId, memberId, vendorId, roles
                );

                var authentication = new UsernamePasswordAuthenticationToken(customUserDetails, null, authorities);
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authentication);
                log.debug("[JwtFilter] Authenticated user: {}, setting SecurityContext", account);
            }
        } catch (Exception e) {
            SecurityContextHolder.clearContext();
            log.error("[JwtFilter] Could not set user authentication in security context", e);
        }

        filterChain.doFilter(request, response); // 執行過濾鏈
    }

    private String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }

        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("jwt".equals(cookie.getName())) {
                    return cookie.getValue();
                }
            }
        }

        return null;
    }

    public static List<String> extractStringList(Claims claims, String claimName) {
        Object rolesObject = claims.get(claimName);

        return Optional.ofNullable(rolesObject)
                .filter(List.class::isInstance)
                .map(obj -> (List<?>) obj)
                .stream()
                .flatMap(List::stream)
                .filter(String.class::isInstance)
                .map(String.class::cast)
                .collect(Collectors.toList());
    }
}
