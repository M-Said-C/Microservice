package com.example.tickets_microservice.services;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class InternauteService {
    private final RestTemplate restTemplate;

    public InternauteService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @CircuitBreaker(name = "internauteService", fallbackMethod = "fallbackGetInternauteDetails")
    public Object getInternauteDetails(Long internauteId) {
        String url = "http://internaute-microservice/api/internautes/" + internauteId;
        return restTemplate.getForObject(url, Object.class);
    }
    public Object fallbackUpdateNbPlace(Long internauteI, Throwable throwable) {
        System.err.println("Fallback: Impossible de trouver l'internaute " + internauteI);
        return "Fallback: Service indisponible temporairement.";
    }
}
