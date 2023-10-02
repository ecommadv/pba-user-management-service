package com.pba.authservice.controller.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class ForgotPasswordRequest {
    @NotBlank(message = "{email.notblank}")
    @Email(message = "{email.invalid}")
    @Schema(example = "john_doe@gmail.com")
    private String email;
}
