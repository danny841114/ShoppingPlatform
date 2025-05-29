package com.danny.shoppingplatform.controller.member;

import com.danny.shoppingplatform.jwt.JwtUtil;
import com.danny.shoppingplatform.model.Member;
import com.danny.shoppingplatform.service.MemberService;
import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashMap;
import java.util.Map;

@Controller
public class LoginController {
    private final MemberService memberService;
    private final JwtUtil jwtUtil;

    public LoginController(MemberService memberService, JwtUtil jwtUtil) {
        this.memberService = memberService;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/login/controller")
    public String loginBySession(Model model, String account, String password, HttpSession session) {
        Map<String, String> errors = new HashMap<String, String>();
        model.addAttribute("errors", errors);

        Member member = memberService.login(account, password);

        if (member == null) {
            errors.put("error", "帳號或密碼錯誤");
            return "/secure/login";
        }

        session.setAttribute("member", member);
        return "redirect:/index";
    }

    @PostMapping("/login")
    @ResponseBody
    public ResponseEntity<?> loginByJwt(@RequestBody Map<String, String> request) {
        String account = request.get("account");
        String password = request.get("password");

        Member member = memberService.login(account, password);

        if (member == null) {
//            Map<String, String> error = new HashMap<>();
//            error.put("error", "帳號或密碼錯誤");
//            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "帳號密碼錯誤");
        }

        // 產生 JWT（可把 account、role 等資訊塞進去）
        String token = jwtUtil.generateToken(member);

        // 回傳 token 給前端（可加上其他使用者資料）
        Map<String, String> response = new HashMap<>();
        response.put("token", token);
        response.put("account", member.getAccount());
        response.put("role", member.getRole());

        return ResponseEntity.ok(response);
    }
}
