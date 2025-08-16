package com.danny.shoppingplatform.service;

import com.danny.shoppingplatform.repository.MemberRepository;
import com.danny.shoppingplatform.model.Member;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.text.ParseException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class MemberServiceTest {
    @Mock
    private MemberRepository memberRepository;

    @InjectMocks
    private MemberService memberService;

    @Test
    public void testRegister_success() {
        String account = "test";
        String password = "1234";
        when(memberRepository.findByAccount(account)).thenReturn(null);

        Member member = memberService.register(account, password);

        assertNotNull(member);
        assertEquals(account, member.getAccount());
        assertEquals(password, member.getPassword());
        assertEquals("USER", member.getRole());
        verify(memberRepository).save(any(Member.class));
    }

    @Test
    void testRegister_accountOrPasswordNull() {
        String account = "";
        String password = "1234";

        Member member = memberService.register(account, password);

        assertNull(member);
        verify(memberRepository, never()).save(any());
    }


    @Test
    void testRegister_accountExists() {
        String account = "test";
        String password = "1234";
        when(memberRepository.findByAccount(account)).thenReturn(new Member());

        Member member = memberService.register(account, password);

        assertNull(member);
        verify(memberRepository, never()).save(any());
    }

    @Test
    public void testLogin_success() {
        String account = "danny";
        String password = "aaa123";
        Member mockMember = new Member();
        mockMember.setAccount(account);
        mockMember.setPassword(password);
        when(memberRepository.findByAccount(account)).thenReturn(mockMember);

        Member actualMember = memberService.login(account, password);

        assertEquals(mockMember, actualMember);
        verify(memberRepository).findByAccount(account);
    }

    @Test
    public void testLogin_accountNotFound() {
        String account = "danny";
        String password = "aaa123";
        when(memberRepository.findByAccount(account)).thenReturn(null);

        Member actualMember = memberService.login(account, password);

        assertNull(actualMember);
        verify(memberRepository).findByAccount(account);
    }

    @Test
    public void testLogin_passwordWrong() {
        String account = "danny";
        String password = "aaa123";
        Member mockMember = new Member();
        mockMember.setAccount(account);
        mockMember.setPassword("bbb456");
        when(memberRepository.findByAccount(account)).thenReturn(mockMember);

        Member member = memberService.login(account, password);

        assertNull(member);
        verify(memberRepository).findByAccount(account);
    }

    @Test
    public void testModifyProfile_success() throws ParseException {
        String account = "test";
        String name = "danny";
        String birthdate = "2020-01-01";
        String email = "danny@gmail.com";
        byte[] photo = new byte[0];
        Member mockMember = new Member();
        when(memberRepository.findByAccount(account)).thenReturn(mockMember);

        memberService.modifyProfile(account,name,birthdate,email,photo);

        verify(memberRepository).findByAccount(anyString());
        verify(memberRepository).save(any(Member.class));
    }

    @Test
    public void testModifyProfile_fail() throws ParseException {
        String account = "test";
        String name = "danny";
        String birthdate = "2020-01-01";
        String email = "danny@gmail.com";
        byte[] photo = new byte[0];
        Member mockMember = new Member();
        when(memberRepository.findByAccount(account)).thenReturn(null);

        memberService.modifyProfile(account,name,birthdate,email,photo);

        verify(memberRepository).findByAccount(anyString());
        verify(memberRepository, never()).save(any(Member.class));
    }
}
