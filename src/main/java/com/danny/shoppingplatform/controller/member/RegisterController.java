package com.danny.shoppingplatform.controller.member;

import com.danny.shoppingplatform.model.Member;
import com.danny.shoppingplatform.service.MemberService;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;

@Controller
public class RegisterController {
    private final MemberService memberService;

    public RegisterController(MemberService memberService) {
        this.memberService = memberService;
    }

    @PostMapping("/register/controller")
    public String register(Model model, String account, String password, HttpSession session) {
        Map<String, String> errors = new HashMap<String, String>();
        model.addAttribute("errors", errors);

        // 若表單未提送成功，使用者輸入的account會留下。
        model.addAttribute("account", account);

        Member member = memberService.register(account, password);

        if (member == null) {
            errors.put("error", "帳號已存在");
            return "/secure/register";
        }

        return "redirect:/index";
    }

    @ResponseBody
    @PostMapping("/api/register")
    public ResponseEntity<?> register(@RequestBody HashMap<String,String> map) {
        String account = map.get("account");
        String password = map.get("password");

        Member member = memberService.register(account, password);

        if (member == null) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "帳號已存在");
            return ResponseEntity.badRequest().body(error);
        }

        return ResponseEntity.ok().body(member);
    }
}
