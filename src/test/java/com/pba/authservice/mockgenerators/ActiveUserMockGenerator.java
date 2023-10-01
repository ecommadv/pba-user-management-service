package com.pba.authservice.mockgenerators;
import com.pba.authservice.controller.request.LoginRequest;
import com.pba.authservice.controller.request.UserUpdateRequest;
import com.pba.authservice.persistance.model.ActiveUser;
import com.pba.authservice.persistance.model.ActiveUserProfile;
import com.pba.authservice.persistance.model.UserType;
import com.pba.authservice.persistance.model.dtos.UserDto;
import com.pba.authservice.persistance.model.dtos.UserProfileDto;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ActiveUserMockGenerator {
    public static ActiveUser generateMockActiveUser() {
        return ActiveUser.builder()
                .id(new Random().nextLong())
                .uid(UUID.randomUUID())
                .username(generateMockUsername())
                .password(generateMockPassword())
                .build();
    }

    public static List<ActiveUser> generateMockListOfActiveUsers(int size) {
        return Stream.generate(ActiveUserMockGenerator::generateMockActiveUser)
                .limit(size)
                .collect(Collectors.toList());
    }

    public static UserDto generateMockActiveUserDto() {
        return UserDto.builder()
                .uid(UUID.randomUUID())
                .username(generateMockUsername())
                .build();
    }

    public static ActiveUserProfile generateMockActiveUserProfile(Long userId) {
        return ActiveUserProfile.builder()
                .id(new Random().nextLong())
                .firstName(UUID.randomUUID().toString())
                .lastName(UUID.randomUUID().toString())
                .email(generateMockEmail())
                .country(UUID.randomUUID().toString())
                .age(new Random().nextInt())
                .userId(userId)
                .build();
    }

    public static ActiveUserProfile generateMockActiveUserProfile() {
        return generateMockActiveUserProfile(new Random().nextLong());
    }

    public static ActiveUser generateMockActiveUser(String username, String password) {
        return ActiveUser.builder()
                .id(new Random().nextLong())
                .uid(UUID.randomUUID())
                .username(username)
                .password(password)
                .build();
    }

    public static ActiveUserProfile generateMockActiveUserProfile(List<ActiveUser> activeUsers) {
        if (activeUsers.isEmpty()) {
            return null;
        }
        List<Long> activeUserIds = activeUsers.stream().map(ActiveUser::getId).collect(Collectors.toList());
        Collections.shuffle(activeUserIds);
        return generateMockActiveUserProfile(activeUserIds.stream().findFirst().get());
    }

    public static List<ActiveUserProfile> generateMockListOfActiveUserProfiles(List<ActiveUser> activeUsers) {
        return activeUsers
                .stream()
                .map(activeUser -> generateMockActiveUserProfile(activeUser.getId()))
                .collect(Collectors.toList());
    }

    public static UserProfileDto generateMockUserProfileDto() {
        return UserProfileDto.builder()
                .firstName(UUID.randomUUID().toString())
                .lastName(UUID.randomUUID().toString())
                .email(generateMockEmail())
                .country(UUID.randomUUID().toString())
                .age(new Random().nextInt())
                .build();
    }

    public static UserUpdateRequest generateMockUserUpdateRequest() {
        return UserUpdateRequest.builder()
                .username(generateMockUsername())
                .firstName(UUID.randomUUID().toString())
                .lastName(UUID.randomUUID().toString())
                .country(UUID.randomUUID().toString())
                .age(new Random().nextInt())
                .build();
    }

    public static UserType generateMockUserType() {
        return UserType.builder()
                .id(new Random().nextLong())
                .name(generateRandomString(5))
                .build();
    }

    public static UserType generateMockUserType(List<UserType> userTypes) {
        UserType userType = getRandomUserType(userTypes);
        return UserType.builder()
                .id(userType.getId())
                .name(userType.getName())
                .build();
    }

    public static List<UserType> generateMockListOfUserTypes(int sampleSize) {
        return Stream.generate(ActiveUserMockGenerator::generateMockUserType)
                .limit(sampleSize)
                .collect(Collectors.toList());
    }

    public static LoginRequest generateMockLoginRequest() {
        return LoginRequest.builder()
                .username(generateMockUsername())
                .password(generateMockPassword())
                .build();
    }

    public static LoginRequest generateMockLoginRequest(String username, String password) {
        return LoginRequest.builder()
                .username(username)
                .password(password)
                .build();
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

    private static UserType getRandomUserType(List<UserType> userTypes) {
        if (userTypes.isEmpty()) {
            return generateMockUserType();
        }
        List<UserType> userTypeList = new ArrayList<>(userTypes);
        Collections.shuffle(userTypeList);
        return userTypeList.get(0);
    }
}