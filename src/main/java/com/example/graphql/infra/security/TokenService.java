package com.example.graphql.infra.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Service
public class TokenService {

    @Value("${jwt.secret}")
    private String secret;

    public String generateToken(String clientId, String clientSecret) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);

            String token = JWT.create()
                    .withIssuer("auth-api")
                    .withSubject(clientId)
                    .withExpiresAt(getExpirationDate())
                    .withArrayClaim("roles", new String[]{"ROLE_USER"})

                    .sign(algorithm);
            return token;
        } catch (JWTCreationException e) {
            throw new RuntimeException(e);
        }

    }

    public String validateToken(String token) {
        try {
            var verifier = JWT.require(Algorithm.HMAC256(secret)).build();
            var decodedToken = verifier.verify(token);

            if (decodedToken.getExpiresAt() == null || decodedToken.getExpiresAt().toInstant().isBefore(Instant.now())) {
                throw new JWTVerificationException("Token has expired or expiration is invalid");
            }

            return decodedToken.getSubject();
        } catch (JWTVerificationException e) {
            return "";
        }
    }

    private Instant getExpirationDate() {
        return LocalDateTime.now().plusHours(2).toInstant(ZoneOffset.of("-03:00"));
    }
}
