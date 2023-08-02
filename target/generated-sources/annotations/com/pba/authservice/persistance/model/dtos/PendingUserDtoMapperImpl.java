package com.pba.authservice.persistance.model.dtos;

import com.pba.authservice.persistance.model.PendingUser;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2023-08-02T19:53:37+0300",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 19.0.2 (Oracle Corporation)"
)
@Component
public class PendingUserDtoMapperImpl implements PendingUserDtoMapper {

    @Override
    public PendingUser fromPendingUserRequestToPendingUser(PendingUserRequest pendingUserRequest) {
        if ( pendingUserRequest == null ) {
            return null;
        }

        PendingUser.PendingUserBuilder pendingUser = PendingUser.builder();

        pendingUser.username( pendingUserRequest.getUsername() );
        pendingUser.password( pendingUserRequest.getPassword() );
        pendingUser.email( pendingUserRequest.getEmail() );

        pendingUser.uid( java.util.UUID.randomUUID() );
        pendingUser.createdAt( java.sql.Timestamp.from(java.time.Instant.now()) );
        pendingUser.validationCode( java.util.UUID.randomUUID() );

        return pendingUser.build();
    }

    @Override
    public PendingUserResponse fromPendingUserToPendingUserResponse(PendingUser pendingUserResult) {
        if ( pendingUserResult == null ) {
            return null;
        }

        PendingUserResponse.PendingUserResponseBuilder pendingUserResponse = PendingUserResponse.builder();

        pendingUserResponse.uid( pendingUserResult.getUid() );
        pendingUserResponse.username( pendingUserResult.getUsername() );
        pendingUserResponse.password( pendingUserResult.getPassword() );
        pendingUserResponse.email( pendingUserResult.getEmail() );
        pendingUserResponse.createdAt( pendingUserResult.getCreatedAt() );
        pendingUserResponse.validationCode( pendingUserResult.getValidationCode() );

        return pendingUserResponse.build();
    }
}
