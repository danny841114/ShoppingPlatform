package com.danny.shoppingplatform.service;

import com.danny.shoppingplatform.dto.member.*;
import com.danny.shoppingplatform.jwt.JwtUtil;
import com.danny.shoppingplatform.model.Member;
import com.danny.shoppingplatform.model.User;
import com.danny.shoppingplatform.model.Vendor;
import com.danny.shoppingplatform.repository.MemberRepository;
import com.danny.shoppingplatform.repository.UserRepository;
import com.danny.shoppingplatform.repository.VendorRepository;
import com.danny.shoppingplatform.util.JsonHelper;
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
@Transactional(readOnly = true)
@Service
public class UserService implements UserDetailsService {
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;
    private final MemberRepository memberRepository;
    private final VendorRepository vendorRepository;

    public UserService(@Lazy AuthenticationManager authenticationManager,
                       JwtUtil jwtUtil,
                       UserRepository userRepository,
                       MemberRepository memberRepository,
                       VendorRepository vendorRepository) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
        this.userRepository = userRepository;
        this.memberRepository = memberRepository;
        this.vendorRepository = vendorRepository;
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
        User savedUser = userRepository.save(newUser);

        Member newMember = new Member();
        newMember.setUser(savedUser);
        memberRepository.save(newMember);
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

    @Transactional
    public void addVendor(String account) {
        User user = getUserByAccount(account);

        if (user.getVendor() == null) {
            Vendor vendor = new Vendor();
            vendor.setShopName(user.getAccount());
            vendor.setUser(user);
            Vendor savedVendor = vendorRepository.save(vendor);

            JsonHelper.logAsJson(user);
            JsonHelper.logAsJson(savedVendor);
        } else {
            log.info("This user has vendor role already");
        }
    }

    private User getUserByAccount(String account) {
        return userRepository.findByAccount(account)
                .orElseThrow(() -> new UsernameNotFoundException("User with account '%s' not found".formatted(account)));
    }
}