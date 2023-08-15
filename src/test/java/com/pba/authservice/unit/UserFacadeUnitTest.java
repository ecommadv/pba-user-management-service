package com.pba.authservice.unit;

import com.pba.authservice.facade.UserFacadeImpl;
import com.pba.authservice.mapper.ActiveUserMapper;
import com.pba.authservice.mapper.PendingUserMapper;
import com.pba.authservice.mockgenerators.ActiveUserMockGenerator;
import com.pba.authservice.mockgenerators.PendingUserMockGenerator;
import com.pba.authservice.persistance.model.ActiveUser;
import com.pba.authservice.persistance.model.PendingUser;
import com.pba.authservice.controller.request.PendingUserCreateRequest;
import com.pba.authservice.persistance.model.dtos.ActiveUserDto;
import com.pba.authservice.service.ActiveUserService;
import com.pba.authservice.service.PendingUserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserFacadeUnitTest {
    @InjectMocks
    private UserFacadeImpl userFacade;

    @Mock
    private PendingUserService pendingUserService;

    @Mock
    private PendingUserMapper pendingUserMapper;

    @Mock
    private ActiveUserService activeUserService;

    @Mock
    private ActiveUserMapper activeUserMapper;

    @Test
    public void testAddPendingUserRequest() {
        // given
        PendingUserCreateRequest pendingUserRequest = PendingUserMockGenerator.generateMockPendingUserRequest();
        PendingUser pendingUser = PendingUserMockGenerator.generateMockPendingUser();
        when(pendingUserMapper.toPendingUser(pendingUserRequest)).thenReturn(pendingUser);

        // when
        userFacade.addPendingUser(pendingUserRequest);

        // then
        verify(pendingUserMapper).toPendingUser(pendingUserRequest);
        verify(pendingUserService).addPendingUser(pendingUser);
        verifyNoMoreInteractions(pendingUserMapper, pendingUserService);
    }

    @Test
    public void testGetActiveUser() {
        ActiveUser activeUser = ActiveUserMockGenerator.generateMockActiveUser();
        UUID uid = activeUser.getUid();
        ActiveUserDto activeUserDto = ActiveUserMockGenerator.generateMockActiveUserDto();
        when(activeUserService.getByUid(uid)).thenReturn(activeUser);
        when(activeUserMapper.toActiveUserDto(activeUser)).thenReturn(activeUserDto);

        ActiveUserDto result = userFacade.getActiveUser(uid);

        Assertions.assertEquals(activeUserDto, result);
    }
}
