package com.pba.authservice.controller.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class LoginRequest {
    @NotBlank(message = "{username.notblank}")
    @Schema(example = "john_doe")
    private String username;

    @NotBlank(message = "{password.notblank}")
    @Schema(example = "Password50!@")
    private String password;
}
