package com.pba.authservice.controller.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserUpdateRequest {
    @NotBlank(message = "{username.notblank}")
    @Size(min = 4, message = "{username.minsize}")
    @Size(max = 20, message = "{username.maxsize}")
    private String username;

    @NotBlank(message = "{firstname.notblank}")
    @Size(max = 50, message = "{firstname.maxsize}")
    private String firstName;

    @NotBlank(message = "{lastname.notblank}")
    @Size(max = 50, message = "{lastname.maxsize}")
    private String lastName;

    private String country;

    private int age;
}
