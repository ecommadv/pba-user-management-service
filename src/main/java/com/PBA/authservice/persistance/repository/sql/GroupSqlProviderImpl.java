package com.pba.authservice.persistance.repository.sql;

import org.springframework.stereotype.Component;

@Component
public class GroupSqlProviderImpl implements GroupSqlProvider {
    @Override
    public String insert() {
        return """
                INSERT INTO group_type (
                    id,
                    name,
                    uid
                ) VALUES (DEFAULT, ?, ?);
                """;
    }

    @Override
    public String selectById() {
        return """
                SELECT id, name, uid
                FROM group_type
                WHERE
                    id = ?
                """;
    }

    @Override
    public String selectAll() {
        return """
               SELECT * FROM group_type
               """;
    }

    @Override
    public String deleteById() {
        return """
                DELETE FROM group_type
                WHERE id = ?
               """;
    }

    @Override
    public String update() {
        return """
                UPDATE group_type
                SET name = ?, uid = ?
                WHERE id = ?
               """;
    }

    @Override
    public String selectByName() {
        return """
                SELECT id, name, uid
                FROM group_type
                WHERE
                    name = ?
                """;
    }
}
