package com.danny.shoppingplatform.service;

import com.danny.shoppingplatform.dao.MemberDao;
import com.danny.shoppingplatform.model.Member;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class MemberServiceTest {
    @Mock
    private MemberDao memberDao;

    @InjectMocks
    private MemberService memberService;

    @Test
    public void testRegister() {

    }

    @Test
    public void testLogin() {

    }

    @Test
    public void TestFindByAccount() {
        // Arrange
        String account="test";

        Member mockMember = new Member();
        mockMember.setAccount(account);

        when(memberDao.findByAccount(account)).thenReturn(mockMember);

        // Act
        Member resultMember = memberService.findByAccount(account);

        // Assert
//        assertNull(resultMember);
        assertEquals("test", resultMember.getAccount());
    }
}
