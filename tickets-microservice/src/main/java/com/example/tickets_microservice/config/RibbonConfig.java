package com.example.tickets_microservice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;

@Configuration
public class RibbonConfig {

    @Bean
    @LoadBalanced // Active l'intégration avec Ribbon
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
