package com.pba.authservice.persistance.model.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class GroupMemberDto {
    private String username;
    private String roleName;
    private String groupName;
}
