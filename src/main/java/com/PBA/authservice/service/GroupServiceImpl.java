package com.pba.authservice.service;

import com.pba.authservice.persistance.model.Group;
import com.pba.authservice.persistance.model.GroupMember;
import com.pba.authservice.persistance.repository.GroupDao;
import com.pba.authservice.persistance.repository.GroupMemberDao;
import org.springframework.stereotype.Service;

@Service
public class GroupServiceImpl implements GroupService {
    private final GroupDao groupDao;
    private final GroupMemberDao groupMemberDao;

    public GroupServiceImpl(GroupDao groupDao, GroupMemberDao groupMemberDao) {
        this.groupDao = groupDao;
        this.groupMemberDao = groupMemberDao;
    }

    @Override
    public Group addGroup(Group group) {
        return groupDao.save(group);
    }

    @Override
    public GroupMember addGroupMember(GroupMember groupMember) {
        return groupMemberDao.save(groupMember);
    }

    @Override
    public boolean groupWithNameExists(String name) {
        return groupDao.getByName(name).isPresent();
    }
}
