package com.pba.authservice.persistance.repository.sql;

public interface GroupSqlProvider extends SqlProvider {
    public String selectByName();
}
