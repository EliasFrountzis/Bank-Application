package com.bank.dto.response;

import com.bank.model.User;

public class UserResponse {

    public int id;
    public String name;
    public String email;

    public UserResponse(User user) {
        this.id = user.getId();
        this.name = user.getName();
        this.email = user.getEmail();
    }
}