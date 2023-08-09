package com.pba.authservice.mockgenerators;

import com.pba.authservice.persistance.model.ActiveUser;
import net.bytebuddy.dynamic.TypeResolutionStrategy;
import org.mockito.Mockito;
import org.springframework.stereotype.Component;
import org.testcontainers.shaded.org.apache.commons.lang3.RandomStringUtils;

import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.function.Supplier;
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
}
