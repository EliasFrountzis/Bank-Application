package com.bank.service;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.bank.exception.BankException;
import com.bank.model.User;
import com.bank.repository.UserRepository;

import java.util.ArrayList;
import java.util.List;

public class UserServiceTest {

    private UserService userService;

    @BeforeEach
    void setup() {

        UserRepository repository =
                new UserRepository() {

                    private final List<User> users =
                            new ArrayList<>();

                    @Override
                    public User save(User user) {

                        User saved =
                                new User(
                                        users.size() + 1,
                                        user.getName(),
                                        user.getEmail(),
                                        user.getPasswordHash()
                                );

                        users.add(saved);

                        return saved;
                    }

                    @Override
                    public User findById(int id) {

                        return users.stream()
                                .filter(
                                        user ->
                                                user.getId() == id
                                )
                                .findFirst()
                                .orElse(null);
                    }

                    @Override
                    public User findByEmail(String email) {

                        return users.stream()
                                .filter(
                                        user ->
                                                user.getEmail()
                                                        .equalsIgnoreCase(email)
                                )
                                .findFirst()
                                .orElse(null);
                    }

                    @Override
                    public User findByName(String name) {

                        return users.stream()
                                .filter(
                                        user ->
                                                user.getName()
                                                        .equalsIgnoreCase(name)
                                )
                                .findFirst()
                                .orElse(null);
                    }

                    @Override
                    public List<User> findAll() {

                        return users;
                    }
                };

        userService =
                new UserService(repository);
    }

    @Test
    void shouldCreateUser() {

        User user =
                userService.createUser(
                        "Ilias",
                        "ilias@test.com",
                        "password"
                );

        assertNotNull(user);

        assertEquals(
                1,
                user.getId()
        );

        assertEquals(
                "Ilias",
                user.getName()
        );

        assertEquals(
                "ilias@test.com",
                user.getEmail()
        );

        assertEquals(
                "password",
                user.getPasswordHash()
        );
    }

    @Test
    void shouldNotCreateUserWithoutName() {

        assertThrows(
                BankException.class,
                () -> userService.createUser(
                        "",
                        "ilias@test.com",
                        "password"
                )
        );
    }

    @Test
    void shouldNotCreateUserWithoutEmail() {

        assertThrows(
                BankException.class,
                () -> userService.createUser(
                        "Ilias",
                        "",
                        "password"
                )
        );
    }

    @Test
    void shouldNotCreateUserWithoutPassword() {

        assertThrows(
                BankException.class,
                () -> userService.createUser(
                        "Ilias",
                        "ilias@test.com",
                        ""
                )
        );
    }

    @Test
    void shouldNotCreateUserWithDuplicateEmail() {

        userService.createUser(
                "Ilias",
                "ilias@test.com",
                "password"
        );

        assertThrows(
                BankException.class,
                () -> userService.createUser(
                        "Another User",
                        "ilias@test.com",
                        "password"
                )
        );
    }

    @Test
    void shouldFindUserById() {

        User created =
                userService.createUser(
                        "Ilias",
                        "ilias@test.com",
                        "password"
                );

        User found =
                userService.getUserById(
                        created.getId()
                );

        assertNotNull(found);

        assertEquals(
                created.getId(),
                found.getId()
        );

        assertEquals(
                "Ilias",
                found.getName()
        );

        assertEquals(
                "ilias@test.com",
                found.getEmail()
        );
    }

    @Test
    void shouldNotFindNonexistentUser() {

        assertThrows(
                BankException.class,
                () -> userService.getUserById(999)
        );
    }

    @Test
    void shouldFindUserByName() {

        User created =
                userService.createUser(
                        "Ilias",
                        "ilias@test.com",
                        "password"
                );

        User found =
                userService.getUserByName("Ilias");

        assertNotNull(found);

        assertEquals(
                created.getId(),
                found.getId()
        );

        assertEquals(
                "Ilias",
                found.getName()
        );

        assertEquals(
                "ilias@test.com",
                found.getEmail()
        );
    }

    @Test
    void shouldFindUserByNameIgnoringCase() {

        userService.createUser(
                "Ilias",
                "ilias@test.com",
                "password"
        );

        User found =
                userService.getUserByName("ilias");

        assertNotNull(found);

        assertEquals(
                "Ilias",
                found.getName()
        );
    }

    @Test
    void shouldNotFindUserWithUnknownName() {

        assertThrows(
                BankException.class,
                () -> userService.getUserByName("Nobody")
        );
    }
}