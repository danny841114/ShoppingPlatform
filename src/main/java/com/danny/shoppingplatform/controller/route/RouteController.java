package com.danny.shoppingplatform.controller.route;

import com.danny.shoppingplatform.model.Member;
import com.danny.shoppingplatform.model.Product;
import com.danny.shoppingplatform.service.ProductService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class RouteController {
    private final ProductService productService;

    public RouteController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping({"/index", "/"})
    public String index(Model model) {
        List<Product> productList = productService.findAll();
        model.addAttribute("productList", productList);

        /* size、page及totalPages需給預設值，否則會出錯 */
        model.addAttribute("size", 5);
        model.addAttribute("page", 0);
        model.addAttribute("totalPages", 1);

        return "/index";
    }

    @GetMapping("/secure/login")
    public String login() {
        return "/secure/login";
    }

    @GetMapping("/secure/register")
    public String register() {
        return "/secure/register";
    }

    @GetMapping("/product/add")
    public String addProduct() {
        return "/product/add_product";
    }

    @GetMapping("/product/manage")
    public String manageProduct(Model model, HttpServletRequest request) {
        String account = (String) request.getAttribute("account");
        List<Product> productList = productService.findByVendorAccount(account);
        model.addAttribute("productList", productList);
        return "/product/manage_product";
    }

    @GetMapping("/product/modify")
    public String modifyProduct(Model model, @RequestParam Integer id) {
        Product product = productService.findById(id);
        model.addAttribute("product", product);
        return "/product/modify_product";
    }

    @GetMapping(value = "/product/{id}/photo", produces = MediaType.IMAGE_JPEG_VALUE)
    @ResponseBody
    public byte[] getProductImage(@PathVariable Integer id) {
        Product product = productService.findById(id);
        return product.getPhoto();
    }
}
