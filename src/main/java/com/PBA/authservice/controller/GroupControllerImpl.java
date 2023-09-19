package com.pba.authservice.controller;

import com.pba.authservice.controller.request.GroupCreateRequest;
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
    public ResponseEntity<GroupDto> createGroup(GroupCreateRequest groupCreateRequest, String authHeader) {
        GroupDto groupDto = groupFacade.createGroup(groupCreateRequest, authHeader);
        return new ResponseEntity<>(groupDto, HttpStatus.CREATED);
    }
}
