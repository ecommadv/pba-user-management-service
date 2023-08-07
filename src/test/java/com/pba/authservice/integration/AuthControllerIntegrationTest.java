package com.pba.authservice.integration;

import com.pba.authservice.mockgenerators.PendingUserMockGenerator;
import com.pba.authservice.persistance.model.dtos.PendingUserRequest;
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

public class AuthControllerIntegrationTest extends BaseControllerIntegrationTest {
    @Autowired
    private PendingUserDao pendingUserDao;

    @Autowired
    private MockMvc mockMvc;

    private ObjectMapper objectMapper;

    @BeforeEach
    public void setUp() {
        objectMapper = new ObjectMapper();
    }

    @Test
    public void testRegisterUser() throws Exception {
        PendingUserRequest pendingUserRequest = PendingUserMockGenerator.generateMockPendingUserRequest();
        String pendingUserRequestJSON = objectMapper.writeValueAsString(pendingUserRequest);

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/api/auth/register")
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content(pendingUserRequestJSON)
                                ).andExpect(status().isCreated())
                                 .andReturn();

        Assertions.assertEquals(1, pendingUserDao.getAll().size());
    }
}
