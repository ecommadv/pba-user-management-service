package com.pba.authservice.integration;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.pba.authservice.controller.advice.ApiExceptionResponse;
import com.pba.authservice.controller.request.GroupCreateRequest;
import com.pba.authservice.controller.request.GroupInviteRequest;
import com.pba.authservice.controller.request.GroupLoginRequest;
import com.pba.authservice.controller.request.LoginRequest;
import com.pba.authservice.exceptions.ErrorCodes;
import com.pba.authservice.mockgenerators.ActiveUserMockGenerator;
import com.pba.authservice.mockgenerators.GroupMockGenerator;
import com.pba.authservice.persistance.model.*;
import com.pba.authservice.persistance.model.dtos.GroupDto;
import com.pba.authservice.persistance.model.dtos.GroupLoginDto;
import com.pba.authservice.persistance.repository.*;
import com.pba.authservice.security.JwtUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Map;
import java.util.Optional;

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

    @Autowired
    private JwtUtils jwtService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private ObjectMapper objectMapper;

    @BeforeEach
    public void setUp() {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
    }

    @Test
    public void testCreateGroup() throws Exception {
        // given
        GroupCreateRequest groupCreateRequest = GroupMockGenerator.generateMockGroupCreateRequest();
        ActiveUser groupAdmin = ActiveUserMockGenerator.generateMockActiveUser();
        String password = groupAdmin.getPassword();
        groupAdmin.setPassword(passwordEncoder.encode(groupAdmin.getPassword()));
        ActiveUser savedGroupAdmin = userDao.save(groupAdmin);
        String token = this.loginUserAndGetValidToken(savedGroupAdmin, password);
        String createGroupEndpoint = "/api/group";
        String groupCreateRequestJSON = objectMapper.writeValueAsString(groupCreateRequest);
        String authHeaderValue = String.format("Bearer %s", token);

        // when
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post(createGroupEndpoint)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(groupCreateRequestJSON)
                        .header("Authorization", authHeaderValue))
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

        GroupMember savedGroupMember = groupMemberDao.getAll().get(0);
        assertEquals(savedGroup.get().getId(), savedGroupMember.getGroupId());
        Optional<UserType> adminUserType = userTypeDao.getByName(UserTypeName.ADMIN);
        assertTrue(adminUserType.isPresent());
        assertEquals(adminUserType.get().getId(), savedGroupMember.getUserTypeId());
    }

    @Test
    public void testCreateGroupByNonexistentUser() throws Exception {
        // given
        GroupCreateRequest groupCreateRequest = GroupMockGenerator.generateMockGroupCreateRequest();
        String groupCreateRequestJSON = objectMapper.writeValueAsString(groupCreateRequest);
        ActiveUser user = ActiveUserMockGenerator.generateMockActiveUser();
        String invalidToken = jwtService.generateAccessToken(user);
        String authHeaderValue = String.format("Bearer %s", invalidToken);
        String createGroupEndpoint = "/api/group";

        // when
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post(createGroupEndpoint)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(groupCreateRequestJSON)
                        .header("Authorization", authHeaderValue))
                .andExpect(status().isNotFound())
                .andReturn();
        String apiExceptionResponseJSON = result.getResponse().getContentAsString();
        ApiExceptionResponse apiExceptionResponse = objectMapper.readValue(apiExceptionResponseJSON, ApiExceptionResponse.class);

        // then
        assertEquals(0, groupDao.getAll().size());
        assertEquals(0, groupMemberDao.getAll().size());
        Map<String, String> expectedErrorMap = Map.of(
                ErrorCodes.USER_NOT_FOUND,
                String.format("User with uid %s does not exist!", user.getUid())
        );
        assertEquals(expectedErrorMap, apiExceptionResponse.errors());
    }

    @Test
    public void testCreateGroupWhichAlreadyExists() throws Exception {
        // given
        GroupCreateRequest groupCreateRequest = GroupMockGenerator.generateMockGroupCreateRequest();
        this.addGroupWithName(groupCreateRequest.getGroupName());
        ActiveUser groupAdmin = ActiveUserMockGenerator.generateMockActiveUser();
        String password = groupAdmin.getPassword();
        groupAdmin.setPassword(passwordEncoder.encode(groupAdmin.getPassword()));
        ActiveUser savedGroupAdmin = userDao.save(groupAdmin);
        String token = this.loginUserAndGetValidToken(savedGroupAdmin, password);
        String createGroupEndpoint = "/api/group";
        String groupCreateRequestJSON = objectMapper.writeValueAsString(groupCreateRequest);
        String authHeaderValue = String.format("Bearer %s", token);

        // when
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post(createGroupEndpoint)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(groupCreateRequestJSON)
                        .header("Authorization", authHeaderValue))
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

    @Test
    public void testInviteUserToGroup() throws Exception {
        // given
        ActiveUser userToInvite = ActiveUserMockGenerator.generateMockActiveUser();
        Group group = GroupMockGenerator.generateMockGroup();
        userToInvite.setPassword(passwordEncoder.encode(userToInvite.getPassword()));
        ActiveUser savedUserToInvite = userDao.save(userToInvite);
        Group savedGroup = groupDao.save(group);
        ActiveUser groupAdmin = ActiveUserMockGenerator.generateMockActiveUser();
        String password = groupAdmin.getPassword();
        groupAdmin.setPassword(passwordEncoder.encode(groupAdmin.getPassword()));
        ActiveUser savedGroupAdmin = userDao.save(groupAdmin);
        UserType adminType = userTypeDao.getByName(UserTypeName.ADMIN).get();
        GroupMember adminMember = GroupMockGenerator.generateMockGroupMember(savedGroupAdmin.getId(), adminType.getId(), savedGroup.getId());
        groupMemberDao.save(adminMember);

        String token = this.loginUserAndGetValidToken(savedGroupAdmin, password);
        String authHeader = String.format("Bearer %s", token);
        String inviteEndpoint = "/api/group/invite";
        GroupInviteRequest groupInviteRequest = GroupMockGenerator.generateMockGroupInviteRequest(group.getUid(), userToInvite.getUid());
        String groupInviteRequestJSON = objectMapper.writeValueAsString(groupInviteRequest);

        // when
        mockMvc.perform(MockMvcRequestBuilders.post(inviteEndpoint)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(groupInviteRequestJSON)
                        .header("Authorization", authHeader))
                .andExpect(status().isOk())
                .andReturn();

        // then
        assertEquals(2, groupMemberDao.getAll().size());
        Optional<GroupMember> groupMember = groupMemberDao.getByUserIdAndGroupId(savedUserToInvite.getId(), savedGroup.getId());
        assertTrue(groupMember.isPresent());
        UserType regularUserType = userTypeDao.getByName(UserTypeName.REGULAR_USER).get();
        assertEquals(regularUserType.getId(), groupMember.get().getUserTypeId());
    }

    @Test
    public void testInviteUserToGroup_whenUserAlreadyIsInGroup() throws Exception {
        // given
        ActiveUser userToInvite = ActiveUserMockGenerator.generateMockActiveUser();
        userToInvite.setPassword(passwordEncoder.encode(userToInvite.getPassword()));
        Group group = GroupMockGenerator.generateMockGroup();

        ActiveUser savedUserToInvite = userDao.save(userToInvite);
        Group savedGroup = groupDao.save(group);
        ActiveUser groupAdmin = ActiveUserMockGenerator.generateMockActiveUser();
        String password = groupAdmin.getPassword();
        groupAdmin.setPassword(passwordEncoder.encode(groupAdmin.getPassword()));
        ActiveUser savedGroupAdmin = userDao.save(groupAdmin);
        UserType adminType = userTypeDao.getByName(UserTypeName.ADMIN).get();
        GroupMember adminMember = GroupMockGenerator.generateMockGroupMember(savedGroupAdmin.getId(), adminType.getId(), savedGroup.getId());
        groupMemberDao.save(adminMember);
        UserType regularUserType = userTypeDao.getByName(UserTypeName.REGULAR_USER).get();
        GroupMember userToInviteMember = GroupMockGenerator.generateMockGroupMember(savedUserToInvite.getId(), regularUserType.getId(), savedGroup.getId());
        groupMemberDao.save(userToInviteMember);

        String token = this.loginUserAndGetValidToken(savedGroupAdmin, password);
        String authHeader = String.format("Bearer %s", token);
        String inviteEndpoint = "/api/group/invite";
        GroupInviteRequest groupInviteRequest = GroupMockGenerator.generateMockGroupInviteRequest(group.getUid(), userToInvite.getUid());
        String groupInviteRequestJSON = objectMapper.writeValueAsString(groupInviteRequest);

        // when
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post(inviteEndpoint)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(groupInviteRequestJSON)
                        .header("Authorization", authHeader))
                .andExpect(status().isBadRequest())
                .andReturn();
        String apiExceptionResponseJSON = result.getResponse().getContentAsString();
        ApiExceptionResponse apiExceptionResponse = objectMapper.readValue(apiExceptionResponseJSON, ApiExceptionResponse.class);

        // then
        Map<String, String> expectedErrors = Map.of(
                ErrorCodes.GROUP_MEMBER_ALREADY_EXISTS,
                String.format("User with uid %s already is in group with uid %s", userToInvite.getUid(), group.getUid())
        );
        assertEquals(expectedErrors, apiExceptionResponse.errors());
    }

    @Test
    public void testInviteUserToGroup_whenClientIsNotAdminType() throws Exception {
        // given
        ActiveUser userToInvite = ActiveUserMockGenerator.generateMockActiveUser();
        userToInvite.setPassword(passwordEncoder.encode(userToInvite.getPassword()));
        Group group = GroupMockGenerator.generateMockGroup();
        userDao.save(userToInvite);
        Group savedGroup = groupDao.save(group);
        ActiveUser client = ActiveUserMockGenerator.generateMockActiveUser();
        String password = client.getPassword();
        client.setPassword(passwordEncoder.encode(client.getPassword()));
        ActiveUser savedClient = userDao.save(client);
        UserType regularUserType = userTypeDao.getByName(UserTypeName.REGULAR_USER).get();
        GroupMember clientGroupMember = GroupMockGenerator.generateMockGroupMember(savedClient.getId(), regularUserType.getId(), savedGroup.getId());
        groupMemberDao.save(clientGroupMember);

        String token = this.loginUserAndGetValidToken(savedClient, password);
        String authHeader = String.format("Bearer %s", token);
        String inviteEndpoint = "/api/group/invite";
        GroupInviteRequest groupInviteRequest = GroupMockGenerator.generateMockGroupInviteRequest(group.getUid(), userToInvite.getUid());
        String groupInviteRequestJSON = objectMapper.writeValueAsString(groupInviteRequest);

        // when
        mockMvc.perform(MockMvcRequestBuilders.post(inviteEndpoint)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(groupInviteRequestJSON)
                        .header("Authorization", authHeader))
                // then
                .andExpect(status().isUnauthorized())
                .andReturn();
    }

    @Test
    public void testGetGroupLoginInfo() throws Exception {
        // given
        ActiveUser user = ActiveUserMockGenerator.generateMockActiveUser();
        String password = user.getPassword();
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        ActiveUser savedUser = userDao.save(user);
        String userToken = this.loginUserAndGetValidToken(savedUser, password);
        Group group = GroupMockGenerator.generateMockGroup();
        Group savedGroup = groupDao.save(group);
        UserType userType = ActiveUserMockGenerator.generateMockUserType(userTypeDao.getAll());
        GroupMember groupMember = GroupMockGenerator.generateMockGroupMember(savedUser.getId(), userType.getId(), savedGroup.getId());
        groupMemberDao.save(groupMember);
        String groupToken = this.loginUserToGroupAndGetValidToken(savedGroup, userToken);
        String getLoginInfoEndpoint = "/api/group/login-info";
        String header = String.format("Bearer %s", groupToken);

        // when
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get(getLoginInfoEndpoint)
                        .header("Authorization", header))
                .andExpect(status().isOk())
                .andReturn();
        String groupLoginDtoJSON = result.getResponse().getContentAsString();
        GroupLoginDto groupLoginDto = objectMapper.readValue(groupLoginDtoJSON, GroupLoginDto.class);

        // then
        assertEquals(userType.getName(), groupLoginDto.getUserTypeName());
        assertEquals(savedUser.getUid(), groupLoginDto.getUserUid());
        assertEquals(savedGroup.getUid(), groupLoginDto.getGroupUid());
    }

    private String loginUserToGroupAndGetValidToken(Group group, String userToken) throws Exception {
        GroupLoginRequest groupLoginRequest = GroupMockGenerator.generateMockGroupLoginRequest(group.getUid());
        String groupLoginRequestJSON = objectMapper.writeValueAsString(groupLoginRequest);
        String loginToGroupEndpoint = "/api/group/login";
        String header = String.format("Bearer %s", userToken);

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post(loginToGroupEndpoint)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(groupLoginRequestJSON)
                        .header("Authorization", header))
                .andReturn();
        return result.getResponse().getContentAsString();
    }

    private void addGroupWithName(String name) {
        Group group = GroupMockGenerator.generateMockGroup();
        group.setName(name);
        groupDao.save(group);
    }

    private String loginUserAndGetValidToken(ActiveUser user, String password) throws Exception {
        ActiveUserProfile userProfile = ActiveUserMockGenerator.generateMockActiveUserProfile(user.getId());
        userProfileDao.save(userProfile);
        LoginRequest loginRequest = ActiveUserMockGenerator.generateMockLoginRequest(user.getUsername(), password);
        String loginRequestJSON = objectMapper.writeValueAsString(loginRequest);
        String loginEndpoint = "/api/user/login";

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post(loginEndpoint)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(loginRequestJSON))
                .andReturn();
        return result.getResponse().getContentAsString();
    }
}
