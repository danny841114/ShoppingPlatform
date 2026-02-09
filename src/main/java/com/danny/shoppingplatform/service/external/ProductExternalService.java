package com.danny.shoppingplatform.service.external;

import com.danny.shoppingplatform.dto.ProductDto;
import com.danny.shoppingplatform.model.Product;
import com.danny.shoppingplatform.repository.MemberRepository;
import com.danny.shoppingplatform.repository.ProductRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Slf4j
@Service
public class ProductExternalService {
    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private WebClient webClient;

    public List<ProductDto> getProductsFromWebSite() {
        ProductDto[] dtoArray = webClient
                .get()
                .uri("https://fakestoreapi.com/products")
                .retrieve()
                .bodyToMono(ProductDto[].class)
                .block();

        List<ProductDto> dtoList = new ArrayList<>();

        if (dtoArray != null && dtoArray.length > 0) {
            dtoList = Arrays.asList(dtoArray);
        }

        return dtoList;
    }

    public List<Product> insertProductsFromWebSite(List<ProductDto> dtoList) {
        List<Product> productList = new ArrayList<>();

        for (ProductDto dto : dtoList) {
            Product product = new Product();
            product.setName(dto.getTitle());
            product.setDescription(dto.getDescription());
            product.setPrice(dto.getPrice().intValue());
            product.setQuantity(10);
            product.setDate(new Date());
            if (memberRepository.findById(6).isPresent()) {
                product.setMember(memberRepository.findById(6).get());
            }

            try {
                byte[] imageBytes = webClient
                        .get()
                        .uri(dto.getImage())
                        .retrieve()
                        .bodyToMono(byte[].class)
                        .block();
                product.setPhoto(imageBytes);
            } catch (Exception e) {
                log.error("URL to photo image failed: {}", e.getMessage());
            }

            productRepository.save(product);

            productList.add(product);
        }

        return productList;
    }
}
