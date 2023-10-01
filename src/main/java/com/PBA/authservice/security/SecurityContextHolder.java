package com.pba.authservice.security;

import com.pba.authservice.persistance.model.UserType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.RequestScope;

import java.util.UUID;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
@Component
@RequestScope
public class SecurityContextHolder {
    private UUID currentUserUid;
    private UserType currentUserType;
    private UUID currentGroupUid;
}
