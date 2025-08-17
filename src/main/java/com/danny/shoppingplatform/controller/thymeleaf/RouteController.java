package com.danny.shoppingplatform.controller.thymeleaf;

import com.danny.shoppingplatform.model.Cart;
import com.danny.shoppingplatform.model.Member;
import com.danny.shoppingplatform.model.Product;
import com.danny.shoppingplatform.service.CartService;
import com.danny.shoppingplatform.service.MemberService;
import com.danny.shoppingplatform.service.ProductService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class RouteController {
    private final MemberService memberService;
    private final ProductService productService;
    private final CartService cartService;

    public RouteController(ProductService productService, MemberService memberService, CartService cartService) {
        this.memberService = memberService;
        this.productService = productService;
        this.cartService = cartService;
    }

    @GetMapping({"/index", "/"})
    public String index(Model model) {
        List<Product> productList = productService.findAll();
        model.addAttribute("productList", productList);

        /* size、page及totalPages需給預設值，否則會出錯 */
        model.addAttribute("size", 5);
        model.addAttribute("page", 0);
        model.addAttribute("totalPages", 1);

        return "index";
    }

    @GetMapping("/login")
    public String login() {
        return "member/login";
    }

    @GetMapping("/register")
    public String register() {
        return "member/register";
    }

    @GetMapping("/profile")
    public String profile(Model model, HttpServletRequest request) {
        String account = (String) request.getAttribute("account");
        Member member = memberService.findByAccount(account);
        model.addAttribute("member", member);
        return "member/profile";
    }

    @ResponseBody
    @GetMapping(value = "/product/{id}/photo", produces = MediaType.IMAGE_JPEG_VALUE)
    public byte[] getProductImage(@PathVariable Integer id) {
        return productService.findById(id).getPhoto();
    }

    @GetMapping("/product/add")
    public String addProduct() {
        return "product/add_product";
    }

    @GetMapping("/product/manage")
    public String manageProduct(Model model, HttpServletRequest request) {
        String account = (String) request.getAttribute("account");
        List<Product> productList = productService.findByVendorAccount(account);
        model.addAttribute("productList", productList);
        return "product/manage_product";
    }

    @GetMapping("/product/modify")
    public String modifyProduct(Model model, @RequestParam Integer id) {
        Product product = productService.findById(id);
        model.addAttribute("product", product);
        return "product/modify_product";
    }

    @GetMapping("/cart")
    public String cart(Model model, HttpServletRequest request) {
        String account = (String) request.getAttribute("account");
        Member member = memberService.findByAccount(account);
        List<Cart> cartList = cartService.getCartListByMember(member.getId());
        model.addAttribute("cartList", cartList);
        return "cart/cart";
    }
}
