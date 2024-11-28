package com.example.tickets_microservice.services;

import com.example.tickets_microservice.dtos.TicketDTO;
import com.example.tickets_microservice.entities.Ticket;
import com.example.tickets_microservice.repos.TicketRepository;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class TicketService {
    private final TicketRepository ticketRepository;
    private final EventService eventService;
    private final InternauteService internauteService;

    public TicketService(TicketRepository ticketRepository, EventService eventService, InternauteService internauteService) {
        this.ticketRepository = ticketRepository;
        this.eventService = eventService;
        this.internauteService = internauteService;
    }
    @CircuitBreaker(name = "addTicket", fallbackMethod = "ajouterTicketsEtAffecterAEvenementEtInternaute")
    public List<Ticket> ajouterTicketsEtAffecterAEvenementEtInternaute(List<Ticket> tickets, Long idEvenement, Long idInternaute) {
        // Validate Evenement
        @SuppressWarnings("unchecked")
        Map<String, Object> evenementDetails = (Map<String, Object>) eventService.getEventDetails(idEvenement);
        if (evenementDetails == null || !evenementDetails.containsKey("nbPlacesRestantes")) {
            throw new IllegalArgumentException("Invalid event ID or missing data");
        }

        int nbPlacesRestantes = (int) evenementDetails.get("nbPlacesRestantes");

        // Validate Internaute
        @SuppressWarnings("unchecked")
        Map<String, Object> internauteDetails = (Map<String, Object>) internauteService.getInternauteDetails(idInternaute);
        if (internauteDetails == null) {
            throw new IllegalArgumentException("Invalid internaute ID");
        }

        // Check if there are enough places available
        if (tickets.size() > nbPlacesRestantes) {
            throw new UnsupportedOperationException("nombre de places demandées indisponible");
        }

        // Update the number of available places in the event
        try {
            System.out.println("Calling eventService.updateNbPlace...");
            Object updateResponse = eventService.updateNbPlace(idEvenement, -tickets.size());
            System.out.println("Response from eventService.updateNbPlace: " + updateResponse);
            System.out.println("Successfully updated nbPlacesRestantes.");

        // Save tickets
        tickets.forEach(ticket -> {
            ticket.setEvenementId(idEvenement);
            ticket.setInternauteId(idInternaute);
        });

        return ticketRepository.saveAll(tickets);
        } catch (Exception e) {
            System.err.println("Exception occurred while updating nbPlacesRestantes: " + e.getMessage());
            e.printStackTrace();
            throw e; // Re-throw to identify the issue
        }
    }

    public TicketDTO addTicket(TicketDTO ticketDTO) {
        // Validate Evenement ID
        if (eventService.getEventDetails(ticketDTO.getEvenementId()) == null) {
            throw new IllegalArgumentException("Invalid event ID");
        }

        // Validate Internaute ID
        if (internauteService.getInternauteDetails(ticketDTO.getInternauteId()) == null) {
            throw new IllegalArgumentException("Invalid internaute ID");
        }


        // Save ticket
        Ticket ticket = new Ticket();
        ticket.setCodeTicket(ticketDTO.getCodeTicket());
        ticket.setPrixTicket(ticketDTO.getPrixTicket());
        ticket.setTypeTicket(ticketDTO.getTypeTicket());
        ticket.setEvenementId(ticketDTO.getEvenementId());
        ticket.setInternauteId(ticketDTO.getInternauteId());

        Ticket savedTicket = ticketRepository.save(ticket);
        ticketDTO.setCodeTicket(savedTicket.getCodeTicket());
        return ticketDTO;
    }
    public List<TicketDTO> getAllTickets() {
        List<Ticket> tickets = ticketRepository.findAll();
        return tickets.stream()
                .map(ticket -> {
                    TicketDTO ticketDTO = new TicketDTO();
                    ticketDTO.setCodeTicket(ticket.getCodeTicket());
                    ticketDTO.setPrixTicket(ticket.getPrixTicket());
                    ticketDTO.setTypeTicket(ticket.getTypeTicket());
                    ticketDTO.setEvenementId(ticket.getEvenementId());
                    ticketDTO.setInternauteId(ticket.getInternauteId());
                    return ticketDTO;
                })
                .collect(Collectors.toList());
    }

    public Double montantRecupereParEvtEtTypeTicket(String nomEvt, Ticket.TypeTicket typeTicket) {
        try {
            // Fetch event details
            @SuppressWarnings("unchecked")
            Map<String, Object> evenementDetails = (Map<String, Object>) eventService.findByNomEvenement(nomEvt);
            // Validate event details
            if (evenementDetails == null || !evenementDetails.containsKey("id")) {
                throw new IllegalArgumentException("Invalid event name or missing data");
            }

            // Extract event ID
            Long evenementId = ((Number) evenementDetails.get("id")).longValue();
            System.out.println(evenementId);

            // Fetch tickets for the event and ticket type
            List<Ticket> tickets = ticketRepository.findByEvenementIdAndTypeTicket(evenementId, typeTicket);

            // If no tickets are found, return 0
            if (tickets.isEmpty()) {
                return 0.0;
            }

            // Calculate the total amount
            double totalAmount = tickets.stream()
                    .mapToDouble(Ticket::getPrixTicket)
                    .sum();

            return totalAmount;

        } catch (IllegalArgumentException e) {
            System.err.println("Error: " + e.getMessage());
            throw e; // Propagate the exception for further handling if needed
        } catch (Exception e) {
            System.err.println("Unexpected error occurred: " + e.getMessage());
            throw new RuntimeException("An unexpected error occurred while calculating the amount", e);
        }
    }
    public String internauteLePlusActif() {
        // Fetch all tickets
        List<Ticket> tickets = ticketRepository.findAll();

        // Validate if there are any tickets
        if (tickets.isEmpty()) {
            throw new IllegalStateException("No tickets found");
        }

        // Group tickets by internauteId and count them
        Map<Long, Long> internauteTicketCount = tickets.stream()
                .collect(Collectors.groupingBy(Ticket::getInternauteId, Collectors.counting()));

        // Find the internauteId with the maximum count
        Map.Entry<Long, Long> mostActiveInternaute = internauteTicketCount.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .orElseThrow(() -> new IllegalStateException("No active internaute found"));

        // Return the internauteId as a String
        return String.valueOf(mostActiveInternaute.getKey());
    }
    public List<Ticket> ajouterTicketsEtAffecterAEvenementEtInternaute(List<Ticket> tickets, Long idEvenement, Long idInternaute, Throwable throwable) {
        System.err.println("Fallback: Failed to add tickets. Error: " + throwable.getMessage());
        return List.of(); // Return an empty list or any appropriate default value
    }

}
