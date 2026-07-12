package com.danny.shoppingplatform.service;

import com.danny.shoppingplatform.dto.member.MemberDto;
import com.danny.shoppingplatform.dto.member.UserDetailsImpl;
import com.danny.shoppingplatform.repository.MemberRepository;
import com.danny.shoppingplatform.model.Member;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.BadRequestException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

import static com.danny.shoppingplatform.dto.member.MemberDto.fromEntity;

@Slf4j
@RequiredArgsConstructor
@Service
public class MemberService implements UserDetailsService {
    private final MemberRepository memberRepository;

    public Member findByAccount(String account) {
        return memberRepository.findByAccount(account)
                .orElseThrow(() -> new UsernameNotFoundException("Account '%s' not found".formatted(account)));
    }

    @Transactional
    public MemberDto register(String account, String password) throws BadRequestException {
        Optional<Member> memberByAccount = memberRepository.findByAccount(account);
        if (memberByAccount.isPresent()) {
            throw new BadRequestException("Account '%s' already exists".formatted(account));
        }

        Member member = new Member();
        member.setAccount(account);
        member.setPassword(password);
        member.setRole("USER");

        Member savedMember = memberRepository.save(member);

        return fromEntity(savedMember);
    }

    public Member login(String account, String password) {
        Member member = memberRepository.findByAccount(account)
                .orElseThrow(() -> new UsernameNotFoundException("Account '%s' not found".formatted(account)));

        if (password != null) {
            String realPassword = member.getPassword();
            if (password.equals(realPassword)) {
                return member;
            }
        }

        return null;
    }

    public void upgradeRole(String account) {
        Member member = memberRepository.findByAccount(account)
                .orElseThrow(() -> new UsernameNotFoundException("Account '%s' not found".formatted(account)));

        if (member.getRole().equals("USER")) {
            member.setRole("ADMIN");
            memberRepository.save(member);
        }
    }

    public void modifyProfile(String account,
                              String name,
                              LocalDateTime birthdate,
                              String email,
                              byte[] photo) {
        Member member = memberRepository.findByAccount(account)
                .orElseThrow(() -> new UsernameNotFoundException("Account '%s' not found".formatted(account)));

        member.setName(name);
        member.setEmail(email);
        member.setBirthdate(birthdate);

        if (photo != null) {
            member.setPhoto(photo);
        }

        memberRepository.save(member);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Member member = memberRepository.findByAccount(username)
                .orElseThrow(() -> new UsernameNotFoundException("Account '%s' not found".formatted(username)));

        return new UserDetailsImpl(member);
    }
}