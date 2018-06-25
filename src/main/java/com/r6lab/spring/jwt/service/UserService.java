package com.r6lab.spring.jwt.service;

import com.r6lab.spring.jwt.repository.InMemoryUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private InMemoryUserRepository inMemoryUserRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtTokenService jwtTokenService;

    public String login(String email, String password) {
        if (inMemoryUserRepository.findByUsername(email).isPresent()) {
            return jwtTokenService.createToken(email);
        } else {
            throw new UsernameNotFoundException(email);
        }
    }

    public void register(String email, String password) {
        String encodedPassword = passwordEncoder.encode(password);
        inMemoryUserRepository.add(email, encodedPassword);
    }

}
