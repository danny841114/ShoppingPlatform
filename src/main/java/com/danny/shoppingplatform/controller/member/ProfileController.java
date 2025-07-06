package com.danny.shoppingplatform.controller.member;

import com.danny.shoppingplatform.jwt.JwtUtil;
import com.danny.shoppingplatform.model.Member;
import com.danny.shoppingplatform.service.MemberService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.text.ParseException;

@Controller
public class ProfileController {
    private final MemberService memberService;
    private final JwtUtil jwtUtil;

    public ProfileController(MemberService memberService, JwtUtil jwtUtil) {
        this.memberService = memberService;
        this.jwtUtil = jwtUtil;
    }

    @ResponseBody
    @GetMapping(value = "/api/member/{id}/photo", produces = MediaType.IMAGE_JPEG_VALUE)
    public byte[] getMemberPhoto(@PathVariable Integer id) {
        return memberService.findById(id).getPhoto();
    }

    @GetMapping("/role/upgrade/controller")
    public String upgradeMemberAndReturnNewToken(@RequestParam("account") String account, HttpServletResponse response) {
        memberService.upgradeRole(account);

        Member member = memberService.findByAccount(account);
        String token = jwtUtil.generateToken(member);

        Cookie newCookie = new Cookie("jwt", token);
        newCookie.setHttpOnly(true);
        newCookie.setPath("/");
        newCookie.setMaxAge(7 * 24 * 60 * 60); // 設置存活時間 7 天，可依需求調整
        response.addCookie(newCookie);

        return "redirect:/profile";
    }

    @PostMapping("/profile/modify/controller")
    public String modifyProfile(@RequestParam("account") String account, @RequestParam("name") String name, @RequestParam("birthdate") String birthdate, @RequestParam("email") String email, @RequestParam("photo") MultipartFile photo) throws ParseException {

        byte[] photoForUpload = null;

        // 即使沒傳圖片，也會回傳 byte[0]，會覆蓋null，所以要先判斷 isEmpty()
        if (!photo.isEmpty()) {
            try {
                photoForUpload = photo.getBytes();
            } catch (IOException e) {
                System.err.println("圖片上傳失敗");
            }
        }

        memberService.modifyProfile(account, name, birthdate, email, photoForUpload);
        return "redirect:/profile";
    }
}
