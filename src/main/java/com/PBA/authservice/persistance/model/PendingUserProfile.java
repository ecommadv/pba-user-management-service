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
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private Long userId;
}
