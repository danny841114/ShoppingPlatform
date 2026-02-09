package com.danny.shoppingplatform.service.external;

import com.danny.shoppingplatform.dto.UserDto;
import com.danny.shoppingplatform.model.Member;
import com.danny.shoppingplatform.repository.MemberRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Slf4j
@Service
public class MemberExternalService {
    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private RestTemplate restTemplate;

    public List<UserDto> getMembersFromWebSite() {
        List<UserDto> dtoList = new ArrayList<>();

        try {
            ResponseEntity<UserDto[]> responseEntity =
                    restTemplate.getForEntity("https://fakestoreapi.com/users", UserDto[].class);

            if (responseEntity.getStatusCode().is2xxSuccessful()) {
                if (responseEntity.getBody() != null && responseEntity.getBody().length > 0) {
                    UserDto[] dtoArray = responseEntity.getBody();
                    List<UserDto> list = Arrays.asList(dtoArray);
                    dtoList.addAll(list);
                }
            }
        } catch (Exception e) {
            log.error("Get members from web site failed: {}", e.getMessage());
        }

        return dtoList;
    }

    public List<Member> insertMembersFromWebSite(List<UserDto> dtoList) {
        List<Member> memberList = new ArrayList<>();

        for (UserDto dto : dtoList) {
            Member member = new Member();
            member.setAccount(dto.getUsername());
            member.setPassword(dto.getPassword());
            member.setEmail(dto.getEmail());
            member.setName(dto.getName().getLastname() + " " + dto.getName().getFirstname());
            member.setRole("USER");

            memberRepository.save(member);

            memberList.add(member);
        }

        return memberList;
    }
}