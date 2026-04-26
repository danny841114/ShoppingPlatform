package com.danny.shoppingplatform.controller.product;

import com.danny.shoppingplatform.model.Member;
import com.danny.shoppingplatform.model.Product;
import com.danny.shoppingplatform.service.MemberService;
import com.danny.shoppingplatform.service.ProductService;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.security.auth.login.AccountNotFoundException;
import java.io.IOException;

@Slf4j
@Controller
public class ProductController {
    private final ProductService productService;
    private final MemberService memberService;

    public ProductController(ProductService productService, MemberService memberService) {
        this.productService = productService;
        this.memberService = memberService;
    }

    @PostMapping("/product/controller")
    public String addProduct(@RequestParam String name,
                             @RequestParam String description,
                             @RequestParam Integer price,
                             @RequestParam Integer quantity,
                             @RequestParam MultipartFile photo) throws AccountNotFoundException {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String account = auth.getName();
        Member member = memberService.findByAccount(account);

        byte[] photoForUpload = null;
        if (!photo.isEmpty()) {
            try {
                photoForUpload = photo.getBytes();
            } catch (IOException e) {
                log.error("圖片上傳失敗", e);
            }
        }

        productService.addProduct(name, description, price, quantity, photoForUpload);

        return "redirect:/product/manage";
    }

    @GetMapping("/product/controller")
    public String showProduct(@RequestParam(defaultValue = "4") int size,
                              @RequestParam(defaultValue = "0") int page,
                              @RequestParam(required = false) String keyword,
                              Model model) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Product> productPage;

        if (keyword != null && !keyword.isBlank()) {
            productPage = productService.findByNameContaining(keyword, pageable);
        } else {
            productPage = productService.findAllByPageable(pageable);
        }

        model.addAttribute("productList", productPage.getContent());
        model.addAttribute("totalPages", productPage.getTotalPages());
        model.addAttribute("totalElements", productPage.getTotalElements());
        model.addAttribute("page", page);
        model.addAttribute("size", size);
        model.addAttribute("keyword", keyword);

        return "index";
    }

    @PostMapping("/product/modify/controller")
    public String modifyProduct(@RequestParam("id") Integer id,
                                @RequestParam("name") String name,
                                @RequestParam("description") String description,
                                @RequestParam("price") Integer price,
                                @RequestParam("quantity") Integer quantity,
                                @RequestParam("photo") MultipartFile photo) {
        byte[] photoForUpload = null;
        if (!photo.isEmpty()) {
            try {
                photoForUpload = photo.getBytes();
            } catch (IOException e) {
                log.error("圖片上傳失敗", e);
            }
        }

        productService.modifyProduct(id, name, description, price, quantity, photoForUpload);

        return "redirect:/product/manage";
    }
}
