package com.danny.shoppingplatform.service;

import com.danny.shoppingplatform.repository.MemberRepository;
import com.danny.shoppingplatform.repository.ProductRepository;
import com.danny.shoppingplatform.model.Member;
import com.danny.shoppingplatform.model.Product;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ProductServiceTest {
    @Mock
    private MemberRepository memberRepository;

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductService productService;

    @Captor
    ArgumentCaptor<Product> productCaptor;

    @Test
    public void testAddProduct() {
        // Arrange
        String name = "Test Product";
        String description = "This is a test";
        Integer vendorId = 1;
        Integer price = 100;
        Integer quantity = 10;
        byte[] photo = new byte[]{1, 2, 3};

        Member mockMember = new Member();
        mockMember.setId(vendorId);

        when(memberRepository.findById(vendorId)).thenReturn(Optional.of(mockMember));
        when(productRepository.save(any(Product.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        Product result = productService.addProduct(name, description, vendorId, price, quantity, photo);

        // Verify
        verify(memberRepository).findById(vendorId);
        verify(productRepository).save(productCaptor.capture());

        // Assert
        assertNotNull(result);
        assertEquals(name, result.getName());
        assertEquals(description, result.getDescription());
        assertEquals(price, result.getPrice());
        assertEquals(quantity, result.getQuantity());
        assertArrayEquals(photo, result.getPhoto());
        assertEquals(mockMember, result.getMember());
        Product savedProduct = productCaptor.getValue();
        assertEquals(name, savedProduct.getName());
    }

    @Test
    public void testModifyProduct() {
        // Arrange
        Integer id = 2;
        String name = "Test Product";
        String description = "This is a test";
        Integer price = 20;
        Integer quantity = 100;
        byte[] photo = new byte[]{4, 5, 6};

        Product mockProduct = new Product();
        mockProduct.setId(id);
        mockProduct.setName("Old Name");
        mockProduct.setDescription("Old Desc");
        mockProduct.setPrice(999);
        mockProduct.setQuantity(999);
        mockProduct.setPhoto(new byte[]{0, 0, 0});

        when(productRepository.findById(id)).thenReturn(Optional.of(mockProduct));
        when(productRepository.save(any(Product.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        Product result = productService.modifyProduct(id, name, description, price, quantity, photo);

        // Assert
        assertNotNull(result);
        assertEquals(id, result.getId());
        assertEquals(name, result.getName());
        assertEquals(description, result.getDescription());
        assertEquals(price, result.getPrice());
        assertEquals(quantity, result.getQuantity());
        assertArrayEquals(photo, result.getPhoto());

        // Verify
        verify(productRepository).findById(id);
    }

    @Test
    public void testFindById() throws Exception {
        // Arrange
        Product product = new Product();
        product.setId(1);
        product.setName("John");

        when(productRepository.findById(1)).thenReturn(Optional.of(product));

        // Act
        Product resultProduct = productService.findById(1);

        // Assert
        assertNotNull(resultProduct);
        assertEquals(Integer.valueOf(1), resultProduct.getId());
        assertEquals("John", resultProduct.getName());
    }

    @Test
    public void testFindAll() {
        // Arrange

        Product p1 = new Product();
        p1.setName("Product A");
        Product p2 = new Product();
        p2.setName("Product B");
        List<Product> mockProducts = Arrays.asList(p1, p2);

        when(productRepository.findAll()).thenReturn(mockProducts);

        // Act
        List<Product> result = productService.findAll();

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("Product A", result.getFirst().getName());

        // Verify
        verify(productRepository).findAll();
    }

    @Test
    public void testFindByVendorAccount() {
        // Arrange
        String account = "vendor123";
        Member mockMember = new Member();
        mockMember.setId(1);
        mockMember.setAccount(account);

        Product p1 = new Product();
        p1.setName("Product A");
        Product p2 = new Product();
        p2.setName("Product B");
        List<Product> mockProducts = Arrays.asList(p1, p2);

        when(memberRepository.findByAccount(account)).thenReturn(mockMember);
        when(productRepository.findByMember(mockMember)).thenReturn(mockProducts);

        // Act
        List<Product> result = productService.findByVendorAccount(account);

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("Product A", result.getFirst().getName());

        // Verify
        verify(memberRepository).findByAccount(account);
        verify(productRepository).findByMember(mockMember);
    }

    @Test
    public void testFindAllByPageable() {
        // Arrange
        Pageable pageable = PageRequest.of(0, 10);

        List<Product> products = new ArrayList<>();
        for(int i=1; i<=10; i++) {
            Product product = new Product();
            product.setName("Product " + i);
            products.add(product);
        }

        Page<Product> mockPage = new PageImpl<>(products, pageable, products.size());
        when(productRepository.findAll(pageable)).thenReturn(mockPage);

        // Act
        Page<Product> result = productService.findAllByPageable(pageable);

        // Assert
        assertNotNull(result);
        assertEquals(10, result.getContent().size());
        assertEquals("Product 1", result.getContent().getFirst().getName());

        // Verify
        verify(productRepository).findAll(pageable);
    }

//    @Test
//    public void testFindByNameContaining(){
//        // Arrange
//        String name = "Product A";
//
//        Pageable pageable = PageRequest.of(0, 10);
//
//        Product productA = new Product();
//        productA.setName("Product A");
//        List<Product> products = List.of(productA);
//
//        Page<Product> mockPage = new PageImpl<>(products, pageable, products.size());
//        when(productDao.findByNameContaining(name,pageable)).thenReturn(mockPage);
//
//        // Act
//        Page<Product> result = productService.findByNameContaining(name,pageable);
//
//        // Assert
//        assertNotNull(result);
//        assertEquals(10, result.getContent().size());
//        assertEquals("Product 1", result.getContent().getFirst().getName());
//
//        // Verify
//        verify(productDao).findByNameContaining(name,pageable);
//    }

    @Test
    public void testDeleteById() {
        // Arrange
        Integer testId = 2;

        // Act
        productService.deleteById(testId);

        // Verify
        verify(productRepository).deleteById(testId);
    }
}
