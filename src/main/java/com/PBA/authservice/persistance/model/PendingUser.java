package com.pba.authservice.persistance.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.util.UUID;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class PendingUser {
    private long id;
    private UUID uid;
    private String username;
    private String password;
    private String email;
    private Timestamp createdAt;
    private UUID validationCode;
}

