package com.pba.authservice.persistance.repository.sql;

public interface GroupMemberSqlProvider extends SqlProvider {
    public String selectByUserId();
}
