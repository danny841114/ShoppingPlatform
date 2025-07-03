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
    public void testRegister_Success() {
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
    void testRegister_AccountExists() {
        String account = "test";
        String password = "1234";

        when(memberRepository.findByAccount(account)).thenReturn(new Member());

        Member result = memberService.register(account, password);

        assertNull(result);
        verify(memberRepository, never()).save(any()); // 不應該呼叫 save
    }

    @Test
    public void testLogin_Success() {

    }

    @Test
    public void TestFindByAccount() {
        // Arrange
        String account="test";
        Member mockMember = new Member();
        mockMember.setAccount(account);

        when(memberRepository.findByAccount(account)).thenReturn(mockMember);

        // Act
        Member resultMember = memberService.findByAccount(account);

        // Assert
        assertEquals("test", resultMember.getAccount());
    }
}
