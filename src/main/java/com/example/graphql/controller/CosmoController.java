package com.example.graphql.controller;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.example.graphql.entity.UserEntity;
import com.example.graphql.repository.UserRepository;
import lombok.extern.log4j.Log4j2;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Instant;
import java.util.Optional;

@Log4j2
@RestController
public class CosmoController extends BaseController {

    private static final Logger LOGGER = LoggerFactory.getLogger(CosmoController.class);


    private final UserRepository userRepository;

    public CosmoController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    @GetMapping("get-cosmos")
    public ResponseEntity<String> getToken() throws Exception {



        this.userRepository.deleteAll().block();
        LOGGER.info("Deleted all data in container.");

        final UserEntity testUser = new UserEntity("testId", "testFirstName", "testLastName", "test address line one");

        // Save the User class to Azure Cosmos DB database.
        final Mono<UserEntity> saveUserMono = userRepository.save(testUser);

        final Flux<UserEntity> firstNameUserFlux = userRepository.findByFirstName("testFirstName");

        //  Nothing happens until we subscribe to these Monos.
        //  findById won't return the user as user isn't present.
        final Mono<UserEntity> findByIdMono = userRepository.findById(testUser.getId());
        final UserEntity findByIdUser = findByIdMono.block();
        Assert.isNull(findByIdUser, "User must be null");

        final UserEntity savedUser = saveUserMono.block();
        Assert.state(savedUser != null, "Saved user must not be null");
        Assert.state(savedUser.getFirstName().equals(testUser.getFirstName()), "Saved user first name doesn't match");

        firstNameUserFlux.collectList().block();

        final Optional<UserEntity> optionalUserResult = userRepository.findById(testUser.getId()).blockOptional();
        Assert.isTrue(optionalUserResult.isPresent(), "Cannot find user.");

        final UserEntity result = optionalUserResult.get();
        Assert.state(result.getFirstName().equals(testUser.getFirstName()), "query result firstName doesn't match!");
        Assert.state(result.getLastName().equals(testUser.getLastName()), "query result lastName doesn't match!");

        LOGGER.info("findOne in User collection get result: {}", result.toString());
//        throw new TokenExpiredException("xxx", Instant.now());
//        return ResponseEntity.ok("OK");
    }
}
