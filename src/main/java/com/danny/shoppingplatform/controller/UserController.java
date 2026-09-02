package com.danny.shoppingplatform.controller;

import com.danny.shoppingplatform.annotation.CurrentAccount;
import com.danny.shoppingplatform.dto.member.*;
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

import java.util.Map;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class UserController {
    private final UserService userService;
    private final CookieUtil cookieUtil;

    @PostMapping("/login")
    public ResponseEntity<UserInfo> login(@Valid @RequestBody LoginRequest request) {
        LoginResult loginResult = userService.login(request);
        ResponseCookie cookie = cookieUtil.createJwtCookie(loginResult.getToken());
        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .body(loginResult.getUserInfo());
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout() {
        ResponseCookie cookie = cookieUtil.removeJwtCookie();
        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .build();
    }

    @GetMapping("/me")
    public ResponseEntity<?> fetchMe(@CurrentAccount String account) {
        if ("anonymousUser".equals(account)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("message", "Not logged in"));
        }

        UserInfo userInfo = userService.fetchMe(account);
        return ResponseEntity.ok(userInfo);
    }

    @PostMapping("/register")
    public ResponseEntity<Void> register(@Valid @RequestBody RegisterRequest request) throws BadRequestException {
        userService.register(request);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
