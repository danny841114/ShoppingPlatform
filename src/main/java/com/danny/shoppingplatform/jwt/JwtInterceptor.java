package com.danny.shoppingplatform.jwt;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.io.IOException;

@Component
public class JwtInterceptor implements HandlerInterceptor {
//    private final JwtUtil jwtUtil;
//
//    public JwtInterceptor(JwtUtil jwtUtil) {
//        this.jwtUtil = jwtUtil;
//    }
//
//    @Override
//    public boolean preHandle(@NonNull HttpServletRequest request,
//                             @NonNull HttpServletResponse response,
//                             @NonNull Object handler) throws IOException {
//        // 如果是 OPTIONS 請求，直接放行
//        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) return true;
//
//        String token = resolveToken(request);
//        if (token != null && jwtUtil.validateToken(token)) {
//            request.setAttribute("account", jwtUtil.getAccount(token));
//            request.setAttribute("role", jwtUtil.getRole(token));
//            return true;
//        }
//
//        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
//        response.getWriter().write("Unauthorized: Invalid or missing token");
//        return false;
//    }
//
//    private String resolveToken(HttpServletRequest request) {
//        String bearerToken = request.getHeader("Authorization");
//        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
//            return bearerToken.substring(7);
//        }
//
//        Cookie[] cookies = request.getCookies();
//        if (cookies != null) {
//            for (Cookie cookie : cookies) {
//                if ("jwt".equals(cookie.getName())) {
//                    return cookie.getValue();
//                }
//            }
//        }
//
//        return null;
//    }
}
