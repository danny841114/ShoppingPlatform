package com.danny.shoppingplatform.service;

import com.danny.shoppingplatform.repository.MemberRepository;
import com.danny.shoppingplatform.repository.ProductRepository;
import com.danny.shoppingplatform.model.Member;
import com.danny.shoppingplatform.model.Product;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Pageable;

import javax.security.auth.login.AccountNotFoundException;
import java.util.*;
import java.util.stream.Stream;

@Service
public class ProductService {
    private final ProductRepository productRepository;
    private final MemberRepository memberRepository;

    public ProductService(ProductRepository productRepository, MemberRepository memberRepository) {
        this.productRepository = productRepository;
        this.memberRepository = memberRepository;
    }

    public Product findById(Integer id) {
        return productRepository.findById(id).orElse(null);
    }

    public List<Product> findAll() {
        return productRepository.findAll();
    }

    public Page<Product> findAllByPageable(Pageable pageable) {
        return productRepository.findAll(pageable);
    }

    public void deleteById(Integer id) {
        productRepository.deleteById(id);
    }

    public Product addProduct(String name, String description,
                              Integer vendorId,
                              Integer price,
                              Integer quantity,
                              byte[] photo) throws AccountNotFoundException {
        Optional<Member> memberOptional = memberRepository.findById(vendorId);
        if (memberOptional.isEmpty()) {
            throw new AccountNotFoundException();
        }

        Product product = new Product();
        product.setName(name);
        product.setDescription(description);
        product.setMember(memberOptional.get());
        product.setPrice(price);
        product.setQuantity(quantity);
        product.setDate(new Date());
        product.setPhoto(photo);

        productRepository.save(product);
        return product;
    }

    public Product modifyProduct(Integer id,
                                 String name,
                                 String description,
                                 Integer price,
                                 Integer quantity,
                                 byte[] photo) {
        Optional<Product> productOptional = productRepository.findById(id);
        if (productOptional.isEmpty()) {
            throw new EntityNotFoundException();
        }

        Product product = productOptional.get();
        product.setName(name);
        product.setDescription(description);
        product.setPrice(price);
        product.setQuantity(quantity);
        product.setDate(new Date());
        if (photo != null) {
            product.setPhoto(photo);
        }

        productRepository.save(product);
        return product;
    }

    public List<Product> findByVendorAccount(String account) {
        Member member = memberRepository.findByAccount(account);
        return productRepository.findByMember(member);
    }

    public Page<Product> findByNameContaining(String keyword, Pageable pageable) {
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

        return new PageImpl<>(pageContent, pageable, mergedList.size());
    }
}
