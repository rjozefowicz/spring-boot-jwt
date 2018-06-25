package com.r6lab.spring.jwt.config.jwt;

import com.r6lab.spring.jwt.service.JwtTokenService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

public class JwtTokenFilter extends GenericFilterBean {

    private JwtTokenService jwtTokenService;

    public JwtTokenFilter(JwtTokenService jwtTokenService) {
        this.jwtTokenService = jwtTokenService;
    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain filterChain)
            throws IOException, ServletException {

        var token = jwtTokenService.extractTokenFromRequest((HttpServletRequest) req);

        handleAuthentication(token);

        filterChain.doFilter(req, res);
    }

    private void handleAuthentication(String token) {
        try {
            if (token != null && jwtTokenService.validateToken(token)) {
                var auth = jwtTokenService.getAuthentication(token);
                SecurityContextHolder.getContext().setAuthentication(auth);
            }
        } catch (JwtTokenVerificationException ex) {
            System.err.println("Exception while verifying JWT token");
            ex.printStackTrace();
        } catch (UsernameNotFoundException ex) {
            System.err.println("No user found with email " + jwtTokenService.getUsername(token));
            ex.printStackTrace();
        }
    }

}