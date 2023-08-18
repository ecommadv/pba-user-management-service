package com.pba.authservice.persistance.repository.sql;

public interface ActiveUserSqlProvider extends SqlProvider {
    public String selectByUid();
}
