package com.example.graphql.controller;

import com.example.graphql.infra.security.ClientJWTService;
import com.example.graphql.infra.security.TokenService;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Log4j2
@RestController
public class OpenController extends BaseController {

    private final TokenService tokenService;
    private final ClientJWTService clientJWTService;

    public OpenController(TokenService tokenService, ClientJWTService clientJWTService) {
        this.tokenService = tokenService;
        this.clientJWTService = clientJWTService;
    }

    @GetMapping("get-token")
    public ResponseEntity<String> getToken() throws Exception {
        String token = tokenService.generateToken("123", "345");
        String token512 = clientJWTService.generateJsonWebToken();

        log.info("JWT: {}", token512);
        clientJWTService.validateToken(token512);

        log.info("Token: {}", token);
        return ResponseEntity.ok(token512);
    }


    @PreAuthorize("hasRole('USER')")
    @GetMapping("verify-token")
    public ResponseEntity<String> verifyToken() {
        return ResponseEntity.ok("OK");
    }
}
