package com.example.graphql.dataloader;

import com.example.graphql.integration.ReportIntegration;
import com.example.model.Users;
import com.netflix.graphql.dgs.DgsComponent;
import com.netflix.graphql.dgs.DgsQuery;
import com.netflix.graphql.dgs.InputArgument;
import lombok.extern.log4j.Log4j2;
import reactor.core.publisher.Mono;

import java.util.concurrent.CompletableFuture;

@Log4j2
@DgsComponent
public class UserDataLoader {

    private final ReportIntegration reportIntegration;

    public UserDataLoader(ReportIntegration reportIntegration) {
        this.reportIntegration = reportIntegration;
    }

    @DgsQuery
    public CompletableFuture<Users> user(
            @InputArgument String document) {
        log.info("Fetching user with document {}", document);
        Mono<Users> report = reportIntegration.getReport("http://localhost:8080/spring-graphql/v1/users", Users.class);

        return report.toFuture();
    }
}
