package com.pba.authservice.unit;

import com.pba.authservice.mockgenerators.PendingUserMockGenerator;
import com.pba.authservice.persistance.model.PendingUser;
import com.pba.authservice.persistance.repository.PendingUserDao;
import com.pba.authservice.persistance.repository.PendingUserDaoImpl;
import com.pba.authservice.service.PendingUserService;
import com.pba.authservice.service.PendingUserServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class PendingUserServiceUnitTest implements BaseServiceUnitTest {
    @InjectMocks
    private PendingUserServiceImpl pendingUserService;

    @Mock
    private PendingUserDaoImpl pendingUserDao;

    @Test
    @Override
    public void testAdd() {
        // given
        PendingUser pendingUser = PendingUserMockGenerator.generateMockPendingUser();
        when(pendingUserDao.save(pendingUser)).thenReturn(pendingUser);

        // when
        PendingUser result = pendingUserService.addPendingUser(pendingUser);

        // then
        Assertions.assertEquals(pendingUser, result);
    }
}