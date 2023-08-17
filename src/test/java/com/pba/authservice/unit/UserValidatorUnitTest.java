package com.pba.authservice.unit;

import com.pba.authservice.controller.request.UserCreateRequest;
import com.pba.authservice.exceptions.AuthValidationException;
import com.pba.authservice.mockgenerators.PendingUserMockGenerator;
import com.pba.authservice.persistance.model.PendingUser;
import com.pba.authservice.validator.UserValidator;
import com.pba.authservice.validator.UserValidatorImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.LinkedHashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;


public class UserValidatorUnitTest {
    @Autowired
    private UserValidator userValidator;

    @BeforeEach
    public void setUp() {
        this.userValidator = new UserValidatorImpl();
    }

    @Test
    public void testValidateInvalidUserCreateRequest() {
        UserCreateRequest userCreateRequest = PendingUserMockGenerator.generateInvalidUserCreateRequest();
        Map<String, String> errorMap = new LinkedHashMap<>();
        errorMap.put("username", "Username length must be between 4 and 20 characters!");
        errorMap.put("password", "Password must contain at least one special character: !@#$%^&*?");
        errorMap.put("passwordLength", "Password length must be between 7 and 20 characters!");
        errorMap.put("email", "Email address is invalid!");

        assertThatExceptionOfType(AuthValidationException.class)
                .isThrownBy(() -> userValidator.validateUserCreateRequest(userCreateRequest))
                .matches(ex -> ex.getErrorMap().equals(errorMap));
    }

    @Test
    public void testValidateInvalidPendingUser() {
        PendingUser pendingUser = PendingUserMockGenerator.generateMockPendingUser();
        LocalDateTime expiredTime = LocalDateTime.now().minus(25, ChronoUnit.HOURS);
        pendingUser.setCreatedAt(expiredTime);

        Map<String, String> errorMap = new LinkedHashMap<>();
        errorMap.put("createdAt", "The validation code has been expired for over 24 hours since its creation!");

        assertThatExceptionOfType(AuthValidationException.class)
                .isThrownBy(() -> userValidator.validatePendingUser(pendingUser))
                .matches(ex -> ex.getErrorMap().equals(errorMap));
    }
}
