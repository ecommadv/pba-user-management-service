package com.pba.authservice.integration;

import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.pba.authservice.controller.advice.ApiExceptionResponse;
import com.pba.authservice.controller.request.LoginRequest;
import com.pba.authservice.controller.request.UserUpdateRequest;
import com.pba.authservice.exceptions.ErrorCodes;
import com.pba.authservice.mapper.PendingUserMapper;
import com.pba.authservice.mockgenerators.ActiveUserMockGenerator;
import com.pba.authservice.mockgenerators.PendingUserMockGenerator;
import com.pba.authservice.controller.request.UserCreateRequest;
import com.pba.authservice.persistance.model.ActiveUser;
import com.pba.authservice.persistance.model.ActiveUserProfile;
import com.pba.authservice.persistance.model.PendingUser;
import com.pba.authservice.persistance.model.PendingUserProfile;
import com.pba.authservice.persistance.model.dtos.UserDto;
import com.pba.authservice.persistance.repository.*;
import com.pba.authservice.service.JwtService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Pair;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class UserControllerIntegrationTest extends BaseControllerIntegrationTest {
    @Autowired
    private PendingUserDao pendingUserDao;

    @Autowired
    private PendingUserProfileDao pendingUserProfileDao;

    @Autowired
    private ActiveUserDao userDao;

    @Autowired
    private ActiveUserProfileDao userProfileDao;

    @Autowired
    private PendingUserMapper pendingUserMapper;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private MockMvc mockMvc;

    private ObjectMapper objectMapper;

    @BeforeEach
    public void setUp() {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
    }

    @Test
    public void testRegisterUser() throws Exception {
        // given
        UserCreateRequest userCreateRequest = PendingUserMockGenerator.generateMockUserCreateRequest();
        String userCreateRequestJSON = objectMapper.writeValueAsString(userCreateRequest);
        String registerUserEndpoint = "/api/user/register";

        // when
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post(registerUserEndpoint)
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content(userCreateRequestJSON))
                .andExpect(status().isCreated())
                .andReturn();

        // then
        assertEquals(1, pendingUserDao.getAll().size());
        assertEquals(1, pendingUserProfileDao.getAll().size());
    }

    @Test
    public void testRegisterUser_whenPendingUserAlreadyExists() throws Exception {
        // given
        UserCreateRequest userCreateRequest = PendingUserMockGenerator.generateMockUserCreateRequest();
        this.savePendingUser(userCreateRequest);

        String userCreateRequestJSON = objectMapper.writeValueAsString(userCreateRequest);
        String registerUserEndpoint = "/api/user/register";

        // when
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post(registerUserEndpoint)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userCreateRequestJSON))
                .andExpect(status().isBadRequest())
                .andReturn();

        // then
        assertEquals(1, pendingUserDao.getAll().size());
        assertEquals(1, pendingUserProfileDao.getAll().size());
    }

    @Test
    public void testUpdateUser() throws Exception {
        // given
        UserUpdateRequest userUpdateRequest = ActiveUserMockGenerator.generateMockUserUpdateRequest();
        ActiveUser activeUser = ActiveUserMockGenerator.generateMockActiveUser();
        ActiveUser savedActiveUser = userDao.save(activeUser);
        ActiveUserProfile activeUserProfile = ActiveUserMockGenerator.generateMockActiveUserProfile(savedActiveUser.getId());
        userProfileDao.save(activeUserProfile);
        String updateUserEndpoint = "/api/user";
        String userUpdateRequestJSON = objectMapper.writeValueAsString(userUpdateRequest);
        String token = this.getValidTokenForUser(activeUser);
        String authHeaderValue = String.format("Bearer %s", token);

        // when
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.put(updateUserEndpoint)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userUpdateRequestJSON)
                        .header("Authorization", authHeaderValue))
                .andExpect(status().isOk())
                .andReturn();

        // then
        assertEquals(1, userDao.getAll().size());
        assertEquals(1, userProfileDao.getAll().size());
        assertTrue(userDao.getByUid(activeUser.getUid()).isPresent());
        assertTrue(userProfileDao.getByUserId(savedActiveUser.getId()).isPresent());
        ActiveUser updatedActiveUser = userDao.getByUid(activeUser.getUid()).get();
        ActiveUserProfile updatedUserProfile = userProfileDao.getByUserId(savedActiveUser.getId()).get();
        assertEquals(userUpdateRequest.getUsername(), updatedActiveUser.getUsername());
        assertEquals(userUpdateRequest.getFirstName(), updatedUserProfile.getFirstName());
        assertEquals(userUpdateRequest.getLastName(), updatedUserProfile.getLastName());
        assertEquals(userUpdateRequest.getCountry(), updatedUserProfile.getCountry());
        assertEquals(userUpdateRequest.getAge(), updatedUserProfile.getAge());
    }

    @Test
    public void testGetActiveUser() throws Exception {
        // given
        ActiveUser activeUser = ActiveUserMockGenerator.generateMockActiveUser();
        ActiveUser savedActiveUser = userDao.save(activeUser);
        ActiveUserProfile userProfile = ActiveUserMockGenerator.generateMockActiveUserProfile(savedActiveUser.getId());
        userProfileDao.save(userProfile);
        String getUserEndpoint = "/api/user";
        String token = this.getValidTokenForUser(activeUser);
        String authHeaderValue = String.format("Bearer %s", token);

        // when
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get(getUserEndpoint)
                        .header("Authorization", authHeaderValue))
                .andExpect(status().isOk())
                .andReturn();
        String userDtoJSON = result.getResponse().getContentAsString();
        UserDto userDto = objectMapper.readValue(userDtoJSON, UserDto.class);

        // then
        assertEquals(activeUser.getUid(), userDto.getUid());
        assertEquals(userProfile.getFirstName(), userDto.getUserProfile().getFirstName());
        assertEquals(userProfile.getLastName(), userDto.getUserProfile().getLastName());
        assertEquals(userProfile.getCountry(), userDto.getUserProfile().getCountry());
        assertEquals(userProfile.getEmail(), userDto.getUserProfile().getEmail());
        assertEquals(userProfile.getAge(), userDto.getUserProfile().getAge());
    }

    @Test
    public void testActivate() throws Exception {
        // given
        PendingUser pendingUser = PendingUserMockGenerator.generateMockPendingUser();
        pendingUserDao.save(pendingUser);
        PendingUserProfile pendingUserProfile = PendingUserMockGenerator.generateMockPendingUserProfile(pendingUserDao.getAll());
        pendingUserProfileDao.save(pendingUserProfile);
        String validateUserEndpoint = String.format("/api/user/activate/%s", pendingUser.getValidationCode());

        // when
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post(validateUserEndpoint))
                .andExpect(status().isCreated())
                .andReturn();

        // then
        assertEquals(0, pendingUserDao.getAll().size());
        assertEquals(0, pendingUserProfileDao.getAll().size());
        assertEquals(1, userDao.getAll().size());
        assertEquals(1, userProfileDao.getAll().size());
        assertEquals(pendingUser.getUid(), userDao.getAll().get(0).getUid());
    }

    @Test
    public void testLoginExistentUser() throws Exception {
        // given
        LoginRequest loginRequest = ActiveUserMockGenerator.generateMockLoginRequest();
        Pair<ActiveUser, ActiveUserProfile> savedUser = this.saveActiveUser(loginRequest);
        String loginEndpoint = "/api/user/login";
        String loginRequestJSON = objectMapper.writeValueAsString(loginRequest);

        // when
        mockMvc.perform(MockMvcRequestBuilders.post(loginEndpoint)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(loginRequestJSON))
                // then
                .andExpect(status().isOk());
    }

    @Test
    public void testLoginNonexistentUser() throws Exception {
        // given
        LoginRequest loginRequest = ActiveUserMockGenerator.generateMockLoginRequest();
        String loginEndpoint = "/api/user/login";
        String loginRequestJSON = objectMapper.writeValueAsString(loginRequest);

        // when
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post(loginEndpoint)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(loginRequestJSON))
                .andExpect(status().isNotFound())
                .andReturn();
        String apiExceptionResponseJSON = result.getResponse().getContentAsString();
        ApiExceptionResponse apiExceptionResponse = objectMapper.readValue(apiExceptionResponseJSON, ApiExceptionResponse.class);

        // then
        Map<String, String> expectedErrors = Map.of(
                ErrorCodes.USER_NOT_FOUND,
                "Invalid username/password combination"
        );
        assertEquals(expectedErrors, apiExceptionResponse.errors());
    }

    @Test
    public void testLoginBadRequest() throws Exception {
        // given
        final String invalidUsername = null;
        final String invalidPassword = null;
        LoginRequest loginRequest = ActiveUserMockGenerator.generateMockLoginRequest(invalidUsername, invalidPassword);
        String loginEndpoint = "/api/user/login";
        String loginRequestJSON = objectMapper.writeValueAsString(loginRequest);

        // when
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post(loginEndpoint)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(loginRequestJSON))
                .andExpect(status().isBadRequest())
                .andReturn();
        String apiExceptionResponseJSON = result.getResponse().getContentAsString();
        ApiExceptionResponse apiExceptionResponse = objectMapper.readValue(apiExceptionResponseJSON, ApiExceptionResponse.class);

        // then
        Map<String, String> expectedErrors = Map.of(
                "password", "Password is mandatory",
                "username", "Username is mandatory"
        );
        assertEquals(expectedErrors, apiExceptionResponse.errors());
    }

    private void savePendingUser(UserCreateRequest userCreateRequest) {
        PendingUser pendingUser = pendingUserMapper.toPendingUser(userCreateRequest);
        PendingUser savedPendingUser = pendingUserDao.save(pendingUser);
        PendingUserProfile pendingUserProfile = pendingUserMapper.toPendingUserProfile(userCreateRequest, savedPendingUser.getId());
        pendingUserProfileDao.save(pendingUserProfile);
    }

    private Pair<ActiveUser, ActiveUserProfile> saveActiveUser(LoginRequest loginRequest) {
        ActiveUser activeUser = ActiveUserMockGenerator.generateMockActiveUser(loginRequest.getUsername(), loginRequest.getPassword());
        ActiveUser savedActiveUser = userDao.save(activeUser);
        ActiveUserProfile activeUserProfile = ActiveUserMockGenerator.generateMockActiveUserProfile(savedActiveUser.getId());
        ActiveUserProfile savedUserProfile = userProfileDao.save(activeUserProfile);
        return Pair.of(savedActiveUser, savedUserProfile);
    }

    private String getValidTokenForUser(ActiveUser user) throws Exception {
        return jwtService.generateAccessToken(user);
    }
}