package com.example.tickets_microservice;

import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class TicketAspect {

    @AfterThrowing(
            pointcut = "execution(* com.example.tickets_microservice.services.TicketService.ajouterTicketsEtAffecterAEvenementEtInternaute(..))",
            throwing = "ex"
    )
    public void logExceptionForAjouterTickets(Exception ex) {
        if (ex instanceof UnsupportedOperationException) {
            System.out.println("Le nombre de places restantes dépasse le nombre de tickets demandés");
        }
    }
}
