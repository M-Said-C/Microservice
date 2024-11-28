package com.example.tickets_microservice.dtos;

import com.example.tickets_microservice.entities.Ticket.TypeTicket;
import lombok.Data;

@Data
public class TicketDTO {
    private String codeTicket;
    private Double prixTicket;
    private TypeTicket typeTicket;
    private Long evenementId;  // Event ID
    private Long internauteId; // Internaute ID
}
