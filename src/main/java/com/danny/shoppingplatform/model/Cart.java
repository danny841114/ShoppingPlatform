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
    private Instant createdDate;

    @Column(name = "quantity")
    private Integer quantity;

    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    public static Cart create(Member member, Product product) {
        Cart cart = new Cart();
        cart.setMember(member);
        cart.setProduct(product);
        cart.setQuantity(0);
        return cart;
    }
}
