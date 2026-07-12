package com.danny.shoppingplatform.controller.product;

import com.danny.shoppingplatform.dto.product.ProductCreateRequest;
import com.danny.shoppingplatform.dto.product.ProductDto;
import com.danny.shoppingplatform.dto.product.ProductModifyRequest;
import com.danny.shoppingplatform.dto.product.ProductPageDto;
import com.danny.shoppingplatform.model.Product;
import com.danny.shoppingplatform.service.MemberService;
import com.danny.shoppingplatform.service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/product")
public class ProductRestController {
    private final ProductService productService;
    private final MemberService memberService;

    @GetMapping(value = "/{id}/photo", produces = MediaType.IMAGE_JPEG_VALUE)
    public ResponseEntity<byte[]> getProductImage(@PathVariable Integer id) {
        byte[] photo = productService.findById(id).getPhoto();
        return ResponseEntity.ok(photo);
    }

    @GetMapping("")
    public ResponseEntity<List<Product>> getProducts() {
        List<Product> productList = productService.findAll();
        return ResponseEntity.ok(productList);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Product> getProduct(@PathVariable Integer id) {
        Product product = productService.findById(id);
        return ResponseEntity.ok(product);
    }

    @GetMapping("/vendor")
    public ResponseEntity<List<Product>> getVendorProducts() {
        String account = memberService.getLoginMember().getAccount();
        List<Product> productList = productService.findByVendorAccount(account);
        return ResponseEntity.ok(productList);
    }

    @GetMapping("/filter")
    public ResponseEntity<ProductPageDto> getProducts(@RequestParam(defaultValue = "5") int size,
                                                      @RequestParam(defaultValue = "0") int page,
                                                      @RequestParam(required = false) String keyword) {
        Pageable pageable = PageRequest.of(page, size);
        ProductPageDto pageDto;

        if (keyword != null && !keyword.isBlank()) {
            pageDto = productService.findByNameContaining(keyword, pageable);
        } else {
            pageDto = productService.findAllByPageable(pageable);
        }

        return ResponseEntity.ok(pageDto);
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ProductDto> addProduct(@ModelAttribute ProductCreateRequest request) {
        String currentAccount = SecurityContextHolder.getContext().getAuthentication().getName();
        ProductDto productDto = productService.addProduct(request, currentAccount);
        return ResponseEntity.status(HttpStatus.CREATED).body(productDto);
    }

    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> modifyProduct(@PathVariable Integer id, @ModelAttribute ProductModifyRequest request) {
        String currentAccount = SecurityContextHolder.getContext().getAuthentication().getName();
        productService.modifyProduct(id, request, currentAccount);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteProduct(@PathVariable Integer id) {
        String currentAccount = SecurityContextHolder.getContext().getAuthentication().getName();
        productService.deleteProduct(id, currentAccount);
        return ResponseEntity.noContent().build();
    }
}