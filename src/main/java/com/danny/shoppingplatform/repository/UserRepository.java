package com.danny.shoppingplatform.repository;

import com.danny.shoppingplatform.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByAccount(String account);

    Optional<User> findByAccount(String account);
}
