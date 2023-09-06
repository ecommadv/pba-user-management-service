package com.pba.authservice.service;

import com.pba.authservice.exceptions.ErrorCodes;
import com.pba.authservice.exceptions.UserNotFoundException;
import com.pba.authservice.persistance.model.ActiveUser;
import com.pba.authservice.persistance.model.ActiveUserProfile;
import com.pba.authservice.persistance.repository.ActiveUserDao;
import com.pba.authservice.persistance.repository.ActiveUserProfileDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;
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
                .orElseThrow(() -> new UserNotFoundException(ErrorCodes.USER_NOT_FOUND, String.format("User with uid %s does not exist!", uid)));
    }

    @Override
    public ActiveUserProfile getProfileByUserId(Long id) {
        return activeUserProfileDao.getByUserId(id)
                .orElseThrow(() -> new UserNotFoundException(ErrorCodes.USER_NOT_FOUND, String.format("User with id %d does not exist!", id)));
    }

    @Override
    public ActiveUserProfile addUserProfile(ActiveUserProfile activeUserProfile) {
        return activeUserProfileDao.save(activeUserProfile);
    }

    @Override
    public boolean userWithEmailExists(String email) {
        return activeUserProfileDao.getByEmail(email).isPresent();
    }

    @Override
    public boolean userWithUsernameExists(String username) {
        return activeUserDao.getByUsername(username).isPresent();
    }

    @Override
    public void updateUser(ActiveUser updatedUser, ActiveUserProfile updatedProfile) {
        activeUserProfileDao.update(updatedProfile, updatedProfile.getId());
        activeUserDao.update(updatedUser, updatedUser.getId());
    }
}
