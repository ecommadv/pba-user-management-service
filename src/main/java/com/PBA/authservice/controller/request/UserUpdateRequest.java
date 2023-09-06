package com.pba.authservice.controller.request;

import io.swagger.v3.oas.annotations.media.Schema;
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
    @Schema(example = "john_doe")
    private String username;

    @NotBlank(message = "{firstname.notblank}")
    @Size(max = 50, message = "{firstname.maxsize}")
    @Schema(example = "John")
    private String firstName;

    @NotBlank(message = "{lastname.notblank}")
    @Size(max = 50, message = "{lastname.maxsize}")
    @Schema(example = "Doe")
    private String lastName;

    @Schema(example = "United States")
    private String country;

    @Schema(example = "30")
    private int age;
}
