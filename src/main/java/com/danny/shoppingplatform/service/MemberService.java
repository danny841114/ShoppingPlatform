package com.danny.shoppingplatform.service;

import com.danny.shoppingplatform.dto.member.*;
import com.danny.shoppingplatform.jwt.JwtUtil;
import com.danny.shoppingplatform.repository.MemberRepository;
import com.danny.shoppingplatform.model.Member;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.BadRequestException;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Optional;

import static com.danny.shoppingplatform.dto.member.MemberDto.fromEntity;
import static com.danny.shoppingplatform.dto.member.UserInfo.generateUserInfo;

@Slf4j
@Service
public class MemberService implements UserDetailsService {
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final MemberRepository memberRepository;

    public MemberService(@Lazy AuthenticationManager authenticationManager, JwtUtil jwtUtil, MemberRepository memberRepository) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
        this.memberRepository = memberRepository;
    }

    public MemberDto getMemberByAccount(String account) {
        Member member = memberRepository.findByAccount(account)
                .orElseThrow(() -> new UsernameNotFoundException("Account '%s' not found".formatted(account)));

        return fromEntity(member);
    }

    @Transactional
    public MemberDto register(RegisterRequest request) throws BadRequestException {
        String account = request.getAccount();
        String password = request.getPassword();

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

    public LoginResult login(LoginRequest request) {
        Authentication authenticationToken = new UsernamePasswordAuthenticationToken(request.getAccount(), request.getPassword());

        Authentication authentication = authenticationManager.authenticate(authenticationToken);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        String token = jwtUtil.generateToken(userDetails.getMember());

        UserInfo userInfo = generateUserInfo(userDetails.getAccount(), userDetails.getRole());

        return LoginResult.builder()
                .userInfo(userInfo)
                .token(token)
                .build();
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
                              LocalDate birthdate,
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