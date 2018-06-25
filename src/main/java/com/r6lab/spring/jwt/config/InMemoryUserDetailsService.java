package com.r6lab.spring.jwt.config;

import com.r6lab.spring.jwt.repository.InMemoryUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class InMemoryUserDetailsService implements UserDetailsService {

    @Autowired
    private InMemoryUserRepository inMemoryUserRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        var user = inMemoryUserRepository.findByUsername(username);

        if (!user.isPresent()) {
            throw new UsernameNotFoundException("User '" + username + "' not found");
        }

        return User
                .withUsername(username)
                .roles()
                .password(user.get().getPassword())
                .accountExpired(false)
                .accountLocked(false)
                .credentialsExpired(false)
                .disabled(false)
                .build();
    }

}
