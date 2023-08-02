package com.pba.authservice.unit;

import com.pba.authservice.mockgenerators.ActiveUserMockGenerator;
import com.pba.authservice.mockgenerators.PendingUserMockGenerator;
import com.pba.authservice.persistance.model.ActiveUser;
import com.pba.authservice.persistance.model.PendingUser;
import com.pba.authservice.persistance.repository.ActiveUserDao;
import com.pba.authservice.persistance.repository.PendingUserDao;
import com.pba.authservice.service.ActiveUserService;
import com.pba.authservice.service.PendingUserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class PendingUserServiceUnitTest {
    @InjectMocks
    private PendingUserService pendingUserService;

    @Mock
    private PendingUserDao pendingUserDao;

    private PendingUserMockGenerator pendingUserMockGenerator;

    @BeforeEach
    public void setUp() {
        pendingUserMockGenerator = new PendingUserMockGenerator();
    }

    @Test
    public void testAddPendingUser() {
        // given
        PendingUser pendingUser = pendingUserMockGenerator.generateMockPendingUser();
        when(pendingUserDao.save(pendingUser)).thenReturn(pendingUser);

        // when
        PendingUser result = pendingUserService.addPendingUser(pendingUser);

        // then
        Assertions.assertEquals(pendingUser, result);
    }
}