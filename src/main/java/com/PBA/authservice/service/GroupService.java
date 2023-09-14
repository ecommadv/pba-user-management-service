package com.pba.authservice.service;

import com.pba.authservice.persistance.model.Group;
import com.pba.authservice.persistance.model.GroupMember;

public interface GroupService {
    public Group addGroup(Group group);
    public GroupMember addGroupMember(GroupMember groupMember);
    public boolean groupWithNameExists(String name);
}
