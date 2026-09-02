package com.danny.shoppingplatform.repository;

import com.danny.shoppingplatform.model.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    boolean existsByUserAccount(String account);

    Optional<Member> findByUserAccount(String account);
}
