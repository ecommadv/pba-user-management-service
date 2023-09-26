package com.pba.authservice.facade;

import com.pba.authservice.controller.request.GroupCreateRequest;
import com.pba.authservice.controller.request.GroupInviteRequest;
import com.pba.authservice.persistance.model.dtos.GroupDto;

public interface GroupFacade {
    public GroupDto createGroup(GroupCreateRequest groupCreateRequest);
    public void inviteUserToGroup(GroupInviteRequest groupInviteRequest);
}
