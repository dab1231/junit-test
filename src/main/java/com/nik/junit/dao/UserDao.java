package com.nik.junit.dao;

import lombok.SneakyThrows;

import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class UserDao {



    @SneakyThrows
    public boolean delete(Integer id) {
        DriverManager.getConnection("url", "username", "password");
        return true;
    }
}
