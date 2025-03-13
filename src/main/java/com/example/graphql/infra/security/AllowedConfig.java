package com.example.graphql.infra.security;

import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

import java.util.Collections;
import java.util.List;

import static java.util.Arrays.stream;

@Configuration
public class AllowedConfig {

    @Setter
    @Value("${allowed.origin}")
    private String allowedOrigin;

    @Setter
    @Value("${allowed.endpoint}")
    private String allowedEndpoint;

    private static final RequestMatcher[] EMPTY_ALLOWED_ENDPOINT = {};

    public RequestMatcher[] getAllowedEndpointMatcher() {
        return allowedEndpoint.isEmpty() ? EMPTY_ALLOWED_ENDPOINT :
                stream(allowedEndpoint.split(","))
                        .map(String::trim)
                        .map(AntPathRequestMatcher::new)
                        .toArray(RequestMatcher[]::new);
    }

    public List<String> getAllowedOrigin() {
        return allowedOrigin.isEmpty() ? Collections.emptyList() :
                stream(allowedOrigin.split(","))
                        .map(String::trim).toList();
    }

}
