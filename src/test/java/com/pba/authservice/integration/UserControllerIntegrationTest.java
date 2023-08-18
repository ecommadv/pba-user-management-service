package com.pba.authservice.integration;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.pba.authservice.mockgenerators.ActiveUserMockGenerator;
import com.pba.authservice.mockgenerators.PendingUserMockGenerator;
import com.pba.authservice.controller.request.UserCreateRequest;
import com.pba.authservice.persistance.model.ActiveUser;
import com.pba.authservice.persistance.model.ActiveUserProfile;
import com.pba.authservice.persistance.model.PendingUser;
import com.pba.authservice.persistance.model.PendingUserProfile;
import com.pba.authservice.persistance.model.dtos.UserDto;
import com.pba.authservice.persistance.repository.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import com.fasterxml.jackson.databind.ObjectMapper;

import static junit.framework.TestCase.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
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
    private MockMvc mockMvc;

    private ObjectMapper objectMapper;

    @BeforeEach
    public void setUp() {
        objectMapper = new ObjectMapper();
    }

    @Test
    public void testRegisterUser() throws Exception {
        // given
        UserCreateRequest userCreateRequest = PendingUserMockGenerator.generateMockUserCreateRequest();
        String userCreateRequestJSON = objectMapper.writeValueAsString(userCreateRequest);
        String registerUserEndpoint = "/api/user";

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
    public void testValidateUser() throws Exception {
        // given
        PendingUser pendingUser = PendingUserMockGenerator.generateMockPendingUser();
        pendingUserDao.save(pendingUser);
        PendingUserProfile pendingUserProfile = PendingUserMockGenerator.generateMockPendingUserProfile(pendingUserDao.getAll());
        pendingUserProfileDao.save(pendingUserProfile);
        String validateUserEndpoint = String.format("/api/user/activate/%s", pendingUser.getValidationCode());

        // when
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post(validateUserEndpoint))
                .andExpect(status().isOk())
                .andReturn();

        // then
        assertEquals(0, pendingUserDao.getAll().size());
        assertEquals(0, pendingUserProfileDao.getAll().size());
        assertEquals(1, activeUserDao.getAll().size());
        assertEquals(1, activeUserProfileDao.getAll().size());
        assertEquals(pendingUser.getUid(), activeUserDao.getAll().get(0).getUid());
    }
}
