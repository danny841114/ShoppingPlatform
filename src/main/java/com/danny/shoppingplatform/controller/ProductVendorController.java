package com.danny.shoppingplatform.controller;

import com.danny.shoppingplatform.annotation.CurrentAccount;
import com.danny.shoppingplatform.dto.product.ProductCreateRequest;
import com.danny.shoppingplatform.dto.product.ProductDto;
import com.danny.shoppingplatform.dto.product.ProductModifyRequest;
import com.danny.shoppingplatform.service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/vendor/products")
public class ProductVendorController {
    private final ProductService productService;

    @GetMapping
    public ResponseEntity<List<ProductDto>> getProductsByVendor(@CurrentAccount String account) {
        List<ProductDto> products = productService.getProductsByVendor(account);
        return ResponseEntity.ok(products);
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ProductDto> addProduct(@ModelAttribute ProductCreateRequest request, @CurrentAccount String account) {
        ProductDto productDto = productService.addProduct(request, account);
        return ResponseEntity.status(HttpStatus.CREATED).body(productDto);  // TODO: need to fix
    }

    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> modifyProduct(@PathVariable Long id, @ModelAttribute ProductModifyRequest request, @CurrentAccount String account) {
        productService.modifyProduct(id, request, account);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteProduct(@PathVariable Long id, @CurrentAccount String account) {
        productService.deleteProduct(id, account);
        return ResponseEntity.noContent().build();
    }
}