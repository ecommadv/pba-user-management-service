package com.pba.authservice.service;

import com.pba.authservice.exceptions.AuthorizationException;
import com.pba.authservice.exceptions.EntityNotFoundException;
import com.pba.authservice.exceptions.ErrorCodes;
import com.pba.authservice.persistance.model.ActiveUser;
import com.pba.authservice.persistance.model.Group;
import com.pba.authservice.persistance.model.GroupMember;
import com.pba.authservice.persistance.model.UserType;
import com.pba.authservice.persistance.repository.ActiveUserDao;
import com.pba.authservice.persistance.repository.GroupDao;
import com.pba.authservice.persistance.repository.GroupMemberDao;
import com.pba.authservice.persistance.repository.UserTypeDao;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

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

    @Override
    public Group getGroupByUid(UUID uid) {
        return groupDao.getByUid(uid)
                .orElseThrow(() -> new EntityNotFoundException(
                        ErrorCodes.GROUP_NOT_FOUND,
                        String.format("Group with uid %s does not exist", uid)
                ));
    }

    @Override
    public Optional<GroupMember> getGroupMemberByUserIdAndGroupId(Long userId, Long groupId) {
        return groupMemberDao.getByUserIdAndGroupId(userId, groupId);
    }
}
