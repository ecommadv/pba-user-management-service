package com.pba.authservice.unit;

import com.pba.authservice.mockgenerators.ActiveUserMockGenerator;
import com.pba.authservice.persistance.model.ActiveUser;
import com.pba.authservice.persistance.repository.ActiveUserDao;
import com.pba.authservice.persistance.repository.ActiveUserDaoImpl;
import com.pba.authservice.service.ActiveUserService;
import com.pba.authservice.service.ActiveUserServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ActiveUserServiceUnitTest implements BaseServiceUnitTest {
    @InjectMocks
    private ActiveUserServiceImpl activeUserService;

    @Mock
    private ActiveUserDaoImpl activeUserDao;

    @Test
    @Override
    public void testAdd() {
        // given
        ActiveUser activeUser = ActiveUserMockGenerator.generateMockActiveUser();
        when(activeUserDao.save(activeUser)).thenReturn(activeUser);

        // when
        ActiveUser result = activeUserService.addActiveUser(activeUser);

        // then
        Assertions.assertEquals(activeUser, result);
    }
}
