package com.danny.shoppingplatform.service;

import com.danny.shoppingplatform.dto.product.ProductCreateRequest;
import com.danny.shoppingplatform.dto.product.ProductDto;
import com.danny.shoppingplatform.dto.product.ProductModifyRequest;
import com.danny.shoppingplatform.dto.product.ProductPageDto;
import com.danny.shoppingplatform.exception.custom.InternalServerException;
import com.danny.shoppingplatform.model.Vendor;
import com.danny.shoppingplatform.repository.ProductRepository;
import com.danny.shoppingplatform.model.Product;
import com.danny.shoppingplatform.repository.VendorRepository;
import com.danny.shoppingplatform.util.ImageHelper;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.time.Instant;
import java.util.*;

@Slf4j
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class ProductService {
    private final ProductRepository productRepository;
    private final VendorRepository vendorRepository;

    public ProductDto getProductById(Long id) {
        Product product = getById(id);
        Long vendorId = product.getVendor().getId();
        String shopName = product.getVendor().getShopName();
        return ProductDto.fromEntity(product, vendorId, shopName);
    }

    public byte[] getProductPhotoById(Integer id) {
        return productRepository.findPhotoById(id)
                .orElseThrow(() -> new EntityNotFoundException("Product photo not found"));
    }

    @Transactional
    public void deleteProduct(Long id, String account) {
        Product product = getById(id);
        Vendor vendor = getVendorByAccount(account);
        if (!vendor.getId().equals(product.getVendor().getId())) {
            throw new AuthorizationDeniedException("Product owner and current vendor does not match");
        }

        productRepository.delete(product);
    }

    @Transactional
    public ProductDto addProduct(ProductCreateRequest request, String account) {
        byte[] photoByteArray = null;
        if (request.getPhoto() != null && !request.getPhoto().isEmpty()) {
            try {
                photoByteArray = ImageHelper.convertImageToByte(request.getPhoto());
            } catch (IOException e) {
                log.error("Failed to convert image for new product by user: {}", account, e);
                throw new InternalServerException("Upload image failed");
            }
        }

        Vendor vendor = getVendorByAccount(account);

        Product product = new Product();
        product.setName(request.getName());
        product.setDescription(request.getDescription());
        product.setVendor(vendor);
        product.setPrice(request.getPrice());
        product.setQuantity(request.getQuantity());
        product.setDate(Instant.now());
        product.setPhoto(photoByteArray);
        Product savedProduct = productRepository.save(product);

        // avoid N+1 query problem
        Long vendorId = savedProduct.getVendor().getId();
        String shopName = savedProduct.getVendor().getShopName();

        return ProductDto.fromEntity(savedProduct, vendorId, shopName);
    }

    @Transactional
    public void modifyProduct(Long id, ProductModifyRequest request, String account) {
        Product product = getById(id);
        Vendor vendor = getVendorByAccount(account);
        if (!vendor.getId().equals(product.getVendor().getId())) {
            throw new AuthorizationDeniedException("Product owner and current vendor does not match");
        }

        if (request.getPhoto() != null && !request.getPhoto().isEmpty()) {
            try {
                byte[] photoByteArray = ImageHelper.convertImageToByte(request.getPhoto());
                product.setPhoto(photoByteArray);
            } catch (IOException e) {
                throw new InternalServerException("Upload image failed");
            }
        }

        product.setName(request.getName());
        product.setDescription(request.getDescription());
        product.setPrice(request.getPrice());
        product.setQuantity(request.getQuantity());

        productRepository.save(product);
    }

    public List<ProductDto> getProductsByVendor(String account) {
        Vendor vendor = getVendorByAccount(account);
        List<Product> products = productRepository.findByVendor(vendor);

        // avoid N+1 query problem
        Long vendorId = vendor.getId();
        String shopName = vendor.getShopName();

        return products.stream()
                .map(product -> ProductDto.fromEntity(product, vendorId, shopName))
                .toList();
    }

    public ProductPageDto getProducts(Pageable pageable, String keyword) {
        Page<Product> productPage;
        if (keyword != null && !keyword.isBlank()) {
            productPage = productRepository.findByNameContainingOrVendorShopNameContaining(keyword, keyword, pageable);
        } else {
            productPage = productRepository.findAll(pageable);
        }

        return ProductPageDto.fromEntity(productPage);
    }

    private Product getById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Product with ID '%s' not found ".formatted(id)));
    }

    private Vendor getVendorByAccount(String account) {
        return vendorRepository.findByUserAccount(account)
                .orElseThrow(() -> new UsernameNotFoundException("Vendor with account '%s' not found".formatted(account)));
    }
}
