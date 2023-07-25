package com.pba.authservice.persistance.repository;

import org.springframework.stereotype.Component;

@Component
public class UserSqlProvider {
    public String insertUser() {
        return """
                INSERT INTO \"User\"(
                    id,
                    firstname,
                    lastname,
                    email,
                    password
                ) VALUES (?, ?, ?, ?, ?);
                """;
    }

    public String selectAllUsers() {
        return """
               SELECT * FROM \"User\";
               """;
    }
}
