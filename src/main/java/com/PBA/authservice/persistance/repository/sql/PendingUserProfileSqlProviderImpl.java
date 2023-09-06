package com.pba.authservice.persistance.repository.sql;

import org.springframework.stereotype.Component;

@Component
public class PendingUserProfileSqlProviderImpl implements PendingUserProfileSqlProvider {

    @Override
    public String insert() {
        return """
                INSERT INTO pending_user_profile (
                    id,
                    firstname,
                    lastname,
                    email,
                    user_id
                ) VALUES (DEFAULT, ?, ?, ?, ?);
                """;
    }

    @Override
    public String selectById() {
        return """
                SELECT id, firstname, lastname, email, user_id
                FROM pending_user_profile
                WHERE
                    id = ?
                """;
    }

    @Override
    public String selectAll() {
        return """
               SELECT * FROM pending_user_profile
               """;
    }

    @Override
    public String deleteById() {
        return """
                DELETE FROM pending_user_profile
                WHERE id = ?
               """;
    }

    @Override
    public String update() {
        return """
                UPDATE pending_user_profile
                SET firstname = ?, lastname = ?, email = ?, user_id = ?
                WHERE id = ?
               """;
    }

    @Override
    public String selectByUserId() {
        return """
                SELECT id, firstname, lastname, email, user_id
                FROM pending_user_profile
                WHERE
                    user_id = ?
                """;
    }

    @Override
    public String selectByEmail() {
        return """
                SELECT id, firstname, lastname, email, user_id
                FROM pending_user_profile
                WHERE
                    email = ?
                """;
    }
}