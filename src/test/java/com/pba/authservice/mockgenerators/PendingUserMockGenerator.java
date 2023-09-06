package com.pba.authservice.mockgenerators;

import com.pba.authservice.persistance.model.PendingUser;
import com.pba.authservice.controller.request.UserCreateRequest;
import com.pba.authservice.persistance.model.PendingUserProfile;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.Collections;
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
                .username(generateMockUsername())
                .password(generateMockPassword())
                .createdAt(Timestamp.from(Instant.now()).toLocalDateTime())
                .validationCode(UUID.randomUUID())
                .build();
    }

    public static List<PendingUser> generateMockListOfPendingUsers(int size) {
        return Stream.generate(PendingUserMockGenerator::generateMockPendingUser)
                .limit(size)
                .collect(Collectors.toList());
    }

    public static UserCreateRequest generateMockUserCreateRequest() {
        return UserCreateRequest.builder()
                .username(generateMockUsername())
                .password(generateMockPassword())
                .firstName(UUID.randomUUID().toString())
                .lastName(UUID.randomUUID().toString())
                .email(generateMockEmail())
                .build();
    }

    public static UserCreateRequest generateInvalidUserCreateRequest() {
        return UserCreateRequest.builder()
                .username("")
                .password("")
                .firstName("")
                .lastName("")
                .email("")
                .build();
    }

    public static PendingUserProfile generateMockPendingUserProfile(List<PendingUser> pendingUserList) {
        if (pendingUserList.isEmpty()) {
            return null;
        }
        return PendingUserProfile.builder()
                .id(new Random().nextLong())
                .firstName(UUID.randomUUID().toString())
                .lastName(UUID.randomUUID().toString())
                .email(generateMockEmail())
                .userId(getRandomPendingUserId(pendingUserList))
                .build();
    }

    private static Long getRandomPendingUserId(List<PendingUser> pendingUserList) {
        List<Long> pendingUserIds = pendingUserList.stream().map(PendingUser::getId).collect(Collectors.toList());
        Collections.shuffle(pendingUserIds);
        return pendingUserIds.stream().findFirst().get();
    }

    public static List<PendingUserProfile> generateMockListOfPendingUserProfiles(List<PendingUser> pendingUserList, int size) {
        if (pendingUserList.isEmpty()) {
            return null;
        }
        return Stream.generate(() -> PendingUserMockGenerator.generateMockPendingUserProfile(pendingUserList))
                .limit(size)
                .collect(Collectors.toList());
    }

    private static String generateMockEmail() {
        return String.format(
                "%s@%s.%s",
                generateRandomString(3),
                generateRandomString(3),
                generateRandomString(3)
        );
    }

    private static String generateMockPassword() {
        return String.format("@%s", generateRandomString(7));
    }

    private static String generateMockUsername() {
        return generateRandomString(10);
    }

    private static String generateRandomString(int length) {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        Random random = new Random();

        return random.ints(length, 0, chars.length())
                .mapToObj(chars::charAt)
                .map(Object::toString)
                .collect(Collectors.joining());
    }
}
