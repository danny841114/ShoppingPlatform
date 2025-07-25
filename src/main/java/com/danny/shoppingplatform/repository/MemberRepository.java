package com.danny.shoppingplatform.repository;

import com.danny.shoppingplatform.model.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberRepository extends JpaRepository<Member, Integer> {
    Member findByAccount(String account);
}
