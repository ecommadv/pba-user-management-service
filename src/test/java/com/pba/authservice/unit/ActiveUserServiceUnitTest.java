package com.pba.authservice.unit;

import com.pba.authservice.mockgenerators.ActiveUserMockGenerator;
import com.pba.authservice.persistance.model.ActiveUser;
import com.pba.authservice.persistance.repository.ActiveUserDao;
import com.pba.authservice.service.ActiveUserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ActiveUserServiceUnitTest {
    @InjectMocks
    private ActiveUserService activeUserService;

    @Mock
    private ActiveUserDao activeUserDao;

    private ActiveUserMockGenerator activeUserMockGenerator;

    @BeforeEach
    public void setUp() {
        activeUserMockGenerator = new ActiveUserMockGenerator();
    }

    @Test
    public void testAddActiveUser() {
        // given
        ActiveUser activeUser = activeUserMockGenerator.generateMockActiveUser();
        when(activeUserDao.save(activeUser)).thenReturn(activeUser);

        // when
        ActiveUser result = activeUserService.addActiveUser(activeUser);

        // then
        Assertions.assertEquals(activeUser, result);
    }
}
