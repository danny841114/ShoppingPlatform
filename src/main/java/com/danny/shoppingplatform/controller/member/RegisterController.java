package com.danny.shoppingplatform.controller.member;

import com.danny.shoppingplatform.model.Member;
import com.danny.shoppingplatform.service.MemberService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;

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
}
