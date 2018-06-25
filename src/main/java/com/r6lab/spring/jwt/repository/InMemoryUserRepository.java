package com.r6lab.spring.jwt.repository;

import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CopyOnWriteArrayList;

@Component
public class InMemoryUserRepository {

    private List<User> users = new CopyOnWriteArrayList<>();

    public Optional<User> findByUsername(String email) {
        return users.stream().filter(user -> user.getUsername().equals(email)).findAny();
    }

    public void add(String email, String encryptedPassword) {
        users.add(new User(email, encryptedPassword, new ArrayList<>()));
    }

}
