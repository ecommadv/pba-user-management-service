package com.pba.authservice.controller.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserCreateRequest {
    @NotBlank(message = "{username.notblank}")
    @Size(min = 4, message = "{username.minsize}")
    @Size(max = 20, message = "{username.maxsize}")
    @Schema(example = "john_doe")
    private String username;

    @NotBlank(message = "{password.notblank}")
    @Size(min = 7, message = "{password.minsize}")
    @Size(max = 20, message = "{password.maxsize}")
    @Pattern(regexp = ".*[!@#$%^&*?].*", message = "{password.specialchar}")
    @Pattern(regexp = ".*[A-Z].*", message = "{password.uppercase}")
    @Schema(example = "Password50!@")
    private String password;

    @NotBlank(message = "{firstname.notblank}")
    @Size(max = 50, message = "{firstname.maxsize}")
    @Schema(example = "John")
    private String firstName;

    @NotBlank(message = "{lastname.notblank}")
    @Size(max = 50, message = "{lastname.maxsize}")
    @Schema(example = "Doe")
    private String lastName;

    @NotBlank(message = "{email.notblank}")
    @Email(message = "{email.invalid}")
    @Schema(example = "john_doe@gmail.com")
    private String email;
}
