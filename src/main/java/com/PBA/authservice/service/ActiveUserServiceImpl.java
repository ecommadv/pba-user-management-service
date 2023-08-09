package com.pba.authservice.service;

import com.pba.authservice.persistance.model.ActiveUser;
import com.pba.authservice.persistance.repository.ActiveUserDao;
import com.pba.authservice.persistance.repository.ActiveUserDaoImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
}
