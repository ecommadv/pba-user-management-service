package com.pba.authservice.integration;

import com.pba.authservice.mockgenerators.ActiveUserMockGenerator;
import com.pba.authservice.mockgenerators.PendingUserMockGenerator;
import com.pba.authservice.controller.request.PendingUserCreateRequest;
import com.pba.authservice.persistance.model.ActiveUser;
import com.pba.authservice.persistance.model.dtos.ActiveUserDto;
import com.pba.authservice.persistance.repository.ActiveUserDao;
import com.pba.authservice.persistance.repository.PendingUserDao;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import com.fasterxml.jackson.databind.ObjectMapper;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class UserControllerIntegrationTest extends BaseControllerIntegrationTest {
    @Autowired
    private PendingUserDao pendingUserDao;

    @Autowired
    private ActiveUserDao activeUserDao;

    @Autowired
    private MockMvc mockMvc;

    private ObjectMapper objectMapper;

    @BeforeEach
    public void setUp() {
        objectMapper = new ObjectMapper();
    }

    @Test
    public void testRegisterUser() throws Exception {
        PendingUserCreateRequest pendingUserRequest = PendingUserMockGenerator.generateMockPendingUserRequest();
        String pendingUserRequestJSON = objectMapper.writeValueAsString(pendingUserRequest);

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/api/user/register")
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content(pendingUserRequestJSON))
                .andExpect(status().isCreated())
                .andReturn();

        Assertions.assertEquals(1, pendingUserDao.getAll().size());
    }

    @Test
    public void testGetActiveUser() throws Exception {
        ActiveUser activeUser = ActiveUserMockGenerator.generateMockActiveUser();
        activeUserDao.save(activeUser);

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get(String.format("/api/user/active/%s", activeUser.getUid().toString())))
                .andExpect(status().isOk())
                .andReturn();
        String activeUserDtoJSON = result.getResponse().getContentAsString();
        ActiveUserDto activeUserDto = objectMapper.readValue(activeUserDtoJSON, ActiveUserDto.class);

        Assertions.assertEquals(activeUser.getUid(), activeUserDto.getUid());
    }
}
