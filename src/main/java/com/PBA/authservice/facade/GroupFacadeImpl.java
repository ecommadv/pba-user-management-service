package com.pba.authservice.facade;

import com.pba.authservice.controller.request.GroupCreateRequest;
import com.pba.authservice.exceptions.EntityAlreadyExistsException;
import com.pba.authservice.exceptions.ErrorCodes;
import com.pba.authservice.mapper.ActiveUserMapper;
import com.pba.authservice.mapper.GroupMapper;
import com.pba.authservice.persistance.model.*;
import com.pba.authservice.persistance.model.dtos.GroupDto;
import com.pba.authservice.persistance.model.dtos.UserDto;
import com.pba.authservice.persistance.model.dtos.UserProfileDto;
import com.pba.authservice.service.ActiveUserService;
import com.pba.authservice.service.GroupService;
import org.springframework.stereotype.Component;

@Component
public class GroupFacadeImpl implements GroupFacade {
    private final GroupService groupService;
    private final GroupMapper groupMapper;
    private final ActiveUserService userService;
    private final ActiveUserMapper userMapper;

    public GroupFacadeImpl(GroupService groupService, ActiveUserService userService, GroupMapper groupMapper, ActiveUserMapper userMapper) {
        this.groupService = groupService;
        this.groupMapper = groupMapper;
        this.userService = userService;
        this.userMapper = userMapper;
    }

    @Override
    public GroupDto createGroup(GroupCreateRequest groupCreateRequest) {
        this.validateGroupDoesNotAlreadyExist(groupCreateRequest);

        ActiveUser groupCreator = userService.getUserByUid(groupCreateRequest.getUserUid());
        Group groupToCreate = groupMapper.toGroup(groupCreateRequest);
        Group createdGroup = groupService.addGroup(groupToCreate);

        UserType adminType = userService.getUserTypeByName("admin");
        GroupMember groupAdmin = groupMapper.toGroupMember(groupCreator, adminType, createdGroup);
        groupService.addGroupMember(groupAdmin);

        UserDto adminDto = this.getUserDto(groupCreator);
        return groupMapper.toDto(createdGroup, adminDto);
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
}
