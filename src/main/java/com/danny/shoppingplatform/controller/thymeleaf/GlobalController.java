package com.danny.shoppingplatform.controller.thymeleaf;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
public class GlobalController {
    @ModelAttribute
    public void addAttributes(HttpServletRequest request, Model model) {
        Object account = request.getAttribute("account");
        Object role = request.getAttribute("role");

        if (account != null) {
            model.addAttribute("isLogin", true);
            model.addAttribute("account", account.toString());
            model.addAttribute("role", role.toString());
        } else {
            model.addAttribute("isLogin", false);
        }
    }

//    @ModelAttribute
//    public void addAttributes(Model model) {
//        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
//
//        if (auth != null && auth.isAuthenticated() && !"anonymousUser".equals(auth.getPrincipal())) {
//            model.addAttribute("isLogin", true);
//            model.addAttribute("account", auth.getName());
//
//            // 把角色資料轉成 List<String>，或是你要的格式
//            List<String> roles = auth.getAuthorities().stream()
//                    .map(GrantedAuthority::getAuthority)
//                    .collect(Collectors.toList());
//
//            model.addAttribute("roles", roles);
//
//            // 如果你只想顯示第一個角色，可以這樣做
//            if (!roles.isEmpty()) {
//                model.addAttribute("role", roles.getFirst()); // 或自訂邏輯
//            }
//
//        } else {
//            model.addAttribute("isLogin", false);
//        }
//    }
}
