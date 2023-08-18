package com.pba.authservice.controller.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserCreateRequest {
    private String username;
    private String password;
    private String firstName;
    private String lastName;
    private String email;
}
