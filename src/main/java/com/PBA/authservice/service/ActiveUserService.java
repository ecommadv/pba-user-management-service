package com.pba.authservice.service;

import com.pba.authservice.persistance.model.ActiveUser;
import com.pba.authservice.persistance.model.dtos.ActiveUserDto;

import java.util.UUID;

public interface ActiveUserService {
    public ActiveUser addActiveUser(ActiveUser activeUser);

    public ActiveUser getByUid(UUID uid);
}
