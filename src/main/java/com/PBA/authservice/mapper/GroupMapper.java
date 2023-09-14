package com.pba.authservice.mapper;

import com.pba.authservice.controller.request.GroupCreateRequest;
import com.pba.authservice.persistance.model.ActiveUser;
import com.pba.authservice.persistance.model.Group;
import com.pba.authservice.persistance.model.GroupMember;
import com.pba.authservice.persistance.model.UserType;
import com.pba.authservice.persistance.model.dtos.GroupDto;
import com.pba.authservice.persistance.model.dtos.UserDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface GroupMapper {
    @Mapping(target = "name", source = "groupName")
    @Mapping(target = "uid", expression = "java(java.util.UUID.randomUUID())")
    public Group toGroup(GroupCreateRequest groupCreateRequest);

    @Mapping(target = "groupName", expression = "java(group.getName())")
    @Mapping(target = "groupUid", expression = "java(group.getUid())")
    @Mapping(target = "groupAdmin", expression = "java(groupAdmin)")
    public GroupDto toDto(Group group, UserDto groupAdmin);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "userId", expression = "java(user.getId())")
    @Mapping(target = "userTypeId", expression = "java(userType.getId())")
    @Mapping(target = "groupId", expression = "java(group.getId())")
    public GroupMember toGroupMember(ActiveUser user, UserType userType, Group group);
}
