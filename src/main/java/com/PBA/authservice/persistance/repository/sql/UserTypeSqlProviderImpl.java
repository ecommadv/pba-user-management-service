package com.pba.authservice.persistance.repository.sql;

import org.springframework.stereotype.Component;

@Component
public class UserTypeSqlProviderImpl implements UserTypeSqlProvider {
    @Override
    public String insert() {
        return """
                INSERT INTO user_type (
                    id,
                    name
                ) VALUES (DEFAULT, ?);
                """;
    }

    @Override
    public String selectById() {
        return """
                SELECT id, name
                FROM user_type
                WHERE
                    id = ?
                """;
    }

    @Override
    public String selectAll() {
        return """
               SELECT * FROM user_type
               """;
    }

    @Override
    public String deleteById() {
        return """
                DELETE FROM user_type
                WHERE id = ?
               """;
    }

    @Override
    public String update() {
        return """
                UPDATE user_type
                SET name = ?
                WHERE id = ?
               """;
    }

    @Override
    public String selectByName() {
        return """
                SELECT id, name
                FROM user_type
                WHERE
                    name = ?
                """;

    }
}
