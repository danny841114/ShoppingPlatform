package com.danny.shoppingplatform.controller.member;

import com.danny.shoppingplatform.annotation.CurrentAccount;
import com.danny.shoppingplatform.dto.member.*;
import com.danny.shoppingplatform.service.MemberService;
import com.danny.shoppingplatform.util.CookieUtil;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.danny.shoppingplatform.dto.member.UserInfo.generateUserInfo;

@RequiredArgsConstructor
@RestController
public class MemberController {
    private final MemberService memberService;
    private final CookieUtil cookieUtil;

    @PostMapping("/api/login")
    public ResponseEntity<UserInfo> login(@Valid @RequestBody LoginRequest request) {
        LoginResult loginResult = memberService.login(request);
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
    public ResponseEntity<UserInfo> fetchMe(@CurrentAccount String account) {
        MemberDto dto = memberService.getMemberByAccount(account);
        UserInfo response = generateUserInfo(dto.getAccount(), dto.getRole());
        return ResponseEntity.ok(response);
    }

    @PostMapping("/api/register")
    public ResponseEntity<MemberDto> register(@Valid @RequestBody RegisterRequest request) throws BadRequestException {
        MemberDto memberDto = memberService.register(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(memberDto);
    }
}
