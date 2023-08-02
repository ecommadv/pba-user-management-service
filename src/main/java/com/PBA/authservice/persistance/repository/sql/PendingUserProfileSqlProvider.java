package com.pba.authservice.persistance.repository.sql;

import com.pba.authservice.persistance.repository.sql.SqlProvider;
import org.springframework.stereotype.Component;

@Component
public class PendingUserProfileSqlProvider implements SqlProvider {

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