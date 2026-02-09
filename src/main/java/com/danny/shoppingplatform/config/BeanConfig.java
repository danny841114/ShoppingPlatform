package com.danny.shoppingplatform.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class BeanConfig {
    @Bean
    public WebClient webClient() {
        return WebClient
                .builder()
                .exchangeStrategies(ExchangeStrategies
                        .builder()
                        .codecs(configurer ->
                                configurer
                                        .defaultCodecs()
                                        .maxInMemorySize(5 * 1024 * 1024))
                        .build())
                .build();
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
