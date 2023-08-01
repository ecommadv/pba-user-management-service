package com.pba.authservice.service;

import com.pba.authservice.persistance.model.ActiveUser;
import com.pba.authservice.persistance.repository.ActiveUserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ActiveUserService {
    private ActiveUserDao activeUserDao;

    @Autowired
    public ActiveUserService(ActiveUserDao activeUserDao) {
        this.activeUserDao = activeUserDao;
    }

    public ActiveUser addActiveUser(ActiveUser activeUser) {
        return activeUserDao.save(activeUser);
    }
}
