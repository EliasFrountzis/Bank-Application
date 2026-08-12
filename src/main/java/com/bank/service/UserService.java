package com.bank.service;

import com.bank.exception.BankException;
import com.bank.model.User;
import com.bank.repository.UserRepository;

public class UserService {

    private final UserRepository repository;

    public UserService(UserRepository repository) {
        this.repository = repository;
    }

    public User createUser(
            String name,
            String email,
            String passwordHash
    ) {

        if (name == null || name.isBlank()) {
            throw new BankException(
                    "Name cannot be empty",
                    400
            );
        }

        if (email == null || email.isBlank()) {
            throw new BankException(
                    "Email cannot be empty",
                    400
            );
        }

        if (passwordHash == null || passwordHash.isBlank()) {
            throw new BankException(
                    "Password cannot be empty",
                    400
            );
        }

        User existingUser =
                repository.findByEmail(email);

        if (existingUser != null) {
            throw new BankException(
                    "Email already exists",
                    400
            );
        }

        User user =
                new User(
                        0,
                        name,
                        email,
                        passwordHash
                );

        return repository.save(user);
    }

    public User getUserById(int id) {

        User user =
                repository.findById(id);

        if (user == null) {
            throw new BankException(
                    "User not found",
                    404
            );
        }

        return user;
    }
}

