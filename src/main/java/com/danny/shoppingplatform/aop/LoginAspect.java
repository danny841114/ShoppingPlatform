package com.danny.shoppingplatform.aop;

import com.danny.shoppingplatform.jwt.JwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.Map;

@Slf4j
@Aspect
@Component
public class LoginAspect {
    private final JwtUtil jwtUtil;

    public LoginAspect(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    // 從 JoinPoint 中取得 account
    private String getAccountFromArgs(JoinPoint joinPoint) {
        Object[] args = joinPoint.getArgs(); // 取得被攔截方法的所有參數（陣列）

        // 如果方法的參數至少有一個，且第一個參數是 Map 型別，就將它轉型為 map
        if (args.length > 0 && args[0] instanceof Map<?, ?> map) {
            String account = (String) map.get("account");
            return account != null ? account : "未知帳號";
        }

        return "未知帳號"; // 如果參數陣列沒參數或第一個不是Map，直接回傳
    }

    // 攔截登入成功
    @AfterReturning(
            pointcut = "execution(* com.danny.shoppingplatform.controller.member.LoginController.loginByJwt(..))",
            returning = "result")
    public void logLoginSuccess(JoinPoint joinPoint, Object result) {
        Object objectBody = ((ResponseEntity<?>) result).getBody();

        if (objectBody instanceof Map<?, ?> map) {
            String token = (String) map.get("token");
            log.info("登入成功：{}，角色：{}", getAccountFromArgs(joinPoint), jwtUtil.getRole(token));
        }
    }

    // 攔截登入失敗
    @AfterThrowing(
            pointcut = "execution(* com.danny.shoppingplatform.controller.member.LoginController.loginByJwt(..))",
            throwing = "e")
    public void logLoginFailure(JoinPoint joinPoint, Throwable e) {
        log.error("登入失敗：{}，原因：{}", getAccountFromArgs(joinPoint), e.getMessage());
    }
}
