package com.danny.shoppingplatform.controller.product;

import com.danny.shoppingplatform.model.Member;
import com.danny.shoppingplatform.model.Product;
import com.danny.shoppingplatform.service.MemberService;
import com.danny.shoppingplatform.service.ProductService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.ui.Model;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ProductControllerTest {
    @Mock
    private ProductService productService;

    @Mock
    private MemberService memberService;

    @InjectMocks
    private ProductController productController;

    @AfterEach
    public void clearSecurityContext() {
        SecurityContextHolder.clearContext();
    }

    @Test
    public void testAddProduct() {
        // Arrange
        // 模擬 SecurityContext 與帳號
        Authentication authentication = mock(Authentication.class); // Authentication 為一介面，不能直接 new
        when(authentication.getName()).thenReturn("testAccount");

        SecurityContext securityContext = mock(SecurityContext.class); // SecurityContext 為一介面，不能直接 new
        when(securityContext.getAuthentication()).thenReturn(authentication); // securityContext 來自 SecurityContext.getContext()      SecurityContextHolder.setContext(securityContext);

        SecurityContextHolder.setContext(securityContext);

        // 模擬會員
        Member member = new Member();
        member.setId(1);
        member.setAccount("testAccount");
        when(memberService.findByAccount("testAccount")).thenReturn(member);

        // 模擬圖片上傳
        byte[] imageContent = "dummy image content".getBytes();
        MockMultipartFile photo = new MockMultipartFile("photo", "photo.jpg", "image/jpeg", imageContent);

        // Act
        String redirectUrl = productController.addProduct(
                "Test Product",
                "Test Description",
                100,
                10,
                photo
        );

        // Assert
        assertEquals("redirect:/product/manage", redirectUrl);

        verify(productService, times(1)).addProduct(
                eq("Test Product"),
                eq("Test Description"),
                eq(member.getId()),
                eq(100),
                eq(10),
                any()
        );
    }

    @Test
    public void showProductWithoutKeyword() {
        // Arrange
        int size = 5, page = 0;
        Pageable pageable = PageRequest.of(page, size);

        List<Product> productList = List.of(new Product(), new Product());
        Page<Product> mockPage = new PageImpl<>(productList, pageable, productList.size());

        when(productService.findAllByPageable(pageable)).thenReturn(mockPage);

        Model model = mock(Model.class);

        // Act
        String view = productController.showProduct(size, page, null, model);

        // Assert
        verify(productService).findAllByPageable(pageable);
        verify(model).addAttribute("productList", productList);
        verify(model).addAttribute("totalPages", mockPage.getTotalPages());
        verify(model).addAttribute("totalElements", mockPage.getTotalElements());
        verify(model).addAttribute("page", page);
        verify(model).addAttribute("size", size);
        verify(model).addAttribute("keyword", null);
        assertEquals("/index", view);
    }

    @Test
    public void showProductWithKeyword() {
        // Arrange
        String keyword = "keyword";

        int size = 5, page = 0;
        Pageable pageable = PageRequest.of(page, size);

        List<Product> productList = List.of(new Product(), new Product());
        Page<Product> mockPage = new PageImpl<>(productList, pageable, productList.size());

        when(productService.findByNameContaining(keyword,pageable)).thenReturn(mockPage);

        Model model = mock(Model.class);

        // Act
        String view = productController.showProduct(size, page, keyword, model);

        // Assert
        verify(productService).findAllByPageable(pageable);
        verify(model).addAttribute("productList", productList);
        verify(model).addAttribute("totalPages", mockPage.getTotalPages());
        verify(model).addAttribute("totalElements", mockPage.getTotalElements());
        verify(model).addAttribute("page", page);
        verify(model).addAttribute("size", size);
        verify(model).addAttribute("keyword", null);
        assertEquals("/index", view);
    }

    @Test
    public void testModifyProduct() {
        // Arrange
        // 模擬圖片上傳
        byte[] imageContent = "hello image content".getBytes();
        MockMultipartFile photo = new MockMultipartFile("photo", "photo.jpg",
                                                    "image/jpeg", imageContent);

        // Act
        String redirectUrl = productController.modifyProduct(
                1,
                "Test Product",
                "Test Description",
                100,
                10,
                photo
        );

        // Assert
        assertEquals("redirect:/product/manage", redirectUrl);
        verify(productService).modifyProduct(
                eq(1),
                eq("Test Product"),
                eq("Test Description"),
                eq(100),
                eq(10),
                eq(imageContent)
        );
    }

    @Test
    public void testDeleteProduct() {
        // Arrange
        Integer testId = 1;

        // Act
        ResponseEntity<?> responseEntity = productController.deleteProduct(testId);

        // Assert
        verify(productService).deleteById(testId);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertNull(responseEntity.getBody());
    }
}
