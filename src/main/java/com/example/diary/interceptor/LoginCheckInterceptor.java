package com.example.diary.interceptor;

import com.example.diary.exception.AuthenticationException;
import com.example.diary.session.SessionConst;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerInterceptor;
import org.thymeleaf.util.StringUtils;

@Slf4j
public class LoginCheckInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        String requestURI = request.getRequestURI();

        // preflight
        if (request.getMethod().equals("OPTIONS")) {
            return true;
        }

        log.info("로그인 체크 인터셉터 실행 {}", requestURI);

        HttpSession session = request.getSession(false);

        if (session == null || session.getAttribute(SessionConst.LOGIN_MEMBER_ID) == null) {
            if (session == null) {
                log.info("세션이 없습니다.");
            }
            else {
                log.info("로그인 정보가 없습니다.");
            }
            log.info("미인증 사용자 요청 {}", requestURI);
            throw new AuthenticationException("로그인이 필요합니다.");
        }

        return true;
    }
}
