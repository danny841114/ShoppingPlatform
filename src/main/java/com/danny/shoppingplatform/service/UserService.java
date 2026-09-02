package com.danny.shoppingplatform.service;

import com.danny.shoppingplatform.dto.member.*;
import com.danny.shoppingplatform.jwt.JwtUtil;
import com.danny.shoppingplatform.model.Member;
import com.danny.shoppingplatform.model.User;
import com.danny.shoppingplatform.repository.UserRepository;
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

@Slf4j
@Service
public class UserService implements UserDetailsService {
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;

    public UserService(@Lazy AuthenticationManager authenticationManager, JwtUtil jwtUtil, UserRepository userRepository) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = getUserByAccount(username);
        return new UserDetailsImpl(user);
    }

    public UserInfo fetchMe(String account) {
        User user = getUserByAccount(account);
        return UserInfo.fromEntity(user);
    }

    @Transactional
    public void register(RegisterRequest request) throws BadRequestException {
        String account = request.getAccount();
        String password = request.getPassword();

        if (userRepository.existsByAccount(account)) {
            throw new BadRequestException("User with account '%s' already exists".formatted(account));
        }

        User newUser = new User();
        newUser.setAccount(account);
        newUser.setPassword(password);
        Member newMember = new Member();
        newMember.setUser(newUser);
        userRepository.save(newUser);
    }

    public LoginResult login(LoginRequest request) {
        Authentication authenticationToken = new UsernamePasswordAuthenticationToken(request.getAccount(), request.getPassword());
        Authentication authentication = authenticationManager.authenticate(authenticationToken);
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        UserInfo userInfo = UserInfo.fromEntity(userDetails.getUser());

        String token = jwtUtil.generateToken(userDetails.getUser());

        return LoginResult.builder()
                .userInfo(userInfo)
                .token(token)
                .build();
    }

    private User getUserByAccount(String account) {
        return userRepository.findByAccount(account)
                .orElseThrow(() -> new UsernameNotFoundException("User with account '%s' not found".formatted(account)));
    }
}