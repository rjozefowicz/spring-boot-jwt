package com.r6lab.spring.jwt.web;

import com.r6lab.spring.jwt.service.JwtTokenService;
import com.r6lab.spring.jwt.service.UserService;
import com.r6lab.spring.jwt.web.dto.request.CredentialsDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private UserService userService;

    @PostMapping(value = "/login", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public void login(@RequestBody @Validated CredentialsDTO credentialsDTO, HttpServletResponse response) {
        var token = userService.login(credentialsDTO.getEmail(), credentialsDTO.getPassword());
        response.addHeader(JwtTokenService.AUTHORIZATION, JwtTokenService.BEARER_PREFIX + token);
    }

    @PostMapping(value = "/register", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public void register(@RequestBody @Validated CredentialsDTO credentialsDTO) {
        userService.register(credentialsDTO.getEmail(), credentialsDTO.getPassword());
    }

}
