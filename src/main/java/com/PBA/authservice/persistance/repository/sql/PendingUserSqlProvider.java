package com.pba.authservice.persistance.repository.sql;

public interface PendingUserSqlProvider extends SqlProvider {
    public String selectByValidationCode();
    public String selectByUsername();
}
