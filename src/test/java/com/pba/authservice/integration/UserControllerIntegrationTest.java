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
    private ActiveUserDao activeUserDao;

    @Autowired
    private ActiveUserProfileDao activeUserProfileDao;

    @Autowired
    private PendingUserMapper pendingUserMapper;

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
        ActiveUser savedActiveUser = activeUserDao.save(activeUser);
        ActiveUserProfile activeUserProfile = ActiveUserMockGenerator.generateMockActiveUserProfile(activeUserDao.getAll());
        ActiveUserProfile savedUserProfile = activeUserProfileDao.save(activeUserProfile);
        UUID userToUpdateUid = savedActiveUser.getUid();
        String updateUserEndpoint = String.format("/api/user/%s", userToUpdateUid);
        String userUpdateRequestJSON = objectMapper.writeValueAsString(userUpdateRequest);

        // when
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.put(updateUserEndpoint)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userUpdateRequestJSON))
                .andExpect(status().isOk())
                .andReturn();

        // then
        assertEquals(1, activeUserDao.getAll().size());
        assertEquals(1, activeUserProfileDao.getAll().size());
        assertTrue(activeUserDao.getByUid(userToUpdateUid).isPresent());
        assertTrue(activeUserProfileDao.getByUserId(savedActiveUser.getId()).isPresent());
        ActiveUser updatedActiveUser = activeUserDao.getByUid(userToUpdateUid).get();
        ActiveUserProfile updatedUserProfile = activeUserProfileDao.getByUserId(savedActiveUser.getId()).get();
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
        ActiveUser savedActiveUser = activeUserDao.save(activeUser);
        ActiveUserProfile activeUserProfile = ActiveUserMockGenerator.generateMockActiveUserProfile(savedActiveUser.getId());
        ActiveUserProfile savedActiveUserProfile = activeUserProfileDao.save(activeUserProfile);
        String getUserEndpoint = String.format("/api/user/%s", activeUser.getUid().toString());

        // when
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get(getUserEndpoint))
                .andExpect(status().isOk())
                .andReturn();
        String userDtoJSON = result.getResponse().getContentAsString();
        UserDto userDto = objectMapper.readValue(userDtoJSON, UserDto.class);

        // then
        assertEquals(activeUser.getUid(), userDto.getUid());
        assertEquals(activeUserProfile.getFirstName(), userDto.getUserProfile().getFirstName());
        assertEquals(activeUserProfile.getLastName(), userDto.getUserProfile().getLastName());
        assertEquals(activeUserProfile.getCountry(), userDto.getUserProfile().getCountry());
        assertEquals(activeUserProfile.getEmail(), userDto.getUserProfile().getEmail());
        assertEquals(activeUserProfile.getAge(), userDto.getUserProfile().getAge());
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
        assertEquals(1, activeUserDao.getAll().size());
        assertEquals(1, activeUserProfileDao.getAll().size());
        assertEquals(pendingUser.getUid(), activeUserDao.getAll().get(0).getUid());
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
        ActiveUser savedActiveUser = activeUserDao.save(activeUser);
        ActiveUserProfile activeUserProfile = ActiveUserMockGenerator.generateMockActiveUserProfile(savedActiveUser.getId());
        ActiveUserProfile savedUserProfile = activeUserProfileDao.save(activeUserProfile);
        return Pair.of(savedActiveUser, savedUserProfile);
    }
}