package com.danny.shoppingplatform.controller.product;

import com.danny.shoppingplatform.annotation.CurrentAccount;
import com.danny.shoppingplatform.dto.product.ProductCreateRequest;
import com.danny.shoppingplatform.dto.product.ProductDto;
import com.danny.shoppingplatform.dto.product.ProductModifyRequest;
import com.danny.shoppingplatform.dto.product.ProductPageDto;
import com.danny.shoppingplatform.service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/product")
public class ProductController {
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

    @GetMapping("/vendor")
    public ResponseEntity<List<ProductDto>> getProductsByVendor(@CurrentAccount String account) {
        List<ProductDto> products = productService.getProductsByVendor(account);
        return ResponseEntity.ok(products);
    }

    @GetMapping
    public ResponseEntity<ProductPageDto> getProducts(@RequestParam(defaultValue = "10") int size,
                                                      @RequestParam(defaultValue = "0") int page,
                                                      @RequestParam(required = false) String keyword) {
        Pageable pageable = PageRequest.of(page, size);
        ProductPageDto pageDto = productService.getProducts(pageable, keyword);
        return ResponseEntity.ok(pageDto);
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ProductDto> addProduct(@ModelAttribute ProductCreateRequest request, @CurrentAccount String account) {
        ProductDto productDto = productService.addProduct(request, account);
        return ResponseEntity.status(HttpStatus.CREATED).body(productDto);
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