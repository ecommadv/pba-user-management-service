package com.pba.authservice.persistance.repository.sql;

public interface PendingUserProfileSqlProvider extends SqlProvider {
    public String selectByUserId();
    public String selectByEmail();
}
