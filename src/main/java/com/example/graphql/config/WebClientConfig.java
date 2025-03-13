package com.example.graphql.config;

import com.example.graphql.exception.BeanConfigException;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.util.InsecureTrustManagerFactory;
import io.netty.resolver.DefaultAddressResolverGroup;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.reactive.ClientHttpConnector;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;
import reactor.netty.resources.ConnectionProvider;

import javax.net.ssl.SSLException;
import java.time.Duration;

@Configuration
public class WebClientConfig {

    private final String environment = "LOCAL";

    private HttpClient buildHttpClient() {
        HttpClient httpClient;
        try {
            if (environment.equals("LOCAL")) {
                /* Remove certificate */
                SslContext sslContext = SslContextBuilder
                        .forClient()
                        .trustManager(InsecureTrustManagerFactory.INSTANCE)
                        .build();
                httpClient = HttpClient.create().secure(t -> t.sslContext(sslContext))
                        .resolver(DefaultAddressResolverGroup.INSTANCE)
                        .wiretap(true);

            } else {
                ConnectionProvider provider = ConnectionProvider.builder(" custom")
                        .maxLifeTime(Duration.ofSeconds(30))
                        .build();

                httpClient = HttpClient.create(provider)
                        .resolver(DefaultAddressResolverGroup.INSTANCE)
                        .wiretap(true);
            }
            return httpClient;
        } catch (RuntimeException | SSLException e) {
            throw new RuntimeException(e);
        }
    }

    private int toByte(final int value) {
        return value * 1024 * 1024;
    }

    @Bean
    public WebClient webClient(WebClient.Builder builder) {
        WebClient webClient;
        try {

            ClientHttpConnector connector = new ReactorClientHttpConnector(buildHttpClient());
            webClient = builder.clientConnector(connector)
            .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                    .exchangeStrategies(ExchangeStrategies.builder().codecs(codecs ->
                            codecs.defaultCodecs().maxInMemorySize(toByte(30))).build())
                    .build();
        } catch (RuntimeException e) {
            throw new BeanConfigException(e.getMessage());
        }

        return webClient;
    }
}
