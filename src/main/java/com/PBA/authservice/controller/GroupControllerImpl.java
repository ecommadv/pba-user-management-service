package com.pba.authservice.controller;

import com.pba.authservice.controller.request.GroupCreateRequest;
import com.pba.authservice.controller.request.GroupInviteRequest;
import com.pba.authservice.controller.request.GroupLoginRequest;
import com.pba.authservice.facade.GroupFacade;
import com.pba.authservice.persistance.model.dtos.GroupDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GroupControllerImpl implements GroupController {
    private final GroupFacade groupFacade;

    public GroupControllerImpl(GroupFacade groupFacade) {
        this.groupFacade = groupFacade;
    }

    @Override
    public ResponseEntity<GroupDto> createGroup(GroupCreateRequest groupCreateRequest) {
        GroupDto groupDto = groupFacade.createGroup(groupCreateRequest);
        return new ResponseEntity<>(groupDto, HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<Void> inviteUserToGroup(GroupInviteRequest groupInviteRequest) {
        groupFacade.inviteUserToGroup(groupInviteRequest);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Override
    public ResponseEntity<String> loginToGroup(GroupLoginRequest groupLoginRequest) {
        String token = groupFacade.loginToGroup(groupLoginRequest);
        return new ResponseEntity<>(token, HttpStatus.OK);
    }
}
