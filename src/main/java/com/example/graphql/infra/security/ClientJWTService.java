package com.example.graphql.infra.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.example.graphql.exception.ApiException;
import lombok.extern.log4j.Log4j2;
import org.bouncycastle.util.io.pem.PemReader;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.nio.charset.StandardCharsets;
import java.security.KeyFactory;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.stream.Collectors;

@Log4j2
@Service
public class ClientJWTService {

    private static String loadResourceFile(String fileName) throws Exception {
        // Get the file from the resources folder
        InputStream inputStream = ClientJWTService.class.getClassLoader().getResourceAsStream(fileName);
        if (inputStream == null) {
            throw new IllegalArgumentException("File not found...");
        }
        // Read the file contents into a String
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {
            return reader.lines().collect(Collectors.joining("\n"));
        }
    }

    /**
     * Build the JWT signing algorithm using RSA keys.
     *
     * @return Algorithm - RSA512 signing algorithm
     * @throws Exception if any key parsing issues occur
     */
    private Algorithm buildJwtAlgorithm() throws Exception {
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");

        String publicKeyPem = loadResourceFile("public.pem");
        String privateKeyPem = loadResourceFile("private.pem");

        // Build RSA Public Key
        byte[] publicKeyBytes = new PemReader(new StringReader(publicKeyPem)).readPemObject().getContent();
        RSAPublicKey publicKey = (RSAPublicKey) keyFactory.generatePublic(new X509EncodedKeySpec(publicKeyBytes));

        // Build RSA Private Key
        byte[] privateKeyBytes = new PemReader(new StringReader(privateKeyPem)).readPemObject().getContent();
        RSAPrivateKey privateKey = (RSAPrivateKey) keyFactory.generatePrivate(new PKCS8EncodedKeySpec(privateKeyBytes));

        // Build and return the RSA512 algorithm
        return Algorithm.RSA512(publicKey, privateKey);
    }

    /**
     * Generate a signed JSON Web Token (JWT).
     *
     * @return Generated JWT as a String
     * @throws Exception if any signing issues occur
     */
    public String generateJsonWebToken() throws Exception {
        Algorithm jwtSigningAlgorithm = buildJwtAlgorithm();

        return JWT.create()
                .withIssuer(" ")
                .withAudience("urn::https://mycomp.any/v1.0/")
                .withClaim("nameid", "My Company")
                .withExpiresAt(getExpirationDate())
                .sign(jwtSigningAlgorithm);
    }


    public String validateToken(String token) {
        try {
            log.info("Validating token...");
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            String publicKeyPem = loadResourceFile("public.pem");

            byte[] publicKeyBytes = new PemReader(new StringReader(publicKeyPem)).readPemObject().getContent();
            RSAPublicKey publicKey = (RSAPublicKey) keyFactory.generatePublic(new X509EncodedKeySpec(publicKeyBytes));

            Algorithm algorithm = Algorithm.RSA512(publicKey, null);

            // Build JWT Verifier
            JWTVerifier verifier = JWT.require(algorithm)
                    .withIssuer("exampleIssuer") // Optional: Validate the issuer
                    .build();

            // Verify the token
            DecodedJWT jwt = verifier.verify(token);

            // Output token claims (example)
            log.info("Token verified successfully!");
            log.info("Subject: {}", jwt.getSubject());
            log.info("Issuer: {}", jwt.getIssuer());
            log.info("Expiration: {}", jwt.getExpiresAt());
            return jwt.getIssuer();
        } catch (JWTVerificationException e) {
            log.warn("Invalid token: {}", e.getMessage());
            throw e;
//            throw new ApiException("", "", "", HttpStatus.NO_CONTENT);
//            return e.getMessage();
        } catch (Exception e) {
            log.error("Error validating token", e);
            return e.getMessage();
        }


    }

    private Instant getExpirationDate() {
//        return LocalDateTime.now().plusHours(2).toInstant(ZoneOffset.of("-03:00"));
        return LocalDateTime.now().plusMinutes(2).toInstant(ZoneOffset.of("-03:00"));

    }
}
