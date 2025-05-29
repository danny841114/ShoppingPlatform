package com.danny.shoppingplatform.service;

import com.danny.shoppingplatform.dao.MemberDao;
import com.danny.shoppingplatform.model.Member;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class MemberService {
    private final MemberDao memberDao;

    public MemberService(MemberDao memberDao) {
        this.memberDao = memberDao;
    }

    public Member register(String account, String password) {
        if (memberDao.findByAccount(account) != null) {
            return null; // 回傳null，表示未註冊成功。
        }

        if (StringUtils.hasText(account) && StringUtils.hasText(password)) {
            Member member = new Member();
            member.setAccount(account);
            member.setPassword(password);
            member.setRole("USER");
            memberDao.save(member);

            return member;
        }
        return null;
    }

    public Member login(String account, String password) {
        Member member = memberDao.findByAccount(account);
        if (member == null) {
            return null;
        }

        if (password != null) {
            String realPassword = member.getPassword();
            if (password.equals(realPassword)) {
                return member;
            }
        }

        return null;
    }

    public Member findByAccount(String account) {
        return memberDao.findByAccount(account);
    }
}
