package com.pba.authservice.service;

import com.pba.authservice.exceptions.AuthServiceException;
import com.pba.authservice.persistance.model.ActiveUser;
import com.pba.authservice.persistance.model.dtos.ActiveUserDto;
import com.pba.authservice.persistance.repository.ActiveUserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class ActiveUserServiceImpl implements ActiveUserService {
    private final ActiveUserDao activeUserDao;

    @Autowired
    public ActiveUserServiceImpl(ActiveUserDao activeUserDao) {
        this.activeUserDao = activeUserDao;
    }

    public ActiveUser addActiveUser(ActiveUser activeUser) {
        return activeUserDao.save(activeUser);
    }

    @Override
    public ActiveUser getByUid(UUID uid) {
        return activeUserDao.getByUid(uid)
                .orElseThrow(() -> new AuthServiceException(String.format("Active user with uid %s does not exist!", uid.toString())));
    }
}
