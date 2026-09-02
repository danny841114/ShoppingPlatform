package com.danny.shoppingplatform.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "account", unique = true, nullable = false)
    private String account;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "created_date", nullable = false, updatable = false)
    private Instant createdDate = Instant.now();

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Member member;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Vendor vendor;

    public List<String> getRoles() {
        List<String> roles = new ArrayList<>();

        if (this.member != null) roles.add("MEMBER");
        if (this.vendor != null) roles.add("VENDOR");

        return roles;
    }
}
