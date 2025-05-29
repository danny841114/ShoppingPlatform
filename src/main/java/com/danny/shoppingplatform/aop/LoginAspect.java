package com.danny.shoppingplatform.aop;

import com.danny.shoppingplatform.controller.member.LoginController;
import com.danny.shoppingplatform.jwt.JwtUtil;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.Map;

@Aspect
@Component
public class LoginAspect {
    private static final Logger logger = LoggerFactory.getLogger(LoginAspect.class);
    private final JwtUtil jwtUtil;

    public LoginAspect(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    // 從JoinPoint中取得account
    private String getAccountFromArgs(JoinPoint joinPoint) {
        Object[] args = joinPoint.getArgs(); //取得被攔截方法的所有參數（陣列）

        if (args.length > 0 && args[0] instanceof Map<?, ?> map) { // 利用instanceof同時判斷與轉型為Map
            Object account = map.get("account");
            return account != null ? account.toString() : "未知帳號";
        }

        return "未知帳號"; // 如果參數陣列沒參數或第一個不是Map，直接回傳
    }

    // 攔截登入成功
    @AfterReturning(
            pointcut = "execution(* com.danny.shoppingplatform.controller.member.LoginController.loginByJwt(..))",
            returning = "result")
    public void logLoginSuccess(JoinPoint joinPoint, Object result) {
        Map<String, String> body = (Map<String, String>) ((ResponseEntity<?>) result).getBody();
        assert body != null;
        String token = body.get("token");

        logger.info("登入成功：{}，角色：{}", getAccountFromArgs(joinPoint), jwtUtil.getRole(token));
//        System.out.println("登入成功：" + getAccountFromArgs(joinPoint)+"，角色："+jwtUtil.getRole(token));
    }

    // 攔截登入失敗
    @AfterThrowing(
            pointcut = "execution(* com.danny.shoppingplatform.controller.member.LoginController.loginByJwt(..))",
            throwing = "e")
    public void logLoginFailure(JoinPoint joinPoint, Throwable e) {
        logger.error("登入失敗：{}，原因：{}", getAccountFromArgs(joinPoint), e.getMessage());
//        System.out.println("登入失敗：" + getAccountFromArgs(joinPoint) + "，原因：" + e.getMessage());
    }
}
