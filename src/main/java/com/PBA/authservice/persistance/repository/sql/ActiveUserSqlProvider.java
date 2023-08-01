package com.pba.authservice.persistance.repository.sql;

import org.springframework.stereotype.Component;

@Component
public class ActiveUserSqlProvider implements SqlProvider {
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
                    uid = ?
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
                WHERE uid = ?
               """;
    }

    @Override
    public String update() {
        return """
                UPDATE active_user
                SET uid = ?, username = ?, password = ?
                WHERE uid = ?
               """;
    }
}
