package com.s28572.junit.dao;

import lombok.SneakyThrows;

import java.io.IOException;
import java.sql.DriverManager;

public class UserDao {

    @SneakyThrows
    public boolean delete(Integer userId) {
        try (var connection = DriverManager.getConnection("url", "username", "password")) {
            return true;
        }
    }
}
