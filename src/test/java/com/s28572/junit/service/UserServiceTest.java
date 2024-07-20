package com.s28572.junit.service;

import com.s28572.junit.dto.User;
import com.s28572.junit.service.extension.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.*;
import org.mockito.Mockito;

import java.lang.reflect.Field;
import java.time.Duration;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

// PER_METHOD: A new test instance will be created for each test method. (Default)
// PER_CLASS: A new test instance will be created once per test class.
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.DisplayName.class)
@ExtendWith( {
        UserServiceParameterResolver.class,
        PostProcessingExtension.class,
        ConditionalExtension.class,
//        ThrowableExtension.class
//        GlobalExtension.class
})
public class UserServiceTest extends TestBase {

    private static final User ALEX = User.of(1, "Alexander", "password");
    private static final User JOHN = User.of(2, "John", "pass");

    private UserService userService;

    @BeforeAll
    void init() {
        System.out.println("Before all tests");
    }

    @BeforeEach
    void prepare(UserService userService) {
        System.out.println("Before each test: " + this);
        this.userService = userService;
    }

    @Test
    @DisplayName("Users list is empty if no user added")
    void usersEmptyIfNoUserAdded() {
        assertTrue(userService.getUsers().isEmpty(), "Expected empty list");
    }

    @Test
    @DisplayName("Users list size is 2 if 2 users added")
    void usersSizeIfUserAdded() {
        userService.add(ALEX, JOHN);
//        assertEquals(2, userService.getUsers().size(), "Expected list size 2");
        assertThat(userService.getUsers()).hasSize(2);

    }

    @Test
    @DisplayName("Users are mapped by id")
    void usersMappedById() {
        userService.add(ALEX, JOHN);

        Map<Integer, User> usersById = userService.getUsersMappedById();


        assertAll(
                () -> assertThat(usersById).containsKeys(ALEX.getId(), JOHN.getId()),
                () -> assertThat(usersById).containsValues(ALEX, JOHN)
        );
    }

    @AfterEach
    void afterEach() {
        System.out.println("After each test: " + this);
    }

    @Nested
    @Tag("login")
    @DisplayName("Login functionality")
    @Timeout(value = 200, unit = TimeUnit.MILLISECONDS)
    class LoginTest {
        @Test
        @DisplayName("Login success if user exists")
        void loginSuccessIfUserExists() {
            userService.add(ALEX);
            Optional<User> user = userService.login(ALEX.getUsername(), ALEX.getPassword());

//        assertTrue(user.isPresent());
            assertThat(user).isPresent();
//        user.ifPresent(usr -> assertEquals(ALEX, usr));
            user.ifPresent(usr -> assertThat(usr).isEqualTo(ALEX));
        }

        @Test
        @DisplayName("Login success if user exists")
        void loginThrowsExceptionIfUserOrPasswordIsNull() {
            assertAll(
                    () -> {
                        var exception = assertThrows(IllegalArgumentException.class, () -> userService.login("username", null));
                        assertThat(exception).hasMessage("Username or password is null");
                    },
                    () -> assertThrows(IllegalArgumentException.class, () -> userService.login(null, "password")),
                    () -> assertThrows(IllegalArgumentException.class, () -> userService.login(null, null))
            );
        }

        @Test
        @DisplayName("Login fail if password incorrect")
        void loginFailIfPasswordIncorrect() {
            userService.add(ALEX);
            Optional<User> user = userService.login(ALEX.getUsername(), "wrong_password");
            assertTrue(user.isEmpty());
        }

//        @Test
        @DisplayName("Login fail if user does not exist")
//        @Disabled("flaky")
        @RepeatedTest(value = 5, name = RepeatedTest.LONG_DISPLAY_NAME)
        void loginFailIfUserDoesNotExist(RepetitionInfo repetitionInfo) {
            Optional<User> user = userService.login("dummy", "password");
            assertTrue(user.isEmpty());
        }

        @Test
        void testLoginFunctionalityPerformance() {
            // assertTimeoutPreemptively() executes the task in another thread
            var result = assertTimeout(Duration.ofMillis(200L), () -> {
//                Thread.sleep(300L);
                return userService.login(ALEX.getUsername(), ALEX.getPassword());
            });
        }


        // name parameter has a lot of default placeholders
        @ParameterizedTest(name = ParameterizedTest.DEFAULT_DISPLAY_NAME)
//        @NullAndEmptySource  // Only for tests with one argument
//        @NullSource  // Only for tests with one argument
//        @EmptySource  // Only for tests with one argument
//        @CsvFileSource  // Takes data from .csv file, cannot use complex datatypes
//        @CsvSource  // Data is passed in the annotation parameter (no complex types)
        @MethodSource("com.s28572.junit.service.UserServiceTest#getArgumentsForLoginTest")
        void loginParametrizedTest(String username, String password, Optional<User> supposedUser) {
            userService.add(ALEX, JOHN);
            var user = userService.login(username, password);

            assertThat(user).isEqualTo(supposedUser);
        }
    }

    static Stream<Arguments> getArgumentsForLoginTest() {
        return Stream.of(
                Arguments.of(ALEX.getUsername(), ALEX.getPassword(), Optional.of(ALEX)),
                Arguments.of(JOHN.getUsername(), JOHN.getPassword(), Optional.of(JOHN)),
                Arguments.of(ALEX.getUsername(), "wrong", Optional.empty()),
                Arguments.of("wrong", JOHN.getPassword(), Optional.empty())
        );
    }

    @AfterAll
    static void afterAll() {
        System.out.println("After all tests");
    }
}
