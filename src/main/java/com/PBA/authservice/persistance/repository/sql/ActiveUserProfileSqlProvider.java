package com.pba.authservice.persistance.repository.sql;

public interface ActiveUserProfileSqlProvider extends SqlProvider {
    public String selectByUserId();
}
