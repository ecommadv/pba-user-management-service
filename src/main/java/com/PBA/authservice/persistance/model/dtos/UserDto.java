package com.pba.authservice.persistance.model.dtos;

import com.pba.authservice.persistance.model.ActiveUserProfile;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserDto {
    @Schema(example = "3fa85f64-5717-4562-b3fc-2c963f66afa6")
    private UUID uid;

    @Schema(example = "john_doe")
    private String username;

    private UserProfileDto userProfile;
}
