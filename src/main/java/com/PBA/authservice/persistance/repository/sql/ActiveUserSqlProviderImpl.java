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
}
