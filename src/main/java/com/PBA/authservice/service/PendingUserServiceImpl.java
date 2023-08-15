package com.pba.authservice.service;

import com.pba.authservice.persistance.model.PendingUser;
import com.pba.authservice.persistance.repository.PendingUserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PendingUserServiceImpl implements PendingUserService {
    private final PendingUserDao pendingUserDao;

    public PendingUserServiceImpl(PendingUserDao pendingUserDao) {
        this.pendingUserDao = pendingUserDao;
    }

    @Override
    public PendingUser addPendingUser(PendingUser pendingUser) {
        return pendingUserDao.save(pendingUser);
    }
}
