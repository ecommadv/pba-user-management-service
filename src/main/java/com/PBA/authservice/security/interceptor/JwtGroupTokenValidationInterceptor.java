package com.pba.authservice.security.interceptor;

import com.pba.authservice.exceptions.AuthorizationException;
import com.pba.authservice.persistance.model.ActiveUser;
import com.pba.authservice.persistance.model.Group;
import com.pba.authservice.persistance.model.GroupMember;
import com.pba.authservice.persistance.model.UserType;
import com.pba.authservice.security.JwtUtils;
import com.pba.authservice.service.ActiveUserService;
import com.pba.authservice.service.GroupService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.UUID;

@Component
public class JwtGroupTokenValidationInterceptor implements HandlerInterceptor {
    private final JwtUtils jwtUtils;
    private final ActiveUserService userService;
    private final GroupService groupService;

    public JwtGroupTokenValidationInterceptor(JwtUtils jwtUtils, ActiveUserService userService, GroupService groupService) {
        this.jwtUtils = jwtUtils;
        this.userService = userService;
        this.groupService = groupService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String header = request.getHeader("Authorization");
        String token = jwtUtils.extractTokenFromHeader(header);
        UUID groupUid = jwtUtils.extractGroupUid(token);
        UUID userUid = jwtUtils.extractUserUidFromGroupToken(token);
        String userTypeName = jwtUtils.extractUserType(token);

        ActiveUser user = userService.getUserByUid(userUid);
        Group group = groupService.getGroupByUid(groupUid);
        UserType userType = userService.getUserTypeByName(userTypeName);
        this.validateUserIsInGroupWithType(user, group, userType);

        if (!jwtUtils.isTokenValid(token)) {
            throw new AuthorizationException();
        }
        return true;
    }

    private void validateUserIsInGroupWithType(ActiveUser user, Group group, UserType userType) {
        GroupMember groupMember = groupService.getGroupMemberByUserIdAndGroupId(user.getId(), group.getId())
                .orElseThrow(AuthorizationException::new);
        if (groupMember.getUserTypeId() != userType.getId()) {
            throw new AuthorizationException();
        }
    }
}
