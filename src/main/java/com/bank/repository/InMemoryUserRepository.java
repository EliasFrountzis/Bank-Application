package com.bank.repository;

import com.bank.model.User;

import java.util.ArrayList;
import java.util.List;

public class InMemoryUserRepository implements UserRepository {

    private final List<User> users = new ArrayList<>();

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
                .filter(user -> user.getId() == id)
                .findFirst()
                .orElse(null);
    }

    @Override
    public User findByEmail(String email) {

        return users.stream()
                .filter(user ->
                        user.getEmail().equalsIgnoreCase(email)
                )
                .findFirst()
                .orElse(null);
    }

    @Override
    public User findByName(String name) {

        return users.stream()
                .filter(user ->
                        user.getName().equalsIgnoreCase(name)
                )
                .findFirst()
                .orElse(null);
    }

    @Override
    public List<User> findAll() {

        return new ArrayList<>(users);
    }
}