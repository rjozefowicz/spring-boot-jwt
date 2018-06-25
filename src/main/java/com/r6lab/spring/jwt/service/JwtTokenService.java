package com.r6lab.spring.jwt.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.r6lab.spring.jwt.config.InMemoryUserDetailsService;
import com.r6lab.spring.jwt.config.jwt.JwtTokenVerificationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

@Component
public class JwtTokenService {

    public static final String SECRET_JWT = "SECRET_JWT"; // this should be extracted to external configuration
    public static final String AUTHORIZATION = "Authorization";
    public static final String BEARER_PREFIX = "Bearer ";

    public static final Algorithm ALGORITHM = Algorithm.HMAC256(SECRET_JWT);

    @Autowired
    private InMemoryUserDetailsService inMemoryUserDetailsService;

    public String createToken(String username) {
        var expirationDate = Date
                .from(LocalDateTime.now().plusMinutes(10).atZone(ZoneId.systemDefault()).toInstant());
        return JWT.create().withSubject(username).withExpiresAt(expirationDate).sign(ALGORITHM);
    }

    public Authentication getAuthentication(String token) {
        var userDetails = inMemoryUserDetailsService.loadUserByUsername(getUsername(token));
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

    public String getUsername(String token) {
        return JWT.decode(token).getSubject();
    }

    public String extractTokenFromRequest(HttpServletRequest req) {
        var bearerToken = req.getHeader(AUTHORIZATION);
        if (bearerToken != null && bearerToken.startsWith(BEARER_PREFIX)) {
            return bearerToken.substring(7);
        }
        return null;
    }

    public boolean validateToken(String token) throws JwtTokenVerificationException {
        try {
            JWT.require(ALGORITHM).build().verify(token);
            return true;
        } catch (Exception e) {
            throw new JwtTokenVerificationException(e);
        }
    }

}