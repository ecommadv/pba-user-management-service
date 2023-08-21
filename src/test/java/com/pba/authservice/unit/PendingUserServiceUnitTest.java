package com.pba.authservice.unit;

import com.pba.authservice.mockgenerators.PendingUserMockGenerator;
import com.pba.authservice.persistance.model.PendingUser;
import com.pba.authservice.persistance.model.PendingUserProfile;
import com.pba.authservice.persistance.repository.PendingUserDaoImpl;
import com.pba.authservice.persistance.repository.PendingUserProfileDao;
import com.pba.authservice.service.PendingUserServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class PendingUserServiceUnitTest {
    @InjectMocks
    private PendingUserServiceImpl pendingUserService;

    @Mock
    private PendingUserDaoImpl pendingUserDao;

    @Mock
    private PendingUserProfileDao pendingUserProfileDao;

    @Test
    public void testAdd() {
        // given
        PendingUser pendingUser = PendingUserMockGenerator.generateMockPendingUser();
        when(pendingUserDao.save(pendingUser)).thenReturn(pendingUser);

        // when
        PendingUser result = pendingUserService.addPendingUser(pendingUser);

        // then
        assertEquals(pendingUser, result);
    }

    @Test
    public void testUserWithEmailExists_whenUserWithGivenEmailExists() {
        // given
        String email = "test@gmail.com";
        when(pendingUserProfileDao.getByEmail(email)).thenReturn(Optional.of(new PendingUserProfile()));

        // when
        boolean result = pendingUserService.userWithEmailExists(email);

        // then
        assertTrue(result);
    }

    @Test
    public void testUserWithEmailExists_whenUserWithGivenEmailDoesNotExist() {
        // given
        String email = "test@gmail.com";
        when(pendingUserProfileDao.getByEmail(email)).thenReturn(Optional.empty());

        // when
        boolean result = pendingUserService.userWithEmailExists(email);

        // then
        assertFalse(result);
    }

    @Test
    public void testUserWithUsernameExists_whenUserWithGivenUsernameExists() {
        // given
        String username = "test";
        when(pendingUserDao.getByUsername(username)).thenReturn(Optional.of(new PendingUser()));

        // when
        boolean result = pendingUserService.userWithUsernameExists(username);

        // then
        assertTrue(result);
    }

    @Test
    public void testUserWithUsernameExists_whenUserWithGivenUsernameDoesNotExist() {
        // given
        String username = "test";
        when(pendingUserDao.getByUsername(username)).thenReturn(Optional.empty());

        // when
        boolean result = pendingUserService.userWithUsernameExists(username);

        // then
        assertFalse(result);
    }
}