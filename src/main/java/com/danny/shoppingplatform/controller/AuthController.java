package com.danny.shoppingplatform.controller;

import com.danny.shoppingplatform.dto.user.*;
import com.danny.shoppingplatform.service.UserService;
import com.danny.shoppingplatform.util.CookieUtil;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final UserService userService;
    private final CookieUtil cookieUtil;

    @PostMapping("/login")
    public ResponseEntity<UserInfo> login(@Valid @RequestBody LoginRequest request) {
        LoginResult loginResult = userService.login(request.account(), request.password());
        ResponseCookie cookie = cookieUtil.createJwtCookie(loginResult.token());
        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .body(loginResult.userInfo());
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout() {
        ResponseCookie cookie = cookieUtil.removeJwtCookie();
        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .build();
    }

    @PostMapping("/register")
    public ResponseEntity<Void> register(@Valid @RequestBody RegisterRequest request) throws BadRequestException {
        userService.register(request.account(), request.password());
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
