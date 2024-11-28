package com.example.internaute_microservice.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Internaute {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private Long identifiant;

    @Column(nullable = false)
    private String nom;

    @Enumerated(EnumType.STRING)
    private TrancheAge trancheAge;

    public enum TrancheAge {
        ENFANT, ADOLESCENT, ADULTE
    }
}
