package com.pba.authservice.service;

import com.pba.authservice.persistance.model.Group;
import com.pba.authservice.persistance.model.GroupMember;

import java.util.Optional;
import java.util.UUID;

public interface GroupService {
    public Group addGroup(Group group);
    public GroupMember addGroupMember(GroupMember groupMember);
    public boolean groupWithNameExists(String name);
    public Group getGroupByUid(UUID uid);
    public Optional<GroupMember> getGroupMemberByUserIdAndGroupId(Long userId, Long groupId);
}
