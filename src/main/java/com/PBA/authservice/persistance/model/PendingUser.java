package com.pba.authservice.persistance.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class PendingUser {
    private Long id;
    private UUID uid;
    private String username;
    private String password;
    private LocalDateTime createdAt;
    private UUID validationCode;

    public boolean isExpired() {
        return createdAt.isBefore(LocalDateTime.now().minusDays(1));
    }
}

