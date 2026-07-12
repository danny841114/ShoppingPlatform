package com.danny.shoppingplatform.service;

import com.danny.shoppingplatform.dto.product.ProductCreateRequest;
import com.danny.shoppingplatform.dto.product.ProductDto;
import com.danny.shoppingplatform.dto.product.ProductModifyRequest;
import com.danny.shoppingplatform.dto.product.ProductPageDto;
import com.danny.shoppingplatform.exception.InternalServerException;
import com.danny.shoppingplatform.repository.MemberRepository;
import com.danny.shoppingplatform.repository.ProductRepository;
import com.danny.shoppingplatform.model.Member;
import com.danny.shoppingplatform.model.Product;
import com.danny.shoppingplatform.util.ImageHelper;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.*;
import java.util.stream.Stream;

@Slf4j
@RequiredArgsConstructor
@Service
public class ProductService {
    private final ProductRepository productRepository;
    private final MemberRepository memberRepository;

    public Product findById(Integer id) {
        return productRepository.findById(id).orElse(null);
    }

    public List<Product> findAll() {
        return productRepository.findAll();
    }

    public ProductPageDto findAllByPageable(Pageable pageable) {
        Page<Product> productPage = productRepository.findAll(pageable);

        return ProductPageDto.builder()
                .products(productPage.getContent())
                .totalPages(productPage.getTotalPages())
                .totalElements(productPage.getTotalElements())
                .page(pageable.getPageNumber())
                .size(productPage.getSize())
                .build();
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
    public ProductDto addProduct(ProductCreateRequest request, String currentAccount) {
        byte[] photoByteArray = null;
        if (request.getPhoto() != null && !request.getPhoto().isEmpty()) {
            try {
                photoByteArray = ImageHelper.convertImageToByte(request.getPhoto());
            } catch (IOException e) {
                log.error("Failed to convert image for new product by user: {}", currentAccount, e);
                throw new InternalServerException("Upload image failed");
            }
        }

        Member member = memberRepository.findByAccount(currentAccount);
        if (member == null) {
            log.error("Member with account '{}' not found", currentAccount);
            throw new EntityNotFoundException("Member with account '%s' not found".formatted(currentAccount));
        }

        Product product = new Product();
        product.setName(request.getName());
        product.setDescription(request.getDescription());
        product.setMember(member);
        product.setPrice(request.getPrice());
        product.setQuantity(request.getQuantity());
        product.setDate(new Date());
        product.setPhoto(photoByteArray);

        Product savedProduct = productRepository.save(product);

        return ProductDto.builder()
                .id(savedProduct.getId())
                .name(savedProduct.getName())
                .description(savedProduct.getDescription())
                .price(savedProduct.getPrice())
                .quantity(savedProduct.getQuantity())
                .date(savedProduct.getDate())
                .photo(savedProduct.getPhoto())
                .build();
    }

    @Transactional
    public void modifyProduct(Integer id, ProductModifyRequest request, String currentAccount) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Product not found with id: %s".formatted(id)));

        if (!product.getMember().getAccount().equals(currentAccount)) {
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

    public List<Product> findByVendorAccount(String account) {
        Member member = memberRepository.findByAccount(account);
        return productRepository.findByMember(member);
    }

    public ProductPageDto findByNameContaining(String keyword, Pageable pageable) {
        // 符合關鍵字的商品
        Page<Product> productsByNameContaining = productRepository.findByNameContaining(keyword, pageable);

        // 符合關鍵字的使用者
        Set<Product> productsByMember = new HashSet<>();
        List<Member> memberList = memberRepository.findByAccountContaining(keyword);
        for (Member member : memberList) {
            List<Product> products = productRepository.findByMember(member);
            productsByMember.addAll(products);
        }

        // 合併並去掉重複
        List<Product> mergedList = Stream
                .concat(productsByNameContaining.stream(), productsByMember.stream())
                .distinct()
                .toList();

        // 處理分頁
        // offset = pageNumber * pageSize (index 從 0 開始)
        // pageNumber 從 0 開始
        int start = (int) pageable.getOffset();
        int end = Math.min(start + pageable.getPageSize(), mergedList.size());
        List<Product> pageContent = mergedList.subList(start, end);

        PageImpl<Product> productPage = new PageImpl<>(pageContent, pageable, mergedList.size());

        return ProductPageDto.builder()
                .products(productPage.getContent())
                .totalPages(productPage.getTotalPages())
                .totalElements(productPage.getTotalElements())
                .page(pageable.getPageNumber())
                .size(productPage.getSize())
                .build();
    }
}
