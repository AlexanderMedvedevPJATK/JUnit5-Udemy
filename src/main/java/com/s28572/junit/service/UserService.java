package com.s28572.junit.service;

import com.s28572.junit.dao.UserDao;
import com.s28572.junit.dto.User;
import lombok.Getter;

import java.util.*;

@Getter
public class UserService {
    
    private final List<User> users = new ArrayList<>();
    private final UserDao userDao;

    public UserService(UserDao userDao) {
        this.userDao = userDao;
    }

    public boolean delete(Integer userId) {
        return userDao.delete(userId);
    }

    public void add(User... users) {
        this.users.addAll(Arrays.asList(users));
    }

    public Optional<User> login(String username, String password) {
        if (username == null || password == null) {
            throw new IllegalArgumentException("Username or password is null");
        }

        return users.stream()
                .filter(user -> user.getUsername().equals(username) && user.getPassword().equals(password))
                .findFirst();

    }

    public Map<Integer, User> getUsersMappedById() {
        return users.stream()
                .collect(HashMap::new, (map, user) -> map.put(user.getId(), user), HashMap::putAll);
    }
}
