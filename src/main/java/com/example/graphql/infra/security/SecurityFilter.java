package com.example.graphql.infra.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class SecurityFilter extends OncePerRequestFilter {

//    private final TokenService tokenService;
    private final ClientJWTService tokenService;

    public SecurityFilter(ClientJWTService tokenService) {
        this.tokenService = tokenService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        var token = getAccessToken(request);
        if (token != null) {
            var subject = tokenService.validateToken(token);
            logger.info(subject);
            var authentication = new UsernamePasswordAuthenticationToken(subject, null, null);
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }
        /* calling next filter */
        filterChain.doFilter(request, response);

    }

    private String getAccessToken(HttpServletRequest request) {
        var authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return null;
        }
        return authHeader.substring("Bearer ".length());
    }
}
