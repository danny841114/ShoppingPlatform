package com.danny.shoppingplatform.controller.member;

import com.danny.shoppingplatform.model.Member;
import com.danny.shoppingplatform.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
@RestController
public class RegisterController {
    private final MemberService memberService;

    @PostMapping("/api/register")
    public ResponseEntity<?> register(@RequestBody HashMap<String, String> map) {
        String account = map.get("account");
        String password = map.get("password");

        Member member = memberService.register(account, password);

        if (member == null) {
            Map<String, String> response = new HashMap<>();
            response.put("error", "帳號已存在");
            return ResponseEntity.badRequest().body(response);
        }

        return ResponseEntity.ok().body(member);
    }
}
