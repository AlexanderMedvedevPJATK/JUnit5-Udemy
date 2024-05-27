package com.s28572.junit.service;

import com.s28572.junit.dto.User;
import org.junit.jupiter.api.*;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

// PER_METHOD: A new test instance will be created for each test method. (Default)
// PER_CLASS: A new test instance will be created once per test class.
@TestInstance(TestInstance.Lifecycle.PER_METHOD)
public class UserServiceTest {

    private static final User ALEX = User.of(1, "Alexander", "password");
    private static final User JOHN = User.of(2, "John", "password");
    private UserService userService;

    @BeforeAll
    static void beforeAll() {
        System.out.println("Before all tests");
    }

    @BeforeEach
    void beforeEach() {
        System.out.println("Before each test: " + this);
        userService = new UserService();
    }

    @Test
    void usersEmptyIfNoUserAdded() {
        assertTrue(userService.getUsers().isEmpty(), "Expected empty list");
    }

    @Test
    void usersSizeIfUserAdded() {
        userService.addUser(ALEX);
        userService.addUser(JOHN);
        assertEquals(2, userService.getUsers().size(), "Expected list size 2");
    }

    @Test
    void loginSuccessIfUserExists() {
        userService.addUser(ALEX);
        Optional<User> user = userService.login("Alexander", "password");

        user.ifPresent(usr -> assertEquals(ALEX, usr));
    }

    @Test
    void loginFailIfPasswordIncorrect() {
        userService.addUser(ALEX);
        Optional<User> user = userService.login(ALEX.getUsername(), "wrong_password");
        assertTrue(user.isEmpty());
    }

    @Test
    void loginFailIfUserDoesNotExist() {
        Optional<User> user = userService.login("dummy", "password");
        assertTrue(user.isEmpty());
    }

    @AfterEach
    void afterEach() {
        System.out.println("After each test: " + this);
    }

    @AfterAll
    static void afterAll() {
        System.out.println("After all tests");
    }
}
