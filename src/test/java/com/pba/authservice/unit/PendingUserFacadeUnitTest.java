package com.pba.authservice.unit;

import com.pba.authservice.facade.PendingUserFacadeImpl;
import com.pba.authservice.mockgenerators.PendingUserMockGenerator;
import com.pba.authservice.persistance.model.PendingUser;
import com.pba.authservice.persistance.model.dtos.PendingUserDtoMapper;
import com.pba.authservice.persistance.model.dtos.PendingUserRequest;
import com.pba.authservice.service.PendingUserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PendingUserFacadeUnitTest {
    @InjectMocks
    private PendingUserFacadeImpl pendingUserFacade;

    @Mock
    private PendingUserService pendingUserService;

    @Mock
    private PendingUserDtoMapper pendingUserDtoMapper;

    @Test
    public void testAddPendingUserRequest() {
        // given
        PendingUserRequest pendingUserRequest = PendingUserMockGenerator.generateMockPendingUserRequest();
        PendingUser pendingUser = PendingUserMockGenerator.generateMockPendingUser();
        when(pendingUserDtoMapper.toPendingUser(pendingUserRequest)).thenReturn(pendingUser);

        // when
        pendingUserFacade.addPendingUser(pendingUserRequest);

        // then
        verify(pendingUserDtoMapper).toPendingUser(pendingUserRequest);
        verify(pendingUserService).addPendingUser(pendingUser);
        verifyNoMoreInteractions(pendingUserDtoMapper, pendingUserService);
    }
}
