package com.pba.authservice.integration;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.pba.authservice.controller.advice.ApiExceptionResponse;
import com.pba.authservice.controller.request.GroupCreateRequest;
import com.pba.authservice.exceptions.ErrorCodes;
import com.pba.authservice.mockgenerators.ActiveUserMockGenerator;
import com.pba.authservice.mockgenerators.GroupMockGenerator;
import com.pba.authservice.persistance.model.*;
import com.pba.authservice.persistance.model.dtos.GroupDto;
import com.pba.authservice.persistance.repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class GroupControllerIntegrationTest extends BaseControllerIntegrationTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ActiveUserDao userDao;

    @Autowired
    private ActiveUserProfileDao userProfileDao;

    @Autowired
    private GroupDao groupDao;

    @Autowired
    private GroupMemberDao groupMemberDao;

    @Autowired
    private UserTypeDao userTypeDao;

    private ObjectMapper objectMapper;

    @BeforeEach
    public void setUp() {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
    }

    @Test
    public void testCreateGroup() throws Exception {
        // given
        this.addMockUsers();
        GroupCreateRequest groupCreateRequest = GroupMockGenerator.generateMockGroupCreateRequest(userDao.getAll());
        ActiveUser groupAdmin = userDao.getByUid(groupCreateRequest.getUserUid()).get();
        String createGroupEndpoint = "/api/group";
        String groupCreateRequestJSON = objectMapper.writeValueAsString(groupCreateRequest);

        // when
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post(createGroupEndpoint)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(groupCreateRequestJSON))
                .andExpect(status().isCreated())
                .andReturn();
        String groupDtoJSON = result.getResponse().getContentAsString();
        GroupDto groupDto = objectMapper.readValue(groupDtoJSON, GroupDto.class);

        // then
        assertEquals(1, groupDao.getAll().size());
        assertEquals(1, groupMemberDao.getAll().size());

        Optional<Group> savedGroup = groupDao.getByName(groupCreateRequest.getGroupName());
        assertTrue(savedGroup.isPresent());
        assertEquals(groupDto.getGroupName(), groupCreateRequest.getGroupName());
        assertEquals(savedGroup.get().getUid(), groupDto.getGroupUid());
        assertEquals(groupAdmin.getUid(), groupDto.getGroupAdmin().getUid());
        assertEquals(groupAdmin.getUsername(), groupDto.getGroupAdmin().getUsername());

        Optional<GroupMember> savedGroupMember = groupMemberDao.getByUserId(groupAdmin.getId());
        assertTrue(savedGroupMember.isPresent());
        assertEquals(savedGroup.get().getId(), savedGroupMember.get().getGroupId());
        Optional<UserType> adminUserType = userTypeDao.getByName("admin");
        assertTrue(adminUserType.isPresent());
        assertEquals(adminUserType.get().getId(), savedGroupMember.get().getUserTypeId());
    }

    @Test
    public void testCreateGroupByNonexistentUser() throws Exception {
        // given
        this.addMockUsers();
        GroupCreateRequest groupCreateRequest = GroupMockGenerator.generateMockGroupCreateRequest(userDao.getAll());
        UUID nonexistentUserUid = UUID.randomUUID();
        groupCreateRequest.setUserUid(nonexistentUserUid);
        String groupCreateRequestJSON = objectMapper.writeValueAsString(groupCreateRequest);
        String createGroupEndpoint = "/api/group";

        // when
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post(createGroupEndpoint)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(groupCreateRequestJSON))
                .andExpect(status().isNotFound())
                .andReturn();
        String apiExceptionResponseJSON = result.getResponse().getContentAsString();
        ApiExceptionResponse apiExceptionResponse = objectMapper.readValue(apiExceptionResponseJSON, ApiExceptionResponse.class);

        // then
        assertEquals(0, groupDao.getAll().size());
        assertEquals(0, groupMemberDao.getAll().size());
        Map<String, String> expectedErrorMap = Map.of(
                ErrorCodes.USER_NOT_FOUND,
                String.format("User with uid %s does not exist!", groupCreateRequest.getUserUid())
        );
        assertEquals(expectedErrorMap, apiExceptionResponse.errors());
    }

    @Test
    public void testCreateGroupWhichAlreadyExists() throws Exception {
        // given
        this.addMockUsers();
        GroupCreateRequest groupCreateRequest = GroupMockGenerator.generateMockGroupCreateRequest(userDao.getAll());
        this.addGroupWithName(groupCreateRequest.getGroupName());
        String createGroupEndpoint = "/api/group";
        String groupCreateRequestJSON = objectMapper.writeValueAsString(groupCreateRequest);

        // when
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post(createGroupEndpoint)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(groupCreateRequestJSON))
                .andExpect(status().isBadRequest())
                .andReturn();
        String apiExceptionResponseJSON = result.getResponse().getContentAsString();
        ApiExceptionResponse apiExceptionResponse = objectMapper.readValue(apiExceptionResponseJSON, ApiExceptionResponse.class);

        // then
        assertEquals(1, groupDao.getAll().size());
        assertEquals(0, groupMemberDao.getAll().size());
        Map<String, String> expectedErrorMap = Map.of(
                ErrorCodes.GROUP_ALREADY_EXISTS,
                String.format("Group with name %s already exists", groupCreateRequest.getGroupName())
        );
        assertEquals(expectedErrorMap, apiExceptionResponse.errors());
    }

    private void addMockUsers() {
        final int sampleSize = 10;
        List<ActiveUser> users = ActiveUserMockGenerator.generateMockListOfActiveUsers(sampleSize);
        users.forEach(user -> userDao.save(user));

        List<ActiveUserProfile> userProfiles = ActiveUserMockGenerator.generateMockListOfActiveUserProfiles(userDao.getAll());
        userProfiles.forEach(userProfile -> userProfileDao.save(userProfile));
    }

    private void addGroupWithName(String name) {
        Group group = GroupMockGenerator.generateMockGroup();
        group.setName(name);
        groupDao.save(group);
    }
}
