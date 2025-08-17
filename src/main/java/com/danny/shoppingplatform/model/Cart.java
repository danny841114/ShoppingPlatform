package com.danny.shoppingplatform.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Entity
@Getter
@Setter
@Table(name = "cart")
public class Cart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name="created_date")
    private Date createdDate;

    @Column(name="quantity")
    private Integer quantity;

    @ManyToOne
    @JoinColumn(name="member_id")
    @JsonManagedReference
    private Member member;

    @ManyToOne
    @JoinColumn(name="product_id")
    @JsonManagedReference
    private Product product;
}
