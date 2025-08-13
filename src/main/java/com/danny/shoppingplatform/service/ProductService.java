package com.danny.shoppingplatform.service;

import com.danny.shoppingplatform.repository.MemberRepository;
import com.danny.shoppingplatform.repository.ProductRepository;
import com.danny.shoppingplatform.model.Member;
import com.danny.shoppingplatform.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Pageable;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class ProductService {
    private final ProductRepository productRepository;
    private final MemberRepository memberRepository;

    public ProductService(ProductRepository productRepository, MemberRepository memberRepository) {
        this.productRepository = productRepository;
        this.memberRepository = memberRepository;
    }

    public Product addProduct(String name, String description, Integer vendorId, Integer price, Integer quantity, byte[] photo) {
        Product product = new Product();
        product.setName(name);
        product.setDescription(description);
        product.setMember(memberRepository.findById(vendorId).orElse(null));
        product.setPrice(price);
        product.setQuantity(quantity);
        product.setDate(new Date());
        product.setPhoto(photo);
        productRepository.save(product);
        return product;
    }

    public Product modifyProduct(Integer id, String name, String description, Integer price, Integer quantity, byte[] photo) {
        Product product = productRepository.findById(id).orElse(null);
        if (product != null) {
            product.setName(name);
            product.setDescription(description);
            product.setPrice(price);
            product.setQuantity(quantity);
            product.setDate(new Date());
            if (photo != null) {
                product.setPhoto(photo);
            }
            productRepository.save(product);
        }
        return product;
    }

    public Product findById(Integer id) {
        return productRepository.findById(id).orElse(null);
    }

    public List<Product> findAll() {
        return productRepository.findAll();
    }

    public List<Product> findByVendorAccount(String account) {
        Member member = memberRepository.findByAccount(account);
        return productRepository.findByMember(member);
    }

    public Page<Product> findAllByPageable(Pageable pageable) {
        return productRepository.findAll(pageable);
    }

    public Page<Product> findByNameContaining(String keyword, Pageable pageable) {
        Page<Product> productsByNameContaining = productRepository.findByNameContaining(keyword, pageable);
        Member member = memberRepository.findByAccount(keyword);
        List<Product> productsByMember = productRepository.findByMember(member);

        // 合併並去掉重複
        List<Product> mergedList  = Stream
                .concat(productsByNameContaining.stream(), productsByMember.stream())
                .distinct()
                .toList();

        // 處理分頁
        int start = (int) pageable.getOffset();
        int end = Math.min(start + pageable.getPageSize(), mergedList.size());
        List<Product> pageContent = mergedList.subList(start, end);

        return new PageImpl<>(pageContent, pageable, mergedList.size());
    }

    public void deleteById(Integer id) {
        productRepository.deleteById(id);
    }
}
