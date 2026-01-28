package com.danny.shoppingplatform.repository;

import com.danny.shoppingplatform.model.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MemberRepository extends JpaRepository<Member, Integer> {
    Member findByAccount(String account);
    List<Member> findByAccountContaining(String account);
}
