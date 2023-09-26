package com.pba.authservice.controller.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class GroupInviteRequest {
    @NotNull(message = "{userUid.notnull}")
    private UUID userUid;

    @NotNull(message = "{groupUid.notnull}")
    private UUID groupUid;
}
