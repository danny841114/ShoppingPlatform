package com.danny.shoppingplatform.controller.member;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;

@CrossOrigin
@Controller
public class LogoutController {
    @GetMapping("/logout/controller")
    public String logout(HttpSession session) {
        session.invalidate(); // 清除Session（即登出）
        return "redirect:/index";
    }

    @GetMapping("/logout")
    public String logout(HttpServletResponse response) {
        // 清除 JWT Cookie（設值為空、設 maxAge 為 0）
        Cookie cookie = new Cookie("jwt", null);
        cookie.setHttpOnly(true); // 和你設置時一致
        cookie.setPath("/");      // 要一致，才能成功覆蓋
        cookie.setMaxAge(0);      // 設 0 秒表示馬上過期
        response.addCookie(cookie);

        return "redirect:/index";
    }

    @ResponseBody
    @PostMapping("/api/logout")
    public ResponseEntity<?> logoutApi(HttpServletResponse response) {
        Cookie cookie = new Cookie("jwt", null);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        cookie.setMaxAge(0);
        response.addCookie(cookie);

        return ResponseEntity.ok(Map.of("message", "成功登出"));
    }
}
