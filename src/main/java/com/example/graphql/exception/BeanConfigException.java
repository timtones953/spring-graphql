package com.example.graphql.exception;

import org.springframework.http.HttpStatus;

import java.io.Serial;

public class BeanConfigException extends ApiException {

    @Serial
    private static final long serialVersionUID = 1L;
    private static final String ERROR_MESSAGE = "BEAN_CONFIG_ERROR";

    public BeanConfigException(String message) {
        super("ERROR_MESSAGE", message, "detail", HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
