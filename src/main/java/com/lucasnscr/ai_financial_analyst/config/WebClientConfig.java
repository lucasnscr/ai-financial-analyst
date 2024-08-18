package com.lucasnscr.ai_financial_analyst.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.web.reactive.function.client.WebClient;

import static org.springframework.http.HttpHeaders.USER_AGENT;

@Configuration
public class WebClientConfig {

    @Value("${Alpha.host}")
    private String API_BASE_URL;

    @Bean
    public WebClient polygonWebClient(WebClient.Builder webClientBuilder) {
        return webClientBuilder
                .baseUrl(API_BASE_URL)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, "application/json")
                .defaultHeader(USER_AGENT, USER_AGENT)
                .build();
    }
}
