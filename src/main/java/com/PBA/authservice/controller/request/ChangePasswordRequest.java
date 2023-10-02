package com.pba.authservice.controller.request;

import io.swagger.v3.oas.annotations.media.Schema;
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
public class ChangePasswordRequest {
    @NotBlank(message = "{password.notblank}")
    @Size(min = 7, message = "{password.minsize}")
    @Size(max = 20, message = "{password.maxsize}")
    @Pattern(regexp = ".*[!@#$%^&*?].*", message = "{password.specialchar}")
    @Pattern(regexp = ".*[A-Z].*", message = "{password.uppercase}")
    @Schema(example = "Password50!@")
    private String newPassword;
}
