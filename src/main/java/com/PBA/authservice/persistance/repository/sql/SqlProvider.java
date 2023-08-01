package com.pba.authservice.persistance.repository.sql;

public interface SqlProvider {
    public String insert();
    public String selectById();
    public String selectAll();
    public String deleteById();
    public String update();
}
