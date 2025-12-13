package com.pratham.bootbase.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatusCode;
import org.springframework.web.client.RestClient;

import static org.springframework.http.HttpHeaders.CONTENT_TYPE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Configuration
public class RestClientConfig {

    @Value("${api.base.url}")
    private String baseUrl;

    @Bean
    //in case of multiple beans of rest client for multiple apis we have to resolve ambiguity
    // so we use qualifier
    @Qualifier("jsonplaceholderRestClient")
    public RestClient getRestClient(){
        return RestClient.builder()
                .baseUrl(baseUrl)
                .defaultHeader(CONTENT_TYPE,APPLICATION_JSON_VALUE)
                .defaultStatusHandler(HttpStatusCode::is5xxServerError,((request, response) -> {
                    throw new RuntimeException("Internal Server Error From Api");
                }))
                .build();
    }
}
