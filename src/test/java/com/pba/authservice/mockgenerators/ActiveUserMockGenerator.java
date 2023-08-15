package com.pba.authservice.mockgenerators;

import com.pba.authservice.persistance.model.ActiveUser;
import com.pba.authservice.persistance.model.dtos.ActiveUserDto;

import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ActiveUserMockGenerator {
    public static ActiveUser generateMockActiveUser() {
        return ActiveUser.builder()
                .id(new Random().nextLong())
                .uid(UUID.randomUUID())
                .username(UUID.randomUUID().toString())
                .password(UUID.randomUUID().toString())
                .build();
    }

    public static List<ActiveUser> generateMockListOfActiveUsers(int size) {
        return Stream.generate(ActiveUserMockGenerator::generateMockActiveUser)
                .limit(size)
                .collect(Collectors.toList());
    }

    public static ActiveUserDto generateMockActiveUserDto() {
        return ActiveUserDto.builder()
                .uid(UUID.randomUUID())
                .username(UUID.randomUUID().toString())
                .password(UUID.randomUUID().toString())
                .build();
    }
}
