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

import static com.danny.shoppingplatform.dto.member.UserInfo.generateUserInfo;

@RequiredArgsConstructor
@RestController
public class UserController {
    private final UserService userService;
    private final CookieUtil cookieUtil;

    @PostMapping("/api/login")
    public ResponseEntity<UserInfo> login(@Valid @RequestBody LoginRequest request) {
        LoginResult loginResult = userService.login(request);
        ResponseCookie cookie = cookieUtil.createJwtCookie(loginResult.getToken());
        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .body(loginResult.getUserInfo());
    }

    @PostMapping("/api/logout")
    public ResponseEntity<?> logout() {
        ResponseCookie cookie = cookieUtil.removeJwtCookie();
        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .build();
    }

    @GetMapping("/api/me")
    public ResponseEntity<?> fetchMe(@CurrentAccount String account) {
        if ("anonymousUser".equals(account)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("message", "Not logged in"));
        }

        UserDto dto = userService.getMemberByAccount(account);
        UserInfo response = generateUserInfo(dto.getAccount(), dto.getRole());
        return ResponseEntity.ok(response);
    }

    @PostMapping("/api/register")
    public ResponseEntity<UserDto> register(@Valid @RequestBody RegisterRequest request) throws BadRequestException {
        UserDto userDto = userService.register(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(userDto);
    }
}
