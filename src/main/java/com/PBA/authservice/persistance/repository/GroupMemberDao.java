package com.pba.authservice.persistance.repository;

import com.pba.authservice.persistance.model.GroupMember;

import java.util.List;
import java.util.Optional;

public interface GroupMemberDao {
    public GroupMember save(GroupMember groupMember);
    public Optional<GroupMember> getById(Long id);
    public List<GroupMember> getAll();
    public GroupMember deleteById(Long id);
    public GroupMember update(GroupMember groupMember, Long id);
    public Optional<GroupMember> getByUserIdAndGroupId(Long userId, Long groupId);
}
