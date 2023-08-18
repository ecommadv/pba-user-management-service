package com.pba.authservice.service;

import com.pba.authservice.exceptions.AuthServiceException;
import com.pba.authservice.persistance.model.ActiveUser;
import com.pba.authservice.persistance.model.ActiveUserProfile;
import com.pba.authservice.persistance.repository.ActiveUserDao;
import com.pba.authservice.persistance.repository.ActiveUserProfileDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class ActiveUserServiceImpl implements ActiveUserService {
    private final ActiveUserDao activeUserDao;
    private final ActiveUserProfileDao activeUserProfileDao;

    @Autowired
    public ActiveUserServiceImpl(ActiveUserDao activeUserDao, ActiveUserProfileDao activeUserProfileDao) {
        this.activeUserDao = activeUserDao;
        this.activeUserProfileDao = activeUserProfileDao;
    }

    public ActiveUser addUser(ActiveUser activeUser) {
        return activeUserDao.save(activeUser);
    }

    @Override
    public ActiveUser getUserByUid(UUID uid) {
        return activeUserDao.getByUid(uid)
                .orElseThrow(() -> new AuthServiceException(String.format("User with uid %s does not exist!", uid.toString())));
    }

    @Override
    public ActiveUserProfile getProfileByUserId(Long id) {
        return activeUserProfileDao.getByUserId(id)
                .orElseThrow(() -> new AuthServiceException(String.format("User profile with user id %d does not exist!", id)));
    }
}
