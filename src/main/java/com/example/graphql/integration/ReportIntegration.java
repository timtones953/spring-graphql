package com.example.graphql.integration;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
public class ReportIntegration {

    private final WebClient webClient;

    public ReportIntegration(WebClient webClient) {
        this.webClient = webClient;
    }

    public <T> Mono<T> getReport(String url, Class<T> clazz) {
        return webClient.get()
                .uri(url)
                .retrieve()
                .bodyToMono(clazz);
    }
}