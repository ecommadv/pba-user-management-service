package com.pba.authservice.persistance.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class GroupMember {
    private long id;
    private long userId;
    private long userTypeId;
    private long groupId;
}
