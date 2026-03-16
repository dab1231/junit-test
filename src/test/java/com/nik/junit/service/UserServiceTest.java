package com.nik.junit.service;

import com.nik.junit.dao.UserDao;
import com.nik.junit.dto.User;
import com.nik.junit.extension.GlobalExtension;
import com.nik.junit.extension.PostProcessingExtension;
import org.assertj.core.api.Assertions;
import org.assertj.core.api.MatcherAssert;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;

import java.time.Duration;
import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@Tag("user")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.Random.class)
@ExtendWith({
        GlobalExtension.class,
        PostProcessingExtension.class
})
class UserServiceTest {

    private static final User IVAN = new User(1, "Ivan", "123");
    private static final User PETR = new User(2, "Petr", "111");
    private UserService userService;
    private UserDao userDao;

    @BeforeAll
    void init() {
        System.out.println("Before all: " + this);
    }

    @BeforeEach
    void prepare() {
        this.userDao = Mockito.mock(UserDao.class);
        userService = new UserService(userDao);
        System.out.println("Before each: " + this);
    }

    @Test
    void shouldDeleteExistedUser() {
        userService.add(IVAN);
        Mockito.doReturn(true).when(userDao).delete(IVAN.getId());
//        Mockito.doReturn(true).when(userDao).delete(Mockito.any());

        var delete = userService.delete(IVAN.getId());

        assertThat(delete).isTrue();
    }

    @Test
    void usersEmptyIfNoUsersAdded() {
        System.out.println("Test 1: " + this);

        var users = userService.getAll();
        assertThat(users).isEmpty();
    }

    @Test
    void usersSizeIfUserAdded() {
        System.out.println("Test 2: " + this);
        // UserService userService = new UserService();
        userService.add(IVAN);
        userService.add(PETR);

        var users = userService.getAll();
        assertThat(users).hasSize(2);
    }

    @Test
    void usersConvertedToMapById() {
        userService.add(IVAN, PETR);
        Map<Integer, User> users = userService.getAllConvertedById();

        assertAll(
                () -> assertThat(users).containsKeys(IVAN.getId(), PETR.getId()),
                () -> assertThat(users).containsValues(IVAN, PETR)
        );
    }

    @AfterEach
    void deleteDataFromDatabase() {
        System.out.println("After each: " + this);
    }

    @AfterAll
    void closeConnectionPool() {
        System.out.println("After all: ");
    }

    @Nested
    @DisplayName("test user login functionality")
    @Tag("login")
    class LoginTest {

        @Test
        void checkLoginFunctionalityPerformance() {
            assertTimeout(Duration.ofMillis(200L), () -> userService.login("dummy", IVAN.getPassword()));
        }

        @Test
        void loginSuccessIfUserExists() {
            userService.add(IVAN);
            Optional<User> maybeUser = userService.login(IVAN.getUserName(), IVAN.getPassword());

            assertThat(maybeUser).isPresent();
            maybeUser.ifPresent(user -> assertThat(user).isEqualTo(IVAN));
        }

        @Test
        void throwExceptionIfUsernameOrPasswordIsNull() {
            assertAll(
                    () -> assertThrows(IllegalArgumentException.class, () -> userService.login(null, "dummy")),
                    () -> assertThrows(IllegalArgumentException.class, () -> userService.login("dummy", null))

            );
        }

        @Test
        void loginFailIfPasswordIsNotCorrect() {
            userService.add(IVAN);
            var maybeUser = userService.login(IVAN.getUserName(), "dummy");

            assertTrue(maybeUser.isEmpty());
        }

        @Test
        void loginFailIfUserDoesNotExists() {
            userService.add(IVAN);
            var maybeUser = userService.login("dummy", IVAN.getPassword());

            assertTrue(maybeUser.isEmpty());
        }
    }
}
