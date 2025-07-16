package com.eventmagement.backend.service;

import com.eventmagement.backend.model.User;

public interface UserService {
    User registerUser(User user);

    User findByEmail(String email);
}
