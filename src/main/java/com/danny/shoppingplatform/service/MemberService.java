package com.danny.shoppingplatform.service;

import com.danny.shoppingplatform.repository.MemberRepository;
import com.danny.shoppingplatform.model.Member;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class MemberService {
    private final MemberRepository memberRepository;

    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    public Member register(String account, String password) {
        if (memberRepository.findByAccount(account) != null) {
            return null; // 回傳null，表示未註冊成功。
        }

        if (StringUtils.hasText(account) && StringUtils.hasText(password)) {
            Member member = new Member();
            member.setAccount(account);
            member.setPassword(password);
            member.setRole("USER");
            memberRepository.save(member);

            return member;
        }
        return null;
    }

    public Member login(String account, String password) {
        Member member = memberRepository.findByAccount(account);
        if (member == null) {
            return null;
        }

        if (password != null) {
            String realPassword = member.getPassword();
            if (password.equals(realPassword)) {
                return member;
            }
        }

        return null;
    }

    public Member findByAccount(String account) {
        return memberRepository.findByAccount(account);
    }
}
