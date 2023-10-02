package com.pba.authservice.service;

import com.pba.authservice.exceptions.ErrorCodes;
import com.pba.authservice.exceptions.EntityNotFoundException;
import com.pba.authservice.persistance.model.ActiveUser;
import com.pba.authservice.persistance.model.ActiveUserProfile;
import com.pba.authservice.persistance.model.PasswordToken;
import com.pba.authservice.persistance.model.UserType;
import com.pba.authservice.persistance.repository.ActiveUserDao;
import com.pba.authservice.persistance.repository.ActiveUserProfileDao;
import com.pba.authservice.persistance.repository.PasswordTokenDao;
import com.pba.authservice.persistance.repository.UserTypeDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class ActiveUserServiceImpl implements ActiveUserService {
    private final ActiveUserDao activeUserDao;
    private final ActiveUserProfileDao activeUserProfileDao;
    private final UserTypeDao userTypeDao;
    private final PasswordTokenDao passwordTokenDao;

    @Autowired
    public ActiveUserServiceImpl(ActiveUserDao activeUserDao, ActiveUserProfileDao activeUserProfileDao, UserTypeDao userTypeDao, PasswordTokenDao passwordTokenDao) {
        this.activeUserDao = activeUserDao;
        this.activeUserProfileDao = activeUserProfileDao;
        this.userTypeDao = userTypeDao;
        this.passwordTokenDao = passwordTokenDao;
    }

    @Override
    public ActiveUser addUser(ActiveUser activeUser) {
        return activeUserDao.save(activeUser);
    }

    @Override
    public void updateUser(ActiveUser user) {
        activeUserDao.update(user, user.getId());
    }

    @Override
    public ActiveUser getUserByUid(UUID uid) {
        return activeUserDao.getByUid(uid)
                .orElseThrow(() -> new EntityNotFoundException(ErrorCodes.USER_NOT_FOUND, String.format("User with uid %s does not exist!", uid)));
    }

    @Override
    public ActiveUserProfile getProfileByUserId(Long id) {
        return activeUserProfileDao.getByUserId(id)
                .orElseThrow(() -> new EntityNotFoundException(ErrorCodes.USER_NOT_FOUND, String.format("User with id %d does not exist!", id)));
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
    public UserType getUserTypeByName(String name) {
        return userTypeDao.getByName(name)
                .orElseThrow(() -> new EntityNotFoundException(ErrorCodes.USER_TYPE_NOT_FOUND, String.format("User type with name %s does not exist!", name)));
    }

    public void updateUser(ActiveUser updatedUser, ActiveUserProfile updatedProfile) {
        activeUserProfileDao.update(updatedProfile, updatedProfile.getId());
        activeUserDao.update(updatedUser, updatedUser.getId());
    }

    @Override
    public ActiveUser findByUsernameAndPassword(String username, String password, PasswordEncoder passwordEncoder) {
        return activeUserDao
                .getAll()
                .stream()
                .filter(user -> passwordEncoder.matches(password, user.getPassword()) && username.equals(user.getUsername()))
                .findAny()
                .orElseThrow(() -> new EntityNotFoundException(
                        ErrorCodes.USER_NOT_FOUND,
                        "Invalid username/password combination"
                ));
    }

    @Override
    public Optional<UserType> getUserTypeById(Long id) {
        return userTypeDao.getById(id);
    }

    @Override
    public PasswordToken addPasswordToken(PasswordToken passwordToken) {
        return passwordTokenDao.save(passwordToken);
    }

    @Override
    public ActiveUser getUserByEmail(String email) {
        return activeUserDao.getByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException(
                        ErrorCodes.USER_NOT_FOUND,
                        String.format("User with email %s does not exist", email)
                ));
    }

    @Override
    public ActiveUser getUserByPasswordToken(UUID token) {
        return activeUserDao.getByPasswordToken(token)
                .orElseThrow(() -> new EntityNotFoundException(
                        ErrorCodes.USER_NOT_FOUND,
                        String.format("No user with forgot password token %s exists", token)
                ));
    }

    @Override
    public void deletePasswordToken(UUID token) {
        Optional<PasswordToken> passwordToken = passwordTokenDao.getByToken(token);
        passwordToken.ifPresent(value -> passwordTokenDao.deleteById(value.getId()));
    }
}
