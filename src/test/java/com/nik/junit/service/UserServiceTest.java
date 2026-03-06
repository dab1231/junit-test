package com.nik.junit.service;

import com.nik.junit.dto.User;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class UserServiceTest {

    private UserService userService;

    @BeforeAll
    static void init() {
        System.out.println("Before all: " );
    }

    @BeforeEach
    void prepare() {
        userService = new UserService();
        System.out.println("Before each: " + this);
    }

    @Test
    void usersEmptyIfNoUsersAdded() {
        System.out.println("Test 1: " + this);

        var users = userService.getAll();
        assertTrue(users.isEmpty(), () -> "User list should be empty");
    }

    @Test
    void usersSizeIfUserAdded() {
        System.out.println("Test 2: " + this);
        UserService userService = new UserService();
        userService.add(new User());
        userService.add(new User());
        var users = userService.getAll();
        assertEquals(2, users.size());
    }

    @AfterEach
    void deleteDataFromDatabase() {
        System.out.println("After each: " + this);
    }

    @AfterAll
    static void closeConnectionPool() {
        System.out.println("After all: ");
    }
}
