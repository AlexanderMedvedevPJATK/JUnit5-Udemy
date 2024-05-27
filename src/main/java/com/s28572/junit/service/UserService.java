package com.s28572.junit.service;

import com.s28572.junit.dto.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UserService {
    
    private List<User> users = new ArrayList<>();

    public List<User> getUsers() {
        return users;
    }

    public void addUser(User user) {
        users.add(user);
    }

    public Optional<User> login(String username, String password) {
        return users.stream()
                .filter(user -> user.getUsername().equals(username) && user.getPassword().equals(password))
                .findFirst();

    }
}
