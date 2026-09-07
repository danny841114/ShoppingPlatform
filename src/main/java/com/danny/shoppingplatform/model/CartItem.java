package com.danny.shoppingplatform.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Entity
@Getter
@Setter
@Table(name = "cart_item",
        uniqueConstraints = {@UniqueConstraint(columnNames = {"member_id", "product_id"})}
)
public class CartItem {
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

    public static CartItem create(Member member, Product product, Integer quantity) {
        CartItem cartItem = new CartItem();
        cartItem.setMember(member);
        cartItem.setProduct(product);
        cartItem.setQuantity(quantity);
        return cartItem;
    }
}
