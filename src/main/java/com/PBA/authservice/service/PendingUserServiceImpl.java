package com.pba.authservice.service;

import com.pba.authservice.persistance.model.PendingUser;
import com.pba.authservice.persistance.model.PendingUserProfile;
import com.pba.authservice.persistance.repository.PendingUserDao;
import com.pba.authservice.persistance.repository.PendingUserProfileDao;
import org.springframework.stereotype.Service;

@Service
public class PendingUserServiceImpl implements PendingUserService {
    private final PendingUserDao pendingUserDao;
    private final PendingUserProfileDao pendingUserProfileDao;

    public PendingUserServiceImpl(PendingUserDao pendingUserDao, PendingUserProfileDao pendingUserProfileDao) {
        this.pendingUserDao = pendingUserDao;
        this.pendingUserProfileDao = pendingUserProfileDao;
    }

    @Override
    public PendingUser addPendingUser(PendingUser pendingUser) {
        return pendingUserDao.save(pendingUser);
    }

    @Override
    public PendingUserProfile addPendingUserProfile(PendingUserProfile pendingUserProfile) {
        return pendingUserProfileDao.save(pendingUserProfile);
    }
}
