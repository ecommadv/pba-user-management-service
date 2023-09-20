package com.pba.authservice.security.interceptor;

import com.pba.authservice.exceptions.AuthorizationException;
import com.pba.authservice.persistance.model.ActiveUser;
import com.pba.authservice.service.ActiveUserService;
import com.pba.authservice.security.JwtUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.UUID;

@Component
public class JwtValidationInterceptor implements HandlerInterceptor {
    private final JwtUtils jwtUtils;
    private final ActiveUserService userService;

    public JwtValidationInterceptor(JwtUtils jwtUtils, ActiveUserService userService) {
        this.jwtUtils = jwtUtils;
        this.userService = userService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String header = request.getHeader("Authorization");
        String token = jwtUtils.extractTokenFromHeader(header);
        UUID userUid = jwtUtils.extractUserUid(token);
        ActiveUser activeUser = userService.getUserByUid(userUid);
        if (!jwtUtils.isTokenValid(token, activeUser)) {
            throw new AuthorizationException();
        }
        return true;
    }
}
