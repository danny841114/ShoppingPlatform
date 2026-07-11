package com.danny.shoppingplatform.controller.member;

import com.danny.shoppingplatform.dto.UserDetailsImpl;
import com.danny.shoppingplatform.jwt.JwtUtil;
import com.danny.shoppingplatform.model.Member;
import com.danny.shoppingplatform.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@CrossOrigin
@RestController
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;

    @PostMapping("/api/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> request) {
        String account = request.get("account");
        String password = request.get("password");

        try {
            Authentication authenticationToken = new UsernamePasswordAuthenticationToken(account, password);
            Authentication authentication = authenticationManager.authenticate(authenticationToken);

            UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
            String token = jwtUtil.generateToken(userDetails.getMember());

            ResponseCookie cookie = ResponseCookie
                    .from("jwt", token)
                    .httpOnly(true)
                    .secure(false) // 本機開發先 false，正式 HTTPS 改 true
                    .path("/")
                    .maxAge(86400)
                    .sameSite("Lax")
                    .build();

            Map<String, String> response = new HashMap<>();
            response.put("account", userDetails.getAccount());
            response.put("role", userDetails.getRole());

            return ResponseEntity
                    .ok()
                    .header(HttpHeaders.SET_COOKIE, cookie.toString())
                    .body(response);
        } catch (BadCredentialsException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid username or password");
        }
    }

    @PostMapping("/api/logout")
    public ResponseEntity<?> logout() {
        ResponseCookie cookie = ResponseCookie
                .from("jwt", "")
                .httpOnly(true)
                .secure(false)
                .path("/")
                .maxAge(0)
                .build();

        return ResponseEntity
                .ok()
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .body("logout success");
    }

    @GetMapping("/api/me")
    public ResponseEntity<?> me(Authentication authentication) {
        if (authentication == null
                || !authentication.isAuthenticated()
                || "anonymousUser".equals(authentication.getName())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        Member member = memberService.findByAccount(authentication.getName());

        Map<String, String> response = new HashMap<>();
        response.put("account", member.getAccount());
        response.put("role", member.getRole());

        return ResponseEntity.ok(response);
    }
}
