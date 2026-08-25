package com.danny.shoppingplatform.dto.member;

import com.danny.shoppingplatform.model.Member;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MemberDto {
    private Long id;
    private String account;
    private String role;
    private String name;
    private LocalDate birthdate;
    private String email;
    private byte[] photo;

    public static MemberDto fromEntity(Member member) {
        if (member == null) return null;

        return MemberDto.builder()
                .id(member.getId())
                .account(member.getAccount())
                .role(member.getRole())
                .name(member.getName())
                .birthdate(member.getBirthdate())
                .email(member.getEmail())
                .photo(member.getPhoto())
                .build();
    }
}
