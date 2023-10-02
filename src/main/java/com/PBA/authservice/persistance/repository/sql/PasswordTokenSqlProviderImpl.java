package com.pba.authservice.persistance.repository.sql;

import org.springframework.stereotype.Component;

@Component
public class PasswordTokenSqlProviderImpl implements PasswordTokenSqlProvider {
    @Override
    public String insert() {
        return """
                INSERT INTO password_token (
                    id,
                    user_id,
                    token
                ) VALUES (DEFAULT, ?, ?);
                """;
    }

    @Override
    public String selectById() {
        return """
                SELECT *
                FROM password_token
                WHERE
                    id = ?
                """;
    }

    @Override
    public String selectAll() {
        return """
               SELECT * FROM password_token
               """;
    }

    @Override
    public String deleteById() {
        return """
                DELETE FROM password_token
                WHERE id = ?
               """;
    }

    @Override
    public String update() {
        return """
                UPDATE password_token
                SET user_id = ?, token = ?
                WHERE id = ?
               """;
    }

    @Override
    public String selectByToken() {
        return """
               SELECT *
               FROM password_token
               WHERE token = ? 
               """;
    }
}
