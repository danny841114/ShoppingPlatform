package com.danny.shoppingplatform.service;

import com.danny.shoppingplatform.repository.MemberRepository;
import com.danny.shoppingplatform.model.Member;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;

@Service
public class MemberService {
    private final MemberRepository memberRepository;

    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    public Member findByAccount(String account) {
        return memberRepository.findByAccount(account);
    }

    public Member findById(Integer id) {
        return memberRepository.findById(id).orElse(null);
    }

    public Member register(String account, String password) {
        if (!StringUtils.hasText(account) || !StringUtils.hasText(password)) {
            return null;
        }

        if (memberRepository.findByAccount(account) != null) {
            return null;
        }

        Member member = new Member();
        member.setAccount(account);
        member.setPassword(password);
        member.setRole("USER");
        memberRepository.save(member);

        return member;
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

    public void upgradeRole(String account) {
        Member member = memberRepository.findByAccount(account);
        if (member.getRole().equals("USER")) {
            member.setRole("ADMIN");
            memberRepository.save(member);
        }
    }

    public Member modifyProfile(String account, String name, String birthdate, String email, byte[] photo) throws ParseException {
        Member member = memberRepository.findByAccount(account);
        if (member == null) {
            return null;
        }
        member.setName(name);
        member.setBirthdate(new SimpleDateFormat("yyyy-MM-dd").parse(birthdate));
        member.setEmail(email);
        if (photo != null) {
            member.setPhoto(photo);
        }
        memberRepository.save(member);
        return member;
    }

    public Member getLoginMember() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String account = auth.getName();
        return memberRepository.findByAccount(account);
    }
}
