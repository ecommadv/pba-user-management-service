package com.pba.authservice.security.interceptor;

import com.pba.authservice.security.JwtSecurityService;
import com.pba.authservice.security.JwtUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.UUID;

@Component
public class UserContextHolderProviderInterceptor implements HandlerInterceptor {
    private final JwtSecurityService jwtSecurityService;
    private final JwtUtils jwtUtils;

    public UserContextHolderProviderInterceptor(JwtSecurityService jwtSecurityService, JwtUtils jwtUtils) {
        this.jwtSecurityService = jwtSecurityService;
        this.jwtUtils = jwtUtils;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String header = request.getHeader("Authorization");
        UUID userUid = jwtUtils.extractUserUidFromHeader(header);
        jwtSecurityService.setCurrentUserUid(userUid);
        return true;
    }
}
