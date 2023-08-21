package com.pba.authservice.unit;

import com.pba.authservice.facade.UserFacadeImpl;
import com.pba.authservice.mapper.ActiveUserMapper;
import com.pba.authservice.mapper.PendingUserMapper;
import com.pba.authservice.mockgenerators.ActiveUserMockGenerator;
import com.pba.authservice.mockgenerators.PendingUserMockGenerator;
import com.pba.authservice.persistance.model.ActiveUser;
import com.pba.authservice.persistance.model.ActiveUserProfile;
import com.pba.authservice.persistance.model.PendingUser;
import com.pba.authservice.controller.request.UserCreateRequest;
import com.pba.authservice.persistance.model.PendingUserProfile;
import com.pba.authservice.persistance.model.dtos.UserDto;
import com.pba.authservice.persistance.model.dtos.UserProfileDto;
import com.pba.authservice.service.ActiveUserService;
import com.pba.authservice.service.EmailService;
import com.pba.authservice.service.PendingUserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
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

    @Mock
    private EmailService emailService;

    @Test
    public void testRegisterUser() {
        // given
        UserCreateRequest userCreateRequest = PendingUserMockGenerator.generateMockUserCreateRequest();
        PendingUser pendingUser = PendingUserMockGenerator.generateMockPendingUser();
        List<PendingUser> pendingUserList = PendingUserMockGenerator.generateMockListOfPendingUsers(10);
        PendingUserProfile pendingUserProfile = PendingUserMockGenerator.generateMockPendingUserProfile(pendingUserList);
        when(pendingUserMapper.toPendingUser(userCreateRequest)).thenReturn(pendingUser);
        when(pendingUserService.addPendingUser(pendingUser)).thenReturn(pendingUser);
        when(pendingUserMapper.toPendingUserProfile(userCreateRequest, pendingUser.getId())).thenReturn(pendingUserProfile);

        // when
        userFacade.registerUser(userCreateRequest);

        // then
        verify(pendingUserMapper).toPendingUser(userCreateRequest);
        verify(pendingUserService).addPendingUser(pendingUser);
        verify(pendingUserMapper).toPendingUserProfile(userCreateRequest, pendingUser.getId());
        verify(pendingUserService).addPendingUserProfile(pendingUserProfile);
        verify(pendingUserService).userWithEmailExists(userCreateRequest.getEmail());
        verify(pendingUserService).userWithUsernameExists(userCreateRequest.getUsername());
        verify(activeUserService).userWithEmailExists(userCreateRequest.getEmail());
        verify(activeUserService).userWithUsernameExists(userCreateRequest.getUsername());
        verify(emailService).sendVerificationEmail(userCreateRequest.getEmail(), pendingUser.getValidationCode());
        verifyNoMoreInteractions(pendingUserMapper, pendingUserService);
    }

    @Test
    public void testGetActiveUser() {
        // given
        ActiveUser activeUser = ActiveUserMockGenerator.generateMockActiveUser();
        UUID uid = activeUser.getUid();
        UserDto userDto = ActiveUserMockGenerator.generateMockActiveUserDto();
        UserProfileDto userProfileDto = ActiveUserMockGenerator.generateMockUserProfileDto();
        ActiveUserProfile activeUserProfile = ActiveUserMockGenerator.generateMockActiveUserProfile();
        when(activeUserService.getUserByUid(uid)).thenReturn(activeUser);
        when(activeUserService.getProfileByUserId(activeUser.getId())).thenReturn(activeUserProfile);
        when(activeUserMapper.toUserProfileDto(activeUserProfile)).thenReturn(userProfileDto);
        when(activeUserMapper.toUserDto(activeUser, userProfileDto)).thenReturn(userDto);

        // when
        UserDto result = userFacade.getUser(uid);

        // then
        assertEquals(userDto, result);
    }
}
