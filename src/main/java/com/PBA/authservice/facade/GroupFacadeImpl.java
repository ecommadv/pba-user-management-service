package com.pba.authservice.facade;

import com.pba.authservice.controller.request.GroupCreateRequest;
import com.pba.authservice.controller.request.GroupInviteRequest;
import com.pba.authservice.exceptions.AuthorizationException;
import com.pba.authservice.exceptions.EntityAlreadyExistsException;
import com.pba.authservice.exceptions.EntityNotFoundException;
import com.pba.authservice.exceptions.ErrorCodes;
import com.pba.authservice.mapper.ActiveUserMapper;
import com.pba.authservice.mapper.GroupMapper;
import com.pba.authservice.persistance.model.*;
import com.pba.authservice.persistance.model.dtos.GroupDto;
import com.pba.authservice.persistance.model.dtos.UserDto;
import com.pba.authservice.persistance.model.dtos.UserProfileDto;
import com.pba.authservice.security.JwtSecurityService;
import com.pba.authservice.service.ActiveUserService;
import com.pba.authservice.service.GroupService;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Component
public class GroupFacadeImpl implements GroupFacade {
    private final GroupService groupService;
    private final GroupMapper groupMapper;
    private final ActiveUserService userService;
    private final ActiveUserMapper userMapper;
    private final JwtSecurityService jwtSecurityService;

    public GroupFacadeImpl(GroupService groupService, ActiveUserService userService, GroupMapper groupMapper, ActiveUserMapper userMapper, JwtSecurityService jwtSecurityService) {
        this.groupService = groupService;
        this.groupMapper = groupMapper;
        this.userService = userService;
        this.userMapper = userMapper;
        this.jwtSecurityService = jwtSecurityService;
    }

    @Override
    @Transactional
    public GroupDto createGroup(GroupCreateRequest groupCreateRequest) {
        this.validateGroupDoesNotAlreadyExist(groupCreateRequest);

        UUID groupCreatorUid = jwtSecurityService.getCurrentUserUid();
        ActiveUser groupCreator = userService.getUserByUid(groupCreatorUid);
        Group groupToCreate = groupMapper.toGroup(groupCreateRequest);
        Group createdGroup = groupService.addGroup(groupToCreate);

        UserType adminType = userService.getUserTypeByName(UserTypeName.ADMIN);
        GroupMember groupAdmin = groupMapper.toGroupMember(groupCreator, adminType, createdGroup);
        groupService.addGroupMember(groupAdmin);

        UserDto adminDto = this.getUserDto(groupCreator);
        return groupMapper.toDto(createdGroup, adminDto);
    }

    @Override
    public void inviteUserToGroup(GroupInviteRequest groupInviteRequest) {
        UUID userToInviteUid = groupInviteRequest.getUserUid();
        ActiveUser userToInvite = userService.getUserByUid(userToInviteUid);
        UUID groupUid = groupInviteRequest.getGroupUid();
        Group group = groupService.getGroupByUid(groupUid);
        UUID adminUid = jwtSecurityService.getCurrentUserUid();
        ActiveUser admin = userService.getUserByUid(adminUid);

        this.validateUserIsAdmin(group, admin);
        this.validateUserIsNotAlreadyInGroup(group, userToInvite);

        UserType regularUserType = userService.getUserTypeByName(UserTypeName.REGULAR_USER);
        GroupMember memberToAdd = groupMapper.toGroupMember(userToInvite, regularUserType, group);
        groupService.addGroupMember(memberToAdd);
    }

    private UserDto getUserDto(ActiveUser activeUser) {
        ActiveUserProfile userProfile = userService.getProfileByUserId(activeUser.getId());
        UserProfileDto userProfileDto = userMapper.toUserProfileDto(userProfile);
        return userMapper.toUserDto(activeUser, userProfileDto);
    }

    private void validateGroupDoesNotAlreadyExist(GroupCreateRequest groupCreateRequest) {
        if (groupService.groupWithNameExists(groupCreateRequest.getGroupName())) {
            throw new EntityAlreadyExistsException(
                    ErrorCodes.GROUP_ALREADY_EXISTS,
                    String.format("Group with name %s already exists", groupCreateRequest.getGroupName())
            );
        }
    }

    private void validateUserIsAdmin(Group group, ActiveUser user) {
        GroupMember groupMember = groupService.getGroupMemberByUserIdAndGroupId(user.getId(), group.getId())
                .orElseThrow(AuthorizationException::new);
        UserType adminType = userService.getUserTypeByName(UserTypeName.ADMIN);
        if (groupMember.getUserTypeId() != adminType.getId()) {
            throw new AuthorizationException();
        }
    }

    private void validateUserIsNotAlreadyInGroup(Group group, ActiveUser user) {
        if (groupService.getGroupMemberByUserIdAndGroupId(user.getId(), group.getId()).isPresent()) {
            throw new EntityAlreadyExistsException(
                    ErrorCodes.GROUP_MEMBER_ALREADY_EXISTS,
                    String.format("User with uid %s already is in group with uid %s", user.getUid(), group.getUid())
            );
        }
    }
}
