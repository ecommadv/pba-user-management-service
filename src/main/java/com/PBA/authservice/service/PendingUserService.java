package com.pba.authservice.service;

import com.pba.authservice.persistance.model.PendingUser;
import com.pba.authservice.persistance.repository.PendingUserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PendingUserService {
    private final PendingUserDao pendingUserDao;

    @Autowired
    public PendingUserService(PendingUserDao pendingUserDao) {
        this.pendingUserDao = pendingUserDao;
    }

    public PendingUser addPendingUser(PendingUser pendingUser) {
        return pendingUserDao.save(pendingUser);
    }
}
