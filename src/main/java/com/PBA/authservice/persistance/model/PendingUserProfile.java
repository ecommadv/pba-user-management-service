package com.pba.authservice.persistance.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class PendingUserProfile {
    private long id;
    private String firstName;
    private String lastName;
    private String email;
    private long userId;
}
