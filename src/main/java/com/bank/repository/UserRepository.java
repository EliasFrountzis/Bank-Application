package com.bank.repository;

import com.bank.model.User;

import java.util.List;

public interface UserRepository {

    User save(User user);

    User findById(int id);

    User findByEmail(String email);

    List<User> findAll();
}

