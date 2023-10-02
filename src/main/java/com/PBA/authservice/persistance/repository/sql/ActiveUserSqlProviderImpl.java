package com.pba.authservice.persistance.repository.sql;

import org.springframework.stereotype.Component;

@Component
public class ActiveUserSqlProviderImpl implements ActiveUserSqlProvider {
    @Override
    public String insert() {
        return """
                INSERT INTO active_user (
                    id,
                    uid,
                    username,
                    password
                ) VALUES (DEFAULT, ?, ?, ?);
                """;
    }

    @Override
    public String selectById() {
        return """
                SELECT id, uid, username, password
                FROM active_user
                WHERE
                    id = ?
                """;
    }

    @Override
    public String selectAll() {
        return """
               SELECT * FROM active_user
               """;
    }

    @Override
    public String deleteById() {
        return """
                DELETE FROM active_user
                WHERE id = ?
               """;
    }

    @Override
    public String update() {
        return """
                UPDATE active_user
                SET uid = ?, username = ?, password = ?
                WHERE id = ?
               """;
    }

    @Override
    public String selectByUid() {
        return """
                SELECT id, uid, username, password
                FROM active_user
                WHERE
                    uid = ?
                """;
    }

    @Override
    public String selectByUsername() {
        return """
                SELECT id, uid, username, password
                FROM active_user
                WHERE
                    username = ?
                """;
    }

    @Override
    public String selectByUsernameAndPassword() {
        return """
                SELECT id, uid, username, password
                FROM active_user
                WHERE
                    username = ? AND password = ?
                """;
    }

    @Override
    public String selectByEmail() {
        return """
               SELECT active_user.*
               FROM active_user_profile
                    INNER JOIN active_user ON active_user_profile.user_id = active_user.id
               WHERE active_user_profile.email = ?
               """;
    }

    @Override
    public String selectByPasswordToken() {
        return """
               SELECT active_user.*
               FROM password_token
                    INNER JOIN active_user ON password_token.user_id = active_user.id
               WHERE password_token.token = ?
               """;
    }
}
