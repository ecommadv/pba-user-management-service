package com.pba.authservice.mockgenerators;

import com.pba.authservice.persistance.model.PendingUser;
import com.pba.authservice.controller.request.PendingUserCreateRequest;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class PendingUserMockGenerator {
    public static PendingUser generateMockPendingUser() {
        return PendingUser.builder()
                .id(new Random().nextLong())
                .uid(UUID.randomUUID())
                .username(UUID.randomUUID().toString())
                .password(UUID.randomUUID().toString())
                .email(UUID.randomUUID().toString())
                .createdAt(Timestamp.from(Instant.now()).toLocalDateTime())
                .validationCode(UUID.randomUUID())
                .build();
    }

    public static List<PendingUser> generateMockListOfPendingUsers(int size) {
        return Stream.generate(PendingUserMockGenerator::generateMockPendingUser)
                .limit(size)
                .collect(Collectors.toList());
    }

    public static PendingUserCreateRequest generateMockPendingUserRequest() {
        return PendingUserCreateRequest.builder()
                .username(UUID.randomUUID().toString())
                .password(UUID.randomUUID().toString())
                .email(UUID.randomUUID().toString())
                .build();
    }
}
