package com.pba.authservice.persistance.repository.sql;

import org.springframework.stereotype.Component;

@Component
public class GroupMemberSqlProviderImpl implements GroupMemberSqlProvider {
    @Override
    public String insert() {
        return """
                INSERT INTO group_member (
                    id,
                    user_id,
                    user_type_id,
                    group_id
                ) VALUES (DEFAULT, ?, ?, ?);
                """;
    }

    @Override
    public String selectById() {
        return """
                SELECT id, user_id, user_type_id, group_id
                FROM group_member
                WHERE
                    id = ?
                """;
    }

    @Override
    public String selectAll() {
        return """
               SELECT * FROM group_member
               """;
    }

    @Override
    public String deleteById() {
        return """
                DELETE FROM group_member
                WHERE id = ?
               """;
    }

    @Override
    public String update() {
        return """
                UPDATE group_member
                SET user_id = ?, user_type_id = ?, group_id = ?
                WHERE id = ?
               """;
    }

    @Override
    public String selectByUserIdAndGroupId() {
        return """
                SELECT id, user_id, user_type_id, group_id
                FROM group_member
                WHERE
                    user_id = ? AND group_id = ?
                """;
    }
}
