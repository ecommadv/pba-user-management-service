package com.pba.authservice.persistance.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class ActiveUserProfile {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String country;
    private Integer age;
    private Long userId;
}
