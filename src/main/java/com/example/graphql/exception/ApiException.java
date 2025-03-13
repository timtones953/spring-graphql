package com.example.graphql.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ApiException extends RuntimeException {
    private String code;
    private String message;
    private String details;
    private HttpStatus httpStatus;
}
