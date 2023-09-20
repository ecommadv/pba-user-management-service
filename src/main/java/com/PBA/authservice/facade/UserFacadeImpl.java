package com.pba.authservice.facade;

import com.pba.authservice.controller.request.LoginRequest;
import com.pba.authservice.controller.request.UserUpdateRequest;
import com.pba.authservice.exceptions.ErrorCodes;
import com.pba.authservice.exceptions.EntityNotFoundException;
import com.pba.authservice.mapper.ActiveUserMapper;
import com.pba.authservice.mapper.PendingUserMapper;
import com.pba.authservice.persistance.model.ActiveUser;
import com.pba.authservice.persistance.model.ActiveUserProfile;
import com.pba.authservice.persistance.model.PendingUser;
import com.pba.authservice.persistance.model.PendingUserProfile;
import com.pba.authservice.persistance.model.dtos.UserDto;
import com.pba.authservice.persistance.model.dtos.UserProfileDto;
import com.pba.authservice.security.JwtSecurityService;
import com.pba.authservice.security.JwtUtils;
import com.pba.authservice.service.*;
import com.pba.authservice.controller.request.UserCreateRequest;
import com.pba.authservice.validator.UserRequestValidator;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Component
public class UserFacadeImpl implements UserFacade {
    private final PendingUserService pendingUserService;
    private final PendingUserMapper pendingUserMapper;
    private final ActiveUserService activeUserService;
    private final ActiveUserMapper activeUserMapper;
    private final EmailService emailService;
    private final UserRequestValidator userRequestValidator;
    private final JwtSecurityService jwtSecurityService;

    public UserFacadeImpl(PendingUserService pendingUserService,
                          PendingUserMapper pendingUserMapper,
                          ActiveUserService activeUserService,
                          ActiveUserMapper activeUserMapper,
                          EmailService emailService,
                          UserRequestValidator userRequestValidator,
                          JwtSecurityService jwtSecurityService) {
        this.pendingUserService = pendingUserService;
        this.pendingUserMapper = pendingUserMapper;
        this.activeUserService = activeUserService;
        this.activeUserMapper = activeUserMapper;
        this.emailService = emailService;
        this.userRequestValidator = userRequestValidator;
        this.jwtSecurityService = jwtSecurityService;
    }

    @Override
    @Transactional
    public void registerUser(UserCreateRequest userCreateRequest) {
        userRequestValidator.validateUserDoesNotAlreadyExistWhenCreate(userCreateRequest);
        PendingUser pendingUser = pendingUserMapper.toPendingUser(userCreateRequest);
        PendingUser savedPendingUser = pendingUserService.addPendingUser(pendingUser);

        PendingUserProfile pendingUserProfile = pendingUserMapper.toPendingUserProfile(userCreateRequest, savedPendingUser.getId());
        pendingUserService.addPendingUserProfile(pendingUserProfile);

        emailService.sendVerificationEmail(userCreateRequest.getEmail(), savedPendingUser.getValidationCode());
    }

    @Override
    public UserDto getUser() {
        UUID userUid = jwtSecurityService.getCurrentUserUid();
        ActiveUser activeUser = activeUserService.getUserByUid(userUid);
        ActiveUserProfile activeUserProfile = activeUserService.getProfileByUserId(activeUser.getId());

        UserProfileDto userProfileDto = activeUserMapper.toUserProfileDto(activeUserProfile);
        return activeUserMapper.toUserDto(activeUser, userProfileDto);
    }

    @Override
    @Transactional
    public UserDto verifyUser(UUID validationCode) {
        PendingUser userToValidate = pendingUserService.getPendingUserByValidationCode(validationCode);
        this.validatePendingUser(userToValidate);
        PendingUserProfile pendingUserProfile = pendingUserService.getPendingUserProfileByUserId(userToValidate.getId());
        this.deletePendingUser(userToValidate, pendingUserProfile);

        Pair<ActiveUser, ActiveUserProfile> savedUserAndProfile = this.addActiveUser(userToValidate, pendingUserProfile);
        ActiveUser savedActiveUser = savedUserAndProfile.getFirst();
        ActiveUserProfile savedUserProfile = savedUserAndProfile.getSecond();
        UserProfileDto userProfileDto = activeUserMapper.toUserProfileDto(savedUserProfile);
        return activeUserMapper.toUserDto(savedActiveUser, userProfileDto);
    }

    @Override
    @Transactional
    public UserDto updateUser(UserUpdateRequest userUpdateRequest) {
        UUID userToUpdateUid = jwtSecurityService.getCurrentUserUid();
        ActiveUser userToUpdate = activeUserService.getUserByUid(userToUpdateUid);
        ActiveUserProfile profileToUpdate = activeUserService.getProfileByUserId(userToUpdate.getId());

        userRequestValidator.validateUserDoesNotAlreadyExistWhenUpdate(userUpdateRequest, userToUpdate, profileToUpdate);

        ActiveUser updatedUser = activeUserMapper.toUser(userUpdateRequest, userToUpdate);
        ActiveUserProfile updatedProfile = activeUserMapper.toUserProfile(userUpdateRequest, profileToUpdate);
        activeUserService.updateUser(updatedUser, updatedProfile);

        UserProfileDto userProfileDto = activeUserMapper.toUserProfileDto(updatedProfile);
        return activeUserMapper.toUserDto(updatedUser, userProfileDto);
    }

    @Override
    public String loginUser(LoginRequest loginRequest) {
        ActiveUser user = activeUserService.findByUsernameAndPassword(loginRequest.getUsername(), loginRequest.getPassword());
        return jwtSecurityService.generateAccessToken(user);
    }

    private void deletePendingUser(PendingUser pendingUser, PendingUserProfile pendingUserProfile) {
        pendingUserService.deletePendingProfileById(pendingUserProfile.getId());
        pendingUserService.deletePendingUserById(pendingUser.getId());
    }

    private void validatePendingUser(PendingUser pendingUser) {
        if (pendingUser.isExpired()) {
            String errorMessage = String.format("Pending user with validation code %s has expired", pendingUser.getValidationCode());
            throw new EntityNotFoundException(ErrorCodes.USER_IS_EXPIRED, errorMessage);
        }
    }

    private Pair<ActiveUser, ActiveUserProfile> addActiveUser(PendingUser pendingUser, PendingUserProfile pendingUserProfile) {
        ActiveUser activeUser = pendingUserMapper.toActiveUser(pendingUser);
        ActiveUser savedActiveUser = activeUserService.addUser(activeUser);
        ActiveUserProfile activeUserProfile = pendingUserMapper.toActiveUserProfile(pendingUserProfile, savedActiveUser.getId());
        ActiveUserProfile savedUserProfile = activeUserService.addUserProfile(activeUserProfile);
        return Pair.of(savedActiveUser, savedUserProfile);
    }
}
