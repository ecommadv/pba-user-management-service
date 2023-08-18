package com.pba.authservice.persistance.model.dtos;

import com.pba.authservice.persistance.model.ActiveUserProfile;
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
    private UUID uid;
    private String username;
    private UserProfileDto userProfile;
}
