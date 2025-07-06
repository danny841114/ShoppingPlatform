package com.danny.shoppingplatform.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Entity
@Getter
@Setter
@Table(name="product")
public class Product {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name="name")
    private String name;

    @Column(name="description")
    private String description;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name="vendor_id")
    private Member member;

    @Column(name="price")
    private Integer price;

    @Column(name="quantity")
    private Integer quantity;

    @Column(name="date")
    private Date date;

    @Column(name="photo")
    private byte[] photo;
}
