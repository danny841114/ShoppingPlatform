package com.danny.shoppingplatform.controller;

import com.danny.shoppingplatform.dto.product.ProductDto;
import com.danny.shoppingplatform.dto.product.ProductPageDto;
import com.danny.shoppingplatform.service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/public/products")
public class ProductPublicController {
    private final ProductService productService;

    @GetMapping(value = "/{id}/photo", produces = MediaType.IMAGE_JPEG_VALUE)
    public ResponseEntity<byte[]> getProductImageById(@PathVariable Integer id) {
        byte[] photo = productService.getProductPhotoById(id);
        return ResponseEntity.ok(photo);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductDto> getProductById(@PathVariable Long id) {
        ProductDto productDto = productService.getProductById(id);
        return ResponseEntity.ok(productDto);
    }

    @GetMapping
    public ResponseEntity<ProductPageDto> getProducts(@RequestParam(defaultValue = "10") int size,
                                                      @RequestParam(defaultValue = "0") int page,
                                                      @RequestParam(required = false) String keyword) {
        Pageable pageable = PageRequest.of(page, size);
        ProductPageDto pageDto = productService.getProducts(pageable, keyword);
        return ResponseEntity.ok(pageDto);
    }
}