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
                    email,
                    created_at,
                    validation_code
                ) VALUES (DEFAULT, ?, ?, ?, ?, ?, ?);
                """;
    }

    @Override
    public String selectById() {
        return """
                SELECT id, uid, username, password, email, created_at, validation_code
                FROM pending_user
                WHERE
                    uid = ?
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
                WHERE uid = ?
               """;
    }

    @Override
    public String update() {
        return """
                UPDATE pending_user
                SET uid = ?, username = ?, password = ?, email = ?, created_at = ?, validation_code = ?
                WHERE uid = ?
               """;
    }
}
