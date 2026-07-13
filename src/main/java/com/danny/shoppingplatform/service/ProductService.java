package com.danny.shoppingplatform.service;

import com.danny.shoppingplatform.dto.product.ProductCreateRequest;
import com.danny.shoppingplatform.dto.product.ProductDto;
import com.danny.shoppingplatform.dto.product.ProductModifyRequest;
import com.danny.shoppingplatform.dto.product.ProductPageDto;
import com.danny.shoppingplatform.exception.custom.InternalServerException;
import com.danny.shoppingplatform.repository.MemberRepository;
import com.danny.shoppingplatform.repository.ProductRepository;
import com.danny.shoppingplatform.model.Member;
import com.danny.shoppingplatform.model.Product;
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
import java.util.*;

@Slf4j
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class ProductService {
    private final ProductRepository productRepository;
    private final MemberRepository memberRepository;

    public ProductDto getProductById(Integer id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Product not found"));
        return ProductDto.fromEntity(product, product.getMember().getId());
    }

    public byte[] getProductPhotoById(Integer id) {
        return productRepository.findPhotoById(id)
                .orElseThrow(() -> new EntityNotFoundException("Product photo not found"));
    }

    @Transactional
    public void deleteProduct(Integer id, String currentAccount) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Product with id '%s' not found ".formatted(id)));

        if (!product.getMember().getAccount().equals(currentAccount)) {
            log.error("[deleteProduct] Wrong account from member");
            throw new AuthorizationDeniedException("Wrong account from member");
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

        Member member = memberRepository.findByAccount(account)
                .orElseThrow(() -> new UsernameNotFoundException("Account '%s' not found".formatted(account)));

        Product product = new Product();
        product.setName(request.getName());
        product.setDescription(request.getDescription());
        product.setMember(member);
        product.setPrice(request.getPrice());
        product.setQuantity(request.getQuantity());
        product.setDate(new Date());
        product.setPhoto(photoByteArray);

        Product savedProduct = productRepository.save(product);

        // avoid N+1 query problem
        Integer vendorId = savedProduct.getMember().getId();

        return ProductDto.fromEntity(savedProduct, vendorId);
    }

    @Transactional
    public void modifyProduct(Integer id, ProductModifyRequest request, String account) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Product not found with id: %s".formatted(id)));

        if (!product.getMember().getAccount().equals(account)) {
            log.error("Wrong account from member");
            throw new AuthorizationDeniedException("Wrong account from member");
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
        Member member = memberRepository.findByAccount(account)
                .orElseThrow(() -> new UsernameNotFoundException("Account '%s' not found".formatted(account)));

        List<Product> products = productRepository.findByMember(member);

        // avoid N+1 query problem
        Integer vendorId = member.getId();

        return products.stream()
                .map(product -> ProductDto.fromEntity(product, vendorId))
                .toList();
    }

    public ProductPageDto getProducts(Pageable pageable, String keyword) {
        Page<Product> productPage;
        if (keyword != null && !keyword.isBlank()) {
            productPage = productRepository.findByNameOrMemberAccountContaining(keyword, pageable);
        } else {
            productPage = productRepository.findAll(pageable);
        }

        return ProductPageDto.fromEntity(productPage);
    }
}
