package com.example.tickets_microservice.controllers;
import com.example.tickets_microservice.dtos.TicketDTO;
import com.example.tickets_microservice.entities.Ticket;
import com.example.tickets_microservice.services.TicketService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/tickets")
public class TicketController {
    private final TicketService ticketService;

    public TicketController(TicketService ticketService) {
        this.ticketService = ticketService;
    }

    @PostMapping
    public ResponseEntity<TicketDTO> addTicket(@RequestBody TicketDTO ticketDTO) {
        return ResponseEntity.ok(ticketService.addTicket(ticketDTO));
    }
    @PostMapping("/add")
    public ResponseEntity<List<Ticket>> ajouterTicketsEtAffecterAEvenementEtInternaute(
            @RequestBody Map<String, Object> payload,
            @RequestParam Long evenementId,
            @RequestParam Long internauteId) {

            // Extract the list of tickets from the payload
            @SuppressWarnings("unchecked")
            List<Map<String, Object>> ticketsData = (List<Map<String, Object>>) payload.get("tickets");

            if (ticketsData == null) {
                return ResponseEntity.badRequest().body(null); // Return 400 if tickets are missing
            }

            // Map the tickets data to Ticket entities
            List<Ticket> tickets = ticketsData.stream()
                    .map(ticketMap -> {
                        Ticket ticket = new Ticket();
                        ticket.setCodeTicket((String) ticketMap.get("codeTicket"));
                        ticket.setPrixTicket(Double.valueOf((Integer) ticketMap.get("prixTicket")));
                        ticket.setTypeTicket(Ticket.TypeTicket.valueOf((String) ticketMap.get("typeTicket")));
                        return ticket;
                    })
                    .collect(Collectors.toList());

            // Call the service to add tickets
            List<Ticket> savedTickets = ticketService.ajouterTicketsEtAffecterAEvenementEtInternaute(tickets, evenementId, internauteId);
            return ResponseEntity.ok(savedTickets);
    }

    @GetMapping
    public ResponseEntity<List<TicketDTO>> getAllTickets() {
        return ResponseEntity.ok(ticketService.getAllTickets());
    }

    @GetMapping("/montant-par-evenement-et-type")
    public ResponseEntity<Double> getMontantRecupereParEvtEtTypeTicket(
            @RequestParam String nomEvt,
            @RequestParam Ticket.TypeTicket typeTicket
    ) {

        Double montant = ticketService.montantRecupereParEvtEtTypeTicket(nomEvt, typeTicket);
        return ResponseEntity.ok(montant);
    }
    @GetMapping("/internaute-le-plus-actif")
    public ResponseEntity<String> getInternauteLePlusActif() {
        try {
            String internauteId = ticketService.internauteLePlusActif();
            return ResponseEntity.ok("The most active internaute ID: " + internauteId);
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Error: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Unexpected error occurred: " + e.getMessage());
        }
    }
}

