package com.pba.authservice.interceptor;

import com.pba.authservice.exceptions.AuthorizationException;
import com.pba.authservice.persistance.model.ActiveUser;
import com.pba.authservice.service.ActiveUserService;
import com.pba.authservice.service.JwtService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.UUID;

@Component
public class JwtValidationInterceptor implements HandlerInterceptor {
    private final JwtService jwtService;
    private final ActiveUserService userService;

    public JwtValidationInterceptor(JwtService jwtService, ActiveUserService userService) {
        this.jwtService = jwtService;
        this.userService = userService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String header = request.getHeader("Authorization");
        String token = jwtService.extractTokenFromHeader(header);
        UUID userUid = jwtService.extractUserUid(token);
        ActiveUser activeUser = userService.getUserByUid(userUid);
        if (!jwtService.isTokenValid(token, activeUser)) {
            throw new AuthorizationException();
        }
        return true;
    }
}
