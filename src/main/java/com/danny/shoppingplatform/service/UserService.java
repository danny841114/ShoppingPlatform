package com.danny.shoppingplatform.service;

import com.danny.shoppingplatform.dto.member.*;
import com.danny.shoppingplatform.jwt.JwtUtil;
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

import java.util.Optional;

import static com.danny.shoppingplatform.dto.member.UserDto.fromEntity;
import static com.danny.shoppingplatform.dto.member.UserInfo.generateUserInfo;

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

    public UserDto getMemberByAccount(String account) {
        User user = getUserByAccount(account);
        return fromEntity(user);
    }

    @Transactional
    public UserDto register(RegisterRequest request) throws BadRequestException {
        String account = request.getAccount();
        String password = request.getPassword();

        Optional<User> userByAccount = userRepository.findByAccount(account);
        if (userByAccount.isPresent()) {
            throw new BadRequestException("User with account '%s' already exists".formatted(account));
        }

        User newUser = new User();
        newUser.setAccount(account);
        newUser.setPassword(password);
        newUser.setRole("USER");
        User savedUser = userRepository.save(newUser);

        return fromEntity(savedUser);
    }

    public LoginResult login(LoginRequest request) {
        Authentication authenticationToken = new UsernamePasswordAuthenticationToken(request.getAccount(), request.getPassword());

        Authentication authentication = authenticationManager.authenticate(authenticationToken);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        String token = jwtUtil.generateToken(userDetails.getUser());

        UserInfo userInfo = generateUserInfo(userDetails.getAccount(), userDetails.getRole());

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