package com.pba.authservice.mockgenerators;

import com.pba.authservice.controller.request.GroupCreateRequest;
import com.pba.authservice.controller.request.GroupInviteRequest;
import com.pba.authservice.controller.request.GroupLoginRequest;
import com.pba.authservice.persistance.model.ActiveUser;
import com.pba.authservice.persistance.model.Group;
import com.pba.authservice.persistance.model.GroupMember;
import com.pba.authservice.persistance.model.UserType;

import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class GroupMockGenerator {
    public static Group generateMockGroup() {
        return Group.builder()
                .id(new Random().nextLong())
                .name(UUID.randomUUID().toString())
                .uid(UUID.randomUUID())
                .build();
    }

    public static List<Group> generateMockListOfGroups(int sampleSize) {
        return Stream.generate(GroupMockGenerator::generateMockGroup)
                .limit(sampleSize)
                .collect(Collectors.toList());
    }

    public static GroupMember generateMockGroupMember(Long userId, Long userTypeId, Long groupId) {
        return GroupMember.builder()
                .id(new Random().nextLong())
                .userId(userId)
                .userTypeId(userTypeId)
                .groupId(groupId)
                .build();
    }

    public static GroupMember generateMockGroupMember(List<ActiveUser> activeUsers, List<UserType> userTypes, List<Group> groupList) {
        List<Long> activeUsersIds = activeUsers.stream().map(ActiveUser::getId).collect(Collectors.toList());
        List<Long> userTypesIds = userTypes.stream().map(UserType::getId).collect(Collectors.toList());
        List<Long> groupListIds = groupList.stream().map(Group::getId).collect(Collectors.toList());

        return generateMockGroupMember(
                getRandomId(activeUsersIds),
                getRandomId(userTypesIds),
                getRandomId(groupListIds)
        );
    }

    public static List<GroupMember> generateMockListOfGroupMembers(int sampleSize, List<ActiveUser> activeUsers, List<UserType> userTypes, List<Group> groupList) {
        return Stream.generate(() -> generateMockGroupMember(activeUsers, userTypes, groupList))
                .limit(sampleSize)
                .collect(Collectors.toList());
    }

    public static GroupCreateRequest generateMockGroupCreateRequest() {
        return GroupCreateRequest.builder()
                .groupName(UUID.randomUUID().toString())
                .build();
    }

    public static GroupInviteRequest generateMockGroupInviteRequest(UUID groupUid, UUID userUid) {
        return GroupInviteRequest.builder()
                .groupUid(groupUid)
                .userUid(userUid)
                .build();
    }

    public static GroupLoginRequest generateMockGroupLoginRequest(UUID groupUid) {
        return GroupLoginRequest.builder()
                .groupUid(groupUid)
                .build();
    }

    private static Long getRandomId(List<Long> ids) {
        Collections.shuffle(ids);
        return ids.get(0);
    }

    private static UUID getRandomUid(List<UUID> uids) {
        Collections.shuffle(uids);
        return uids.get(0);
    }
}
