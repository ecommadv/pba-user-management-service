package com.pba.authservice.integration;

public interface BaseDaoIntegrationTest {
    public void testSave();
    public void testGetAll();
    public void testGetPresentById();
    public void testGetAbsentById();
    public void testDeletePresentById();
    public void testDeleteAbsentById();
    public void testUpdatePresent();
    public void testUpdateAbsent();
}
