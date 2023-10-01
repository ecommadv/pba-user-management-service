package com.pba.authservice.persistance.model.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class GroupLoginDto {
    private UUID userUid;
    private UUID groupUid;
    @JsonProperty("role")
    private String userTypeName;
}
