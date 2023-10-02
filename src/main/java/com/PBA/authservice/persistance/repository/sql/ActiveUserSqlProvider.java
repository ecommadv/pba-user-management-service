package com.pba.authservice.persistance.repository.sql;

public interface ActiveUserSqlProvider extends SqlProvider {
    public String selectByUid();
    public String selectByUsername();
    public String selectByUsernameAndPassword();
    public String selectByEmail();
    public String selectByPasswordToken();
}
