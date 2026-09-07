package com.danny.shoppingplatform.controller;

import com.danny.shoppingplatform.annotation.CurrentAccount;
import com.danny.shoppingplatform.dto.user.*;
import com.danny.shoppingplatform.service.UserService;
import com.danny.shoppingplatform.util.CookieUtil;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/users")
public class UserController {
    private final UserService userService;
    private final CookieUtil cookieUtil;

    @GetMapping("/me")
    public ResponseEntity<?> fetchMe(@CurrentAccount String account) {
        if ("anonymousUser".equals(account)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("message", "Not logged in"));
        }

        UserInfo userInfo = userService.fetchMe(account);
        return ResponseEntity.ok(userInfo);
    }

    @PostMapping("/me/vendor-profile")
    public ResponseEntity<Void> addVendor(@CurrentAccount String account) {
        userService.addVendor(account);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PutMapping("/me/active-role")
    public ResponseEntity<Void> setRole(@Valid @RequestBody SetRoleRequest request, @CurrentAccount String account) {
        String newToken = userService.setRole(request.getRole(), account);
        ResponseCookie cookie = cookieUtil.createJwtCookie(newToken);
        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .build();
    }
}
