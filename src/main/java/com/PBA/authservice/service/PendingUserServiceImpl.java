package com.pba.authservice.service;

import com.pba.authservice.exceptions.ErrorCodes;
import com.pba.authservice.exceptions.UserNotFoundException;
import com.pba.authservice.persistance.model.PendingUser;
import com.pba.authservice.persistance.model.PendingUserProfile;
import com.pba.authservice.persistance.repository.PendingUserDao;
import com.pba.authservice.persistance.repository.PendingUserProfileDao;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.UUID;

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

    @Override
    public PendingUser getPendingUserByValidationCode(UUID validationCode) {
        return pendingUserDao.getByValidationCode(validationCode)
                .orElseThrow(() -> new UserNotFoundException(ErrorCodes.USER_NOT_FOUND, String.format("User with validation code %s does not exist!", validationCode)));
    }

    @Override
    public PendingUser deletePendingUserById(Long id) {
        return pendingUserDao.deleteById(id);
    }

    @Override
    public PendingUserProfile getPendingUserProfileByUserId(Long id) {
        return pendingUserProfileDao.getByUserId(id)
                .orElseThrow(() -> new UserNotFoundException(ErrorCodes.USER_NOT_FOUND, String.format("User with id %d does not exist!", id)));
    }

    @Override
    public PendingUserProfile deletePendingProfileById(Long id) {
        return pendingUserProfileDao.deleteById(id);
    }

    @Override
    public boolean userWithEmailExists(String email) {
        return pendingUserProfileDao.getByEmail(email).isPresent();
    }

    @Override
    public boolean userWithUsernameExists(String username) {
        return pendingUserDao.getByUsername(username).isPresent();
    }
}
