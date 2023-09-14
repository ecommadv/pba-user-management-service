package com.pba.authservice.persistance.model.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class GroupDto {
    private String groupName;
    private UUID groupUid;
    private UserDto groupAdmin;
}
