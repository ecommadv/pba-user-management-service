package com.pba.authservice.persistance.repository.sql;

import org.springframework.stereotype.Component;

@Component
public class PendingUserSqlProviderImpl implements PendingUserSqlProvider {

    @Override
    public String insert() {
        return """
                INSERT INTO pending_user (
                    id,
                    uid,
                    username,
                    password,
                    created_at,
                    validation_code
                ) VALUES (DEFAULT, ?, ?, ?, ?, ?);
                """;
    }

    @Override
    public String selectById() {
        return """
                SELECT id, uid, username, password, created_at, validation_code
                FROM pending_user
                WHERE
                    id = ?
                """;
    }

    @Override
    public String selectAll() {
        return """
               SELECT * FROM pending_user
               """;
    }

    @Override
    public String deleteById() {
        return """
                DELETE FROM pending_user
                WHERE id = ?
               """;
    }

    @Override
    public String update() {
        return """
                UPDATE pending_user
                SET uid = ?, username = ?, password = ?, created_at = ?, validation_code = ?
                WHERE id = ?
               """;
    }

    @Override
    public String selectByValidationCode() {
        return """
                SELECT id, uid, username, password, created_at, validation_code
                FROM pending_user
                WHERE
                    validation_code = ?
                """;
    }

    @Override
    public String selectByUsername() {
        return """
                SELECT id, uid, username, password, created_at, validation_code
                FROM pending_user
                WHERE
                    username = ?
                """;
    }
}
