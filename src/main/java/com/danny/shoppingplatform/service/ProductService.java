package com.danny.shoppingplatform.service;

import com.danny.shoppingplatform.dao.MemberDao;
import com.danny.shoppingplatform.dao.ProductDao;
import com.danny.shoppingplatform.model.Member;
import com.danny.shoppingplatform.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Pageable;

import java.util.Date;
import java.util.List;

@Service
public class ProductService {
    private final ProductDao productDao;
    private final MemberDao memberDao;

    public ProductService(ProductDao productDao, MemberDao memberDao) {
        this.productDao = productDao;
        this.memberDao = memberDao;
    }

    public Product addProduct(String name, String description, Integer vendorId,
                              Integer price, Integer quantity, byte[] photo) {
        Product product = new Product();
        product.setName(name);
        product.setDescription(description);
        product.setMember(memberDao.findById(vendorId).orElse(null));
        product.setPrice(price);
        product.setQuantity(quantity);
        product.setDate(new Date());
        product.setPhoto(photo);
        productDao.save(product);
        return product;
    }

    public Product modifyProduct(Integer id, String name, String description,
                                 Integer price, Integer quantity, byte[] photo) {
        Product product = productDao.findById(id).orElse(null);
        if (product != null) {
            product.setName(name);
            product.setDescription(description);
            product.setPrice(price);
            product.setQuantity(quantity);
            product.setDate(new Date());
            if (photo != null) {
                product.setPhoto(photo);
            }
            productDao.save(product);
        }
        return product;
    }

    public Product findById(Integer id) {
        return productDao.findById(id).orElse(null);
    }

    public List<Product> findAll() {
        return productDao.findAll();
    }

    public List<Product> findByVendorAccount(String account) {
        Member member = memberDao.findByAccount(account);
        return productDao.findByMember(member);
    }

    public Page<Product> findAllByPageable(Pageable pageable) {
        return productDao.findAll(pageable);
    }

    public Page<Product> findByNameContaining(String name, Pageable pageable) {
        return productDao.findByNameContaining(name, pageable);
    }

    public void deleteById(Integer id) {
        productDao.deleteById(id);
    }
}
