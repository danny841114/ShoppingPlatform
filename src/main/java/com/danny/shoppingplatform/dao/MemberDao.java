package com.danny.shoppingplatform.dao;

import com.danny.shoppingplatform.model.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberDao extends JpaRepository<Member, Integer> {
    public Member findByAccount(String account);
}
