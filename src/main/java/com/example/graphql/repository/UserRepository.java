package com.example.graphql.repository;

import com.azure.spring.data.cosmos.repository.ReactiveCosmosRepository;
import com.example.graphql.entity.UserEntity;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public interface UserRepository extends ReactiveCosmosRepository<UserEntity, String> {
    Flux<UserEntity> findByFirstName(String firstName);
}