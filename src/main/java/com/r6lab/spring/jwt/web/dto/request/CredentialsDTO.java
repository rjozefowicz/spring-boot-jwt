package com.r6lab.spring.jwt.web.dto.request;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

public class CredentialsDTO {

    @NotNull
    @Email
    private String email;

    @NotNull
    private String password;

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }
}
