package com.danny.shoppingplatform.service;

import com.danny.shoppingplatform.repository.MemberRepository;
import com.danny.shoppingplatform.model.Member;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
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

        Member result = memberService.register(account, password);

        assertNotNull(result);
        assertEquals(account, result.getAccount());
        assertEquals(password, result.getPassword());
        assertEquals("USER", result.getRole());
    }

    @Test
    void testRegister_accountExists() {
        String account = "test";
        String password = "1234";

        when(memberRepository.findByAccount(account)).thenReturn(new Member());

        Member result = memberService.register(account, password);

        assertNull(result);
        verify(memberRepository, never()).save(any()); // 不應該呼叫 save
    }

    @Test
    public void testLogin_success() {
        // Arrange
        String account = "danny";
        String password = "aaa123";
        Member mockMember = new Member();
        mockMember.setAccount(account);
        mockMember.setPassword(password);

        when(memberRepository.findByAccount(account)).thenReturn(mockMember);

        // Act
        Member actualMember = memberService.login(account, password);

        // Assert
        assertEquals(mockMember,actualMember);
    }

    @Test
    public void testLogin_accountNotFound() {
        // Arrange
        String account = "danny";
        String password = "aaa123";

        when(memberRepository.findByAccount(account)).thenReturn(null);

        // Act
        Member actualMember = memberService.login(account, password);

        // Assert
        assertNull(actualMember);
        verify(memberRepository).findByAccount(account);
    }

    @Test
    public void testLogin_passwordWrong() {
        // Arrange
        String account = "danny";
        String password = "aaa123";
        Member mockMember = new Member();
        mockMember.setAccount(account);
        mockMember.setPassword("bbb456");

        when(memberRepository.findByAccount(account)).thenReturn(mockMember);

        // Act
        Member actualMember = memberService.login(account, password);

        // Assert
        assertNull(actualMember);
        verify(memberRepository).findByAccount(account);
    }

    @Test
    public void testFindByAccount() {
        // Arrange
        String account = "test";
        Member mockMember = new Member();
        mockMember.setAccount(account);

        when(memberRepository.findByAccount(account)).thenReturn(mockMember);

        // Act
        Member resultMember = memberService.findByAccount(account);

        // Assert
        assertEquals("test", resultMember.getAccount());
    }
}
