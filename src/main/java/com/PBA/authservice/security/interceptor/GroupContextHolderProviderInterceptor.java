package com.pba.authservice.security.interceptor;

import com.pba.authservice.persistance.model.UserType;
import com.pba.authservice.security.JwtSecurityService;
import com.pba.authservice.security.JwtUtils;
import com.pba.authservice.service.ActiveUserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.UUID;

@Component
public class GroupContextHolderProviderInterceptor implements HandlerInterceptor {
    private final JwtSecurityService jwtSecurityService;
    private final JwtUtils jwtUtils;
    private final ActiveUserService userService;

    public GroupContextHolderProviderInterceptor(JwtSecurityService jwtSecurityService, JwtUtils jwtUtils, ActiveUserService userService) {
        this.jwtSecurityService = jwtSecurityService;
        this.jwtUtils = jwtUtils;
        this.userService = userService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String header = request.getHeader("Authorization");
        String token = jwtUtils.extractTokenFromHeader(header);

        String userTypeName = jwtUtils.extractUserType(token);
        UserType userType = userService.getUserTypeByName(userTypeName);
        jwtSecurityService.setCurrentUserType(userType);

        UUID userUid = jwtUtils.extractUserUidFromGroupToken(token);
        jwtSecurityService.setCurrentUserUid(userUid);

        UUID groupUid = jwtUtils.extractGroupUid(token);
        jwtSecurityService.setCurrentGroupUid(groupUid);
        return true;
    }
}
