package com.pba.authservice.persistance.repository.sql;

public interface PasswordTokenSqlProvider extends SqlProvider {
    public String selectByToken();
}
