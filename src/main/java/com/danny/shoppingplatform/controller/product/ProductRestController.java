package com.danny.shoppingplatform.controller.product;

import com.danny.shoppingplatform.model.Member;
import com.danny.shoppingplatform.model.Product;
import com.danny.shoppingplatform.service.MemberService;
import com.danny.shoppingplatform.service.ProductService;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class ProductRestController {
    private static final Logger logger = LoggerFactory.getLogger(ProductRestController.class);
    private final ProductService productService;
    private final MemberService memberService;

    public ProductRestController(ProductService productService, MemberService memberService) {
        this.productService = productService;
        this.memberService = memberService;
    }

    @GetMapping(value = "/product/{id}/photo", produces = MediaType.IMAGE_JPEG_VALUE)
    public byte[] getProductImage(@PathVariable Integer id) {
        return productService.findById(id).getPhoto();
    }

    @GetMapping("/api/product")
    public ResponseEntity<?> getProducts() {
        List<Product> productList = productService.findAll();
        return ResponseEntity.ok(productList);
    }

    @GetMapping("/api/product/{id}")
    public ResponseEntity<?> getProduct(@PathVariable Integer id) {
        Product product = productService.findById(id);
        return ResponseEntity.ok(product);
    }

    @GetMapping("/api/product/vendor")
    public ResponseEntity<?> getVendorProducts() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String account = auth.getName();
        List<Product> productList = productService.findByVendorAccount(account);

        System.out.println("目前登入帳號：" + account);

        return ResponseEntity.ok(productList);
    }

    @GetMapping("/api/product/filter")
    public ResponseEntity<?> getProducts(
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(required = false) String keyword) {

        Pageable pageable = PageRequest.of(page, size);
        Page<Product> productPage;

        if (keyword != null && !keyword.isBlank()) {
            productPage = productService.findByNameContaining(keyword, pageable);
        } else {
            productPage = productService.findAllByPageable(pageable);
        }

        Map<String, Object> response = new HashMap<>();
        response.put("products", productPage.getContent());
        response.put("totalPages", productPage.getTotalPages());
        response.put("totalElements", productPage.getTotalElements());
        response.put("page", productPage.getNumber());
        response.put("size", productPage.getSize());
        response.put("keyword", keyword);

        return ResponseEntity.ok(response);
    }

    @PostMapping(value = "/api/product", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> addProduct(@RequestParam("name") String name,
                                        @RequestParam("description") String description,
                                        @RequestParam("price") Integer price,
                                        @RequestParam("quantity") Integer quantity,
                                        @RequestPart(value = "photo", required = false) MultipartFile photo) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String account = auth.getName();
        Member member = memberService.findByAccount(account);

        byte[] photoForUpload = null;

        if (photo != null && !photo.isEmpty()) {
            try {
                photoForUpload = photo.getBytes();
            } catch (IOException e) {
                logger.error("圖片上傳失敗", e);
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("error", "圖片處理失敗"));
            }
        }

        Product product = productService.addProduct(name, description, member.getId(), price, quantity, photoForUpload);

        return ResponseEntity.status(HttpStatus.CREATED).body(product);
    }

    @PutMapping(value = "/api/product/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> modifyProduct(@PathVariable Integer id,
                                           @RequestParam("name") String name,
                                           @RequestParam("description") String description,
                                           @RequestParam("price") Integer price,
                                           @RequestParam("quantity") Integer quantity,
                                           @RequestPart(value = "photo", required = false) MultipartFile photo) {

        byte[] photoForUpload = null;

        // 即使沒傳圖片，也會回傳 byte[0]，會覆蓋null，所以要先判斷 isEmpty()
        if (photo != null && !photo.isEmpty()) {
            try {
                photoForUpload = photo.getBytes();
            } catch (IOException e) {
                logger.error("圖片上傳失敗", e);
            }
        }

        Product product = productService.modifyProduct(id, name, description, price, quantity, photoForUpload);
        return ResponseEntity.ok(product);
    }

    @DeleteMapping("/api/product/{id}")
    public ResponseEntity<?> deleteProduct(@PathVariable Integer id) {
        productService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
