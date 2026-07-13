package com.danny.shoppingplatform.util;

import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;

@Component
public class CookieUtil {
    public ResponseCookie createJwtCookie(String token) {
        return ResponseCookie.from("jwt", token)
                .httpOnly(true)
                .secure(false)  // 本機開發先 false，正式 HTTPS 改 true
                .path("/")
                .maxAge(86400)
                .sameSite("Lax")
                .build();
    }

    public ResponseCookie removeJwtCookie() {
        return ResponseCookie.from("jwt", "")
                .httpOnly(true)
                .secure(false)  // 本機開發先 false，正式 HTTPS 改 true
                .path("/")
                .maxAge(0)
                .build();
    }
}
