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
import com.pba.authservice.security.JwtSecurityService;
import com.pba.authservice.service.ActiveUserService;
import com.pba.authservice.service.EmailService;
import com.pba.authservice.service.PendingUserService;
import com.pba.authservice.validator.UserRequestValidator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

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

    @Mock
    private UserRequestValidator userRequestValidator;

    @Mock
    private JwtSecurityService jwtSecurityService;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Test
    public void testGetActiveUser() {
        // given
        ActiveUser activeUser = ActiveUserMockGenerator.generateMockActiveUser();
        UserDto userDto = ActiveUserMockGenerator.generateMockActiveUserDto();
        UserProfileDto userProfileDto = ActiveUserMockGenerator.generateMockUserProfileDto();
        ActiveUserProfile activeUserProfile = ActiveUserMockGenerator.generateMockActiveUserProfile();
        UUID uid = UUID.randomUUID();
        when(jwtSecurityService.getCurrentUserUid()).thenReturn(uid);
        when(activeUserService.getUserByUid(uid)).thenReturn(activeUser);
        when(activeUserService.getProfileByUserId(activeUser.getId())).thenReturn(activeUserProfile);
        when(activeUserMapper.toUserProfileDto(activeUserProfile)).thenReturn(userProfileDto);
        when(activeUserMapper.toUserDto(activeUser, userProfileDto)).thenReturn(userDto);

        // when
        UserDto result = userFacade.getUser();

        // then
        assertEquals(userDto, result);
    }
}
