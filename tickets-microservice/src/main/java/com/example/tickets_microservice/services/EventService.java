package com.example.tickets_microservice.services;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class EventService {
    private final RestTemplate restTemplate;

    public EventService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public Object getEventDetails(Long evenementId) {
        String url = "http://evenements-categories-microservice/api/evenements/" + evenementId;
        return restTemplate.getForObject(url, Object.class);
    }

    public Object findByNomEvenement(String nomEvt) {
        System.out.println(nomEvt);
        String url = "http://evenements-categories-microservice/api/evenements/by-name/" + nomEvt;
        return restTemplate.getForObject(url, Object.class);
    }
    @CircuitBreaker(name = "eventService", fallbackMethod = "fallbackUpdateNbPlace")
    public Object updateNbPlace(Long evenementId, int ticketsToAddOrRemove) {
        System.out.println("heere");
        String url = "http://evenements-categories-microservice/api/evenements/update-nb-place/" + evenementId + "?ticketsToAddOrRemove=" + ticketsToAddOrRemove;
        return restTemplate.exchange(
                url,
                HttpMethod.PUT,
                null, // No request body needed for this endpoint
                Object.class
        ).getBody();
    }
    public Object fallbackUpdateNbPlace(Long idEvenement, int ticketsToAddOrRemove, Throwable throwable) {
        System.err.println("Fallback: Impossible de mettre à jour les places pour l'événement " + idEvenement);
        return "Fallback: Service indisponible temporairement.";
    }

}
