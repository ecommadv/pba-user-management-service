package com.pba.authservice.persistance.repository.sql;

public interface UserTypeSqlProvider extends SqlProvider {
    public String selectByName();
}
