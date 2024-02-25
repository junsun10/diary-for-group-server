package com.example.diary.session;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

public class SessionUtils {

    private SessionUtils(){}

    /**
     * 세션에서 memberId 추출
     */
    public static Long getMemberIdFromSession(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        Long memberId = (Long) session.getAttribute(SessionConst.LOGIN_MEMBER_ID);
        return memberId;
    }
}
