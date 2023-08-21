package com.pba.authservice.unit;

import com.pba.authservice.mockgenerators.ActiveUserMockGenerator;
import com.pba.authservice.persistance.model.ActiveUser;
import com.pba.authservice.persistance.model.ActiveUserProfile;
import com.pba.authservice.persistance.repository.ActiveUserDaoImpl;
import com.pba.authservice.persistance.repository.ActiveUserProfileDao;
import com.pba.authservice.service.ActiveUserServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ActiveUserServiceUnitTest {
    @InjectMocks
    private ActiveUserServiceImpl activeUserService;

    @Mock
    private ActiveUserDaoImpl activeUserDao;

    @Mock
    private ActiveUserProfileDao activeUserProfileDao;

    @Test
    public void testAdd() {
        // given
        ActiveUser activeUser = ActiveUserMockGenerator.generateMockActiveUser();
        when(activeUserDao.save(activeUser)).thenReturn(activeUser);

        // when
        ActiveUser result = activeUserService.addUser(activeUser);

        // then
        Assertions.assertEquals(activeUser, result);
    }

    @Test
    public void testUserWithEmailExists_whenUserWithGivenEmailExists() {
        // given
        String email = "test@gmail.com";
        when(activeUserProfileDao.getByEmail(email)).thenReturn(Optional.of(new ActiveUserProfile()));

        // when
        boolean result = activeUserService.userWithEmailExists(email);

        // then
        assertTrue(result);
    }

    @Test
    public void testUserWithEmailExists_whenUserWithGivenEmailDoesNotExist() {
        // given
        String email = "test@gmail.com";
        when(activeUserProfileDao.getByEmail(email)).thenReturn(Optional.empty());

        // when
        boolean result = activeUserService.userWithEmailExists(email);

        // then
        assertFalse(result);
    }

    @Test
    public void testUserWithUsernameExists_whenUserWithGivenUsernameExists() {
        // given
        String username = "test";
        when(activeUserDao.getByUsername(username)).thenReturn(Optional.of(new ActiveUser()));

        // when
        boolean result = activeUserService.userWithUsernameExists(username);

        // then
        assertTrue(result);
    }

    @Test
    public void testUserWithUsernameExists_whenUserWithGivenUsernameDoesNotExist() {
        // given
        String username = "test";
        when(activeUserDao.getByUsername(username)).thenReturn(Optional.empty());

        // when
        boolean result = activeUserService.userWithUsernameExists(username);

        // then
        assertFalse(result);
    }
}
