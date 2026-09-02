package com.danny.shoppingplatform.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Entity
@Getter
@Setter
@Table(name = "cart",
        uniqueConstraints = {@UniqueConstraint(columnNames = {"member_id", "product_id"})}
)
public class Cart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "created_date")
    private Instant createdDate = Instant.now();

    @Column(name = "quantity")
    private Integer quantity;

    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    public static Cart create(Member member, Product product, Integer quantity) {
        Cart cart = new Cart();
        cart.setMember(member);
        cart.setProduct(product);
        cart.setQuantity(quantity);
        return cart;
    }
}
