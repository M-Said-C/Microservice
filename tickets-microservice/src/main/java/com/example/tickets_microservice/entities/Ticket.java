package com.example.tickets_microservice.entities;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Ticket {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String codeTicket;

    @Column(nullable = false)
    private Double prixTicket;

    @Enumerated(EnumType.STRING)
    private TypeTicket typeTicket;

    @Column(nullable = false)
    private Long evenementId; // Store only the ID of the event

    @Column(nullable = false)
    private Long internauteId; // Store only the ID of the internaute

    public enum TypeTicket {
        CLASSIQUE, VIP
    }
}
