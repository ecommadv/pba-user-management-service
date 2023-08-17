package com.pba.authservice.validator;

import com.pba.authservice.controller.request.UserCreateRequest;
import com.pba.authservice.exceptions.AuthValidationException;
import com.pba.authservice.persistance.model.PendingUser;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Component
public class UserValidatorImpl implements UserValidator {
    @Override
    public void validateUserCreateRequest(UserCreateRequest userCreateRequest) {
        Map<String, String> errorMap = new LinkedHashMap<>();
        errorMap.put("username", usernameMessage(userCreateRequest.getUsername()));
        errorMap.put("password", passwordSpecialCharMessage(userCreateRequest.getPassword()));
        errorMap.put("passwordLength", passwordValidLengthMessage(userCreateRequest.getPassword()));
        errorMap.put("firstName", firstNameMessage(userCreateRequest.getFirstName()));
        errorMap.put("lastName", lastNameMessage(userCreateRequest.getLastName()));
        errorMap.put("email", emailMessage(userCreateRequest.getEmail()));

        Map<String, String> filteredErrorMap = this.filterNonBlankEntryValues(errorMap);

        if (!filteredErrorMap.isEmpty()) {
            throw new AuthValidationException(filteredErrorMap);
        }
    }

    @Override
    public void validatePendingUser(PendingUser pendingUser) {
        Map<String, String> errorMap = new LinkedHashMap<>();
        errorMap.put("createdAt", createdAtMessage(pendingUser.getCreatedAt()));

        Map<String, String> filteredErrorMap = this.filterNonBlankEntryValues(errorMap);

        if (!filteredErrorMap.isEmpty()) {
            throw new AuthValidationException(filteredErrorMap);
        }
    }

    private String createdAtMessage(LocalDateTime createdAt) {
        return createdAt.until(LocalDateTime.now(), ChronoUnit.HOURS) > 24
                ? "The validation code has been expired for over 24 hours since its creation!"
                : "";
    }

    private String usernameMessage(String username) {
        return username.length() < 4 || username.length() > 20
                ? "Username length must be between 4 and 20 characters!"
                : "";
    }

    private String passwordSpecialCharMessage(String password) {
        String specialChars = "!@#$%^&*?";
        String containsSpecialCharRegex = String.format(".*[%s].*", specialChars);
        boolean hasSpecialChar = password.matches(containsSpecialCharRegex);

        return !hasSpecialChar
                ? "Password must contain at least one special character: !@#$%^&*?"
                : "";
    }

    private String passwordValidLengthMessage(String password) {
        return password.length() < 7 || password.length() > 20
                ? "Password length must be between 7 and 20 characters!"
                : "";
    }

    private String firstNameMessage(String firstName) {
        return firstName.length() > 50
                ? "First name length can be at most 50 characters!"
                : "";
    }

    private String lastNameMessage(String lastName) {
        return lastName.length() > 50
                ? "Last name length can be at most 50 characters!"
                : "";
    }

    private String emailMessage(String email) {
        String regexPattern = "^(.+)@(\\S+\\.\\S+)$";
        return !emailPatternMatches(email, regexPattern)
                ? "Email address is invalid!"
                : "";
    }

    private boolean emailPatternMatches(String emailAddress, String regexPattern) {
        return Pattern.compile(regexPattern)
                .matcher(emailAddress)
                .matches();
    }

    private Map<String, String> filterNonBlankEntryValues(Map<String, String> errorMap) {
        return errorMap.entrySet()
                .stream()
                .filter(entry -> !entry.getValue().isBlank())
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (x, y) -> y, LinkedHashMap::new));
    }
}
