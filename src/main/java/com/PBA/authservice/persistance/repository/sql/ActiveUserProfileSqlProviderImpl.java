package com.pba.authservice.persistance.repository.sql;

import org.springframework.stereotype.Component;

@Component
public class ActiveUserProfileSqlProviderImpl implements ActiveUserProfileSqlProvider {
    @Override
    public String insert() {
        return """
                INSERT INTO active_user_profile (
                    id,
                    firstname,
                    lastname,
                    email,
                    country,
                    age,
                    user_id
                ) VALUES (DEFAULT, ?, ?, ?, ?, ?, ?);
                """;
    }

    @Override
    public String selectById() {
        return """
                SELECT id, firstname, lastname, email, country, age, user_id
                FROM active_user_profile
                WHERE
                    id = ?
                """;
    }

    @Override
    public String selectAll() {
        return """
               SELECT * FROM active_user_profile
               """;
    }

    @Override
    public String deleteById() {
        return """
                DELETE FROM active_user_profile
                WHERE id = ?
               """;
    }

    @Override
    public String update() {
        return """
                UPDATE active_user_profile
                SET firstname = ?, lastname = ?, email = ?, country = ?, age = ?, user_id = ?
                WHERE id = ?
               """;
    }

    @Override
    public String selectByUserId() {
        return """
                SELECT id, firstname, lastname, email, country, age, user_id
                FROM active_user_profile
                WHERE
                    user_id = ?
                """;
    }
}
